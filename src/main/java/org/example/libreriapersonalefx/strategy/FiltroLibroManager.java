package org.example.libreriapersonalefx.strategy;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.example.libreriapersonalefx.model.Libro;
import org.example.libreriapersonalefx.model.Stato;

import java.util.*;
import java.util.stream.Collectors;

public class FiltroLibroManager {

    private ComboBox<String> filtroGenereComboBox;
    private ComboBox<Stato> filtroStatoComboBox;
    private TextField searchTextField;
    private ComboBox<String> tipoRicercaComboBox;

    public FiltroLibroManager(ComboBox<String> filtroGenereComboBox,
                              ComboBox<Stato> filtroStatoComboBox,
                              TextField searchTextField,
                              ComboBox<String> tipoRicercaComboBox) {
        this.filtroGenereComboBox = filtroGenereComboBox;
        this.filtroStatoComboBox = filtroStatoComboBox;
        this.searchTextField = searchTextField;
        this.tipoRicercaComboBox = tipoRicercaComboBox;
    }

    public List<Libro> applicaFiltri(List<Libro> libri) {
        List<FiltroLibroStrategy> filtri = new ArrayList<>();

        // Filtro per Genere
        String genereSelezionato = filtroGenereComboBox.getValue();
        if (genereSelezionato != null && !genereSelezionato.equals("Tutti")) {
            filtri.add(new FiltroPerGenere(genereSelezionato));
        }

        // Filtro per Stato
        Stato statoSelezionato = filtroStatoComboBox.getValue();
        if (statoSelezionato != null) {
            filtri.add(new FiltroPerStato(statoSelezionato));
        }

        // Filtro per Titolo o Autore
        String testoRicerca = searchTextField.getText();
        String tipoRicerca = tipoRicercaComboBox.getValue();
        if (testoRicerca != null && !testoRicerca.trim().isEmpty()) {
            if ("Titolo".equals(tipoRicerca)) {
                filtri.add(new FiltroPerTitolo(testoRicerca));
            } else if ("Autore".equals(tipoRicerca)) {
                filtri.add(new FiltroPerAutore(testoRicerca));
            }
        }

        // Utilizzo del Gestore Filtro per applicare tutte le strategie
        GestoreFiltro gestoreFiltro = new GestoreFiltro();
        gestoreFiltro.setStrategie(filtri);

        return gestoreFiltro.filtra(libri);
    }

    public void aggiornaFiltriDisponibili(List<Libro> tuttiLibri) {
        if (tuttiLibri == null || tuttiLibri.isEmpty()) {
            System.out.println("La lista dei libri è vuota o null");
            return; // Non procedere se la lista è nulla o vuota
        }

        // Aggiungi un controllo per i valori null prima di popolare i set
        Set<String> generi = tuttiLibri.stream()
                .map(Libro::getGenere)
                .filter(g -> g != null && !g.isEmpty()) // Filtra i valori nulli e vuoti
                .collect(Collectors.toCollection(TreeSet::new));

        Set<Stato> stati = tuttiLibri.stream()
                .map(Libro::getStatoLettura)
                .filter(Objects::nonNull) // Filtra i valori nulli
                .collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(Enum::name))));

        // Se i set sono vuoti, aggiungi un valore di default
        if (generi.isEmpty()) {
            generi.add("Nessun genere disponibile");
        }
        if (stati.isEmpty()) {
            stati.add(null); // null = Tutti
        }

        // Aggiornamento del ComboBox per il Genere
        filtroGenereComboBox.getItems().clear();
        filtroGenereComboBox.getItems().add("Tutti");
        filtroGenereComboBox.getItems().addAll(generi);

        // Seleziona il genere precedente, ma assicurati che non sia null
        String genereSelezionato = filtroGenereComboBox.getValue();
        if (genereSelezionato == null || !generi.contains(genereSelezionato)) {
            filtroGenereComboBox.setValue("Tutti");
        } else {
            filtroGenereComboBox.setValue(genereSelezionato);
        }

        // Aggiornamento del ComboBox per lo Stato
        filtroStatoComboBox.getItems().clear();
        filtroStatoComboBox.getItems().add(null); // null = Tutti
        filtroStatoComboBox.getItems().addAll(stati);

        // Seleziona lo stato precedente, ma assicurati che non sia null
        Stato statoSelezionato = filtroStatoComboBox.getValue();
        if (statoSelezionato == null || !stati.contains(statoSelezionato)) {
            filtroStatoComboBox.setValue(null);
        } else {
            filtroStatoComboBox.setValue(statoSelezionato);
        }
    }




}

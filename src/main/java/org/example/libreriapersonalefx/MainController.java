package org.example.libreriapersonalefx;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.stage.Stage;
import org.example.libreriapersonalefx.strategy.*;

import java.util.*;

import java.io.IOException;
import java.util.stream.Collectors;

public class MainController implements Observer {

    @FXML private TableView<Libro> libriTableView;
    @FXML private TableColumn<Libro, String> titoloColumn;
    @FXML private TableColumn<Libro, String> autoreColumn;
    @FXML private TableColumn<Libro, String> isbnColumn;
    @FXML private TableColumn<Libro, String> genereColumn;
    @FXML private TableColumn<Libro, Valutazione> valutazioneColumn;
    @FXML private TableColumn<Libro, Stato> statoColumn;
    @FXML private TableColumn<Libro, Boolean> selezionaColumn;
    @FXML private Button modificaButton;
    @FXML private ComboBox<String> filtroGenereComboBox;
    @FXML private ComboBox<Stato> filtroStatoComboBox;
    @FXML private TextField searchTextField;
    @FXML private ComboBox<String> tipoRicercaComboBox;




    private GestoreFiltro gestoreFiltro = new GestoreFiltro();



    @FXML
    public void initialize() {
        // Colonne classiche
        titoloColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitolo()));
        autoreColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAutore()));
        isbnColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getISBN()));
        genereColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGenere()));
        valutazioneColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getValutazionePersonale()));
        statoColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getStatoLettura()));

        // Colonna CheckBox: direttamente legata alla proprietà del modello
        selezionaColumn.setCellValueFactory(cellData -> cellData.getValue().selezionatoProperty());
        selezionaColumn.setCellFactory(CheckBoxTableCell.forTableColumn(selezionaColumn));

        filtroGenereComboBox.setOnAction(e -> applicaFiltro());
        filtroStatoComboBox.setOnAction(e -> applicaFiltro());
        // Popola tipoRicercaComboBox
        tipoRicercaComboBox.setItems(FXCollections.observableArrayList("Titolo", "Autore"));
        tipoRicercaComboBox.getSelectionModel().selectFirst(); // default: Titolo

        // Se vuoi filtro dinamico alla digitazione:
        searchTextField.textProperty().addListener((obs, oldVal, newVal) -> applicaFiltro());

        // Aggiunta libri di esempio
        GestoreLibreria.getInstance().aggiungiLibro(new Libro("1984", "George Orwell", "1234567890", "Distopico", Valutazione.tre, Stato.inLettura));
       // GestoreLibreria.getInstance().aggiungiLibro(new Libro("Il piccolo principe", "A. de Saint-Exupéry", "0987654321", "Favola", Valutazione.cinque, Stato.daLeggere));

        // Osservazione modello
        GestoreLibreria.getInstance().addObserver(this);

        update();
    }
    @FXML
    public void onSearchClick(ActionEvent event) {
        applicaFiltro();
    }

    private void applicaFiltro() {
        List<Libro> tuttiLibri = GestoreLibreria.getInstance().getLibri();
        List<FiltroLibroStrategy> filtri = new ArrayList<>();

        // Filtro per genere
        String genereSelezionato = filtroGenereComboBox.getValue();
        if (genereSelezionato != null && !genereSelezionato.equals("Tutti")) {
            filtri.add(new FiltroPerGenere(genereSelezionato));
        }

        // Filtro per stato
        Stato statoSelezionato = filtroStatoComboBox.getValue();
        if (statoSelezionato != null) {
            filtri.add(new FiltroPerStato(statoSelezionato));
        }

        // Filtro ricerca testo
        String testoRicerca = searchTextField.getText();
        String tipoRicerca = tipoRicercaComboBox.getValue();

        if (testoRicerca != null && !testoRicerca.trim().isEmpty()) {
            if ("Titolo".equals(tipoRicerca)) {
                filtri.add(new FiltroPerTitolo(testoRicerca));
            } else if ("Autore".equals(tipoRicerca)) {
                filtri.add(new FiltroPerAutore(testoRicerca));
            }
        }

        gestoreFiltro.setStrategie(filtri);
        List<Libro> libriFiltrati = gestoreFiltro.filtra(tuttiLibri);

        libriTableView.getItems().setAll(libriFiltrati);
        aggiornaVisibilita();
    }



    @Override
    public void update() {
        javafx.application.Platform.runLater(() -> {
            aggiornaFiltriDisponibili();  // <-- aggiunto
            applicaFiltro();


            // Aggiungi listener per ciascun libro alla sua property selezionato
            for (Libro libro : libriTableView.getItems()) {
                libro.selezionatoProperty().addListener((obs, oldVal, newVal) -> aggiornaVisibilita());
            }

            aggiornaVisibilita(); // aggiorna subito anche all'avvio
        });
    }
    private void aggiornaFiltriDisponibili() {
        List<Libro> tuttiLibri = GestoreLibreria.getInstance().getLibri();

        // Generi unici ordinati
        Set<String> generi = tuttiLibri.stream()
                .map(Libro::getGenere)
                .filter(g -> g != null && !g.isEmpty())
                .collect(Collectors.toCollection(TreeSet::new));

        // Stati unici ordinati
        Set<Stato> stati = tuttiLibri.stream()
                .map(Libro::getStatoLettura)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(Enum::name))));

        // Salva selezione precedente
        String genereSelezionato = filtroGenereComboBox.getValue();
        Stato statoSelezionato = filtroStatoComboBox.getValue();

        // --- GENERE ---
        filtroGenereComboBox.getItems().clear();
        filtroGenereComboBox.getItems().add("Tutti");
        filtroGenereComboBox.getItems().addAll(generi);
        filtroGenereComboBox.setValue(
                genereSelezionato != null && generi.contains(genereSelezionato) ? genereSelezionato : "Tutti"
        );

        // --- STATO ---
        filtroStatoComboBox.getItems().clear();
        filtroStatoComboBox.getItems().add(null); // null rappresenta "Tutti"
        filtroStatoComboBox.getItems().addAll(stati);

        // Personalizza la visualizzazione di null come "Tutti"
        filtroStatoComboBox.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Stato stato, boolean empty) {
                super.updateItem(stato, empty);
                setText(empty ? "" : (stato == null ? "Tutti" : stato.toString()));
            }
        });

        filtroStatoComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Stato stato, boolean empty) {
                super.updateItem(stato, empty);
                setText(empty || stato == null ? "Tutti" : stato.toString());
            }
        });

        filtroStatoComboBox.setValue(
                statoSelezionato != null && stati.contains(statoSelezionato) ? statoSelezionato : null
        );
    }


    @FXML
    public void onVaiAggiungiLibroClick(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AggiungiLibro.fxml"));
            Parent root = loader.load();

            LibroController controller = loader.getController();
            controller.setCallback(this::update);

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void rimuoviSelezionati(ActionEvent event) {
        List<Libro> daRimuovere = new ArrayList<>();
        for (Libro libro : GestoreLibreria.getInstance().getLibri()) {
            if (libro.isSelezionato())  daRimuovere.add(libro);
        }
        GestoreLibreria.getInstance().rimuoviLibro(daRimuovere);

    }


    public void modificaSelezionato(ActionEvent actionEvent) {

        for(Libro libro : GestoreLibreria.getInstance().getLibri()) {
             if (libro.isSelezionato()) {

                 GestoreLibreria.getInstance().setLibroModifica(libro);
             }

        }
        onVaiAggiungiLibroClick(actionEvent);

    }
    public void aggiornaVisibilita() {
        long selezionati =0;
        for (Libro libro : GestoreLibreria.getInstance().getLibri()) {
           if (libro.isSelezionato()) selezionati++;
        }
        modificaButton.setVisible(selezionati == 1);  // visibile solo se uno è selezionato
    }



}

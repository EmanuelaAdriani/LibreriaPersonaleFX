/*package org.example.libreriapersonalefx.test;

import org.example.libreriapersonalefx.controller.MainController;
import org.example.libreriapersonalefx.model.Libro;
import org.example.libreriapersonalefx.model.Stato;
import org.example.libreriapersonalefx.model.Valutazione;
import org.example.libreriapersonalefx.strategy.GestoreFiltro;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

public class MainControllerTest {

    private MainController controller;
    private List<Libro> libri;

    @BeforeEach
    void setUp() {
        // Inizializza il controller
        controller = new MainController();

        // Inizializza la lista di libri
        libri = new ArrayList<>();
        libri.add(new Libro("1984", "George Orwell", "1234567890", "Distopico", Valutazione.tre, Stato.inLettura));
        libri.add(new Libro("Fahrenheit 451", "Ray Bradbury", "0987654321", "Distopico", Valutazione.quattro, Stato.completato));
        libri.add(new Libro("Brave New World", "Aldous Huxley", "1122334455", "Utopico", Valutazione.cinque, Stato.inLettura));

        // Simula un GestoreFiltro con i libri
        controller.gestoreFiltro = new GestoreFiltro();
    }

    @Test
    void testApplicaFiltroPerGenere() {
        // Imposta un filtro per il genere "Distopico"
        controller.filtroGenereComboBox.setValue("Distopico");

        // Chiama il metodo per applicare il filtro
        controller.applicaFiltro();

        // Verifica che il risultato contenga solo i libri "Distopico"
        assertEquals(2, controller.libriTableView.getItems().size());  // Due libri con genere "Distopico"
    }

    @Test
    void testApplicaFiltroPerStato() {
        // Imposta un filtro per lo stato "In lettura"
        controller.filtroStatoComboBox.setValue(Stato.inLettura);

        // Chiama il metodo per applicare il filtro
        controller.applicaFiltro();

        // Verifica che il risultato contenga solo i libri "In lettura"
        assertEquals(2, controller.libriTableView.getItems().size());  // Due libri con stato "In lettura"
    }

    @Test
    void testApplicaFiltroPerRicerca() {
        // Imposta il tipo di ricerca su "Titolo" e cerca il titolo "1984"
        controller.tipoRicercaComboBox.setValue("Titolo");
        controller.searchTextField.setText("1984");

        // Chiama il metodo per applicare il filtro
        controller.applicaFiltro();

        // Verifica che il risultato contenga solo il libro con il titolo "1984"
        assertEquals(1, controller.libriTableView.getItems().size());
        assertEquals("1984", controller.libriTableView.getItems().get(0).getTitolo());
    }
}

 */

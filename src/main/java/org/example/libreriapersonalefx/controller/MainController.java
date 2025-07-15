package org.example.libreriapersonalefx.controller;

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
import org.example.libreriapersonalefx.command.*;
import org.example.libreriapersonalefx.model.Libro;
import org.example.libreriapersonalefx.model.Stato;
import org.example.libreriapersonalefx.model.Valutazione;
import org.example.libreriapersonalefx.singleton.GestoreLibreria;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainController {

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

    private FiltroLibroManager filtroLibroManager;

    public TableView<Libro> getLibriTableView() {
        return libriTableView;
    }

    @FXML
    public void initialize() {
        // Setup colonne
        titoloColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTitolo()));
        autoreColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getAutore()));
        isbnColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getISBN()));
        genereColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getGenere()));
        valutazioneColumn.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getValutazionePersonale()));
        statoColumn.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getStatoLettura()));

        selezionaColumn.setCellValueFactory(c -> c.getValue().selezionatoProperty());
        selezionaColumn.setCellFactory(CheckBoxTableCell.forTableColumn(selezionaColumn));

        filtroGenereComboBox.setOnAction(e -> applicaFiltro());
        filtroStatoComboBox.setOnAction(e -> applicaFiltro());

        tipoRicercaComboBox.setItems(FXCollections.observableArrayList("Titolo", "Autore"));
        tipoRicercaComboBox.getSelectionModel().selectFirst();

        searchTextField.textProperty().addListener((obs, oldV, newV) -> applicaFiltro());

        filtroLibroManager = new FiltroLibroManager(filtroGenereComboBox, filtroStatoComboBox, searchTextField, tipoRicercaComboBox);

        aggiornaDati(); // inizializza la tabella con i dati
    }

    public void aggiornaDati() {
        List<Libro> tuttiLibri = GestoreLibreria.getInstance().getLibri();
        filtroLibroManager.aggiornaFiltriDisponibili(tuttiLibri);
        applicaFiltro();
    }

    @FXML
    private void applicaFiltro() {
        List<Libro> tuttiLibri = GestoreLibreria.getInstance().getLibri();
        List<Libro> libriFiltrati = filtroLibroManager.applicaFiltri(tuttiLibri);

        // Aggiungi listener a ogni checkbox selezionata
        for (Libro libro : libriFiltrati) {
            libro.selezionatoProperty().addListener((obs, oldVal, newVal) -> aggiornaVisibilita());
        }

        libriTableView.getItems().setAll(libriFiltrati);
        aggiornaVisibilita();
    }

    @FXML
    public void libroController(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/libreriapersonalefx/AggiungiLibro.fxml"));
            Parent root = loader.load();

            LibroController controller = loader.getController();
            controller.setCallback(this::aggiornaDati); // aggiorna dopo salvataggio

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
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
            if (libro.isSelezionato()) daRimuovere.add(libro);
        }
        GestoreLibreria.getInstance().rimuoviLibro(daRimuovere);
        aggiornaDati(); // aggiorna la vista
    }

    @FXML
    public void modificaSelezionato(ActionEvent event) {
        for (Libro libro : GestoreLibreria.getInstance().getLibri()) {
            if (libro.isSelezionato()) {
                GestoreLibreria.getInstance().setLibroModifica(libro);
                break;
            }
        }
        libroController(event);
    }

    public void aggiornaVisibilita() {
        long selezionati = GestoreLibreria.getInstance().getLibri().stream()
                .filter(Libro::isSelezionato)
                .count();
        modificaButton.setVisible(selezionati == 1);
    }

    // Comandi menu
    @FXML
    public void onNuovo(ActionEvent event) {
        new NuovoCommand(this).esegui();
    }

    @FXML
    public void onApri(ActionEvent event) {
        new ApriFileCommand(this).esegui();
    }

    @FXML
    public void onSalva(ActionEvent event) {
        new SalvaFileCommand(this).esegui();
    }

    @FXML
    public void onSalvaCome(ActionEvent event) {
        new SalvaComeFileCommand(this).esegui();
    }

    @FXML
    public void onEsci(ActionEvent event) {
        new EsciCommand(this).esegui();
    }

    @FXML
    public void onAbout(ActionEvent event) {
        new OnAboutCommand().esegui();
    }
}

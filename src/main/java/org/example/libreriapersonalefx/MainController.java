package org.example.libreriapersonalefx;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainController implements Observer {

    @FXML private TableView<Libro> libriTableView;
    @FXML private TableColumn<Libro, String> titoloColumn;
    @FXML private TableColumn<Libro, String> autoreColumn;
    @FXML private TableColumn<Libro, String> isbnColumn;
    @FXML private TableColumn<Libro, String> genereColumn;
    @FXML private TableColumn<Libro, Valutazione> valutazioneColumn;
    @FXML private TableColumn<Libro, Stato> statoColumn;
    @FXML private TableColumn<Libro, Boolean> selezionaColumn;
    @FXML private Button azioneSelezionatiButton;

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

        // Aggiunta libri di esempio
        GestoreLibreria.getInstance().aggiungiLibro(new Libro("1984", "George Orwell", "1234567890", "Distopico", Valutazione.tre, Stato.inLettura));
        GestoreLibreria.getInstance().aggiungiLibro(new Libro("Il piccolo principe", "A. de Saint-Exupéry", "0987654321", "Favola", Valutazione.cinque, Stato.daLeggere));

        // Osservazione modello
        GestoreLibreria.getInstance().addObserver(this);

        update();
    }

    @Override
    public void update() {
        javafx.application.Platform.runLater(() -> {
            libriTableView.getItems().setAll(GestoreLibreria.getInstance().getLibri());
        });
    }

    @FXML
    public void onVaiAggiungiLibroClick(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AggiungiLibro.fxml"));
            Parent root = loader.load();

            AggiungiLibroController controller = loader.getController();
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

    }
}

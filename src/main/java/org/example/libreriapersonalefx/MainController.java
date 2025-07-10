package org.example.libreriapersonalefx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;

public class MainController {

    @FXML private TableView<Libro> libriTableView;
    @FXML private TableColumn<Libro, String> titoloColumn;
    @FXML private TableColumn<Libro, String> autoreColumn;
    @FXML private TableColumn<Libro, String> isbnColumn;
    @FXML private TableColumn<Libro, String> genereColumn;
    @FXML private TableColumn<Libro, Valutazione> valutazioneColumn;
    @FXML private TableColumn<Libro, Stato> statoColumn;


    private final ObservableList<Libro> libriList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Mappa le colonne alle proprietà della classe Libro
        titoloColumn.setCellValueFactory(new PropertyValueFactory<>("titolo"));
        autoreColumn.setCellValueFactory(new PropertyValueFactory<>("autore"));
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("ISBN"));
        genereColumn.setCellValueFactory(new PropertyValueFactory<>("genere"));
        valutazioneColumn.setCellValueFactory(new PropertyValueFactory<>("valutazionePersonale"));
        statoColumn.setCellValueFactory(new PropertyValueFactory<>("statoLettura"));
        GestoreLibreria.getInstance().aggiungiLibro( new Libro("1984", "George Orwell", "1234567890", "Distopico", Valutazione.tre, Stato.inLettura));
        GestoreLibreria.getInstance().aggiungiLibro(new Libro("Il piccolo principe", "A. de Saint-Exupéry", "0987654321", "Favola", Valutazione.cinque, Stato.daLeggere));

        libriTableView.setItems(GestoreLibreria.getInstance().getLibri());
    }

    public void onVaiAggiungiLibroClick(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AggiungiLibro.fxml"));
            Parent root = loader.load();

            AggiungiLibroController controller = loader.getController();
            controller.setCallback(() -> {
                // Dopo aggiunta, potresti voler ricaricare i dati o aggiornare la lista
                // Esempio: libriList.add(nuovoLibro) se passi l'oggetto
            });

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

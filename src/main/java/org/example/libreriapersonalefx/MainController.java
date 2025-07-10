package org.example.libreriapersonalefx;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;

public class MainController implements Observer {

    @FXML private TableView<Libro> libriTableView;
    @FXML private TableColumn<Libro, String> titoloColumn;
    @FXML private TableColumn<Libro, String> autoreColumn;
    @FXML private TableColumn<Libro, String> isbnColumn;
    @FXML private TableColumn<Libro, String> genereColumn;
    @FXML private TableColumn<Libro, Valutazione> valutazioneColumn;
    @FXML private TableColumn<Libro, Stato> statoColumn;

    // Nuova colonna checkbox per selezionare i libri
    @FXML private TableColumn<Libro, Boolean> selezionaColumn;

    // Pulsante per azioni sui selezionati (da collegare al FXML)
    @FXML private Button azioneSelezionatiButton;

    @FXML
    public void initialize() {
        // Colonne normali
        titoloColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitolo()));
        autoreColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAutore()));
        isbnColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getISBN()));
        genereColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGenere()));
        valutazioneColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getValutazionePersonale()));
        statoColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getStatoLettura()));

        // Colonna checkbox: si lega al campo selezionato del libro e aggiorna lo stato nel modello
        selezionaColumn.setCellValueFactory(cellData -> {
            Libro libro = cellData.getValue();
            // Property Boolean che si sincronizza con libro.isSelezionato()
            BooleanProperty selectedProp = new SimpleBooleanProperty(libro.isSelezionato());

            // Listener per aggiornare il modello Libro quando si cambia checkbox
            selectedProp.addListener((obs, wasSelected, isNowSelected) -> {
                libro.setSelezionato(isNowSelected);
            });

            return selectedProp;
        });

        // Usa una cella CheckBox standard
        selezionaColumn.setCellFactory(CheckBoxTableCell.forTableColumn(selezionaColumn));

        // Aggiungi libri di esempio (puoi rimuovere dopo)
        GestoreLibreria.getInstance().aggiungiLibro(new Libro("1984", "George Orwell", "1234567890", "Distopico", Valutazione.tre, Stato.inLettura));
        GestoreLibreria.getInstance().aggiungiLibro(new Libro("Il piccolo principe", "A. de Saint-Exupéry", "0987654321", "Favola", Valutazione.cinque, Stato.daLeggere));

        // Aggiungi observer per aggiornare la tabella quando cambia la lista libri
        GestoreLibreria.getInstance().addObserver(this);

        // Carica dati iniziali in tabella
        update();
    }

    @Override
    public void update() {
        // Aggiorna tabella con lista libri da GestoreLibreria (su thread UI)
        javafx.application.Platform.runLater(() -> {
            libriTableView.getItems().setAll(GestoreLibreria.getInstance().getLibri());
        });
    }

    public void onVaiAggiungiLibroClick(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AggiungiLibro.fxml"));
            Parent root = loader.load();

            AggiungiLibroController controller = loader.getController();
            controller.setCallback(() -> {
                // Dopo aggiunta libro, eventualmente aggiorna tabella
                update();
            });

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void azioneSuSelezionati(ActionEvent event) {
        // Esempio: stampa titoli dei libri selezionati
        for (Libro libro : GestoreLibreria.getInstance().getLibri()) {
            if (libro.isSelezionato()) {
                System.out.println("Azione su libro selezionato: " + libro.getTitolo());
                // Qui metti la tua logica per ogni libro selezionato
            }
        }
    }

    public void onAzioneSelezionatiClick(ActionEvent actionEvent) {
    }
}

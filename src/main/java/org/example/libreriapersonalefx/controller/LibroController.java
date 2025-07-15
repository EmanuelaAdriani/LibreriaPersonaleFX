package org.example.libreriapersonalefx.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.libreriapersonalefx.singleton.GestoreLibreria;
import org.example.libreriapersonalefx.model.Libro;
import org.example.libreriapersonalefx.model.Stato;
import org.example.libreriapersonalefx.model.Valutazione;

import java.io.IOException;

public class LibroController {

    @FXML private TextField titoloTextField;
    @FXML private TextField autoreTextField;
    @FXML private TextField isbnTextField;
    @FXML private TextField genereTextField;
    @FXML private ComboBox<Valutazione> valutazioneComboBox;
    @FXML private ComboBox<Stato> statoComboBox;

    private Runnable callback;

    public void setCallback(Runnable callback) {
        this.callback = callback;
    }

    @FXML
    private void initialize() {
        valutazioneComboBox.getItems().setAll(Valutazione.values());
        statoComboBox.getItems().setAll(Stato.values());
        if(GestoreLibreria.getInstance().getLibroModifica()!=null) {
            titoloTextField.setText(GestoreLibreria.getInstance().getLibroModifica().getTitolo());
            autoreTextField.setText(GestoreLibreria.getInstance().getLibroModifica().getAutore());
            isbnTextField.setText(GestoreLibreria.getInstance().getLibroModifica().getISBN());
            genereTextField.setText(GestoreLibreria.getInstance().getLibroModifica().getGenere());
            valutazioneComboBox.setValue(GestoreLibreria.getInstance().getLibroModifica().getValutazionePersonale());
            statoComboBox.setValue(GestoreLibreria.getInstance().getLibroModifica().getStatoLettura());
        }
    }

    @FXML
    private void handleConferma() {
        // Raccolta dati
        String titolo = titoloTextField.getText();
        String autore = autoreTextField.getText();
        String isbn = isbnTextField.getText();
        String genere = genereTextField.getText();
        Valutazione valutazione = valutazioneComboBox.getValue();
        Stato stato = statoComboBox.getValue();
        if(GestoreLibreria.getInstance().getLibroModifica()!=null){
            Libro l=GestoreLibreria.getInstance().getLibroModifica();
            l.setTitolo(titolo);
            l.setAutore(autore);
            l.setISBN(isbn);
            l.setGenere(genere);
            l.setStatoLettura(stato);
            l.setValutazionePersonale(valutazione);
            GestoreLibreria.getInstance().setLibroModifica(null);
        }
        else if(GestoreLibreria.getInstance().isISBNIn(isbn)){

            showAlert(Alert.AlertType.ERROR, "Errore", "Il libro è già presente");
            return;
        }
        else {
            GestoreLibreria.getInstance().aggiungiLibro(new Libro(titolo, autore, isbn, genere, valutazione, stato));
        }
        // Validazione minima
        if (isbn.isEmpty()||titolo.isEmpty() || autore.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Errore", "ISBN,Titolo e Autore sono obbligatori.");
            return;
        }


        // Qui potresti salvare il libro in una lista o database

        showAlert(Alert.AlertType.INFORMATION, "Conferma", "Libro aggiunto correttamente!");

        if (callback != null) callback.run();

        tornaAllaPaginaPrincipale();
    }
    @FXML
    private void tornaAllaPaginaPrincipale() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/libreriapersonalefx/Main.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) titoloTextField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}

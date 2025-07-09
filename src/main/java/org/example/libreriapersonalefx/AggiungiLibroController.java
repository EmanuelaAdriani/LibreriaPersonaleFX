package org.example.libreriapersonalefx;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class AggiungiLibroController {

    // I tuoi TextField definiti nell'FXML
    @FXML private TextField nomeTextField;
    @FXML private TextField cognomeTextField;
    @FXML private TextField indirizzoTextField;
    @FXML private TextField telefonoTextField;
    @FXML private TextField emailTextField;

    // Campo per la callback
    private Runnable callback;

    // Metodo per impostare la callback
    public void setCallback(Runnable callback) {
        this.callback = callback;
    }

    @FXML
    private void handleConferma() {
        // Simula una conferma (puoi aggiungere logica per il salvataggio dei dati)
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Conferma");
        alert.setHeaderText(null);
        alert.setContentText("Dati aggiunti correttamente!");
        alert.showAndWait();

        // Ora torniamo al Main.fxml o Hello.fxml
        tornaAllaPaginaPrincipale();
    }

    // Metodo per tornare alla scena principale (Main.fxml o Hello.fxml)
    private void tornaAllaPaginaPrincipale() {
        try {
            // Carica il file FXML principale (Main.fxml o Hello.fxml)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));  // Usa "Hello.fxml" se è quello che desideri
            Parent root = loader.load();

            // Ottieni il nodo che ha generato l'evento (ad esempio, un bottone)
            Stage stage = (Stage) nomeTextField.getScene().getWindow();

            // Imposta la scena con il nuovo root caricato da FXML
            stage.setScene(new Scene(root));

            // Mostra la nuova scena
            stage.show();

        } catch (IOException e) {
            // Se c'è un errore nel caricamento del file FXML, stampa lo stack trace
            e.printStackTrace();
        }
    }
}

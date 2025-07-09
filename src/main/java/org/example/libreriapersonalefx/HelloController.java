package org.example.libreriapersonalefx;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloController {

    @FXML
    public void onVaiAggiungiLibroClick(javafx.event.ActionEvent actionEvent) {
        // Carica il file FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AggiungiLibro.fxml"));

        try {
            // Carica il file FXML e ottieni il nodo root
            Parent root = loader.load();

            // Ottieni il nodo che ha generato l'evento (ad esempio, un bottone)
            Node sourceNode = (Node) actionEvent.getSource();

            // Ottieni lo stage (finestra) corrente
            Stage stage = (Stage) sourceNode.getScene().getWindow();

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

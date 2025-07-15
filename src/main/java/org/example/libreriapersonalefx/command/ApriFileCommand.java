package org.example.libreriapersonalefx.command;

import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.libreriapersonalefx.controller.MainController;
import org.example.libreriapersonalefx.singleton.GestoreLibreria;

import java.io.File;
import java.io.IOException;

public class ApriFileCommand implements Command {

    private MainController mainController;

    public ApriFileCommand(MainController mainController) {
        this.mainController = mainController;
    }


    @Override
    public void esegui() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Apri file");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CSV files", "*.csv"),
                new FileChooser.ExtensionFilter("JSON files", "*.json")
        );
        Stage stage = (Stage) mainController.getLibriTableView().getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            String name = file.getName().toLowerCase();
            try {
                if (name.endsWith(".csv")) {
                    GestoreLibreria.getInstance().caricaDaCSV(file);
                } else if (name.endsWith(".json")) {
                    GestoreLibreria.getInstance().caricaDaJson(file);
                } else {
                    mostraErrore("Formato file non supportato");
                    return;
                }
                GestoreLibreria.getInstance().setLastUsedFile(file);
                mainController.aggiornaDati();
            } catch (IOException e) {
                mostraErrore("Errore caricamento file: " + e.getMessage());
            }
        }

    }
    public static void mostraErrore(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Errore");
        alert.setHeaderText("Errore");
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
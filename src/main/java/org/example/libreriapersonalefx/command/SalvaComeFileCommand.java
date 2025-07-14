package org.example.libreriapersonalefx.command;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.libreriapersonalefx.controller.MainController;
import org.example.libreriapersonalefx.singleton.GestoreLibreria;

import java.io.File;
import java.io.IOException;

import static org.example.libreriapersonalefx.command.ApriFileCommand.mostraErrore;

public class SalvaComeFileCommand implements Command {

    private MainController mainController;

    public SalvaComeFileCommand(MainController mainController) {
        this.mainController = mainController;
    }

    @Override
    public void esegui()  {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Salva file");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CSV files", "*.csv"),
                new FileChooser.ExtensionFilter("JSON files", "*.json")
        );
        Stage stage = (Stage) mainController.getLibriTableView().getScene().getWindow();
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            try {
                salvaInFile(file);
               GestoreLibreria.getInstance().setLastUsedFile(file);
            } catch (IOException e) {
                mostraErrore("Errore salvataggio: " + e.getMessage());
            }
        }
    }
    private void salvaInFile(File file) throws IOException {
        String name = file.getName().toLowerCase();
        if (name.endsWith(".csv")) {
            GestoreLibreria.getInstance().salvaCSV(file);
        } else if (name.endsWith(".json")) {
            GestoreLibreria.getInstance().salvaJson(file);
        } else {
            throw new IOException("Formato file non supportato per il salvataggio.");
        }
        mainController.update();
    }
}

package org.example.libreriapersonalefx.command;

import org.example.libreriapersonalefx.controller.MainController;
import org.example.libreriapersonalefx.singleton.GestoreLibreria;

import java.io.File;
import java.io.IOException;

import static org.example.libreriapersonalefx.command.ApriFileCommand.mostraErrore;

public class SalvaFileCommand implements Command {
    private MainController mainController;
    public SalvaFileCommand(MainController mainController) {
        this.mainController = mainController;
    }
    public void esegui() {
        if (mainController.getLastUsedFile() == null) {
            new SalvaComeFileCommand(mainController).esegui();
            return;
        }
        try {
            salvaInFile(mainController.getLastUsedFile());
        } catch (IOException e) {
            mostraErrore("Errore salvataggio: " + e.getMessage());
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

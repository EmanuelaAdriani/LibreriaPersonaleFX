package org.example.libreriapersonalefx.command;

import org.example.libreriapersonalefx.controller.MainController;
import org.example.libreriapersonalefx.singleton.GestoreLibreria;

public class NuovoCommand implements Command {
    private MainController mainController;
    public NuovoCommand(MainController mainController) {
        this.mainController = mainController;
    }
    public void esegui(){
        GestoreLibreria.getInstance().reset(); // Assicurati che reset() svuoti la lista libri
        mainController.update();
        GestoreLibreria.getInstance().setLastUsedFile(null);
    }
}

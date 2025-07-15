package org.example.libreriapersonalefx.command;

import javafx.stage.Stage;
import org.example.libreriapersonalefx.controller.MainController;

public class EsciCommand implements Command {
    private MainController mainController;
    public EsciCommand(MainController mainController) {
        this.mainController = mainController;
    }
    public void esegui() {
        Stage stage = (Stage) mainController.getLibriTableView().getScene().getWindow();
        stage.close();
    }
}


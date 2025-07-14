package org.example.libreriapersonalefx.command;

import javafx.scene.control.Alert;

public class OnAboutCommand implements Command {

    @Override
    public void esegui()  {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("Libreria Personale");
        alert.setContentText("Applicazione di gestione libreria personale\n Realizzata da Emanuela Adriani");
        alert.showAndWait();

    }
}

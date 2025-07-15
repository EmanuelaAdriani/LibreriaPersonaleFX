package org.example.libreriapersonalefx.command;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import org.example.libreriapersonalefx.controller.MainController;
import org.example.libreriapersonalefx.singleton.GestoreLibreria;

public class EsciCommand implements Command {
    private MainController mainController;

    public EsciCommand(MainController mainController) {
        this.mainController = mainController;
    }

    public void esegui() {
        // Controlla se ci sono modifiche non salvate
        if (GestoreLibreria.getInstance().isModified()) {
            // Crea un alert di tipo conferma con le opzioni "Sì", "No" e "Annulla"
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Conferma uscita");
            alert.setHeaderText("Hai delle modifiche non salvate!");
            alert.setContentText("Vuoi salvare le modifiche effettuate?");

            // Aggiungi le opzioni Sì, No e Annulla
            ButtonType buttonTypeYes = new ButtonType("Sì");
            ButtonType buttonTypeNo = new ButtonType("No");
            ButtonType buttonTypeCancel = new ButtonType("Annulla");
            alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo, buttonTypeCancel);

            // Mostra l'alert e aspetta la risposta dell'utente
            alert.showAndWait().ifPresent(response -> {
                if (response == buttonTypeYes) {
                    // Se l'utente clicca su Sì, salva e poi chiudi la finestra
                    new SalvaFileCommand(mainController).esegui();
                    if(!GestoreLibreria.getInstance().isModified()) {
                        Stage stage = (Stage) mainController.getLibriTableView().getScene().getWindow();
                        stage.close();
                    }
                } else if (response == buttonTypeNo) {
                    // Se l'utente clicca su No, chiudi senza salvare
                    Stage stage = (Stage) mainController.getLibriTableView().getScene().getWindow();
                    stage.close();
                }
                // Se l'utente clicca su Annulla, non fare nulla (la finestra rimane aperta)
            });
        } else {
            // Se non ci sono modifiche, chiudi direttamente la finestra
            Stage stage = (Stage) mainController.getLibriTableView().getScene().getWindow();
            stage.close();
        }
    }
}

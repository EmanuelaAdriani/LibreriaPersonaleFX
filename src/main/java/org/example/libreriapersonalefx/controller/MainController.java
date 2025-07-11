package org.example.libreriapersonalefx.controller;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.libreriapersonalefx.entity.Libro;
import org.example.libreriapersonalefx.entity.Stato;
import org.example.libreriapersonalefx.entity.Valutazione;
import org.example.libreriapersonalefx.observer.Observer;
import org.example.libreriapersonalefx.singleton.GestoreLibreria;
import org.example.libreriapersonalefx.strategy.*;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class MainController implements Observer {

    @FXML private TableView<Libro> libriTableView;
    @FXML private TableColumn<Libro, String> titoloColumn;
    @FXML private TableColumn<Libro, String> autoreColumn;
    @FXML private TableColumn<Libro, String> isbnColumn;
    @FXML private TableColumn<Libro, String> genereColumn;
    @FXML private TableColumn<Libro, Valutazione> valutazioneColumn;
    @FXML private TableColumn<Libro, Stato> statoColumn;
    @FXML private TableColumn<Libro, Boolean> selezionaColumn;
    @FXML private Button modificaButton;
    @FXML private ComboBox<String> filtroGenereComboBox;
    @FXML private ComboBox<Stato> filtroStatoComboBox;
    @FXML private TextField searchTextField;
    @FXML private ComboBox<String> tipoRicercaComboBox;

    private GestoreFiltro gestoreFiltro = new GestoreFiltro();

    private File lastUsedFile = null;  // Per Save/Save As

    @FXML
    public void initialize() {
        // Setup colonne
        titoloColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTitolo()));
        autoreColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getAutore()));
        isbnColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getISBN()));
        genereColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getGenere()));
        valutazioneColumn.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getValutazionePersonale()));
        statoColumn.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getStatoLettura()));

        selezionaColumn.setCellValueFactory(c -> c.getValue().selezionatoProperty());
        selezionaColumn.setCellFactory(CheckBoxTableCell.forTableColumn(selezionaColumn));

        filtroGenereComboBox.setOnAction(e -> applicaFiltro());
        filtroStatoComboBox.setOnAction(e -> applicaFiltro());

        tipoRicercaComboBox.setItems(FXCollections.observableArrayList("Titolo", "Autore"));
        tipoRicercaComboBox.getSelectionModel().selectFirst();

        searchTextField.textProperty().addListener((obs, oldV, newV) -> applicaFiltro());

        // Esempio libro iniziale
        GestoreLibreria.getInstance().aggiungiLibro(new Libro("1984", "George Orwell", "1234567890", "Distopico", Valutazione.tre, Stato.inLettura));

        GestoreLibreria.getInstance().addObserver(this);

        update();
    }

    @FXML
    public void onSearchClick(ActionEvent event) {
        applicaFiltro();
    }

    private void applicaFiltro() {
        List<Libro> tuttiLibri = GestoreLibreria.getInstance().getLibri();
        List<FiltroLibroStrategy> filtri = new ArrayList<>();

        String genereSelezionato = filtroGenereComboBox.getValue();
        if (genereSelezionato != null && !genereSelezionato.equals("Tutti")) {
            filtri.add(new FiltroPerGenere(genereSelezionato));
        }

        Stato statoSelezionato = filtroStatoComboBox.getValue();
        if (statoSelezionato != null) {
            filtri.add(new FiltroPerStato(statoSelezionato));
        }

        String testoRicerca = searchTextField.getText();
        String tipoRicerca = tipoRicercaComboBox.getValue();

        if (testoRicerca != null && !testoRicerca.trim().isEmpty()) {
            if ("Titolo".equals(tipoRicerca)) {
                filtri.add(new FiltroPerTitolo(testoRicerca));
            } else if ("Autore".equals(tipoRicerca)) {
                filtri.add(new FiltroPerAutore(testoRicerca));
            }
        }

        gestoreFiltro.setStrategie(filtri);
        List<Libro> filtrati = gestoreFiltro.filtra(tuttiLibri);

        libriTableView.getItems().setAll(filtrati);
        aggiornaVisibilita();
    }

    @Override
    public void update() {
        javafx.application.Platform.runLater(() -> {
            aggiornaFiltriDisponibili();
            applicaFiltro();

            for (Libro libro : libriTableView.getItems()) {
                libro.selezionatoProperty().addListener((obs, oldVal, newVal) -> aggiornaVisibilita());
            }

            aggiornaVisibilita();
        });
    }

    private void aggiornaFiltriDisponibili() {
        List<Libro> tuttiLibri = GestoreLibreria.getInstance().getLibri();

        Set<String> generi = tuttiLibri.stream()
                .map(Libro::getGenere)
                .filter(g -> g != null && !g.isEmpty())
                .collect(Collectors.toCollection(TreeSet::new));

        Set<Stato> stati = tuttiLibri.stream()
                .map(Libro::getStatoLettura)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(Enum::name))));

        String genereSelezionato = filtroGenereComboBox.getValue();
        Stato statoSelezionato = filtroStatoComboBox.getValue();

        filtroGenereComboBox.getItems().clear();
        filtroGenereComboBox.getItems().add("Tutti");
        filtroGenereComboBox.getItems().addAll(generi);
        filtroGenereComboBox.setValue(
                genereSelezionato != null && generi.contains(genereSelezionato) ? genereSelezionato : "Tutti"
        );

        filtroStatoComboBox.getItems().clear();
        filtroStatoComboBox.getItems().add(null); // null = Tutti
        filtroStatoComboBox.getItems().addAll(stati);

        filtroStatoComboBox.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Stato stato, boolean empty) {
                super.updateItem(stato, empty);
                setText(empty ? "" : (stato == null ? "Tutti" : stato.toString()));
            }
        });
        filtroStatoComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Stato stato, boolean empty) {
                super.updateItem(stato, empty);
                setText(empty || stato == null ? "Tutti" : stato.toString());
            }
        });

        filtroStatoComboBox.setValue(
                statoSelezionato != null && stati.contains(statoSelezionato) ? statoSelezionato : null
        );
    }

    @FXML
    public void onVaiAggiungiLibroClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/libreriapersonalefx/AggiungiLibro.fxml"));
            Parent root = loader.load();

            LibroController controller = loader.getController();
            controller.setCallback(this::update);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void rimuoviSelezionati(ActionEvent event) {
        List<Libro> daRimuovere = new ArrayList<>();
        for (Libro libro : GestoreLibreria.getInstance().getLibri()) {
            if (libro.isSelezionato()) daRimuovere.add(libro);
        }
        GestoreLibreria.getInstance().rimuoviLibro(daRimuovere);
    }

    @FXML
    public void modificaSelezionato(ActionEvent event) {
        for (Libro libro : GestoreLibreria.getInstance().getLibri()) {
            if (libro.isSelezionato()) {
                GestoreLibreria.getInstance().setLibroModifica(libro);
                break; // Modifica uno solo
            }
        }
        onVaiAggiungiLibroClick(event);
    }

    public void aggiornaVisibilita() {
        long selezionati = GestoreLibreria.getInstance().getLibri().stream()
                .filter(Libro::isSelezionato)
                .count();
        modificaButton.setVisible(selezionati == 1);
    }

    // ---- MENU FILE ----

    @FXML
    public void onNuovo(ActionEvent event) {
        GestoreLibreria.getInstance().reset(); // Assicurati che reset() svuoti la lista libri
        update();
        lastUsedFile = null;
    }


    @FXML
    public void onApri(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Apri file");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CSV files", "*.csv"),
                new FileChooser.ExtensionFilter("JSON files", "*.json")
        );
        Stage stage = (Stage) libriTableView.getScene().getWindow();
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
                lastUsedFile = file;
                update();
            } catch (IOException e) {
                mostraErrore("Errore caricamento file: " + e.getMessage());
            }
        }
    }

    @FXML
    public void onSalva(ActionEvent event) {
        if (lastUsedFile == null) {
            onSalvaCome(event);
            return;
        }
        try {
            salvaInFile(lastUsedFile);
        } catch (IOException e) {
            mostraErrore("Errore salvataggio: " + e.getMessage());
        }
    }

    @FXML
    public void onSalvaCome(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Salva file");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CSV files", "*.csv"),
                new FileChooser.ExtensionFilter("JSON files", "*.json")
        );
        Stage stage = (Stage) libriTableView.getScene().getWindow();
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            try {
                salvaInFile(file);
                lastUsedFile = file;
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
        update();
    }

    @FXML
    public void onEsci(ActionEvent event) {
        Stage stage = (Stage) libriTableView.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void onAbout(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("Libreria Personale FX");
        alert.setContentText("Applicazione di gestione libreria personale\nVersione 1.0");
        alert.showAndWait();
    }

    private void mostraErrore(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Errore");
        alert.setHeaderText("Errore");
        alert.setContentText(msg);
        alert.showAndWait();
    }

    public void onSalvaCSV(ActionEvent actionEvent) {
    }

    public void onSalvaJSON(ActionEvent actionEvent) {
    }

    public void onCaricaCSV(ActionEvent actionEvent) {
    }

    public void onCaricaJSON(ActionEvent actionEvent) {
    }
}

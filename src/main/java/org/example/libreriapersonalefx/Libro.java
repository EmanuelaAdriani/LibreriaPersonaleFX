package org.example.libreriapersonalefx;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.ArrayList;
import java.util.List;

public class Libro implements Observable {
    private String titolo;
    private String autore;
    private String ISBN;
    private String genere;
    private Valutazione valutazionePersonale;
    private Stato statoLettura;

    private final BooleanProperty selezionato = new SimpleBooleanProperty(false);
    private final List<Observer> observers = new ArrayList<>();

    public Libro(String titolo, String autore, String ISBN, String genere, Valutazione valutazione, Stato stato) {
        this.titolo = titolo;
        this.autore = autore;
        this.ISBN = ISBN;
        this.genere = genere;
        this.valutazionePersonale = valutazione;
        this.statoLettura = stato;

        // Se cambia la proprietà, notifica gli observer
        selezionato.addListener((obs, oldVal, newVal) -> notifyObservers());
    }

    // Getter standard
    public String getTitolo() { return titolo; }
    public String getAutore() { return autore; }
    public String getISBN() { return ISBN; }
    public String getGenere() { return genere; }
    public Valutazione getValutazionePersonale() { return valutazionePersonale; }
    public Stato getStatoLettura() { return statoLettura; }

    // Checkbox property
    public boolean isSelezionato() {
        return selezionato.get();
    }

    public void setSelezionato(boolean value) {
        selezionato.set(value);
    }

    public BooleanProperty selezionatoProperty() {
        return selezionato;
    }

    // Observer pattern
    @Override
    public void addObserver(Observer o) {
        if (!observers.contains(o)) {
            observers.add(o);
        }
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers() {
        for (Observer o : observers) {
            o.update();
        }
    }
}

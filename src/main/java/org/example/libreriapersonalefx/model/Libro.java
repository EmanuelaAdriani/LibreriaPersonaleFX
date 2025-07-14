package org.example.libreriapersonalefx.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import org.example.libreriapersonalefx.observer.Observable;
import org.example.libreriapersonalefx.observer.Observer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }
    public void setAutore(String autore) {
        this.autore = autore;
    }
    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }
    public void setGenere(String genere) {
        this.genere = genere;
    }

    public void setValutazionePersonale(Valutazione valutazionePersonale) {
        this.valutazionePersonale = valutazionePersonale;
    }
    public void setStatoLettura(Stato statoLettura) {
        this.statoLettura = statoLettura;
    }

    // Checkbox property
    public boolean isSelezionato() {
        return selezionato.get();
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
    public void notifyObservers() {
        for (Observer o : observers) {
            o.update();
        }
    }

    @Override
    public String toString() {
        return "Libro{" +
                "titolo='" + titolo + '\'' +
                ", autore='" + autore + '\'' +
                ", ISBN='" + ISBN + '\'' +
                ", genere='" + genere + '\'' +
                ", valutazionePersonale=" + valutazionePersonale +
                ", statoLettura=" + statoLettura +
                ", selezionato=" + selezionato +
                ", observers=" + observers +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Libro libro = (Libro) o;
        return Objects.equals(titolo, libro.titolo) && Objects.equals(autore, libro.autore) && Objects.equals(ISBN, libro.ISBN) && Objects.equals(genere, libro.genere) && valutazionePersonale == libro.valutazionePersonale && statoLettura == libro.statoLettura && Objects.equals(selezionato, libro.selezionato) && Objects.equals(observers, libro.observers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(titolo, autore, ISBN, genere, valutazionePersonale, statoLettura, selezionato, observers);
    }
}

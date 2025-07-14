package org.example.libreriapersonalefx.singleton;

import org.example.libreriapersonalefx.util.LibroCSVUtil;
import org.example.libreriapersonalefx.util.LibroJsonUtil;
import org.example.libreriapersonalefx.model.Libro;
import org.example.libreriapersonalefx.observer.Observable;
import org.example.libreriapersonalefx.observer.Observer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GestoreLibreria implements Observable {

    private static GestoreLibreria istanza = null;
    private static Libro libroModifica=null;
    // Lista di libri
    private List<Libro> libri = new ArrayList<>();

    // Lista di observer (listener)
    private List<Observer> observers = new ArrayList<>();

    private GestoreLibreria() {}

    public static GestoreLibreria getInstance() {
        if (istanza == null) {
            istanza = new GestoreLibreria();
        }
        return istanza;
    }

    // Metodo per aggiungere observer

    public void addObserver(Observer o) {
        observers.add(o);
    }

    // Metodo per rimuovere observer
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    // Metodo per notificare observer
    public void notifyObservers() {
        for (Observer o : observers) {
            o.update();
        }
    }

    // Aggiungi libro e notifica cambiamento
    public void aggiungiLibro(Libro libro) {
        libri.add(libro);
        notifyObservers();
    }
    public void rimuoviLibro(Libro libro) {
        libri.remove(libro);
        notifyObservers();
    }
    public void rimuoviLibro(List<Libro> libro) {
        libri.removeAll(libro);
        notifyObservers();
    }

    public List<Libro> getLibri() {
        return libri;
    }

    public Libro getLibroModifica() {
        return libroModifica;
    }
    public void setLibroModifica(Libro libroModifica) {
        this.libroModifica = libroModifica;
    }

    public boolean isISBNIn(String isbn) {
        for (Libro libro : libri) {
            if (libro.getISBN().equals(isbn)) {
                return true;
            }
        }
        return false;
    }
    // Carica la lista libri da file CSV (sovrascrive la lista attuale)
    public void caricaDaCSV(File file) throws IOException {
        List<Libro> caricati = LibroCSVUtil.caricaDaCSV(file);
        libri.clear();
        libri.addAll(caricati);
        notifyObservers();
    }

    public void reset() {
        libri.clear();
        notifyObservers();
    }

    // Carica la lista libri da file JSON (sovrascrive la lista attuale)
    public void caricaDaJson(File file) throws IOException {
        List<Libro> caricati = LibroJsonUtil.caricaDaJson(file);
        libri.clear();
        libri.addAll(caricati);
        notifyObservers();
    }

    public void salvaCSV(File file) throws IOException {
        LibroCSVUtil.salvaInCSV(libri, file);
    }

    public void salvaJson(File file) throws IOException {
        LibroJsonUtil.salvaInJson(libri, file);
    }

    // Altri metodi (rimuovi, modifica, ecc.) qui e chiamare notifyObservers() quando cambiano i dati
}

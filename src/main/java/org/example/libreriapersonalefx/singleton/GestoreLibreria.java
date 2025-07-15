package org.example.libreriapersonalefx.singleton;

import org.example.libreriapersonalefx.util.LibroCSVUtil;
import org.example.libreriapersonalefx.util.LibroJsonUtil;
import org.example.libreriapersonalefx.model.Libro;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GestoreLibreria  {

    private static GestoreLibreria istanza = null;
    private static Libro libroModifica=null;
    // Lista di libri
    private List<Libro> libri = new ArrayList<>();
    private File lastUsedFile = null;  // Per Save/Save As

    private GestoreLibreria() {}

    public File getLastUsedFile() {
        return lastUsedFile;
    }
    public void setLastUsedFile(File lastUsedFile) {
        this.lastUsedFile = lastUsedFile;
    }

    public static GestoreLibreria getInstance() {
        if (istanza == null) {
            istanza = new GestoreLibreria();
        }
        return istanza;
    }


    // Aggiungi libro e notifica cambiamento
    public void aggiungiLibro(Libro libro) {
        libri.add(libro);

    }

    public void rimuoviLibro(List<Libro> libro) {
        libri.removeAll(libro);

    }
    public void rimuoviLibro(Libro libro) {
        libri.remove(libro);

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

    }

    public void reset() {
        libri.clear();

    }

    // Carica la lista libri da file JSON (sovrascrive la lista attuale)
    public void caricaDaJson(File file) throws IOException {
        List<Libro> caricati = LibroJsonUtil.caricaDaJson(file);
        libri.clear();
        libri.addAll(caricati);

    }

    public void salvaCSV(File file) throws IOException {
        LibroCSVUtil.salvaInCSV(libri, file);
    }

    public void salvaJson(File file) throws IOException {
        LibroJsonUtil.salvaInJson(libri, file);
    }

    // Altri metodi (rimuovi, modifica, ecc.) qui e chiamare notifyObservers() quando cambiano i dati
}

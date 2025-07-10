package org.example.libreriapersonalefx;

import java.util.ArrayList;
import java.util.List;

public class GestoreLibreria {
    private static GestoreLibreria istanza = null;
    private List<Libro> libri = new ArrayList<>();

    private GestoreLibreria() {}

    public static GestoreLibreria getInstance() {
        if (istanza == null) {
            istanza = new GestoreLibreria();
        }
        return istanza;
    }

    public void aggiungiLibro(Libro libro) {
        libri.add(libro);
    }
    public List<Libro> getLibri() {
        return libri;
    }

    // Altri metodi: rimuovi, modifica, cerca, salva, carica, ecc.
}

package org.example.libreriapersonalefx.strategy;

import org.example.libreriapersonalefx.Libro;

public class FiltroPerAutore implements FiltroLibroStrategy {
    private final String testoRicerca;

    public FiltroPerAutore(String testoRicerca) {
        this.testoRicerca = testoRicerca.toLowerCase();
    }

    @Override
    public boolean filtra(Libro libro) {
        return libro.getAutore().toLowerCase().contains(testoRicerca);
    }
}
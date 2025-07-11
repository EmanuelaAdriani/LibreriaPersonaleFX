package org.example.libreriapersonalefx.strategy;

import org.example.libreriapersonalefx.Libro;

public class FiltroPerTitolo implements FiltroLibroStrategy {
    private final String testoRicerca;

    public FiltroPerTitolo(String testoRicerca) {
        this.testoRicerca = testoRicerca.toLowerCase();
    }

    @Override
    public boolean filtra(Libro libro) {
        return libro.getTitolo().toLowerCase().contains(testoRicerca);
    }
}
package org.example.libreriapersonalefx.strategy;

import org.example.libreriapersonalefx.model.Libro;

public class FiltroPerGenere implements FiltroLibroStrategy {
    private final String genere;

    public FiltroPerGenere(String genere) {
        this.genere = genere;
    }

    @Override
    public boolean filtra(Libro libro) {
        return libro.getGenere().equalsIgnoreCase(genere);
    }
}

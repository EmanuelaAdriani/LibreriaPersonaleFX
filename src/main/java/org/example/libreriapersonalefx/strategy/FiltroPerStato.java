package org.example.libreriapersonalefx.strategy;

import org.example.libreriapersonalefx.model.Libro;
import org.example.libreriapersonalefx.model.Stato;

public class FiltroPerStato implements FiltroLibroStrategy {
    private final Stato stato;

    public FiltroPerStato(Stato stato) {
        this.stato = stato;
    }

    @Override
    public boolean filtra(Libro libro) {
        return libro.getStatoLettura() == stato;
    }
}

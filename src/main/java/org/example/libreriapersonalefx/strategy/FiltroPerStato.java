package org.example.libreriapersonalefx.strategy;

import org.example.libreriapersonalefx.entity.Libro;
import org.example.libreriapersonalefx.entity.Stato;

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

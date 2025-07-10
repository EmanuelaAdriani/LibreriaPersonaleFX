package org.example.libreriapersonalefx;

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

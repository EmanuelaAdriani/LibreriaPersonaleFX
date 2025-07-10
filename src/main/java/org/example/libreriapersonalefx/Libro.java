package org.example.libreriapersonalefx;

import org.example.libreriapersonalefx.Stato;
import org.example.libreriapersonalefx.Valutazione;

public class Libro {
    private String titolo;
    private String autore;
    private String ISBN;
    private String genere;
    private Valutazione valutazionePersonale;
    private Stato statoLettura;

    public Libro(String titolo, String autore, String ISBN, String genere, Valutazione valutazione, Stato stato) {
        this.titolo = titolo;
        this.autore = autore;
        this.ISBN = ISBN;
        this.genere = genere;
        this.valutazionePersonale = valutazione;
        this.statoLettura = stato;
    }

    public String getTitolo() { return titolo; }
    public String getAutore() { return autore; }
    public String getISBN() { return ISBN; }
    public String getGenere() { return genere; }
    public Valutazione getValutazionePersonale() { return valutazionePersonale; }
    public Stato getStatoLettura() { return statoLettura; }
}

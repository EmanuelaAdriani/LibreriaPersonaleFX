import static org.junit.jupiter.api.Assertions.*;

import org.example.libreriapersonalefx.strategy.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.example.libreriapersonalefx.model.Libro;
import org.example.libreriapersonalefx.model.Stato;
import org.example.libreriapersonalefx.model.Valutazione;
import org.example.libreriapersonalefx.singleton.GestoreLibreria;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


 class LibreriaTest {
     private final GestoreLibreria gestore=GestoreLibreria.getInstance();
     private Libro libro1, libro2, libro3;
     private File fileCSV;
     private File fileJSON;


     @BeforeEach
     public void setUp() throws IOException {
         libro1=new Libro("Il nome della rosa", "Umberto Eco", "1980", "Giallo storico",Valutazione.cinque,Stato.daLeggere);
         libro2=new Libro("Se questo è un uomo", "Primo Levi", "1947","Memoriale",  Valutazione.quattro,Stato.inLettura);
         libro3=new Libro("Baudolino", "Umberto Eco", "2000","Romanzo storico" , Valutazione.tre,Stato.Letto);

         fileCSV = File.createTempFile("libreria_test", ".csv");
         fileCSV.deleteOnExit();

         fileJSON = File.createTempFile("libreria_test", ".json");
         fileJSON.deleteOnExit();
     }


     @Test
     public void testAggiungiERimuoviLibro(){
         gestore.aggiungiLibro(libro1);
         assertEquals(gestore.getLibri().size(), 1);
         assertTrue(gestore.getLibri().contains(libro1));
         gestore.rimuoviLibro(libro1);
         assertEquals(gestore.getLibri().size(), 0);
     }
     private void riempi(){
         gestore.aggiungiLibro(libro1);
         gestore.aggiungiLibro(libro2);
         gestore.aggiungiLibro(libro3);
     }

     @Test
     public void testFiltroPerTitolo() {
         riempi();
         GestoreFiltro gestoreFiltro = new GestoreFiltro();
         List<FiltroLibroStrategy> strategie= new ArrayList<>();
         strategie.add(new FiltroPerTitolo("Baudolino"));
         gestoreFiltro.setStrategie(strategie);

         List<Libro> filtrati = gestoreFiltro.filtra(gestore.getLibri());

         assertEquals(1, filtrati.size());
         assertEquals("Baudolino", filtrati.get(0).getTitolo());

     }
     @Test
     public void testFiltroPerAutore() {
         riempi();
         GestoreFiltro gestoreFiltro = new GestoreFiltro();
         List<FiltroLibroStrategy> strategie= new ArrayList<>();
         strategie.add(new FiltroPerAutore("Umberto Eco"));
         gestoreFiltro.setStrategie(strategie);

         List<Libro> filtrati = gestoreFiltro.filtra(gestore.getLibri());

         assertEquals(2, filtrati.size());


     }
     @Test
     public void testFiltroPerStato() {
         riempi();
         GestoreFiltro gestoreFiltro = new GestoreFiltro();
         List<FiltroLibroStrategy> strategie= new ArrayList<>();
         strategie.add(new FiltroPerStato(Stato.inLettura));
         gestoreFiltro.setStrategie(strategie);

         List<Libro> filtrati = gestoreFiltro.filtra(gestore.getLibri());

         assertEquals(1, filtrati.size());
         assertEquals(Stato.inLettura, filtrati.get(0).getStatoLettura());

     }
     @Test
     public void testFiltroPerGenere() {
         riempi();
         GestoreFiltro gestoreFiltro = new GestoreFiltro();
         List<FiltroLibroStrategy> strategie= new ArrayList<>();
         strategie.add(new FiltroPerGenere("Memoriale"));
         gestoreFiltro.setStrategie(strategie);

         List<Libro> filtrati = gestoreFiltro.filtra(gestore.getLibri());

         assertEquals(1, filtrati.size());
         assertEquals("Memoriale", filtrati.get(0).getGenere());

     }
     @Test
     public void testSalvaECaricaCSV() throws IOException {
         // SALVA
         riempi();
         gestore.salvaCSV(fileCSV);

         // SVUOTA e CARICA
         gestore.getLibri().clear();
         assertEquals(0, gestore.getLibri().size());

         gestore.caricaDaCSV(fileCSV);

         // VERIFICA
         List<Libro> libri = gestore.getLibri();
         assertEquals(3, libri.size());
         assertEquals("Il nome della rosa", libri.get(0).getTitolo());
         assertEquals("Umberto Eco", libri.get(0).getAutore());
     }
     @Test
     public void testSalvaECaricaJSON() throws IOException {
         // SALVA
         riempi();
         gestore.salvaJson(fileJSON);

         // SVUOTA e CARICA
         gestore.getLibri().clear();
         assertEquals(0, gestore.getLibri().size());

         gestore.caricaDaJson(fileJSON);

         // VERIFICA
         List<Libro> libri = gestore.getLibri();
         assertEquals(3, libri.size());
         assertEquals("Se questo è un uomo", libri.get(1).getTitolo());
         assertEquals("Primo Levi", libri.get(1).getAutore());
     }


     @AfterEach
     public void tearDown() {
         gestore.getLibri().clear();
         if (fileCSV.exists()) fileCSV.delete();
         if (fileJSON.exists()) fileJSON.delete();
     }


}

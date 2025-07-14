package org.example.libreriapersonalefx.util;

import java.io.*;
import java.util.*;
import org.example.libreriapersonalefx.model.Libro;
import org.example.libreriapersonalefx.model.Valutazione;
import org.example.libreriapersonalefx.model.Stato;

public class LibroCSVUtil {

    // Salva lista libri in CSV
    public static void salvaInCSV(List<Libro> libri, File file) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            // Header CSV
            bw.write("titolo,autore,ISBN,genere,valutazione,stato");
            bw.newLine();

            for (Libro libro : libri) {
                String line = String.join(",",
                        escapeCsv(libro.getTitolo()),
                        escapeCsv(libro.getAutore()),
                        escapeCsv(libro.getISBN()),
                        escapeCsv(libro.getGenere()),
                        libro.getValutazionePersonale().name(),
                        libro.getStatoLettura().name()
                );
                bw.write(line);
                bw.newLine();
            }
        }
    }

    // Carica lista libri da CSV
    public static List<Libro> caricaDaCSV(File file) throws IOException {
        List<Libro> libri = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine(); // salta header

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                // parts[0] titolo, parts[1] autore, parts[2] ISBN, parts[3] genere, parts[4] valutazione, parts[5] stato

                Libro libro = new Libro(
                        unescapeCsv(parts[0]),
                        unescapeCsv(parts[1]),
                        unescapeCsv(parts[2]),
                        unescapeCsv(parts[3]),
                        Valutazione.valueOf(parts[4]),
                        Stato.valueOf(parts[5])
                );
                libri.add(libro);
            }
        }
        return libri;
    }

    private static String escapeCsv(String input) {
        if (input == null) return "";
        if (input.contains(",") || input.contains("\"") || input.contains("\n")) {
            input = input.replace("\"", "\"\"");
            return "\"" + input + "\"";
        }
        return input;
    }

    private static String unescapeCsv(String input) {
        if (input == null) return "";
        if (input.startsWith("\"") && input.endsWith("\"")) {
            input = input.substring(1, input.length()-1);
            input = input.replace("\"\"", "\"");
        }
        return input;
    }
}

package org.example.libreriapersonalefx.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.example.libreriapersonalefx.entity.Libro;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class LibroJsonUtil {

    private static final ObjectMapper mapper = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT);

    public static void salvaInJson(List<Libro> libri, File file) throws IOException {
        mapper.writeValue(file, libri);
    }

    public static List<Libro> caricaDaJson(File file) throws IOException {
        return mapper.readValue(file,
                mapper.getTypeFactory().constructCollectionType(List.class, Libro.class));
    }
}
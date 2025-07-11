package org.example.libreriapersonalefx.strategy;

import org.example.libreriapersonalefx.Libro;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GestoreFiltro {
    private List<FiltroLibroStrategy> strategie = new ArrayList<>();

    public void setStrategie(List<FiltroLibroStrategy> strategie) {
        this.strategie = strategie;
    }

    public List<Libro> filtra(List<Libro> libri) {
        if (strategie.isEmpty()) return libri;

        return libri.stream()
                .filter(libro -> strategie.stream().allMatch(s -> s.filtra(libro)))
                .collect(Collectors.toList());
    }
}

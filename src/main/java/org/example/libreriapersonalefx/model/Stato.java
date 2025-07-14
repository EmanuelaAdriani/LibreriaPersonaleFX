package org.example.libreriapersonalefx.model;

public enum Stato {
    Letto,daLeggere,inLettura;
    public String toString(){
        switch(this){
            case Letto:
                return "Letto";
            case daLeggere:
                return "Da Leggere";
            case inLettura:
                return "In Lettura";
            default:
                return "Tutto";
        }
    }
}

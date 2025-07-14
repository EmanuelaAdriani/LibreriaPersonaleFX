package org.example.libreriapersonalefx.model;

public enum Valutazione {
    uno, due, tre, quattro, cinque;

    // Metodo per ottenere la rappresentazione a stelle
    public String getStelle() {
        int stellePiene = this.ordinal() + 1; // ordinal parte da 0
        int stelleVuote = 5 - stellePiene;

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < stellePiene; i++) {
            sb.append("★");  // stellina piena
        }
        for (int i = 0; i < stelleVuote; i++) {
            sb.append("☆");  // stellina vuota
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        // Se vuoi mostrare sempre le stelline come toString()
        return getStelle();
    }
}

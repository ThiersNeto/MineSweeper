package org.example;

/**
 * Representa uma coordenada no tabuleiro, x-y
 */
public class Coordinate {
    private int x, y;

    /**
     *
     * @param x Linha
     * @param y Coluna
     */
    public Coordinate(int x,int y){
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return String.format("%c %d", (char)('A' + x), y);
    }
}

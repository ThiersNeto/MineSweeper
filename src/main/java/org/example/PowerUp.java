package org.example;

import org.example.Board;
import org.example.Coordinate;
import org.example.PowerUpType;

import java.util.*;

/**
 * Classe que representa um PowerUp no jogo.
 */
public class PowerUp {
    private PowerUpType type;

    public PowerUp(PowerUpType type) {
        this.type = type;
    }

    public PowerUpType getType() {
        return type;
    }


    public boolean activate(Board board, Coordinate coord) {
        switch (type) {
            case SHIELD:
                return activateShield(board);
            case ICE:
                return activateIce(board);
            case LINE:
                return activateLine(board, coord);
            case COLUMN:
                return activateColumn(board, coord);
            default:
                System.out.println("Tipo de PowerUp desconhecido.");
                return false;
        }
    }

    /**
     * Impede que uma mina exploda na próxima jogada em que o jogador abrir uma célula minada.
     * Coloca uma bandeira permanente na célula minada.
     */
    private boolean activateShield(Board board) {
        System.out.println("Shield ativado! Você está protegido na próxima jogada.");
        board.activateShield();

        return true;
    }

    /**
     * Congela o tempo (cronômetro) durante 3 jogadas.
     */
    private boolean activateIce(Board board) {
        System.out.println("Ice ativado! O tempo está congelado por 3 jogadas.");
        //board.freezeTime(3); // Congela o tempo por 3 jogadas
        board.activateIce(3);
        return true;
    }

    /**
     * Revela todas as células de uma linha, exceto células minadas.
     * Coloca uma bandeira permanente nas células minadas.
     */
    private boolean activateLine(Board board, Coordinate coord) {
        int row = coord.getX();
        for (int col = 0; col < board.getCols(); col++) {
            if (!board.hasMine(row, col)) {
                board.revealCell(row, col); // Revela células sem minas
            } else {
                board.toggleFlagStatus(row, col); // Coloca bandeira permanente em células minadas
            }
        }
        System.out.println("Line ativado! A linha " + row + " foi revelada.");
        return true;
    }

    /**
     * Revela todas as células de uma coluna, exceto células minadas.
     * Coloca uma bandeira permanente nas células minadas.
     */
    private boolean activateColumn(Board board, Coordinate coord) {
        int col = coord.getY();
        for (int row = 0; row < board.getRows(); row++) {
            if (!board.hasMine(row, col)) {
                board.revealCell(row, col); // Revela células sem minas
            } else {
                board.toggleFlagStatus(row, col); // Coloca bandeira permanente em células minadas
            }
        }
        System.out.println("Column ativado! A coluna " + col + " foi revelada.");
        return true;
    }

    @Override
    public String toString() {
        return "" + type;
    }
}
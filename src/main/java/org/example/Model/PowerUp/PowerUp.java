package org.example.Model.PowerUp;

import org.example.Model.Game.Board;
import org.example.Model.Game.Coordinate;

/**
 * Classe que representa um PowerUp no jogo.
 */
public class PowerUp {
    private PowerUpType type;
    private int usesRemaining; // Para PowerUps incrementais

    public PowerUp(PowerUpType type, int uses) {
        this.type = type;
        this.usesRemaining = uses;
    }

    public PowerUpType getType() {
        return type;
    }

    public int getUsesRemaining() {
        return usesRemaining;
    }

    public boolean activate(Board board, Coordinate coord) {
        if (usesRemaining <= 0) {
            System.out.println("Nenhum uso restante para este PowerUp.");
            return false;
        }

        switch (type) {
            case SHIELD:
                return activateShield(board, coord);
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
    private boolean activateShield(Board board, Coordinate coord) {
        System.out.println("Shield ativado! Você está protegido na próxima jogada.");
        usesRemaining--;
        return true;
    }

    /**
     * Congela o tempo (cronômetro) durante 3 jogadas.
     */
    private boolean activateIce(Board board) {
        System.out.println("Ice ativado! O tempo está congelado por 3 jogadas.");
        board.freezeTime(3); // Congela o tempo por 3 jogadas
        usesRemaining--;
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
                board.toggleFlag(row, col); // Coloca bandeira permanente em células minadas
            }
        }
        System.out.println("Line ativado! A linha " + row + " foi revelada.");
        usesRemaining--;
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
                board.toggleFlag(row, col); // Coloca bandeira permanente em células minadas
            }
        }
        System.out.println("Column ativado! A coluna " + col + " foi revelada.");
        usesRemaining--;
        return true;
    }
}
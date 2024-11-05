package Service;

import Model.Board;
import java.util.Scanner;

/**
 *  Classe Game responsável por controlar o fluxo do Campo Minado
 */
public class Game {
    private Board board;
    private boolean gameActive;

    /**
     *
     * @param rows      Número de linhas
     * @param cols      Número de Colunas
     * @param mines     Número de Minas
     */
    public Game(int rows, int cols, int mines) {
        this.board = new Board(rows, cols, mines);
        this.gameActive = true;
    }

    /**
     * Inicia o loop do jogo.
     * @param scanner      Objeto Scanner
     */
    public void start(Scanner scanner) {
        while (gameActive) {
            board.printBoard();
            System.out.print("Digite as coordenadas (linha e coluna): ");
            int x = scanner.nextInt();
            int y = scanner.nextInt();

            if (board.hasMine(x, y)) {
                System.out.println("Você acertou uma mina! Fim de jogo.");
                gameActive = false;
            } else {
                board.revealCell(x, y);
                if (checkVictory()) {
                    System.out.println("Parabéns, você venceu!");
                    gameActive = false;
                }
            }
        }
    }

    /**
     *
     * @return "true" se o player venceu o game
     * @return "false" caso perca
     */
    private boolean checkVictory() {
        for (int i = 0; i < board.getRows(); i++) {
            for (int j = 0; j < board.getCols(); j++) {
                if (!board.hasMine(i, j) && !board.isRevealed(i, j)) {
                    return false;
                }
            }
        }
        return true;
    }
}

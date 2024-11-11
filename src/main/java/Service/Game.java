package Service;

import Model.Board;
import java.util.Scanner;

/**
 *  Classe Game responsável por controlar o fluxo do Campo Minado
 */
public class Game {
    private Board board;
    private boolean gameActive;
    private Scanner scanner;

    /**
     *
     * @param rows      Número de linhas
     * @param cols      Número de Colunas
     * @param mines     Número de Minas
     */
    public Game(int rows, int cols, int mines) {
        this.board = new Board(rows, cols, mines);
        this.gameActive = true;
        this.scanner = new Scanner(System.in); // Scanner para ler as entradas do jogador
    }

    /**
     * Inicia o loop do jogo.
     */
    public void start() {
        // O jogo começa com o tabuleiro impresso
        while (gameActive) {
            board.printBoard();

            // Solicita ao jogador as coordenadas
            System.out.print("Digite as coordenadas (linha e coluna): ");
            int x = scanner.nextInt();
            //valor tem de ser alterado para char pois user escolhe de A a F
            int y = scanner.nextInt();

            // Verifica se o jogador acertou uma mina
            if (board.hasMine(x, y)) {
                System.out.println("Você acertou uma mina! Fim de jogo.");
                gameActive = false;
            } else {
                // Revela a célula escolhida pelo jogador
                board.revealCell(x, y);

                // Verifica se o jogador venceu
                if (checkVictory()) {
                    System.out.println("Parabéns, você venceu!");
                    gameActive = false;
                }
            }
        }
        scanner.close();
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

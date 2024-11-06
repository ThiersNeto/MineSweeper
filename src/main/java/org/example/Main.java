package org.example;

import Service.Game;
import java.util.Scanner;

/**
 * Classe Principal
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Menu inicial
        int choice;
        do {
            System.out.println("=== MENU ===");
            System.out.println("1. Começar Jogo");
            System.out.println("2. Sair");
            System.out.print("Escolha uma opção: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    int rows = 9;
                    int cols = 9;
                    int mines = 10;

                    // Início do jogo
                    Game game = new Game(rows, cols, mines);
                    game.start();
                    break;

                case 2:
                    System.out.println("Saindo do jogo...");
                    break;

                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        } while (choice != 2);

        scanner.close(); // Fecha o scanner ao finalizar o jogo
        System.out.println("Obrigado por jogar Campo Minado!");
    }
}

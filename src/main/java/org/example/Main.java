package org.example;

import Service.Game;
import java.util.Scanner;

/**
 *  Classe Principal
 */
public class Main {
    public static void main(String[] args) {

        // Configurações iniciais
        Scanner scanner = new Scanner(System.in);
        System.out.println("Bem-vindo ao Campo Minado!");
        System.out.print("Digite o número de linhas: ");
        int rows = scanner.nextInt();
        System.out.print("Digite o número de colunas: ");
        int cols = scanner.nextInt();
        System.out.print("Digite o número de minas: ");
        int mines = scanner.nextInt();

        // Início do jogo
        Game game = new Game(rows, cols, mines);
        game.start(scanner);

        scanner.close();
        System.out.println("Obrigado por jogar Campo Minado!");
    }
}

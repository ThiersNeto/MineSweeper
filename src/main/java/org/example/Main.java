package org.example;

import java.util.Scanner;

/**
 * Classe Principal
 */
public class Main {

    static int rows = 9, cols = 9, mines = 1;
    static int lastGameAmount = 10;
    static Board gameBoard;
    static int gameCount = 1;
    static int wonGameCount;
    static GameStatus currentGameStatus;
    static Game[] lastWonGames;
    static String nickname;
    static boolean gameRunning = false;
    static Scanner scanner = new Scanner(System.in);  // Global Scanner

    public static void main(String[] args) {
        // Menu inicial
        lastWonGames = new Game[lastGameAmount];
        showStartMenu();
    }

    /**
     * Escreve o menu inicial na consola
     */
    private static void showStartMenu() {
        int choice;
        while (true) {
            System.out.println("=== MENU ===");
            System.out.println("1. Começar Jogo");
            System.out.println("2. Mostrar últimos " + lastWonGames.length + " jogos ganhos");
            System.out.println("3. Sair");
            System.out.print("Escolha uma opção: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline

            switch (choice) {
                case 1:
                    // Início do jogo
                    startGame();
                    break;
                case 2:
                    showLastGames();
                    break;
                case 3:
                    System.out.println("Obrigado por jogar Campo Minado!");
                    //scanner.close();
                    System.exit(0);
                    return;  // Exit the program
                default:
                    System.out.println("Opção inválida. Tente novamente.");
                    break;
            }
        }
    }

    /**
     * Lê, interpreta e executa comandos
     */
    public static void interpretCommands() {
        do {
            System.out.println("[Escreva /help para ver os comandos]");
            System.out.print("Comando > ");
            String inputtedCommand = scanner.nextLine();
            String[] slicedCommand = inputtedCommand.split(" ");

            int x = -1;
            int y = -1;
            if (slicedCommand.length > 1) {
                x = asciiConvert(slicedCommand[1].toUpperCase().charAt(0));
                y = asciiConvert(slicedCommand[2].toUpperCase().charAt(0));

                // Coordinates are invalid, return to the beginning of the loop
                if (!gameBoard.isCoordinateValid(x, y)) {
                    System.out.println("Coordenadas invalidas!");
                    continue;
                }
            }

            System.out.print("\n");
            boolean toggled;
            // Remove the "/" from the command
            switch (slicedCommand[0].substring(1)) {
                case ("help"):
                    System.out.print('\n');
                    System.out.println("/help : Apresenta a lista de comandos, a sua função e utilização.");
                    System.out.println("/open <linha> <coluna> : Abre a célula nas coordenadas de tabuleiro - linha/coluna, e.g., /open A 2.");
                    System.out.println("/flag <linha> <coluna> : Marca a célula nas coordenadas de tabuleiro linha/coluna com uma bandeira. Se já existir uma bandeira nessa célula, remove-a.");
                    System.out.println("/hint : Sugere de forma aleatória, uma célula que não contém minas.");
                    System.out.println("/cheat : Comuta o jogo para modo de “batota”, onde as minas são reveladas a cada mostragem do tabuleiro.");
                    System.out.println("/quit : Termina o jogo e volta para o menu principal. Um jogo assim terminado não entra na lista de vitórias.");
                    break;
                case ("open"):
                    openCell(x, y);
                    break;
                case ("flag"):
                    toggled = gameBoard.toggleFlagStatus(x, y);
                    System.out.println("Bandeira " + (toggled ? "colocada" : "removida"));
                    break;
                case ("hint"):
                    Coordinate cleanCoord = gameBoard.getRandomCleanCoordinate();
                    System.out.println("A célula " + cleanCoord + " não tem mina");
                    break;
                case ("cheat"):
                    toggled = gameBoard.toggleCheatMode();
                    System.out.println("Batota " + (toggled ? "ativada" : "desativada"));
                    break;
                case ("quit"):
                    System.out.print("Tem a certeza (y/n): ");
                    char choice = scanner.next().toUpperCase().charAt(0);
                    scanner.nextLine(); // Consume the newline
                    if (choice == 'Y') {
                        return;  // Exit to the menu
                    }
                    break;
            }
            System.out.print('\n');

            // Verifica se o jogador venceu
            if (gameBoard.checkVictory()) {
                currentGameStatus = GameStatus.Won;
                saveGame();
                return;
            }

            System.out.println(gameBoard);

        } while (gameRunning);
    }

    /**
     * Pede a alcunha e inicia o jogo.
     */
    public static void startGame() {
        gameBoard = new Board(rows, cols, mines);

        System.out.print("Insira a alcunha: ");
        nickname = scanner.nextLine();  // Use the global scanner
        if (nickname.isEmpty()) nickname = "Anonymous " + gameCount;
        System.out.println();

        currentGameStatus = GameStatus.Playing;

        System.out.println(gameBoard);
        gameRunning = true;
        interpretCommands();
    }

    /**
     * Abre uma célula no tabuleiro e atua as consequências
     *
     * @param x Linha
     * @param y Coluna
     */
    private static void openCell(int x, int y) {
        // Verifica se o jogador acertou uma mina
        if (gameBoard.hasMine(x, y)) {
            gameBoard.setLosingBoard(x, y);
            System.out.println(gameBoard);
            System.out.println("Você acertou uma mina! Fim de jogo.");
            currentGameStatus = GameStatus.Lost;
            saveGame();
            return;
        }
        // Revela a célula escolhida pelo jogador
        gameBoard.revealCell(x, y);
    }

    /**
     * Salva o jogo nos ultimos ganhos (caso tenha ganhado)
     * Aumenta o numero de jogos feitos para uso nas alcunhas de omissão
     */
    private static void saveGame() {
        gameRunning = false;
        switch (currentGameStatus) {
            case Won:
                System.out.println(gameBoard);
                System.out.println("Parabéns, você venceu!");
                lastWonGames[(wonGameCount) % lastWonGames.length] = new Game(currentGameStatus, nickname, gameBoard.toString());
                wonGameCount++;
                break;
            case Lost:
                break;
        }

        gameCount++;
        showStartMenu();  // Return to the main menu
    }

    /**
     * Escreve os ultimos jogos com as suas estaticas para a consola
     */
    private static void showLastGames() {
        for (Game game : lastWonGames) {
            if (game == null) break;
            System.out.println("==========================================");
            System.out.println(game);
        }
    }

    /**
     * Converte um caracter (A-Z) de ascii em numero, 65 = A, ao retirar 65 estamos a voltar para numeros (B = 66, 66-65 = 1 = posição 1)
     * Se o caracter introduzido for um numero, retorna o numero
     *
     * @param character o codigo ascii para converte
     * @return o caracter interpretado de 0-9
     */
    public static int asciiConvert(int character) {
        if (character >= 65)
            character -= 65;
        else
            character = (character - '0');

        return character;
    }
}

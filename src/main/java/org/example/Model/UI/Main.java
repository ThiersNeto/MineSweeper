package org.example.Model.UI;

import org.example.Model.Game.*;
import org.example.Model.Model.Settings;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Main Class
 */
public class Main {

    //static int rows = 9, cols = 9, mines = 10;
    static int lastGameAmount = 10;
    static Board gameBoard;
    static int gameCount = 1;
    static int wonGameCount;
    static GameStatus currentGameStatus;
    static Game[] lastWonGames;
    static String nickname;
    static boolean gameRunning = false;
    static List<Long> bestTimes = new ArrayList<>(3);
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        lastWonGames = new Game[lastGameAmount];
        simulateTestWins();
        showStartMenu();
    }

    /**
     * Writes the initial menu on the console
     */
    private static void showStartMenu() {
        int choice;
        while (true) {
            System.out.println("=== MENU ===");
            System.out.println("1. Começar Jogo");
            System.out.println("2. Mostrar 3 melhores vitórias");
            System.out.println("3. Mostrar últimos " + lastWonGames.length + " jogos ganhos");;
            System.out.println("4. Sair");
            System.out.print("Escolha uma opção: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline

            switch (choice) {
                case 1:
                    // Início do jogo
                    chooseDifficulty();
                    break;
                case 2:
                    showBestTimes();
                    break;
                case 3:
                    showLastGames();
                    break;
                case 4:
                    System.out.println("Obrigado por jogar Campo Minado!");
                    System.exit(0); // Exit the program
                    return;

                default:
                    System.out.println("Opção inválida. Tente novamente.");
                    break;
            }
        }
    }

    // Função para simular 3 vitórias
    private static void simulateTestWins() {
        bestTimes.add(45L);  // Vitória 1 com tempo de 45 segundos
        bestTimes.add(30L);  // Vitória 2 com tempo de 30 segundos
        bestTimes.add(25L);  // Vitória 3 com tempo de 25 segundos

        // Salva essas vitórias no array de jogos
        lastWonGames[0] = new Game(GameStatus.Won, "Gabriel", "Board 1");
        lastWonGames[1] = new Game(GameStatus.Won, "Saymon", "Board 2");
        lastWonGames[2] = new Game(GameStatus.Won, "Vemba", "Board 3");

        wonGameCount = 3;
    }

    public static void chooseDifficulty(){
        int choice = 0;
        do{
            System.out.print("\n=== Escolha a dificuldade ===");
            for(int i = 1; i <= Difficulty.values().length; i++){
                System.out.printf("\n%d.%s", i, Difficulty.valueOfInt(i).toString());
            }
            System.out.print("\nEscolha uma opção: ");
            choice = scanner.nextInt();
            //if inserted value is a difficulty, its settings are sent over to startGame
            if(Difficulty.exists(choice))
            {
                startGame(Difficulty.valueOfInt(choice).getSettings());
            }
            else
            {
                choice = 0; //invalid choice (no such difficulty exists)
                System.out.println("Valor inserido é inválido");
            }
        }while (choice < 1);
    }

    /**
     * Exibe as 3 melhores vitórias do jogador, com seus tempos.
     */
    static void showBestTimes() {
        System.out.println("=== MELHORES 3 VITÓRIAS ===");
        if (bestTimes.isEmpty()) {
            System.out.println("Ainda não há vitórias registradas.");
        } else {
            for (int i = 0; i < bestTimes.size(); i++) {
                System.out.println("Vitória " + (i + 1) + ": " + bestTimes.get(i) + " segundos");
            }
        }
    }


    /**
     * Reads, interprets and executes commands
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

                //coordinates are invalid, return to the beginning of the loop
                if (!gameBoard.isCoordinateValid(x, y)) {
                    System.out.println("Coordenadas invalidas!");
                    continue;
                }
            }

            System.out.print("\n");
            boolean toggled;
            //remove the "/" from the command
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

            //check if player won
            if (gameBoard.checkVictory()) {
                currentGameStatus = GameStatus.Won;
                saveGame();
                return;
            }

            System.out.println(gameBoard);

        } while (gameRunning);
    }

    /**
     * Inicia o jogo solicitando o nome do jogador, configurando o tabuleiro e iniciando o loop de comandos.
     *
     * A função solicita que o jogador insira um nome (alcunha). Se o jogador não fornecer um nome,
     * será atribuído um nome genérico, como "Anonymous #". Em seguida, a função exibe uma mensagem de
     * boas-vindas dependendo se o jogador inseriu um nome novo ou repetido. O estado inicial do jogo é
     * configurado, o tabuleiro é mostrado, e o loop de interação com o jogador é iniciado.
     *
     * @param settings Configurações do jogo que definem o número de linhas, colunas e minas no tabuleiro.
     */
    public static void startGame(Settings settings) {
        gameBoard = new Board(settings.rows(), settings.cols(), settings.mines());

        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }

        System.out.print("Insira a alcunha: ");
        nickname = scanner.nextLine().trim();

        if (nickname.isEmpty()) {
            // Se o nome for vazio, atribui "Anonymous #" ao jogador
            nickname = "Anonymous " + gameCount;
            gameCount++; // Incrementa o número para o próximo nome anônimo
        }

        // Verifica se o nome foi inserido pela primeira vez
        if (nickname.equals(nickname)) {
            // Se for o mesmo nome do jogador anterior
            System.out.println("Bem-vindo novamente, " + nickname + "!");
        } else {
            // Se for um novo nome, apenas imprime a mensagem de boas-vindas
            System.out.println("Bem-vindo, " + nickname + "!");
        }

        nickname = nickname;
        currentGameStatus = GameStatus.Playing;
        System.out.println(gameBoard);
        gameRunning = true;
        interpretCommands();
    }


    /**
     * Opens a cell on the board and acts accordingly
     *
     * @param x Row
     * @param y Column
     */
    private static void openCell(int x, int y) {
        //check if player hit a mine
        if (gameBoard.hasMine(x, y)) {
            gameBoard.setLosingBoard(x, y);
            System.out.println(gameBoard);
            System.out.println("Você acertou uma mina! Fim de jogo.");
            currentGameStatus = GameStatus.Lost;
            saveGame();
            return;
        }
        //reveals the cell chosen by the player
        gameBoard.revealCell(x, y);
    }

    /**
     * Saves the game on lastWonGames (if player has won)
     * Increments the number of games played to use on default nicknames
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
        showStartMenu();  //return to the main menu
    }

    /**
     * Writes the last won games and their respective status to the console
     */
    private static void showLastGames() {
        for (Game game : lastWonGames) {
            if (game == null) break;
            System.out.println("==========================================");
            System.out.println(game);
        }
    }

    /**
     * Converts a character (A-Z) from ascii into a number (65 = A, so by subtracting 65 we're going back to numbers (B = 66, 66-65 = 1 = position 1))
     * If the inserted character is a number, returns the number
     *
     * @param character ascii code to convert
     * @return character interpreted in 0-9
     */
    public static int asciiConvert(int character) {
        if (character >= 65)
            character -= 65;
        else
            character = (character - '0');

        return character;
    }
}
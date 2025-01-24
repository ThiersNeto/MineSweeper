package org.example;

//import java.util.Scanner;
//import java.util.List;
//import java.util.ArrayList;
import org.w3c.dom.ls.LSOutput;

import java.util.*; //imports everything I could ever need :)
//temp

/**
 * Main Class
 */
public class Main {
    //main is the equivalent of GameManager in Unity!!!
    //static int rows = 9, cols = 9, mines = 10;
    static int lastGameAmount = 10;
    static Board gameBoard;
    static int gameCount = 1;
    static int wonGameCount;
    static GameStatus currentGameStatus;
    static Game[] lastWonGames;
    //static String nickname;

    static boolean gameRunning = false;
    static Scanner scanner = new Scanner(System.in);  // Global Scanner


    //V2 addons here
    static List<Player> players;
    static int currentPlayer; //lets us know the index of the currentPlayer for easier access on the list
    static Map<Player, List<Game>> gameHistory; //stores games won by a player


    public static void main(String[] args) {
        // Menu inicial
        players = new ArrayList<>();
        gameHistory = new HashMap<>();
        lastWonGames = new Game[lastGameAmount];
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
            System.out.println("2. Mostrar últimos " + lastWonGames.length + " jogos ganhos");
            System.out.println("3. Sair");
            System.out.print("Escolha uma opção: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline

            switch (choice) {
                case 1:
                    // Início do jogo
                    //startGame();
                    chooseNickname();
                    break;
                case 2:
                    showLastGames();
                    break;
                case 3:
                    System.out.println("Obrigado por jogar Campo Minado!");
                    //scanner.close();
                    System.exit(0); //exit the program
                    return;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
                    break;
            }
        }
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
                System.out.println(choice + " não corresponde a nenhuma dificuldade. Por favor tente novamente.");
                choice = 0; //invalid choice (no such difficulty exists)
            }
        }while (choice < 1);
    }

    /**
     * Reads, interprets and executes commands
     */
    public static void interpretCommands() {
        do {
            System.out.println("[Escreva /help para ver os comandos]");
            System.out.print("Comando > ");
            String inputtedCommand = scanner.nextLine();
            if(inputtedCommand.isBlank()){
                System.out.println("Nenhum comando detetado. \nPor favor pressione Enter se entende.");
                scanner.nextLine();
                continue;
            }
            String[] slicedCommand = inputtedCommand.split(" ");

            int x = -1;
            int y = -1;
            if (slicedCommand.length > 1) {
                //if the format the length is less than 3 but greater than 1, there is a probability the player forgot to space the chars
                if(slicedCommand.length < 3)
                {
                    //if the length of the first string in the array is greater than 1, then there are 2 chars there, meaning a 0 was typed as a0!!
                    if(slicedCommand[1].length() > 1)
                    {
                        x = asciiConvert(slicedCommand[1].toUpperCase().charAt(0));
                        y = asciiConvert(slicedCommand[1].toUpperCase().charAt(1));
                    }
                }
                //command is in the correct format
                else if(slicedCommand.length == 3){
                    x = asciiConvert(slicedCommand[1].toUpperCase().charAt(0));
                    y = asciiConvert(slicedCommand[2].toUpperCase().charAt(0));
                }
                else{
                    System.out.println("Comando com formato inválido");
                    continue;
                }
            }
            //coordinates are invalid, return to the beginning of the loop
            if (slicedCommand[0].substring(1).equals("open") && !gameBoard.isCoordinateValid(x, y)) {
                System.out.println("Coordenadas invalidas!");
                continue;
            }

            System.out.print("\n");
            boolean toggled;
            gameBoard.addCommand(inputtedCommand);
            //remove the "/" from the command
            switch (slicedCommand[0].substring(1)) {
                case ("help"):
                    System.out.print('\n');
                    System.out.println("/help : Apresenta a lista de comandos, a sua função e utilização.");
                    System.out.println("/open <linha> <coluna> : Abre a célula nas coordenadas de tabuleiro - linha/coluna, e.g., /open A 2.");
                    System.out.println("/flag <linha> <coluna> : Marca a célula nas coordenadas de tabuleiro linha/coluna com uma bandeira. Se já existir uma bandeira nessa célula, remove-a.");
                    System.out.println("/hint : Sugere de forma aleatória, uma célula que não contém minas.");
                    System.out.println("/cheat : Comuta o jogo para modo de “batota”, onde as minas são reveladas a cada mostragem do tabuleiro.");
                    System.out.println("/wins : Mostra ao utilizador um histórico de vitórias de todos os utilizadores (ordenados pela alcunha).");
                    System.out.println("/top : Mostra ao utilizador os melhores 3 tempos, junto da alcunha do respetivo jogador.");
                    System.out.println("/history : Mostra ao utilizador todos os comandos utilizados na consola durante o jogo atual.");
                    System.out.println("/inventory : Mostra ao utilizador o conteúdo do seu inventário de PowerUps.");
                    System.out.println("/use <powerUp> <argumento> : Utiliza um PowerUp. Pode ser utilizado sem argumento (e.g.: /use shield) ou com argumento (e.g /use column C).\nSó é possível utilizar um PowerUp se este estiver no inventário do jogador.");
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
                case ("wins"):
                    for(Player p : players)
                    {
                        //the toString of class game will print it all for me :)
                        System.out.println(gameHistory.get(p));
                        //if this no worki as intended then just \n that
                    }
                    break;
                case ("top"):
                    System.out.println("it never skibidis but it ohios");
                    break;
                case ("history"):
                    gameBoard.showCommandHistory();
                    break;
                case ("inventory"):
                    showPlayerInventory();
                    System.out.println("we're just a bunch of rizzlers");
                    break;
                case ("use"):
                    List<PowerUp> powerUps = new ArrayList<>(players.get(currentPlayer).getInventory().getPowerUps());
                    String s = powerUps.size() > 0 ? "Powerups disponíveis: \n" : "Não tem powerups!";
                    System.out.println(s);
                    for(int i = 0; i < powerUps.size(); i++)
                    {
                        System.out.println((i + 1) + ". " + powerUps.get(i));
                    }

                    PowerUp powerUp = null;
                    int powerUpChoice = scanner.nextInt() - 1;
                    scanner.nextLine();
                    if (powerUps.get(powerUpChoice) != null)
                    {
                        powerUp = powerUps.get(powerUpChoice);
                        powerUps.remove(powerUpChoice);
                    }
                    else{
                        System.out.println("Escolha inválida");
                        continue;
                    }
                    /*if(powerUpChoice < 1 || powerUpChoice > 4) {
                        System.out.println("Escolha inválida");
                        break;
                    }*/

                    /*PowerUpType powerUpType = powerUpType = null;
                    switch (powerUpChoice) {
                        case 1:
                            powerUpType = PowerUpType.SHIELD;
                            break;
                        case 2:
                            powerUpType = PowerUpType.ICE;
                            //gameBoard.activateIce();
                            break;
                        case 3:
                            powerUpType = PowerUpType.LINE;
                            break;
                        case 4:
                            powerUpType = PowerUpType.COLUMN;
                            break;
                        default:
                            System.out.println("Escolha inválida. Operação cancelada.");
                            break;
                    }

                    if (powerUpType == null) {
                        return;
                    }*/

                    //PowerUp powerUp = players.get(currentPlayer).getPowerUp(powerUpType);

                    /*if (powerUp == null) {
                        System.out.println("Você não tem um " + powerUpType + " no inventário.");
                        break;
                    }*/

                    PowerUpType powerUpType = powerUp.getType();
                    Coordinate coord = null;
                    if (powerUpType == PowerUpType.LINE || powerUpType == PowerUpType.COLUMN) {
                        System.out.print("Digite a linha (A, B, C, ...): ");
                        char rowChar = scanner.next().toUpperCase().charAt(0);
                        System.out.print("Digite a coluna (1, 2, 3, ...): ");
                        int col = scanner.nextInt();
                        scanner.nextLine();

                        int row = asciiConvert(rowChar);
                        int colIndex = asciiConvert(col);
                        coord = new Coordinate(row, colIndex);
                    }

                    boolean activated = powerUp.activate(gameBoard, coord);
                    if (activated) {
                        System.out.println(powerUpType + " ativado com sucesso!");
                    } else {
                        System.out.println("Falha ao ativar " + powerUpType + ".");
                    }
                    break;
                case ("quit"):
                    System.out.print("Tem a certeza (y/n): ");
                    char choice = scanner.next().toUpperCase().charAt(0);
                    scanner.nextLine(); // Consume the newline
                    if (choice == 'Y') {
                        return;  // Exit to the menu
                    }
                    break;
                default:
                    System.out.println("Comando inserido é inválido");
                    gameBoard.clearInvalidCommand(); //clears the last inserted command since it's invalid
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
     * Asks for a nickname and begins the game.
     */
    public static void startGame(Settings settings) {
        //System.out.println("GAME START GOT: " + settings);
        System.out.println();
        //if there is the need to get player i can do a current with the index of the list!!!
        gameBoard = new Board(settings.rows(), settings.cols(), settings.mines());
        currentGameStatus = GameStatus.Playing;

        Player player = players.get(currentPlayer);
        player.assignRandomPowerUps();

        //get time from board (needs to be saved on player history)
        //maybe a map where the key is the key and tha game is the value for top 3 fastest
        //ACTUALLY THO, UM MAPA (KEY = PLAYER; VALUE = LIST<GAMES> POGGIWOGGI
        System.out.println(gameBoard);
        gameRunning = true;
        scanner.nextLine();
        interpretCommands();
    }

    public static void chooseNickname(){
        String nickname;
        System.out.print("Insira a alcunha: ");
        nickname = scanner.nextLine();  // Use the global scanner
        if (nickname.isEmpty()) nickname = "Anonymous " + gameCount;
        Player player = new Player(nickname);
        if(players.contains(player)){
            currentPlayer = players.indexOf(player);
            System.out.println("\nBem vindo de volta " + nickname + "!");
        }
        else{
            players.add(player);
            currentPlayer = players.indexOf(player);
            //sorts by alphabetical order (bc it's required for wins to be ordered by nickname)
            players.sort(Comparator.comparing(Player::getNickname));
            //registers the player on the games history and initializes his own history
            gameHistory.put(player, new ArrayList<>());
        }
        chooseDifficulty();
    }

    private static void showPlayerInventory() {
        Player currentPlayerObj = players.get(currentPlayer);
        List<PowerUp> powerUps = currentPlayerObj.getInventory().getPowerUps();

        if (powerUps.isEmpty()) {
            System.out.println("Seu inventário está vazio.");
            return;
        }

        System.out.println("=== INVENTÁRIO ===");
        for (int i = 0; i < powerUps.size(); i++) {
            PowerUp powerUp = powerUps.get(i);
            System.out.printf("%d. %s (Usos restantes: %d)\n",
                    i + 1,
                    powerUp.getType(),
                    powerUp.getUsesRemaining()
            );
        }
    }

    /**
     * Opens a cell on the board and acts accordingly
     *
     * @param x Row
     * @param y Column
     */
    private static void openCell(int x, int y) {
        //check if player hit a mine
        //only if there is no flag on that cell can it be opened
        if(!gameBoard.isFlagged(x, y))
        {
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
        else{
            System.out.println("Não pode abrir uma célula marcada com uma bandeira! Por favor desmarque a bandeira se pretender abrir a célula");
        }
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
                lastWonGames[(wonGameCount) % lastWonGames.length] = new Game(currentGameStatus, players.get(currentPlayer).getNickname(), gameBoard.toString(), gameBoard.getTime());
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

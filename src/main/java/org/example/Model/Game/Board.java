package org.example.Model.Game;

import org.example.Model.Player.Player;
import org.example.Model.PowerUp.PowerUp;
import org.example.Model.PowerUp.PowerUpType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Classe que representa o tabuleiro do jogo.
 */
public class Board {
    private int rows;
    private int cols;
    private int totalMines;
    private int flagCount;
    private long startingTime;
    private boolean cheatMode;
    private int frozenTurns; // Para o PowerUp Ice

    private Cell[][] board;
    private List<String> commandsHistory;

    public Board(int rows, int cols, int totalMines) {
        this.rows = rows;
        this.cols = cols;
        this.totalMines = totalMines;

        flagCount = totalMines;
        startingTime = System.currentTimeMillis();
        board = new Cell[rows][cols];
        commandsHistory = new ArrayList<>();
        frozenTurns = 0; // Inicializa o tempo congelado como 0

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                board[i][j] = new Cell();
            }
        }
        placeMines();
    }

    /**
     * Coloca minas aleatoriamente no tabuleiro.
     */
    private void placeMines() {
        Random random = new Random();
        int minesPlaced = 0;

        while (minesPlaced < totalMines) {
            int x = random.nextInt(rows);
            int y = random.nextInt(cols);

            if (!board[x][y].hasMine()) {
                board[x][y].placeMine();
                minesPlaced++;
            }
        }

        setNeighboringMineCounts();
    }

    /**
     * Calcula o número de minas vizinhas para cada célula.
     */
    private void setNeighboringMineCounts() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                board[i][j].setNeighbouringMines(countNeighboringMines(i, j));
            }
        }
    }

    /**
     * Conta o número de minas vizinhas de uma célula.
     *
     * @param x Linha da célula.
     * @param y Coluna da célula.
     * @return Número de minas vizinhas.
     */
    private int countNeighboringMines(int x, int y) {
        int count = 0;

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int nx = x + i;
                int ny = y + j;
                if (nx >= 0 && nx < rows && ny >= 0 && ny < cols && board[nx][ny].hasMine()) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Revela uma célula e suas vizinhas, se necessário.
     *
     * @param x Linha da célula.
     * @param y Coluna da célula.
     */
    public void revealCell(int x, int y) {
        if (!isCoordinateValid(x, y) || board[x][y].isRevealed() || board[x][y].hasMine()) {
            return;
        }

        board[x][y].revealCell();
        updateVisual(x, y);

        // Se a célula não tiver minas vizinhas, revela as células vizinhas
        if (board[x][y].getNeighbouringMines() == 0) {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (i == 0 && j == 0) continue; // Ignora a célula atual
                    revealCell(x + i, y + j);
                }
            }
        }
    }

    /**
     * Abre uma célula e verifica se o jogador acertou uma mina.
     *
     * @param x      Linha da célula.
     * @param y      Coluna da célula.
     * @param player O jogador atual.
     */
    public void openCell(int x, int y, Player player) {
        if (!isCoordinateValid(x, y) || board[x][y].isRevealed()) {
            return;
        }

        if (board[x][y].hasMine()) {
            if (player.hasPowerUp(PowerUpType.SHIELD) && player.getPowerUp(PowerUpType.SHIELD).getUsesRemaining() > 0) {
                // Usar o Shield para impedir a explosão
                System.out.println("Shield ativado! Mina não explodiu.");
                board[x][y].toggleFlag(); // Colocar bandeira permanente
                player.getPowerUp(PowerUpType.SHIELD).activate(this, new Coordinate(x, y));
                return;
            } else {
                // Explodir a mina (fim de jogo)
                setLosingBoard(x, y);
                System.out.println("Você acertou uma mina! Fim de jogo.");
                return;
            }
        }

        // Revelar a célula normalmente
        revealCell(x, y);
    }

    public boolean toggleFlagStatus(int x, int y){
        if(!board[x][y].isFlagged() && flagCount <= 0){
            System.out.println("Sem bandeiras disponíveis!");
            return false;
        }

        if(!board[x][y].isFlagged())
            flagCount--;
        else
            flagCount++;

        board[x][y].toggleFlag();

        updateVisual(x,y);

        return true;
    }

    /**
     * Revela todas as células do tabuleiro quando o jogador perde o jogo.
     */
    public void setLosingBoard(int x, int y) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                board[i][j].revealCell(); // Revela todas as células
            }
        }
        // Atualiza a visualização da célula minada que foi aberta
        board[x][y].setChar('\u25C8');
        System.out.println(this); // Exibe o tabuleiro atualizado
    }

    /**
     * Alterna o status da bandeira em uma célula.
     *
     * @param x Linha da célula.
     * @param y Coluna da célula.
     * @return "true" se a bandeira foi colocada ou removida com sucesso.
     */
    public boolean toggleFlag(int x, int y) {
        if (!isCoordinateValid(x, y)) {
            return false;
        }

        if (!board[x][y].isFlagged() && flagCount <= 0) {
            System.out.println("Sem bandeiras disponíveis!");
            return false;
        }

        if (!board[x][y].isFlagged()) {
            flagCount--;
        } else {
            flagCount++;
        }

        board[x][y].toggleFlag();
        updateVisual(x, y);
        return true;
    }

    /**
     * Congela o tempo por um número específico de jogadas.
     *
     * @param turns Número de jogadas com o tempo congelado.
     */
    public void freezeTime(int turns) {
        this.frozenTurns = turns;
        System.out.println("Tempo congelado por " + turns + " jogadas.");
    }

    /**
     * Atualiza o tempo do jogo, considerando o tempo congelado.
     */
    public void updateTimer() {
        if (frozenTurns > 0) {
            System.out.println("Tempo congelado. Jogadas restantes: " + frozenTurns);
            return; // Não atualiza o tempo real
        } else {
            // Atualiza o tempo normalmente
            long elapsedTime = System.currentTimeMillis() - startingTime;
            String formattedTime = String.format("%02d:%02d:%02d",
                    TimeUnit.MILLISECONDS.toHours(elapsedTime),
                    TimeUnit.MILLISECONDS.toMinutes(elapsedTime) % 60,
                    TimeUnit.MILLISECONDS.toSeconds(elapsedTime) % 60
            );
            System.out.println("Tempo decorrido: " + formattedTime);
        }
    }

    /**
     * Verifica se o jogador venceu o jogo.
     *
     * @return "true" se o jogador venceu.
     */
    public boolean checkVictory() {
        boolean allMinesFlagged = true;
        boolean allNonMinesRevealed = true;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (board[i][j].hasMine() && !board[i][j].isFlagged()) {
                    allMinesFlagged = false;
                }
                if (!board[i][j].hasMine() && !board[i][j].isRevealed()) {
                    allNonMinesRevealed = false;
                }
            }
        }
        return allMinesFlagged || allNonMinesRevealed;
    }

    /**
     * Retorna uma coordenada aleatória que não contém uma mina e não foi revelada.
     *
     * @return Coordenada segura.
     */
    public Coordinate getRandomCleanCoordinate() {
        Random random = new Random();
        int x, y;
        do {
            x = random.nextInt(rows);
            y = random.nextInt(cols);
        } while (board[x][y].hasMine() || board[x][y].isRevealed() || !isCoordinateValid(x, y));
        return new Coordinate(x, y);
    }

    /**
     * Verifica se uma célula contém uma mina.
     *
     * @param x Linha da célula.
     * @param y Coluna da célula.
     * @return "true" se a célula contém uma mina.
     */
    public boolean hasMine(int x, int y) {
        return isCoordinateValid(x, y) && board[x][y].hasMine();
    }

    /**
     * Alterna o modo de trapaça (cheat mode).
     *
     * @return "true" se o modo de trapaça está ativado.
     */
    public boolean toggleCheatMode() {
        cheatMode = !cheatMode;
        updateBoardVisual();
        return cheatMode;
    }

    /**
     * Verifica se as coordenadas são válidas.
     *
     * @param x Linha da célula.
     * @param y Coluna da célula.
     * @return "true" se as coordenadas são válidas.
     */
    public boolean isCoordinateValid(int x, int y) {
        return x >= 0 && x < rows && y >= 0 && y < cols;
    }

    /**
     * Atualiza a visualização de todas as células do tabuleiro.
     */
    private void updateBoardVisual() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                updateVisual(i, j);
            }
        }
    }

    /**
     * Atualiza a visualização de uma célula específica.
     *
     * @param x Linha da célula.
     * @param y Coluna da célula.
     */
    private void updateVisual(int x, int y) {
        if (!board[x][y].isRevealed()) {
            board[x][y].setChar('\u25A0'); // ■
            if (cheatMode && board[x][y].hasMine()) {
                board[x][y].setChar('\u25C6'); // ◆
            }
            if (board[x][y].isFlagged()) {
                board[x][y].setChar('\u25B6'); // ▶
            }
            return;
        }

        // Célula revelada
        board[x][y].setChar('\u25A1'); // □
        if (board[x][y].getNeighbouringMines() > 0) {
            board[x][y].setChar((char) (board[x][y].getNeighbouringMines() + '0'));
        }
        if (board[x][y].isFlagged()) {
            board[x][y].setChar('\u25B6'); // ▶
        }
        if (board[x][y].hasMine()) {
            board[x][y].setChar('\u25C6'); // ◆
        }
    }


    // V2 - Thiers
    private void placePowerUp() {
        Random random = new Random();
        List<PowerUpType> powerUpTypes = Arrays.asList(
                PowerUpType.SHIELD,
                PowerUpType.ICE,
                PowerUpType.LINE,
                PowerUpType.COLUMN
        );

        for (PowerUpType powerUpType : powerUpTypes) {
            boolean placed = false;
            while (!placed) {
                int x = random.nextInt(rows);
                int y = random.nextInt(cols);

                Cell cell = board[x][y];
                if (!cell.hasMine() && !cell.hasPowerUp()) {
                    cell.setPowerUp(powerUpType);
                    placed = true;
                }
            }
        }
    }

    /**
     * Adiciona um comando ao histórico de comandos.
     *
     * @param command Comando a ser adicionado.
     */
    public void addCommand(String command) {
        commandsHistory.add(command);
    }

    /**
     * Mostra o histórico de comandos utilizados.
     */
    public void showCommandHistory() {
        System.out.println("HISTÓRICO DE COMANDOS UTILIZADOS:");
        for (int i = 0; i < commandsHistory.size(); i++) {
            System.out.printf("%d. %s\n", (i + 1), commandsHistory.get(i));
        }
    }

    /**
     * Remove o último comando inválido do histórico.
     */
    public void clearInvalidCommand() {
        if (!commandsHistory.isEmpty()) {
            commandsHistory.remove(commandsHistory.size() - 1);
        }
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    /**
     * Retorna uma representação visual do tabuleiro.
     *
     * @return String representando o tabuleiro.
     */
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\u2665⠀ "); // ♥
        for (int i = 0; i < cols; i++) {
            stringBuilder.append(i).append("  ");
        }
        stringBuilder.append('\n');

        for (int i = 0; i < rows; i++) {
            stringBuilder.append((char) ('A' + i)).append("  ");
            for (int j = 0; j < cols; j++) {
                stringBuilder.append(board[i][j].getChar()).append("  ");
            }
            stringBuilder.append('\n');
        }

        stringBuilder.append("\n\tBandeiras Disponíveis: ").append(flagCount);

        long elapsedTime = System.currentTimeMillis() - startingTime;
        String formattedTime = String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(elapsedTime),
                TimeUnit.MILLISECONDS.toMinutes(elapsedTime) % 60,
                TimeUnit.MILLISECONDS.toSeconds(elapsedTime) % 60
        );

        stringBuilder.append("\n\tTempo decorrido: ").append(formattedTime);
        stringBuilder.append('\n');
        return stringBuilder.toString();
    }

}
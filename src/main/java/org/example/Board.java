package org.example;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Class Board represents the board
 */
public class Board {
    private int rows;
    private int cols;
    private int totalMines;
    private int flagCount;
    private long startingTime; //will need to mess with this for ice
    private boolean cheatMode;
    private int frozenTurns;

    //V2 (TODO board and starting time and even totalMines can all be final)
    private Cell[][] board;
    private List<String> commandsHistory;
    private long iceStart;
    private boolean iceActive;

    /**
     *
     * @param rows          Number of Rows
     * @param cols          Number of Columns
     * @param totalMines    Number of Mines
     */
    public Board(int rows, int cols, int totalMines) {
        this.rows = rows;
        this.cols = cols;
        this.totalMines = totalMines;

        //on the beginning ice will never be able to start as activated right away
        this.iceStart = 0;
        this.iceActive = false;

        flagCount = totalMines;
        startingTime = System.currentTimeMillis();
        board = new Cell[rows][cols];
        commandsHistory = new ArrayList<>();
        frozenTurns = 0;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                board[i][j] = new Cell();
            }
        }
        placeMines();
    }

    /**
     * Randomly places mines
     */
    private void placeMines() {
        Random random = new Random();
        int minesPlaced = 0;

        while (minesPlaced < totalMines) {
            int x = random.nextInt(rows);
            int y = random.nextInt(cols);

            if(!board[x][y].hasMine())
            {
                board[x][y].placeMine();
                minesPlaced++;
            }
        }

        setNeighboringMineCounts();
    }

    /**
     * Calculates and defines the number of neighbouring mine on all cells
     */
    private void setNeighboringMineCounts() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                board[i][j].setNeighbouringMines(countNeighboringMines(i, j));
            }
        }
    }

    /**
     * Calculates the amount of neighbouring mines of a cell
     *
     * @param x     Row
     * @param y     Column
     * @return      Number of mines
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
     * Recursive function that reveals the cell and the surrounding cells if these don't have mines or neighbouring mines
     *
     * @param x Row
     * @param y Column
     */
    public void revealCell(int x, int y) {
        if ( !isCoordinateValid(x, y) || board[x][y].isRevealed() || board[x][y].hasMine())  //se já tiver revelado ou posição invalida, sair
            return;

        //initial flag verification is done on main's side, so if this gets here it means main gave the okay
        //which means that a flag isn't being opened directly, but via a neighbouring cell
        if(board[x][y].isFlagged()) toggleFlagStatus(x, y);
        board[x][y].revealCell();
        updateVisual(x,y);

        //se a celula não tiver minas ou minas vizinhas, abrir as vizinhas
        if (board[x][y].getNeighbouringMines() == 0) {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (i == 0 && j == 0) continue; // Passar a frente a célula atual (x, y)
                    revealCell(x + i, y + j);
                }
            }
        }
    }

    /**
     * Updates a cell's visual
     *
     * @param x Row
     * @param y Column
     */
    private void updateVisual(int x, int y){
        //orderm de operações é importante aqui

        if(!board[x][y].isRevealed()){
            board[x][y].setChar('\u25A0'); //■
            if(cheatMode){
                if(board[x][y].hasMine()) board[x][y].setChar('\u25C6'); //◆
            }
            if(board[x][y].isFlagged()) board[x][y].setChar('\u25B6'); //▶

            return;
        }

        //case cell is revealed
        board[x][y].setChar('\u25A1'); //□
        if(board[x][y].getNeighbouringMines() > 0) board[x][y].setChar((char) (board[x][y].getNeighbouringMines() + '0')); //maneira de converter char para int, o +0 serve para mudar o codigo ascii
        if(board[x][y].isFlagged()) board[x][y].setChar('\u25B6'); //▶
        if(board[x][y].hasMine()) board[x][y].setChar('\u25C6'); //◆
    }

    /**
     * Toggles the flag on a cell
     * Increases or decreases the amount of flags
     *
     * @param x Row
     * @param y Column
     *
     * @return "true" if user managed to set/unset a flag
     */
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

    /*public void freezeTime(int turns) {
        this.frozenTurns = turns;
        System.out.println("Tempo congelado por " + turns + " jogadas.");
    }/*


    /**
     * Changes the board to show all discovered mines
     *
     * @param x Row of the selected mine
     * @param y Column of the selected mine
     */
    public void setLosingBoard(int x, int y){
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                board[i][j].revealCell();
            }
        }
        updateBoardVisual();
        //updates the visual of the clicked mine
        board[x][y].setChar('\u25C8'); //◈;
        System.out.println(this);
    }

    /**
     *
     * @return Returns "true" if the board sets winning conditions
     */
    public boolean checkVictory() {
        boolean allMinesFlagged = true;
        boolean allNonMinesRevealed = true;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if ((board[i][j].hasMine() && !board[i][j].isFlagged()))
                    allMinesFlagged = false;
                if((!board[i][j].hasMine() && !board[i][j].isRevealed()))
                    allNonMinesRevealed = false;


            }
        }
        return allMinesFlagged || allNonMinesRevealed;
    }

    /**
     *
     * @return A random coordinate inside the board that does not have a mine and is not revealed
     */
    public Coordinate getRandomCleanCoordinate(){
        Random random = new Random();
        int x;
        int y;
        do{
            x = random.nextInt(rows);
            y = random.nextInt(cols);
        } while(board[x][y].hasMine() || board[x][y].isRevealed() || !isCoordinateValid(x,y)); //repeats if there is a mine, is already revealed or is not valid

        return new Coordinate(x,y);
    }

    /**
     * Checks if the cell has a mine
     *
     * @param x Row
     * @param y Column
     * @return "true" if it has a mine
     */
    public boolean hasMine(int x, int y) {
        return board[x][y].hasMine();
    }

    /**
     * Toggles cheat mode
     *
     * @return "true" if cheat mode is turned on
     */
    public boolean toggleCheatMode(){
        cheatMode = !cheatMode;

        updateBoardVisual();
        return cheatMode;
    }

    /**
     * Validates a coordinate
     *
     * @param x Row
     * @param y Column
     * @return "true" if the coordinates are inside the bounds
     */
    public boolean isCoordinateValid(int x, int y){
        return (x >= 0 && x < rows && y >= 0 && y < cols);
    }

    /**
     * Runs through every cell and updates their visual (character)
     */
    private void updateBoardVisual(){
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                updateVisual(i,j);
            }
        }
    }

    // V2
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

    public void addCommand(String command)
    {
        commandsHistory.add(command);
    }

    public void showCommandHistory(){
        System.out.println("HISTÓRICO DE COMANDOS UTILIZADOS: ");
        for(int i = 0; i < commandsHistory.size(); i++)
        {
            System.out.printf("%d. %s\n", (i + 1), commandsHistory.get(i));
        }
    }

    public void clearInvalidCommand(){
        commandsHistory.remove(commandsHistory.size() - 1);
    }

    public boolean isFlagged(int x, int y)
    {
        return board[x][y].isFlagged();
    }

    public long getTime(){
        return (System.currentTimeMillis() - startingTime);
    }

    public void activateIce(int turns)
    {
        frozenTurns += turns;
        iceStart = System.currentTimeMillis();
        iceActive = true;
    }

    public void deactivateIce() {
        iceActive = false;
        startingTime = startingTime + (System.currentTimeMillis() - iceStart);
        iceStart = 0L;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    /**
     * Build a string of the board, with statistics
     * Takes advantage of Ascii to turn a number into a character, A is 65 in ascii, the following follow in alphabetic order (B = 66, C = 67)
     *
     * @return The Board
     */
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\u2665⠀ "); //♥
        //print columns
        for (int i = 0; i < cols; i++) {
            stringBuilder.append((i)).append("  ");
        }
        stringBuilder.append('\n');
        for (int i = 0; i < rows; i++) {
            stringBuilder.append((char) ('A' + i)).append("  "); //print rows // ascii trick explained in javadoc
            for (int j = 0; j < cols; j++) {
                stringBuilder.append(board[i][j].getChar()).append("  ");
            }
            stringBuilder.append('\n');
        }

        stringBuilder.append('\n').append("\tBandeiras Disponiveis: ").append(flagCount);

        long elapsedTime;
        if(iceActive)
        {
            long frozenTime = System.currentTimeMillis() - iceStart;
            elapsedTime = System.currentTimeMillis() - startingTime - frozenTime;
        }
        else {
            elapsedTime = System.currentTimeMillis() - startingTime;
        }
        String formattedTime = String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(elapsedTime),
                TimeUnit.MILLISECONDS.toMinutes(elapsedTime) % 60,
                TimeUnit.MILLISECONDS.toSeconds(elapsedTime) % 60
        );

        stringBuilder.append('\n').append("\tTempo decorrido: ").append(formattedTime);

        stringBuilder.append('\n');
        return stringBuilder.toString();
    }
}

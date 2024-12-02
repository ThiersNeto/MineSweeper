package org.example;

import java.text.SimpleDateFormat;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Class Board represents the board
 */
public class Board {
    private char[][] visualGrid;
    private int[][] neighboringCountGrid;
    private boolean[][] minesGrid;
    private boolean[][] flagsGrid;
    private boolean[][] revealedGrid;
    private int rows;
    private int cols;
    private int totalMines;
    private int flagCount;
    private long startingTime;
    private boolean cheatMode;

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
        flagCount = totalMines;

        startingTime = System.currentTimeMillis();

        visualGrid = new char[rows][cols];
        minesGrid = new boolean[rows][cols];
        revealedGrid = new boolean[rows][cols];
        flagsGrid = new boolean[rows][cols];
        neighboringCountGrid = new int[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                visualGrid[i][j] = '■';
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

            if (!minesGrid[x][y]) {
                minesGrid[x][y] = true;
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
                neighboringCountGrid[i][j] = countNeighboringMines(i, j);
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
                if (nx >= 0 && nx < rows && ny >= 0 && ny < cols && minesGrid[nx][ny]) {
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
        if ( !isCoordinateValid(x, y) || revealedGrid[x][y] || minesGrid[x][y])  //se já tiver revelado ou posição invalida, sair
            return;

        revealedGrid[x][y] = true;
        updateVisual(x,y);

        //se a celula não tiver minas ou minas vizinhas, abrir as vizinhas
        if (neighboringCountGrid[x][y] == 0) {
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

        if(!revealedGrid[x][y]){
            visualGrid[x][y] = '■';
            if(cheatMode){
                if(minesGrid[x][y]) visualGrid[x][y] = '◆';
            }
            if(flagsGrid[x][y]) visualGrid[x][y] = '▶';

            return;
        }

        //case cell is revealed
        visualGrid[x][y] = '□';
        if(neighboringCountGrid[x][y] > 0) visualGrid[x][y] = (char) (neighboringCountGrid[x][y] + '0'); //maneira de converter char para int, o +0 serve para mudar o codigo ascii
        if(flagsGrid[x][y]) visualGrid[x][y] = '▶';
        if(minesGrid[x][y]) visualGrid[x][y] = '◆';
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
        if(!flagsGrid[x][y] && flagCount <= 0)
            return false;

        if(!flagsGrid[x][y])
            flagCount--;
        else
            flagCount++;


        flagsGrid[x][y] = !flagsGrid[x][y];

        updateVisual(x,y);

        return true;
    }



    /**
     * Changes the board to show all discovered mines
     *
     * @param x Row of the selected mine
     * @param y Column of the selected mine
     */
    public void setLosingBoard(int x, int y){
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                revealedGrid[i][j] = true;
            }
        }
        updateBoardVisual();
        //updates the visual of the clicked mine
        visualGrid[x][y] = '◈';
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
                if ((minesGrid[i][j] && !flagsGrid[i][j]))
                    allMinesFlagged = false;
                if((!minesGrid[i][j] && !revealedGrid[i][j]))
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
        } while(minesGrid[x][y] || revealedGrid[x][y] || !isCoordinateValid(x,y)); //repeats if there is a mine, is already revealed or is not valid

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
        return minesGrid[x][y];
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

    /**
     * Build a string of the board, with statistics
     * Takes advantage of Ascii to turn a number into a character, A is 65 in ascii, the following follow in alphabetic order (B = 66, C = 67)
     *
     * @return The Board
     */
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("♥⠀ ");
        //print columns
        for (int i = 0; i < cols; i++) {
            stringBuilder.append((i)).append("  ");
        }
        stringBuilder.append('\n');
        for (int i = 0; i < rows; i++) {
            stringBuilder.append((char) ('A' + i)).append("  "); //print rows // ascii trick explained in javadoc
            for (int j = 0; j < cols; j++) {
                stringBuilder.append(visualGrid[i][j]).append("  ");
            }
            stringBuilder.append('\n');
        }

        stringBuilder.append('\n').append("\tBandeiras Disponiveis: ").append(flagCount);

        long elapsedTime = System.currentTimeMillis() - startingTime;
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

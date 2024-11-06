package Model;

import java.util.Random;

/**
 * A Classe Board representa o tabuleiro
 */
public class Board {
    private Cell[][] grid;
    private int rows;
    private int cols;
    private int totalMines;

    /**
     *
     * @param rows          Número de Linhas
     * @param cols          Número de Colunas
     * @param totalMines    Número de Minas
     */
    public Board(int rows, int cols, int totalMines) {
        this.rows = 9;
        this.cols = 9;
        this.totalMines = totalMines;
        this.grid = new Cell[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[i][j] = new Cell();
            }
        }
        placeMines();
        calculateNeighboringMines();
    }

    /**
     * Coloca minas aleatoriamente
     */
    private void placeMines() {
        Random random = new Random();
        int minesPlaced = 0;

        while (minesPlaced < totalMines) {
            int x = random.nextInt(rows);
            int y = random.nextInt(cols);

            if (!grid[x][y].hasMine()) {
                grid[x][y].placeMine();
                minesPlaced++;
            }
        }
    }

    /**
     * Calcula e define o número de minas vizinhas
     */
    private void calculateNeighboringMines() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int count = countNeighboringMines(i, j);
                grid[i][j].setNeighboringMines(count);
            }
        }
    }

    /**
     *
     * @param x     Linha
     * @param y     Coluna
     * @return      Número de Minas
     */
    private int countNeighboringMines(int x, int y) {
        int count = 0;
        for (int i = 1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int nx = x + i;
                int ny = y + j;
                if (nx >= 0 && nx < rows && ny >= 0 && ny < cols && grid[nx][ny].hasMine()) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Revela a céluna se ela nao tiver minas vizinhas
     *
     * @param x
     * @param y
     */
    public void revealCell(int x, int y) {
        if (x < 0 || x >= rows || y < 0 || y >= cols || grid[x][y].isRevealed()) {
            return;
        }
        grid[x][y].reveal();
        if (grid[x][y].getNeighboringMines() == 0 && !grid[x][y].hasMine()) {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    revealCell(x + i, y + j);
                }
            }
        }
    }

    public boolean hasMine(int x, int y) {
        return grid[x][y].hasMine();
    }

    public boolean isRevealed(int x, int y) {
        return grid[x][y].isRevealed();
    }

    /**
     * Imprime o tabuleiro atual
     */
    public void printBoard() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }
}

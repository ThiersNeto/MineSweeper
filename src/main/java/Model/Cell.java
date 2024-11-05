package Model;

/**
 * A classe Cell representa uma célula individual no tabuleiro
 */
public class Cell {
    private boolean mine;
    private boolean revealed;
    private int neighboringMines;

    public Cell() {
        this.mine = false;
        this.revealed = false;
        this.neighboringMines = 0;
    }

    public boolean hasMine() {
        return mine;
    }

    public void placeMine() {
        this.mine = true;
    }

    public boolean isRevealed() {
        return revealed;
    }

    public void reveal() {
        this.revealed = true;
    }

    public int getNeighboringMines() {
        return neighboringMines;
    }

    public void setNeighboringMines(int neighboringMines) {
        this.neighboringMines = neighboringMines;
    }

    @Override
    public String toString() {
        if (!revealed) {
            return ".";
        }
        return mine ? "*" : Integer.toString(neighboringMines);
    }
}

package org.example;

public class Cell {
    private boolean mine;
    private boolean flagged;
    private boolean revealed;
    private int neighbouringMines;

    public Cell(){

    }

    public boolean hasMine()
    {
        return this.mine;
    }

    public boolean placeMine()
    {
        return this.mine = true;
    }

    public boolean isRevealed()
    {
        return this.revealed;
    }

    public boolean revealCell()
    {
        return this.revealed = true;
    }

    public boolean isFlagged(){
        return this.flagged;
    }

    public boolean toggleFlag(){
        flagged = !flagged;
        return flagged;
    }

    public int getNeighbouringMines(){
        return this.neighbouringMines;
    }

    public void setNeighbouringMines(int neighbouringMines){
        this.neighbouringMines = neighbouringMines;
    }
}

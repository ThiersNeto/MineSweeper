package org.example;

public class Cell {
    private boolean mine;
    private boolean flagged;
    private boolean revealed;
    private int neighbouringMines;
    private char visual;

    public Cell(){
        //on creation values will all be as follows:
        this.mine = false;
        this.flagged = false;
        this.revealed = false;
        this.neighbouringMines = 0;
        this.visual = '\u25A0';
    }

    public boolean hasMine()
    {
        return this.mine;
    }

    public void placeMine()
    {
        this.mine = true;
    }

    public boolean isRevealed()
    {
        return this.revealed;
    }

    public void revealCell()
    {
        this.revealed = true;
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

    public void setChar(char visual){
        this.visual = visual;
    }

    public char getChar(){
        return this.visual;
    }
}

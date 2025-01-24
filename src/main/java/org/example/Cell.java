package org.example;

public class Cell {
    private boolean mine;
    private boolean flagged;
    private boolean revealed;
    private int neighbouringMines;
    private char visual;
    private PowerUpType powerUp;
    private boolean lockedCell;

    public Cell(){
        //on creation values will all be as follows:
        this.mine = false;
        this.flagged = false;
        this.revealed = false;
        this.neighbouringMines = 0;
        this.visual = '\u25A0';
        this.lockedCell = false;
    }

    /**
     * Locks the cell, preventing present flags from being removed. <br>
     * It's used when a shield prevents the players death, locking that Cell from being unflagged
     * due to the certainty that there is a mine.
     */
    public void lockCell()
    {
        this.lockedCell = true;
    }

    /**
     * Checks if a Cell is locked.
     * @return true if Cells is Locked, false if not.
     */
    public boolean isCellLocked()
    {
        return this.lockedCell;
    }

    /**
     * Checks whether theres is a mine on this Cell or not
     * @return True if there is a mine, false if there is none
     */
    public boolean hasMine()
    {
        return this.mine;
    }

    /**
     * Places a mine on this Cell
     */
    public void placeMine()
    {
        this.mine = true;
    }

    /**
     * Checks if this Cell is revealed.
     * @return True if this Cells is revealed, false otherwise
     */
    public boolean isRevealed()
    {
        return this.revealed;
    }

    /**
     * Stores the information that this Cell is revealed
     */
    public void revealCell()
    {
        this.revealed = true;
    }

    /**
     * Checks whether or not this Cell is flagged
     * @return True if this cell is flagged, false otherwise
     */
    public boolean isFlagged(){
        return this.flagged;
    }

    /**
     * Toggles the true/false state of the flagged boolean of a Cell
     * @return !flagged
     */
    public boolean toggleFlag(){
        flagged = !flagged;
        return flagged;
    }

    /**
     * Checks whether or not this Cell has a PowerUp associated to it
     *
     * @return true if there is a PowerUp, false otherwise
     */
    public boolean hasPowerUp() {
        return powerUp != null;
    }

    /**
     * Gets the number of mines adjacent to the current Cell
     *
     * @return Number of neighbouring mines
     */
    public int getNeighbouringMines(){
        return this.neighbouringMines;
    }

    /**
     * Sets the number of neighbouring mines that this Cell has
     *
     * @param neighbouringMines number of neighbouring mines counted on board
     */
    public void setNeighbouringMines(int neighbouringMines){
        this.neighbouringMines = neighbouringMines;
    }

    /**
     * Alters the char of the Cell to the one corresponding to its current state
     *
     * @param visual char of flag, mine, etc...
     */
    public void setChar(char visual){
        this.visual = visual;
    }

    /**
     * Gets the char this mine is associated with
     * @return char of flag, mine, etc...
     */
    public char getChar(){
        return this.visual;
    }

    /**
     * Checks what power up is present on this Cell
     * @return the power up type
     */
    public PowerUpType getPowerUp() {
        return powerUp;
    }

    /**
     * Sets this Cell's PowerUp Type
     * @param powerUp Power Up Type
     */
    public void setPowerUp(PowerUpType powerUp) {
        this.powerUp = powerUp;
    }

}

package org.example;

/**
 *
 * Class that represents a PowerUp in the game.
 */
public class PowerUp {
    private PowerUpType type;

    /**
     *
     * Constructs a new PowerUp of the specified type.
     *
     * @param type The type of PowerUp to create (SHIELD, ICE, LINE, COLUMN, etc.)
     *
     */
    public PowerUp(PowerUpType type) {
        this.type = type;
    }

    /**
     * Gets the type of this PowerUp
     * @return The PowerUpType associated with this instance
     *
     */
    public PowerUpType getType() {
        return type;
    }

    /**
     *
     * @param board The game board where the effect will be applied
     * @param coord Coordinate required for some PowerUp types (LINE/COLUMN)
     * @return true if activation succeeded, false otherwise
     */
    public boolean activate(Board board, Coordinate coord) {
        switch (type) {
            case SHIELD:
                return activateShield(board);
            case ICE:
                return activateIce(board);
            case LINE:
                return activateLine(board, coord);
            case COLUMN:
                return activateColumn(board, coord);
            default:
                System.out.println("Tipo de PowerUp desconhecido.");
                return false;
        }
    }

    /**
     * Prevents a mine from exploding on the next turn the player opens a mined cell.
     * Places a permanent flag on the mined cell.
     */
    private boolean activateShield(Board board) {
        System.out.println("Shield ativado! Você está protegido na próxima jogada.");
        board.activateShield();

        return true;
    }

    /**
     *
     * Freezes time (stopwatch) for 3 plays.
     */
    private boolean activateIce(Board board) {
        System.out.println("Ice ativado! O tempo está congelado por 3 jogadas.");
        board.activateIce(3);
        return true;
    }

    /**
     *
     * Reveals all cells in a row except mined cells.
     * Places a permanent flag on mined cells.
     */
    private boolean activateLine(Board board, Coordinate coord) {
        int row = coord.getX();
        for (int col = 0; col < board.getCols(); col++) {
            if (!board.hasMine(row, col)) {
                board.revealCell(row, col);         // Reveals cells without mines
            } else {
                board.toggleFlagStatus(row, col);   // Places permanent flag on mined cells
            }
        }
        System.out.println("Line ativado! A linha " + row + " foi revelada.");
        return true;
    }

    /**
     *
     * Reveals all cells in a column except mined cells.
     * Places a permanent flag on mined cells.
     */
    private boolean activateColumn(Board board, Coordinate coord) {
        int col = coord.getY();
        for (int row = 0; row < board.getRows(); row++) {
            if (!board.hasMine(row, col)) {
                board.revealCell(row, col); // Revela células sem minas
            } else {
                board.toggleFlagStatus(row, col); // Coloca bandeira permanente em células minadas
            }
        }
        System.out.println("Column ativado! A coluna " + col + " foi revelada.");
        return true;
    }

    /**
     *
     * Returns a human-readable string representation of the PowerUp type.
     * <p>
     * Converts enum constants to display-friendly format:
     *
     * <ul>
     *   <li>{@code SHIELD}     → "Shield"</li>
     *   <li>{@code ICE}        → "Ice"</li>
     *   <li>{@code LINE}       → "Line"</li>
     *   <li>{@code COLUMN}     → "Column"</li>
     *   <li>{@code HINT}       → "Hint"</li>
     * </ul>
     *
     * @return Formatted name of the PowerUp type suitable for display
     *
     * <br><br> This override provides localized/capitalized names compared to
     *           the default enum naming convention
     */
    @Override
    public String toString() {
        return "" + type;
    }
}
    package org.example;


    /**
     * Returns the user-friendly textual representation of the PowerUp.
     *
     * Overrides the default implementation of {@code toString()} to provide more descriptive names
     * suitable for display to the user.
     *
     */
    public enum PowerUpType {
        SHIELD, ICE, LINE, COLUMN, HINT;
        @Override
        public String toString() {
            return switch (this){
                case SHIELD -> "Shield";
                case ICE -> "Ice";
                case LINE -> "Line";
                case COLUMN -> "Column";
                case HINT -> "Hint";
            };
        }
    }

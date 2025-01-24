    package org.example;

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

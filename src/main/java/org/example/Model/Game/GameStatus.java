package org.example.Model.Game;

/**
 * Game Status
 */
public enum GameStatus {
    Playing,
    Won,
    Lost;

    @Override
    public String toString(){
        return switch (this) {
            case Playing -> "Progresso";
            case Won -> "Ganho";
            case Lost -> "Perdido";
        };
    }
}

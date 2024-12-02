package org.example;

import java.util.stream.LongStream;

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

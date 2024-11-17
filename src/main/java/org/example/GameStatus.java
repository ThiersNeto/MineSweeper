package org.example;

import java.util.stream.LongStream;

/**
 * Estado do jogo, utilizado
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

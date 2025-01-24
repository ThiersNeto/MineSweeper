package org.example;

import java.util.stream.LongStream;

/**
 * Represents the possible states of a game session.
 * <p>
 * Tracks the current progression and outcome of gameplay, with localized string representations
 * for user interface display in Portuguese.
 */
public enum GameStatus {
    Playing,
    Won,
    Lost;


    /**
     *
     * Provides localized Portuguese display text for game status
     * @return Game status:
     *
     * <p>
     *
     * <ul>
     *   <li>Playing    → "Progresso"</li>
     *   <li>Won        → "Ganho"</li>
     *   <li>Lost       → "Perdido"</li>
     * </ul>
     */
    @Override
    public String toString(){
        return switch (this) {
            case Playing -> "Progresso";
            case Won -> "Ganho";
            case Lost -> "Perdido";
        };
    }
}

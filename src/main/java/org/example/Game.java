package org.example;

import java.util.concurrent.TimeUnit;

/**
 * Used to store statistics of a completed game
 */
public class Game {
    private GameStatus gameStatus;
    private String nickname;
    private String board;
    private long time;


    public Game(GameStatus gameStatus, String nickname, String board, long time) {
        this.gameStatus = gameStatus;
        this.nickname = nickname;
        this.board = board;
        this.time = time;
    }

    /**
     * Checks the time a game took to complete
     * @return
     */
    public long getGameTime(){
        return this.time;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("\tAlcunha: " + nickname + "\n" + "\tResultado: " + gameStatus + "\n\n" + board + "\nTempo: ");
        String formattedTime = String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(time),
                TimeUnit.MILLISECONDS.toMinutes(time) % 60,
                TimeUnit.MILLISECONDS.toSeconds(time) % 60
        );
        sb.append(formattedTime);
        return sb.toString();
    }
}

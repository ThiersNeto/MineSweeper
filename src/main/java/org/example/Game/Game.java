package org.example.Game;

import java.util.concurrent.TimeUnit;

/**
 * Used to store statistics of a completed game
 */
//This class is used solely for aid, no other class depends on it to function
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
        //return "\tAlcunha: " + nickname + "\n" + "\tResultado: " + gameStatus + "\n\n" + board;
    }
}

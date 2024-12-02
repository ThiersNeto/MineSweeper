package org.example;

/**
 * Used to store statistics of a completed game
 */
//This class is used solely for aid, no other class depends on it to function
public class Game {
    private GameStatus gameStatus;
    private String nickname;
    private String board;


    public Game(GameStatus gameStatus, String nickname, String board) {
        this.gameStatus = gameStatus;
        this.nickname = nickname;
        this.board = board;
    }

    @Override
    public String toString(){
        return "\tAlcunha: " + nickname + "\n" + "\tResultado: " + gameStatus + "\n\n" + board;
    }
}

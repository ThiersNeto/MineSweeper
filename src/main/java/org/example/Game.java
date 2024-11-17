package org.example;

/**
 * Usado para guardar estaticas de um jogo completado
 */
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

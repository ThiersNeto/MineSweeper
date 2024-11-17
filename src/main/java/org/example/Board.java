package org.example;

import java.text.SimpleDateFormat;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * A Classe Board representa o tabuleiro
 */
public class Board {
    private char[][] visualGrid;
    private int[][] neighboringCountGrid;
    private boolean[][] minesGrid;
    private boolean[][] flagsGrid;
    private boolean[][] revealedGrid;
    private int rows;
    private int cols;
    private int totalMines;
    private int flagCount;
    private long startingTime;
    private boolean cheatMode;

    /**
     *
     * @param rows          Número de Linhas
     * @param cols          Número de Colunas
     * @param totalMines    Número de Minas
     */
    public Board(int rows, int cols, int totalMines) {
        this.rows = rows;
        this.cols = cols;
        this.totalMines = totalMines;
        flagCount = totalMines;

        startingTime = System.currentTimeMillis();

        visualGrid = new char[rows][cols];
        minesGrid = new boolean[rows][cols];
        revealedGrid = new boolean[rows][cols];
        flagsGrid = new boolean[rows][cols];
        neighboringCountGrid = new int[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                visualGrid[i][j] = '■';
            }
        }
        placeMines();
    }

    /**
     * Coloca minas aleatoriamente
     */
    private void placeMines() {
        Random random = new Random();
        int minesPlaced = 0;

        while (minesPlaced < totalMines) {
            int x = random.nextInt(rows);
            int y = random.nextInt(cols);

            if (!minesGrid[x][y]) {
                minesGrid[x][y] = true;
                minesPlaced++;
            }
        }

        setNeighboringMineCounts();
    }

    /**
     * Calcula e define o número de minas vizinhas em todas as celulas
     */
    private void setNeighboringMineCounts() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                neighboringCountGrid[i][j] = countNeighboringMines(i, j);
            }
        }
    }

    /**
     * Calcula a quantidade de minas vizinhas de uma célula
     *
     * @param x     Linha
     * @param y     Coluna
     * @return      Número de Minas
     */
    private int countNeighboringMines(int x, int y) {
        int count = 0;

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {

                int nx = x + i;
                int ny = y + j;
                if (nx >= 0 && nx < rows && ny >= 0 && ny < cols && minesGrid[nx][ny]) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Função recursiva que revela a céluna e as as células à volta se estas não tiverem minas ou minas vizinhas
     *
     * @param x Linha
     * @param y Coluna
     */
    public void revealCell(int x, int y) {
        if ( !isCoordinateValid(x, y) || revealedGrid[x][y] || minesGrid[x][y])  //se já tiver revelado ou posição invalida, sair
            return;

        revealedGrid[x][y] = true;
        updateVisual(x,y);

        //se a celula não tiver minas ou minas vizinhas, abrir as vizinhas
        if (neighboringCountGrid[x][y] == 0) {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (i == 0 && j == 0) continue; // Passar a frente a célula atual (x, y)
                    revealCell(x + i, y + j);
                }
            }
        }
    }

    /**
     * Atualiza o visual de uma célula
     *
     * @param x Linha
     * @param y Coluna
     */
    private void updateVisual(int x, int y){
        //orderm de operações é importante aqui

        if(!revealedGrid[x][y]){
            visualGrid[x][y] = '■';
            if(cheatMode){
                if(minesGrid[x][y]) visualGrid[x][y] = '◆';
            }
            if(flagsGrid[x][y]) visualGrid[x][y] = '▶';

            return;
        }

        //caso a célula esteja revelada
        visualGrid[x][y] = '□';
        if(neighboringCountGrid[x][y] > 0) visualGrid[x][y] = (char) (neighboringCountGrid[x][y] + '0'); //maneira de converter char para int, o +0 serve para mudar o codigo ascii
        if(flagsGrid[x][y]) visualGrid[x][y] = '▶';
        if(minesGrid[x][y]) visualGrid[x][y] = '◆';
    }

    /**
     * Muda se a célula tem bandeira ou não
     * Diminui ou aumenta a quantidade de bandeiras
     *
     * @param x Linha
     * @param y Coluna
     *
     * @return "true" se o utilizador consegui por/tirar uma bandeira
     */
    public boolean toggleFlagStatus(int x, int y){
        if(!flagsGrid[x][y] && flagCount <= 0)
            return false;

        if(!flagsGrid[x][y])
            flagCount--;
        else
            flagCount++;


        flagsGrid[x][y] = !flagsGrid[x][y];

        updateVisual(x,y);

        return true;
    }



    /**
     * Muda o tabuleiro para ser apresentado com todas as minas descobertas
     *
     * @param x Linha da mina selecionada
     * @param y Coluna da mina selecionada
     */
    public void setLosingBoard(int x, int y){
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                revealedGrid[i][j] = true;
            }
        }
        updateBoardVisual();
        //mudar visual da mina clicada
        visualGrid[x][y] = '◈';
    }

    /**
     *
     * @return Retorna "true" se o campo dá condições de vitória
     */
    public boolean checkVictory() {
        boolean allMinesFlagged = true;
        boolean allNonMinesRevealed = true;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if ((minesGrid[i][j] && !flagsGrid[i][j]))
                    allMinesFlagged = false;
                if((!minesGrid[i][j] && !revealedGrid[i][j]))
                    allNonMinesRevealed = false;


            }
        }
        return allMinesFlagged || allNonMinesRevealed;
    }

    /**
     *
     * @return Uma coordenada aleatória dentro do tabuleiro que não tenha uma mina e não esteja revelado
     */
    public Coordinate getRandomCleanCoordinate(){
        Random random = new Random();
        int x;
        int y;
        do{
            x = random.nextInt(rows);
            y = random.nextInt(cols);
        } while(minesGrid[x][y] || revealedGrid[x][y] || !isCoordinateValid(x,y)); //repete se tiver mina, já tiver revelada ou não for valida

        return new Coordinate(x,y);
    }

    /**
     * Verifica se a célula tem uma mina
     *
     * @param x Linha
     * @param y Coluna
     * @return "true" se tiver uma mina
     */
    public boolean hasMine(int x, int y) {
        return minesGrid[x][y];
    }

    /**
     * Liga/Desliga o modo de batota
     *
     * @return "true" se o modo de batota for ligado
     */
    public boolean toggleCheatMode(){
        cheatMode = !cheatMode;

        updateBoardVisual();
        return cheatMode;
    }

    /**
     * Valida uma coordenada
     *
     * @param x Linha
     * @param y Coluna
     * @return "true" se as coordenadas estiverem dentro da tabela
     */
    public boolean isCoordinateValid(int x, int y){
        return (x >= 0 && x < rows && y >= 0 && y < cols);
    }

    /**
     * Percorre todas as células e atualiza o seu visual (caracter)
     */
    private void updateBoardVisual(){
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                updateVisual(i,j);
            }
        }
    }

    /**
     * Constroi uma string do tabuleiro, com estatisticas
     * Aproveita o uso de ascii para tornar um número numa letra, o A em ascii é o código 65, os seguintes seguem ordem alfabetica (B = 66, C = 67)
     *
     * @return O Tabuleiro
     */
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("♥⠀ ");
        //Imprimir colunas
        for (int i = 0; i < cols; i++) {
            stringBuilder.append((i)).append("  ");
        }
        stringBuilder.append('\n');
        for (int i = 0; i < rows; i++) {
            stringBuilder.append((char) ('A' + i)).append("  "); //imprimir linhas // truque ascii explicado no javadoc
            for (int j = 0; j < cols; j++) {
                stringBuilder.append(visualGrid[i][j]).append("  ");
            }
            stringBuilder.append('\n');
        }

        stringBuilder.append('\n').append("\tBandeiras Disponiveis: ").append(flagCount);

        long elapsedTime = System.currentTimeMillis() - startingTime;
        String formattedTime = String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(elapsedTime),
                TimeUnit.MILLISECONDS.toMinutes(elapsedTime) % 60,
                TimeUnit.MILLISECONDS.toSeconds(elapsedTime) % 60
        );

        stringBuilder.append('\n').append("\tTempo decorrido: ").append(formattedTime);

        stringBuilder.append('\n');
        return stringBuilder.toString();
    }
}

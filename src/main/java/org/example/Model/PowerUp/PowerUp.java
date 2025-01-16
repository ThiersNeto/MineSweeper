package org.example.Model.PowerUp;

import org.example.Model.Game.Board;
import static org.example.Model.PowerUp.PowerUpType.*;

public class PowerUp {
    private PowerUp type;

    public PowerUp(PowerUp type) {
        this.type = type;
    }

    public void activate(Board board) {
        if (type.equals(SHIELD)) {
            activateShield(board);
        } else if (type.equals(ICE)) {
            activateIce(board);
        } else if (type.equals(LINE)) {
            activateLine(board);
        } else if (type.equals(COLUMN)) {
            activateColumn(board);
        } else if (type.equals(HINT)) {
            activateHint(board);
        }
    }

    private void activateShield(Board board) {
        // Implementação do Shield: marcar a célula com uma bandeira permanente
        // Evitar que a mina exploda se o jogador abrir uma célula minada
    }

    private void activateIce(Board board) {
        // Congelar o tempo (parar o cronômetro por 3 jogadas)
    }

    private void activateLine(Board board) {
        // Revelar todas as células de uma linha, exceto as minadas
    }

    private void activateColumn(Board board) {
        // Revelar todas as células de uma coluna, exceto as minadas
    }

    private void activateHint(Board board) {
        // Fornecer uma célula aleatória que não contenha mina
        // Garantir que a dica não seja repetida
    }

}

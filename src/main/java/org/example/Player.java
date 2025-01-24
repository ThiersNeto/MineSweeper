package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Player {
    private String nickname;
    private Inventory inventory;
    private List<Game> wonGames; //list of games won by this player

    public Inventory getInventory() {
        return inventory;
    }

    public Player(String nickname)
    {
        //nickname is already checked locally on main, so there's no need to check for inconsistencies here
        this.nickname = nickname;
        inventory = (new Inventory());
        wonGames = new ArrayList<>();
    }

    public String getNickname()
    {
        return this.nickname;
    }

    public void addWonGame(Game game) { wonGames.add(game); //maybe this can be a boolean for feedback of whether the op was successfull or not
    }

    public boolean hasPowerUp(PowerUpType type) {
        return inventory.hasPowerUp(type);
    }

    public PowerUp getPowerUp(PowerUpType type) {
        return inventory.getPowerUp(type);
    }

    public List<PowerUp> getPowerUps() {
        return inventory.getPowerUps();
    }

    /**
     * Atribui três PowerUps aleatórios ao inventário do jogador.
     */
    public void assignRandomPowerUps() {
        Random random = new Random();
        PowerUpType[] powerUpTypes = PowerUpType.values(); // Todos os tipos de PowerUps

        for (int i = 0; i < 3; i++) {
            // Escolhe um tipo de PowerUp aleatório
            PowerUpType randomType = powerUpTypes[random.nextInt(powerUpTypes.length)];
            PowerUp powerUp = new PowerUp(randomType);
            inventory.addPowerUp(powerUp); // Adiciona ao inventário
        }
    }

    @Override
    public boolean equals(Object a){
        if(this == a) return true; //it's the same object!
        if(a == null || this.getClass() != a.getClass()) return false; //different types!
        Player other = (Player) a;
        return this.getNickname().equals(other.getNickname());
    }
}

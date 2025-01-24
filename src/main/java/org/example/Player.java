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

    /**
     *
     * @param nickname The player's display name (case-sensitive, non-null)
     */
    public Player(String nickname) {
        this.nickname = nickname;
        inventory = (new Inventory());
        wonGames = new ArrayList<>();
    }

    /**
     *
     * Returns the player's nickname
     * @return The case-sensitive display name of the player (guaranteed non-null)
     */
    public String getNickname() {
        return this.nickname;
    }

    /**
     *
     * Adds a game to the player's victory history
     * @param game The Game object to add to won games list
     */
    public void addWonGame(Game game) { wonGames.add(game); //maybe this can be a boolean for feedback of whether the op was successfull or not
    }

    /**
     *
     * Checks if the player possesses a specific type of PowerUp
     * @param type The PowerUpType to check for
     * @return true if the PowerUp exists in inventory, false otherwise
     */
    public boolean hasPowerUp(PowerUpType type) {
        return inventory.hasPowerUp(type);
    }

    /**
     *
     * Retrieves a PowerUp of specified type from inventory
     * @param type The PowerUpType to retrieve
     * @return The requested PowerUp or null if not found
     */
    public PowerUp getPowerUp(PowerUpType type) {
        return inventory.getPowerUp(type);
    }

    /**
     *
     * Provides direct access to the player's PowerUp collection
     * @return Unmodifiable list of all PowerUps in inventory
     */
    public List<PowerUp> getPowerUps() {
        return inventory.getPowerUps();
    }

    /**
     * Assigns three random PowerUps to the player's inventory.
     */
    public void assignRandomPowerUps() {
        Random random = new Random();
        PowerUpType[] powerUpTypes = PowerUpType.values(); // All types of PowerUps

        for (int i = 0; i < 3; i++) {
            // Choose a random PowerUp type
            PowerUpType randomType = powerUpTypes[random.nextInt(powerUpTypes.length)];
            PowerUp powerUp = new PowerUp(randomType);
            inventory.addPowerUp(powerUp); //Adds to inventory
        }
    }

    /**
     *
     * @param a The object to compare with this Player instance
     * @return true if the objects are both Players with identical nicknames, false otherwise
     */
    @Override
    public boolean equals(Object a){
        if(this == a) return true; //it's the same object!
        if(a == null || this.getClass() != a.getClass()) return false; //different types!
        Player other = (Player) a;
        return this.getNickname().equals(other.getNickname());
    }
}

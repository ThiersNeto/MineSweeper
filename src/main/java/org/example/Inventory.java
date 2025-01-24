package org.example;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Represents a collection of PowerUps that a player can possess and use during gameplay.
 * <p>
 * Maintains an ordered list of PowerUp items with capabilities to add, remove, and query items.
 * The inventory does not enforce uniqueness constraints - players can have multiple instances
 * of the same PowerUp type.
 */
public class Inventory {
    private List<PowerUp> powerUps;

    /**
     *
     * Constructs an empty inventory with no PowerUps.
     */
    public Inventory() {
        this.powerUps = new ArrayList<>();
    }

    /**
     *
     * Adds a PowerUp to the inventory.
     * @param powerUp The PowerUp to add to the collection
     */
    public void addPowerUp(PowerUp powerUp) {
        powerUps.add(powerUp);
    }

    /**
     *
     * Removes a PowerUp at the specified position in the inventory.
     * @param index The zero-based index of the PowerUp to remove
     */
    public void removePowerUp(int index)
    {
        powerUps.remove(index);
    }

    /**
     *
     * Checks if the inventory contains at least one PowerUp of the specified type.
     * @param type The PowerUpType to check for
     * @return true if a matching PowerUp exists, false otherwise
     */
    public boolean hasPowerUp(PowerUpType type) {
        return powerUps.stream().anyMatch(p -> p.getType() == type);
    }

    /**
     *
     * Retrieves the first PowerUp of the specified type from the inventory.
     * @param type The PowerUpType to search for
     * @return The first matching PowerUp, or null if none exists
     */
    public PowerUp getPowerUp(PowerUpType type) {
        return powerUps.stream().filter(p -> p.getType() == type).findFirst().orElse(null);
    }

    /**
     *
     * Removes all PowerUps from the inventory.
     */
    public void cleanInventory()
    {
        powerUps.clear();
    }

    /**
     *
     * Returns the complete list of PowerUps in the inventory.
     * <p>
     * This provides direct access to the underlying collection. Modifications to
     * the returned list will affect the inventory's contents.
     * </p>
     * @return A mutable list of PowerUps
     */
    public List<PowerUp> getPowerUps() {
        return powerUps;
    }
}

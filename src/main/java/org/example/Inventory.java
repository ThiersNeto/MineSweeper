package org.example;
import java.util.*;

import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private List<PowerUp> powerUps;

    public Inventory() {
        this.powerUps = new ArrayList<>();
    }

    public void addPowerUp(PowerUp powerUp) {
        powerUps.add(powerUp);
    }

    public void removePowerUp(int index)
    {
        powerUps.remove(index);
    }

    public boolean hasPowerUp(PowerUpType type) {
        return powerUps.stream().anyMatch(p -> p.getType() == type);
    }

    public PowerUp getPowerUp(PowerUpType type) {
        return powerUps.stream().filter(p -> p.getType() == type).findFirst().orElse(null);
    }

    public void cleanInventory()
    {
        powerUps.clear();
    }

    public List<PowerUp> getPowerUps() {
        return powerUps;
    }
}

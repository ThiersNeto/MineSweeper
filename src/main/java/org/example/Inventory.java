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

    public boolean hasPowerUp(PowerUpType type) {
        return powerUps.stream().anyMatch(p -> p.getType() == type);
    }

    public PowerUp getPowerUp(PowerUpType type) {
        return powerUps.stream().filter(p -> p.getType() == type).findFirst().orElse(null);
    }

    public List<PowerUp> getPowerUps() {
        return powerUps;
    }
}

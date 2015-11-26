package controllers;

import javafx.animation.ScaleTransition;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * Created by frostornge on 15. 11. 25.
 */

public class Doors {

    private double healthFull;
    private double currentHealth;
    private String doorFile;
    private String doorName;

    public Doors(double health, String doorFile, String doorName) {
        this.healthFull = health;
        this.currentHealth = health;
        this.doorFile = doorFile;
        this.doorName = doorName;
    }

    public void addDamage(double damage) {
        this.currentHealth = currentHealth - damage;
    }

    public double getCurrentHealth() {
        return currentHealth;
    }

    public double getHealthFull() {
        return healthFull;
    }

    public String getDoorFile() {
        return doorFile;
    }

    public String getDoorName() {
        return doorName;
    }


}

package controllers;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.jmx.MXNodeAlgorithm;
import com.sun.javafx.jmx.MXNodeAlgorithmContext;
import com.sun.javafx.sg.prism.NGNode;
import javafx.animation.ScaleTransition;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * Created by frostornge on 15. 11. 25.
 */

public class Doors extends Node {

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

	//문 생성 시 애니메이션
	public void spawnDoorAnim() {
		ScaleTransition st = new ScaleTransition(Duration.millis(2000), this);
		st.setToX(225);
		st.setToY(400);

		st.play();
	}

    @Override
    protected NGNode impl_createPeer() {
        return null;
    }

    @Override
    public BaseBounds impl_computeGeomBounds(BaseBounds bounds, BaseTransform tx) {
        return null;
    }

    @Override
    protected boolean impl_computeContains(double localX, double localY) {
        return false;
    }

    @Override
    public Object impl_processMXNode(MXNodeAlgorithm alg, MXNodeAlgorithmContext ctx) {
        return null;
    }
}

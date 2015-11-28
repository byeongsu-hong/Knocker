package controllers;

/**
 * Created by frostornge on 15. 11. 25.
 */
public class Tools {

    // Fields
    private final double Power;
    private String toolName;
    private String toolFile;

	// Constructor
    public Tools(double power, String toolFile, String toolName) {
        this.toolName = toolName;
        this.toolFile = "../res/tools/" + toolFile;
        this.Power = power;
    }

	// Getters
    public String getToolFile() {
        return this.toolFile;
    }

    public String getToolName() {
        return this.toolName;
    }

    public double getPower() {
        return this.Power;
    }
}

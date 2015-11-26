package controllers;

/**
 * Created by frostornge on 15. 11. 25.
 */
public class Tools {

    private final double Power;
    private String toolName;
    private String toolFile;
    private String toolMethod;

    public Tools(double power, String toolFile, String toolName, String toolMethod) {
        this.toolName = toolName;
        this.toolFile = "../res/" + toolFile;
        this.toolMethod = "handle" + toolMethod;
        this.Power = power;
    }

    public String getToolFile() {
        return toolFile;
    }

    public String getToolMethod() {
        return toolMethod;
    }

    public String getToolName() {
        return toolName;
    }

    public double getPower() {
        return Power;
    }
}

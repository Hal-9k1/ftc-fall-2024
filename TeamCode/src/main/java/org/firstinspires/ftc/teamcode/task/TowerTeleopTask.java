package org.firstinspires.ftc.teamcode.task;

/**
 * Controls the tower in teleop.
 */
public class TowerTeleopTask implements Task {
    /**
     * The direction and speed to swing the tower in.
     * Negative values lower the tower (towards the front of the robot).
     */
    private final double towerSwingPower;

    /**
     * Constructs a TowerTeleopTask.
     *
     * @param towerSwingPower - the direction and speed to swing the tower in.
     */
    public TowerTeleopTask(double towerSwingPower) {
        this.towerSwingPower = towerSwingPower;
    }

    /**
     * Returns the direction and speed to swing the tower in.
     *
     * @return the direction and speed to swing the tower in. Negative values lower the tower
     * (towards the front of the robot).
     */
    public double getTowerSwingPower() {
        return towerSwingPower;
    }
}

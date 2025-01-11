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
     * The direction and speed to swing the forearm in.
     * Negative values lower the forearm (towards the front of the robot if the tower is fully
     * lowered).
     */
    private final double forearmSwingPower;

    /**
     * Constructs a TowerTeleopTask.
     *
     * @param towerSwingPower - the direction and speed to swing the tower in.
     * @param forearmSwingPower - the direction and speed to swing the forearm in.
     */
    public TowerTeleopTask(double towerSwingPower, double forearmSwingPower) {
        this.towerSwingPower = towerSwingPower;
        this.forearmSwingPower = forearmSwingPower;
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

    /**
     * Returns the direction and speed to swing the forearm in.
     *
     * @return the direction and speed to swing the forearm in. Negative values lower the forearm
     * (towards the front of the robot if the tower is fully lowered).
     */
    public double getForearmSwingPower() {
        return forearmSwingPower;
    }
}

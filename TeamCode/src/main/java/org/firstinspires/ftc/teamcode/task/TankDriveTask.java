package org.firstinspires.ftc.teamcode.task;

/**
 * Specifies relative accelerations for left and right side of the robot.
 * Despite the name, not necessarily produced by tank drive controls.
 */
public class TankDriveTask implements Task {
    /**
     * The relative acceleration to apply to the left side of the robot.
     * Positive values indicate forward movement and negative values indicate backward.
     */
    public final double left;

    /**
     * The relative acceleration to apply to the right side of the robot.
     * Positive values indicate forward movement and negative values indicate backward.
     */
    public final double right;

    /**
     * Constructs a TankDriveTask.
     *
     * @param left the relative acceleration to apply to the left side of the robot.
     * @param right the relative acceleration to apply to the right side of the robot.
     */
    public TankDriveTask(double left, double right) {
        this.left = left;
        this.right = right;
    }
}

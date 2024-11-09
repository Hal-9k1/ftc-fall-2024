package org.firstinspires.ftc.teamcode.task;

/**
 * Specifies relative accelerations for the axial, lateral, and yaw component of a mecanum drive.
 */
public class MecanumDriveTask implements Task {
    /**
     * The relative acceleration to apply in the direction the robot is facing.
     * Positive values indicate forward movement and negative values indicate backward.
     */
    public double axial;
    /**
     * The relative acceleration to apply in the perpendicular direction to the one the robot is
     * facing.
     * Positive values indicate rightward movement and negative values indicate leftward.
     */
    public double lateral;
    /**
     * The relative acceleration to use to turn the robot.
     * Positive values indicate counterclockwise turning and negative values indicate clockwise.
     */
    public double yaw;
    /**
     * Constructs a MecanumDriveTask.
     * @param axial - the relative acceleration to apply in the direction the robot is facing.
     * @param lateral - the relative acceleration to apply in the direction perpendicular to the one
     * the robot is facing.
     * @param yaw - the relative acceleration to use to turn the robot.
     */
    public MecanumDriveTask(double axial, double lateral, double yaw) {
        this.axial = axial;
        this.lateral = lateral;
        this.yaw = yaw;
    }
}

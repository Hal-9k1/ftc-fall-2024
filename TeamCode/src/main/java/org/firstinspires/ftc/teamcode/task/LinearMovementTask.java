package org.firstinspires.ftc.teamcode.task;

/**
 * Tells a robot supporting holonomic drive to move in a straight line without turning.
 */
public class LinearMovementTask implements Task {
    /**
     * The distance in meters to move in the forward direction.
     * Negative values indicate backward movement.
     */
    public double axial;

    /**
     * The distance in meters to move to the robot's rightward direction.
     * Negative values indicate leftward movement.
     */
    public double lateral;

    /**
     * Constructs a LinearMovementTask.
     *
     * @param axial - the distance to move in the forward direction
     * @param lateral - the distance to move in the rightward direction
     */
    public LinearMovementTask(double axial, double lateral) {
        this.axial = axial;
        this.lateral = lateral;
    }
}

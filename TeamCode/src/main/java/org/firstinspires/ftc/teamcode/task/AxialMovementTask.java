package org.firstinspires.ftc.teamcode.task;

/**
 * Moves the robot forwards or backwards by a distance.
 */
public class AxialMovementTask implements Task {
    /**
     * The distance in meters to move the robot forward.
     * Negative values indicate backward movement.
     */
    public double distance;

    /**
     * Constructs an AxialMovementTask.
     *
     * @param distance the distance in meters to move the robot forward.
     */
    public AxialMovementTask(double distance) {
        this.distance = distance;
    }
}
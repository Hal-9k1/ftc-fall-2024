package org.firstinspires.ftc.teamcode.task;

/**
 * Turns the robot in place.
 */
public class TurnTask {
    /**
     * The angle in radians to turn the robot counterclockwise.
     * Negative values indicate clockwise turns.
     */
    public double angle;

    /**
     * Constructs a TurnTask.
     *
     * @param angle the angle in radians to turn the robot counterclockwise.
     */
    public TurnTask(double angle) {
        this.angle = angle;
    }
}

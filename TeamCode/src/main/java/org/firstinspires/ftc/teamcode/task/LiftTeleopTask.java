package org.firstinspires.ftc.teamcode.task;

/**
 * Extends/retracts or swings a lift using absolute powers.
 */
public class LiftTeleopTask implements Task {
    /**
     * The direction and speed the lift should swing with.
     * Must be in the range [-1, 1].
     */
    public final double swing;

    /**
     * The direction andspeed the lift should extend with.
     * Must be in the range [-1, 1]. Negative values indicate retraction.
     */
    public final double extension;

    /**
     * LiftTeleopTask constructor.
     *
     * @param swing - the rotation of the arm/lift in radians.
     * @param extension - some proportional value related to motor powers. (need better explanation later.)
     */
    public LiftTeleopTask(double swing, double extension) {
        this.swing = swing;
        this.extension = extension;
    }
}

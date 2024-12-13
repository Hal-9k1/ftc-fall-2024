package org.firstinspires.ftc.teamcode.task;

/**
 * extends or retracts the lift but in teleop.
 */
public class LiftTeleopTask implements Task {
    /**
     * rotation of the arm/lift in radians.
     * holds value inbetween interval [-1, 1] once normalized.
     */
    public final double swing;
    /**
     * some proportional value related to motor powers. (need better explanation later.)
     * holds value inbetween interval [-1, 1] once normalized.
     * This is used for both extend and retract. Negative value is retract, positive is extend.
     */
    public final double extension;

    /**
     * LiftTeleopTask constructor.
     * @param swing - the rotation of the arm/lift in radians.
     * @param extension - some proportional value related to motor powers. (need better explanation later.)
     */
    public LiftTeleopTask(double swing, double extension) {
        this.swing = swing;
        this.extension = extension;
    }
}

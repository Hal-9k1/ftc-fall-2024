package org.firstinspires.ftc.teamcode.task;

<<<<<<< HEAD
public class LiftTask{
    /**
     * @param swing is the rotation of the lift/arm in radians.
     * @param raise is a boolean to determine if the arm is up.
     * @param lower is a boolean to determine if the arm is down.
     * 
     * Raise and lower cannot both be true at the same time.
     */
    // There is probably a better way to name raise or lower so that it makes more intuitive sense.
    public final double swing;
    public final boolean raise;
    public final boolean lower;

    public LiftTask(double swing, boolean raise, boolean lower){
        this.swing = swing;
        this.raise = raise;
        this.lower = lower;
    }
}
=======
/**
 * Raises or lowers the lift.
 */
public class LiftTask implements Task {
    /**
     * Whether the lift should be raised.
     */
    public boolean raise;
    /**
     * Whether the lift should be lowered.
     */
    public boolean lower;

    /**
     * Constructs a LiftTask.
     * @param raise - whether the lift should be raised.
     * @param lower - whether the lift should be lowered.
     */
    public LiftTask(boolean raise, boolean lower) {
        this.raise = raise;
        this.lower = lower;
        if (raise && lower) {
            throw new IllegalArgumentException("Cannot simultaneously raise and lower the lift.");
        }
    }
}
>>>>>>> origin/main

package org.firstinspires.ftc.teamcode.task;

/**
 * Raises or lowers the lift.
 */
public class LiftTask implements Task {
    /**
     * Whether the lift should be raised.
     */
    public final boolean raise;
    /**
     * Whether the lift should be lowered.
     */
    public final boolean lower;

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

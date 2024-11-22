package org.firstinspires.ftc.teamcode.task;

/**
 * extends or retracts the lift.
 */
public class LiftTask implements Task {
    /**
     * The rotation of the arm in radians.
     */
    public final double swing;
    /**
     * Whether the lift should be extended.
     */
    public final boolean extend;
    /**
     * Whether the lift should be retracted.
     */
    public final boolean retract;

    /**
     * Constructs a LiftTask.
     * @param swing - the rotation of the arm in radians.
     * @param extend - whether the lift should be extended.
     * @param retract - whether the lift should be retracted.
     */
    public LiftTask(double swing, boolean extend, boolean retract) {
        this.swing = swing;
        this.extend = extend;
        this.retract = retract;
        if (extend && retract) {
            throw new IllegalArgumentException("Cannot simultaneously extend and retract the lift.");
        }
    }
}

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
     * Whether the lift should be fully extended.
     */
    public final boolean fullExtend;
    /**
     * Whether the lift should be fully retracted.
     */
    public final boolean fullRetract;
    /**
     * Whether the lift should be fully raised in an arc.
     */
    public final boolean raiseLift;
    /**
     * Whether the lift should be fully lowered in an arc.
     */
    public final boolean lowerLift;

    /**
     * Constructs a LiftTask.
     * @param swing - the rotation of the arm in radians.
     * @param fullExtend- Whether the lift should be fully extended.
     * @param fullRetract- Whether the lift should be fully retracted.
     * @param raiseLift - Whether the lift should be fully raised in an arc.
     * @param lowerLift - Whether the lift should be fully lowered in an arc.
     */
    public LiftTask(double swing, boolean fullExtend, boolean fullRetract, boolean raiseLift, boolean lowerLift) {
        this.swing = swing;
        this.fullExtend = fullExtend;
        this.fullRetract = fullRetract;
        this.raiseLift = raiseLift;
        this.lowerLift = lowerLift;
        
        if (fullExtend && fullRetract) {
            throw new IllegalArgumentException("Cannot simultaneously fully extend and fully retract the lift.");
        } 
        else if (raiseLift && lowerLift) {
            throw new IllegalArgumentException("Cannot simultaneously fully raise and lower the lift in an arc.");
        }
    }
}

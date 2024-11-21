package org.firstinspires.ftc.teamcode.task;

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
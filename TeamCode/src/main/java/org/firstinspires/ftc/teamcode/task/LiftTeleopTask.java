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
     */
    public final double extend;
    /**
     * some proportional value related to motor powers.
     * holds value inbetween interval [-1, 1] once normalized.
     */
    public final double retract;
    
    /**
     * LiftTeleopTask constructor.
     * @param swing - the rotation of the arm/lift in radians.
     * @param extend - some proportional value related to motor powers. (need better explanation later.)
     * @param retract - some proportional value related to motor powers. 
     */
    public LiftTeleopTask(double swing, double extend, double retract) {
        this.swing = swing;
        this.extend = extend;
        this.retract = retract;
    }
}
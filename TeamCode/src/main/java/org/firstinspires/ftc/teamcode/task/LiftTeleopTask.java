package org.firstinspires.ftc.teamcode.task;

public class LiftTeleopTask implements Task {

     // Unsure of what unit extend is supposed to be, is it linear distance the arm extends? or is it something to do with the motor rotations that make
     // the arm extend? Need to add comment later.
    public final double swing;
    public final double extend;
    
    /**
     * @param swing - the rotation of the arm in radians.
     * 
     */
    public LiftTeleopTask(double swing, double extend) {
        this.swing = swing;
        this.extend = extend;
    }
}
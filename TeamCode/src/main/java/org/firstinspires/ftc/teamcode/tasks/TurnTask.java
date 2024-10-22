package main.java.org.firstinspires.ftc.teamcode.tasks;

/**
 * Turn the robot in place.
 */
public class TurnTask
{
    /**
     * The angle in radians to turn the robot counterclockwise.
     * Negative values indicate clockwise turns.
     */
    public double angle;
    public TurnTask(double angle){
        this.angle = angle;
    }
}
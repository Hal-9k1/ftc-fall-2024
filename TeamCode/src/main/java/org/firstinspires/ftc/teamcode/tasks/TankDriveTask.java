package main.java.org.firstinspires.ftc.teamcode.tasks;

/**
 * Specifies relative accelerations for left and right side of the robot.
 * Despite the name, not necessarily produced by tank drive controls.
 */
public class TankDriveTask
{
    /**
     * The relative acceleration to apply to the left side of the robot.
     * Positive values indicate forward movement and negative values indicate backward.
     * The same applies to the right side of the robot.
     */
    public double left;
    public double right;
    public TankDriveTask(double left, double right){
        this.left = left;
        this.right = right;
    }
}
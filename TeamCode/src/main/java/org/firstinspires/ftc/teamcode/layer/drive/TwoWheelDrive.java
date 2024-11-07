package org.firstinspires.ftc.teamcode.layer.drive;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.layer.Layer;
import org.firstinspires.ftc.teamcode.mechanism.Wheel;
import org.firstinspires.ftc.teamcode.task.AxialMovementTask;
import org.firstinspires.ftc.teamcode.task.TankDriveTask;
import org.firstinspires.ftc.teamcode.task.Task;
import org.firstinspires.ftc.teamcode.task.TurnTask;
import org.firstinspires.ftc.teamcode.task.UnsupportedTaskException;
import org.firstinspires.ftc.teamcode.Units;

/**
 * Drive layer for a two-wheel drive robot.
 */
public class TwoWheelDrive implements Layer {
    /**
     * Name of the left drive motor in the robot configuration.
     */
    private static final String LEFT_DRIVE_MOTOR_NAME = "6_spamandeggs";
    /**
     * Name of the right drive motor in the robot configuration.
     */
    private static final String RIGHT_DRIVE_MOTOR_NAME = "6_spamandeggs";
    /**
     * The radius of the drive wheels in meters.
     */
    private static final double WHEEL_RADIUS = 0.42;
    /**
     * The effective gear ratio of the wheels to the motor drive shafts.
     * Expressed as wheelTeeth / hubGearTeeth, ignoring all intermediate meshing gears as they
     * should cancel out. Differently teethed gears driven by the same axle require more
     * consideration.
     */
    private static final double GEAR_RATIO = 1;
    /**
     * Half the distance between the driving wheels in meters.
     */
    private static final double WHEEL_SPAN_RADIUS = 0.84;
    /**
     * Unitless, experimentally determined constant (ew) measuring lack of friction.
     * Measures lack of friction between wheels and floor material. Goal delta distances are directly
     * proportional to this.
     */
    private static final double SLIPPING_CONSTANT = 1;

    // Wheel is a class from Mechanisms.py, probably translated into Mechanism.java
    private final Wheel leftWheel;
    private final Wheel rightWheel;
    private double leftStartPos;
    private double rightStartPos;
    private double leftGoalDelta;
    private double rightGoalDelta;

    // New important lore drop from Eddy: Use DcMotor and Servo interfaces from FTC in place of the wrapper classes from actuators.py

    public TwoWheelDrive(LayerSetupInfo initInfo) {
        // Wheel class has three parameters: motor, radius, and ticks per rotation.
        leftWheel = new Wheel(
            initInfo.getHardwareMap().get(DcMotor.class, LEFT_DRIVE_MOTOR_NAME),
            WHEEL_RADIUS
        );
        rightWheel = new Wheel(
            initInfo.getHardwareMap().hardwareMap.get(DcMotor.class, RIGHT_DRIVE_MOTOR_NAME),
            WHEEL_RADIUS
        );
        
        leftStartPos = 0;
        rightStartPos = 0;
        leftGoalDelta = 0;
        rightGoalDelta = 0;
    }

    public boolean isTaskDone() {
        double leftDelta = leftWheel.getDistance() - leftStartPos;
        boolean leftDeltaSignsMatch = (leftDelta < 0) == (leftGoalDelta < 0);
        boolean leftGoalDeltaExceeded = Math.abs(leftDelta) >= Math.abs(leftGoalDelta);
        boolean leftDone = (leftDeltaSignsMatch && leftGoalDeltaExceeded) || leftGoalDelta == 0;

        double rightDelta = rightWheel.getDistance() - rightStartPos;
        boolean rightDeltaSignsMatch = (rightDelta < 0) == (rightGoalDelta < 0);
        boolean rightGoalDeltaExceeded = Math.abs(rightDelta) >= Math.abs(rightGoalDelta);
        boolean rightDone = (rightDeltaSignsMatch && rightGoalDeltaExceeded) || rightGoalDelta == 0;
        boolean done = leftDone && rightDone;
        if (done) {
            leftWheel.setVelocity(0);
            rightWheel.setVelocity(0);
        }
        return done;
    }

    public Task update() {
        // Adaptive velocity control goes here.
        return null;
    }

<<<<<<< HEAD
    public acceptTask(Task task) {
        this.leftStartPos = this.leftWheel.getDistance();
        this.rightStartPos = this.rightWheel.getDistance();
        double deltaFac = TwoWheelDrive.GEAR_RATIO * TwoWheelDrive.SLIPPING_CONSTANT;
        if (task instanceof AxialMovementTask) {
            this.leftGoalDelta = task.distance * deltaFac;
            this.rightGoalDelta = task.distance * deltaFac;
        } else if (task instanceof TurnTask) {
            // Don't know what access modifier is necessary below. Have to implement later.
            // "Effective" as in "multiplied by all the weird constants we need"
            this.leftGoalDelta = -task.angle * TwoWheelDrive.WHEEL_SPAN_RADIUS * deltaFac;
            this.rightGoalDelta = task.angle * TwoWheelDrive.WHEEL_SPAN_RADIUS * deltaFac;
=======
    public void acceptTask(Task task) {
        leftStartPos = leftWheel.getDistance();
        rightStartPos = rightWheel.getDistance();

        if (task instanceof AxialMovementTask) {
            leftGoalDelta = task.distance;
            rightGoalDelta = task.distance;
        } else if (task instanceof TurnTask) {
            // Don't know what access modifier is necessary below. Have to implement later.
            // "Effective" as in "multiplied by all the weird constants we need"
            double effectiveRadius = WHEEL_SPAN_RADIUS * GEAR_RATIO * SLIPPING_CONSTANT;
            leftGoalDelta = -task.angle * effectiveRadius;
            rightGoalDelta = task.angle * effectiveRadius;
>>>>>>> refs/remotes/origin/tankDriveLayer
        } else if (task instanceof TankDriveTask) {
            // Teleop, set deltas to 0 to pretend we're done
            leftGoalDelta = 0;
            rightGoalDelta = 0;
            // Clamp to 1 to prevent upscaling
            double maxAbsPower = Math.max(Math.abs(task.left), Math.abs(task.right), 1);  
            // Question: Why do we use max if we're trying to prevent upscaling?
            leftWheel.setVelocity(task.left / maxAbsPower);
            rightWheel.setVelocity(task.right / maxAbsPower);
        } else {
            throw new UnsupportedTaskException(task);
        }
        leftWheel.setVelocity(Math.signum(leftGoalDelta));
        rightWheel.setVelocity(Math.signum(rightGoalDelta));
    }
}

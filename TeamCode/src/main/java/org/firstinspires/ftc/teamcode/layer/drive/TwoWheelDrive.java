package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.mechanism.Wheel;
import org.firstinspires.ftc.teamcode.layer.UnsupportedTaskError;
import org.firstinspires.ftc.teamcode.task.AxialMovementTask;
import org.firstinspires.ftc.teamcode.task.TankDriveTask;
import org.firstinspires.ftc.teamcode.task.TurnTask;
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
        boolean leftDone = ((this.leftWheel.getDistance() - this.leftStartPos < 0)
            == (this.leftGoalDelta < 0)) || this.leftGoalDelta == 0;
        boolean rightDone = ((this.rightWheel.getDistance() - this.rightStartPos < 0)
            == (this.rightGoalDelta < 0)) || this.rightGoalDelta == 0;
        boolean done = leftDone && rightDone;
        if (done) {
            this.leftWheel.setVelocity(0);
            this.rightWheel.setVelocity(0);
        }
        return done;
    }

    public update() {
        // Adaptive velocity controller goes here.
    }

    public acceptTask(Task task) {
        this.leftStartPos = this.leftWheel.getDistance();
        this.rightStartPos = this.rightWheel.getDistance();

        if (task instanceof AxialMovementTask) {
            this.leftGoalDelta = task.distance;
            this.rightGoalDelta = task.distance;
        } else if (task instanceof TurnTask) {
            // Don't know what access modifier is necessary below. Have to implement later.
            // "Effective" as in "multiplied by all the weird constants we need"
            double effectiveRadius = WHEEL_SPAN_RADIUS * GEAR_RATIO * SLIPPING_CONSTANT;
            this.leftGoalDelta = -task.angle * effectiveRadius;
            this.rightGoalDelta = task.angle * effectiveRadius;
        } else if (task instanceof TankDriveTask) {
            // Teleop, set deltas to 0 to pretend we're done
            this.leftGoalDelta = 0;
            this.rightGoalDelta = 0;
            // Clamp to 1 to prevent upscaling
            double maxAbsPower = Math.max(Math.abs(task.left), Math.abs(task.right), 1);  
            // Question: Why do we use max if we're trying to prevent upscaling?
            this.leftWheel.setVelocity(task.left / maxAbsPower);
            this.rightWheel.setVelocity(task.right / maxAbsPower);
        } else {
            throw new UnsupportedTaskError(task);
        }
        this.leftWheel.setVelocity(Math.signum(this.leftGoalDelta));
        this.rightWheel.setVelocity(Math.signum(this.rightGoalDelta));
    }
}

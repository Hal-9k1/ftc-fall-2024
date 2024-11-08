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

    /**
     * The robot's left wheel.
     */
    private final Wheel leftWheel;
    /**
     * The robot's right wheel.
     */
    private final Wheel rightWheel;
    /**
     * The position of the left wheel at the start of the currently executing task, in meters.
     */
    private double leftStartPos;
    /**
     * The position of the right wheel at the start of the currently executing task, in meters.
     */
    private double rightStartPos;
    /**
     * The required delta position of the left wheel to complete the currently executing task, in
     * meters.
     */
    private double leftGoalDelta;
    /**
     * The required delta position of the right wheel to complete the currently executing task, in
     * meters.
     */
    private double rightGoalDelta;

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

    @Override
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

    @Override
    public Task update() {
        // Adaptive velocity control goes here.
        return null;
    }

    @Override
    public void acceptTask(Task task) {
        leftStartPos = leftWheel.getDistance();
        rightStartPos = rightWheel.getDistance();
        double deltaFac = GEAR_RATIO * SLIPPING_CONSTANT;
        if (task instanceof AxialMovementTask) {
            AxialMovementTask castedTask = (AxialMovementTask)task;
            leftGoalDelta = castedTask.distance * deltaFac;
            rightGoalDelta = castedTask.distance * deltaFac;
        } else if (task instanceof TurnTask) {
            TurnTask castedTask = (TurnTask)task;
            leftGoalDelta = -castedTask.angle * WHEEL_SPAN_RADIUS * deltaFac;
            rightGoalDelta = castedTask.angle * WHEEL_SPAN_RADIUS * deltaFac;
        } else if (task instanceof TankDriveTask) {
            // Teleop, set deltas to 0 to pretend we're done
            leftGoalDelta = 0;
            rightGoalDelta = 0;
            TankDriveTask castedTask = (TankDriveTask)task;
            double maxAbsPower = Math.max(
                Math.max(Math.abs(castedTask.left), Math.abs(castedTask.right)),
                1 // Clamp to 1 to prevent upscaling
            );
            leftWheel.setVelocity(castedTask.left / maxAbsPower);
            rightWheel.setVelocity(castedTask.right / maxAbsPower);
        } else {
            throw new UnsupportedTaskException(this, task);
        }
        leftWheel.setVelocity(Math.signum(leftGoalDelta));
        rightWheel.setVelocity(Math.signum(rightGoalDelta));
    }
}

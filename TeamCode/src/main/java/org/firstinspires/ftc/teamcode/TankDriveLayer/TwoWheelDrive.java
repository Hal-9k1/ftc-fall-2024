package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;

import java.lang.Math;

import org.firstinspires.ftc.teamcode.task.AxialMovementTask;
import org.firstinspires.ftc.teamcode.task.TankDriveTask;
import org.firstinspires.ftc.teamcode.task.TurnTask;
import org.firstinspires.ftc.teamcode.UnsupportedTaskError;
import org.firstinspires.ftc.teamcode.Units;
/**
 * Drive layer for a two-wheel drive robot.
 * The id of the drive motor controller.
 */

public class TwoWheelDrive implements Layer {

    // Not sure if this is needed or needs to be changed to FTC equivalent.
    private static String driveKoalaBear = "6_spamandeggs";

    // Some values from below might need to be changed according to the hardware parts.
    private static double ticksPerRot = 888;
    private static double wheelRadius = 0.42;
    private static double gearRatio = 1;
    private static double wheelSpanRadius = 0.84;
    private static double slippingConstant = 1;

    // Wheel is a class from Mechanisms.py, probably translated into Mechanism.java
    private Wheel leftWheel;
    private Wheel rightWheel;

    // New important lore drop from Eddy: Use DcMotor and Servo interfaces from FTC in place of the wrapper classes from actuators.py

    public TwoWheelDrive(LayerSetupInfo initInfo){
        // Wheel class has three parameters: motor, radius, and ticks per rotation.
        this.leftWheel = new Wheel(hardwareMap.get(DcMotor.class, "leftdrivemotor"),
            wheelRadius,
            ticksPerRot);
        this.rightWheel = new Wheel(hardwareMap.get(DcMotor.class, "rightdrivemotor"),
            wheelRadius,
            ticksPerRot);
        
        // Not sure what modifiers those four variables below need. Have to implement later.
        double leftStartPos = 0;
        double rightStartPos = 0;

        double leftGoalDelta = 0;
        double rightGoalDelta = 0;
    }

    public boolean isTaskDone(){
        boolean leftDone = ((this.leftWheel.getDistance() - this.leftStartPos < 0)
            == (this.leftGoalDelta < 0)) || this.leftGoalDelta == 0;
        boolean rightDone = ((this.rightWheel.getDistance() - this.rightStartPos< 0)
            == (this.rightGoalDelta < 0)) || this.rightGoalDelta == 0;
        boolean done = leftDone && rightDone;
        if (done) {
            this.leftWheel.setVelocity(0);
            this.rightWheel.setVelocity(0);
            return done;
        }
    }

    public update(){
        // Adaptive velocity controller goes here.
    }

    public acceptTask(Task task){
        this.leftStartPos = this.leftWheel.getDistance();
        this.rightStartPos = this.rightWheel.getDistance();

        if (task instanceof AxialMovementTask){
            this.leftGoalDelta = task.distance;
            this.rightGoalDelta = task.distance;
        }
        else if (task instanceof TurnTask){
            // Don't know what access modifier is necessary below. Have to implement later.
            // "Effective" as in "multiplied by all the weird constants we need"
            double effectiveRadius = TwoWheelDrive.wheelSpanRadius * TwoWheelDrive.gearRatio * TwoWheelDrive.slippingConstant;
            this.leftGoalDelta = -task.angle * effectiveRadius;
            this.rightGoalDelta = task.angle * effectiveRadius;
        }
        else if (task instanceof TankDriveTask){
            // Teleop, set deltas to 0 to pretend we're done
            this.leftGoalDelta = 0;
            this.rightGoalDelta = 0;
            // Clamp to 1 to prevent upscaling
            double maxAbsPower = Math.max(Math.abs(task.left), Math.abs(task.right), 1);  
            // Question: Why do we use max if we're trying to prevent upscaling?
            this.leftWheel.setVelocity(task.left / maxAbsPower);
            this.rightWheel.setVelocity(task.right / maxAbsPower);
        }
        else{
            throw new UnsupportedTaskError(task);
        }
        this.leftWheel.setVelocity(Math.copySign(1, this.leftGoalDelta));
        this.rightWheel.setVelocity(Math.copySign(1, this.rightGoalDelta));
    }
}

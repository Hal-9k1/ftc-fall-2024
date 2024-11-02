package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
// More imports that doesn't exist yet except for Units.
import org.firstinspires.ftc.teamcode.task.AxialMovementTask;
import org.firstinspires.ftc.teamcode.task.TankDriveTask;  //Is there really a tank drive task? or does it mean tank drive layer?
import org.firstinspires.ftc.teamcode.task.TurnTask;
import org.firstinspires.ftc.teamcode.UnsupportedTaskError;  //I may be wrong because UnsupportedTaskError doesn't seem to have been written yet.
import org.firstinspires.ftc.teamcode.Units;
/**
 * Drive layer for a two-wheel drive robot.
 * The id of the drive motor controller.
 */

public class TwoWheelDrive implements Layer {

    // TODO: Find out the FTC equivalent and change the below variable.
    private static String driveKoalaBear = "6_spamandeggs"; // Don't know what the Koala Bear equivalent in FTC is.

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
    // Sure hope that Wheel class is implemented in Java when this is done.

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
        // Gonna fall asleep will do this tomorrow morning
    }
}

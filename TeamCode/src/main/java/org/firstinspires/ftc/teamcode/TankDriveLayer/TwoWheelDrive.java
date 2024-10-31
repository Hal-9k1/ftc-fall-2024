package org.firstinspires.ftc.teamcode;
package org.firstinspires.ftc.teamcode.task;

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

public class TwoWheelDrive implements Layer
{
    private static String driveKoalaBear = "6_spamandeggs"; //I don't know what this means ;-;, also i'm kinda sleepy, and i kinda 
                                                            // Possible answer to the above, I think this may be the motor controller ID.
    private static double ticksPerRot = 888; //remmeber eddy saying that these numbers and some of the improted stuff won't work 
    private static double wheelRadius = 0.42; //here so maybe i'm doing this for nothing
    private static double gearRatio = 1;
    private static double wheelSpanRadius = 0.84;
    // Wheel is a class from Mechanisms.py, probably translated into Mechanism.java
    private Wheel leftWheel;
    private Wheel rightWheel;

    public TwoWheelDrive(LayerSetupInfo initInfo){
        /*Motor is a class from actuators.py, probably translated into actuators.java, 
        either that or we're using something else like HardwareMap.DeviceMapping<DcMotor> dcMotor.
        I don't know what I am talking about I may be going insane. */

        // Wheel class has three parameters: motor, radius, and ticks per rotation.
        // Motor class has three parameters: robot, ControllerID, and motor.
        this.leftWheel = new Wheel(new Motor(initInfo.getRobot(), driveKoalaBear, "a")
                .setInvert(false)
                .setPID(null, null, null), // Not sure if we're supposed to use our own, FTCLib might have their own class for this.
            wheelRadius,
            ticksPerRot
        );
        // More work to do...
    }
}
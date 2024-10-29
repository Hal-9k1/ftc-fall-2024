package org.firstinspires.ftc.teamcode.task;

// More imports that doesn't exist yet except for Units.
import task.AxialMovementTask;
import task.TankDriveTask;  //Is there really a tank drive task? or does it mean tank drive layer?
import task.TurnTask;
import UnsupportedTaskError;
import org.firstinspires.ftc.teamcode.Units;
/**
 * Drive layer for a two-wheel drive robot.
 * The id of the drive motor controller.
 */

public class TwoWheelDrive implements Layer
{
    private static String driveKoalaBear = "6_spamandeggs"; //I don't know what this means ;-;, also i'm kinda sleepy, and i kinda 
    private static double ticksPerRot = 888; //remmeber eddy saying that these numbers and some of the improted stuff won't work 
    private static double wheelRadius = 0.42; //here so maybe i'm doing this for nothing
    private static double gearRation = 1;
    private static double wheelSpanRadius = 0.84;
    
    private Wheel leftWheel;
    private Wheel rightWheel;
    // I hope initInfo is actually from LayerSetupInfo I can't rember
    public TwoWheelDrive(LayerSetupInfo initInfo){
        // some stuff from FTCLib, haven't read it yet.
    }
    //Imma leave it here and call it for now
}
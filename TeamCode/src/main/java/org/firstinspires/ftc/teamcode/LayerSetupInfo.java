package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;
import java.util.Consumer;
import RobotController;

// I didn't change the comments because I may spread misinformation.

public class LayerSetupInfo{
    /*
        Contains the information needed to initialize a layer.
    */
    
    public LayerSetupInfo(HardwareMap hardWareMap, RobotController robotController){
    /*
        Creates a LayerSetupInfo.

        Positional arguments:
        robot -- the Robot or Robot-like object the layer may use to communicate with hardware
        robot_controller -- the RobotController that will run the layer
     */
        this.hardWareMap = hardWareMap;
        this.robotController = robotController;
    }
    public getHardWareMap(){
        return this.hardWareMap;
    }
    /*
     *  Returns the Robot or Robot-like object used to communicate with hardware.
     */
    public addUpdateListener(Consumer<Boolean> listener){
    /*
     *  Registers a function to be called on every update of the owning RobotController.
     */
        this.robotController.addUpdateListener(listener);
    }
}
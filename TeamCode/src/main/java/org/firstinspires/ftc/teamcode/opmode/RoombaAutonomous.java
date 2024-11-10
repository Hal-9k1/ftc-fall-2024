package org.firstinspires.ftc.teamcode.opmode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import java.util.Arrays;
import java.util.List;
import org.firstinspires.ftc.teamcode.layer.Layer;
import org.firstinspires.ftc.teamcode.layer.drive.TwoWheelDrive;

@Autonomous(name="Roomba (broken)")
public class RoombaAutonomous extends LayerOpMode {
    @Override
    protected List<Layer> getLayers() {
        return Arrays.asList(
            new TwoWheelDrive()
            // Other layers here...
        );
    }
}

package org.firstinspires.ftc.teamcode.opmode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import java.util.Arrays;
import java.util.List;
import org.firstinspires.ftc.teamcode.layer.Layer;
import org.firstinspires.ftc.teamcode.layer.drive.TwoWheelDrive;
import org.firstinspires.ftc.teamcode.layer.strategy.PathlessStrategy;
import org.firstinspires.ftc.teamcode.layer.WinLayer;

@Autonomous(name="Roomba (broken)")
public class RoombaAutonomous extends LayerOpMode {
    @Override
    protected List<Layer> getLayers() {
        return Arrays.asList(
            new TwoWheelDrive(),
            new PathlessStrategy(),
            new WinLayer()
        );
    }
}

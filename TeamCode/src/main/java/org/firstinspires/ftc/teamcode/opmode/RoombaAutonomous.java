package org.firstinspires.ftc.teamcode.opmode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.layer.Layer;
import org.firstinspires.ftc.teamcode.layer.drive.MecanumDrive;
import org.firstinspires.ftc.teamcode.layer.strategy.PathlessStrategy;
import org.firstinspires.ftc.teamcode.layer.WinLayer;

import java.util.Arrays;
import java.util.List;

@Autonomous(name="Roomba (broken)")
public class RoombaAutonomous extends LayerOpMode {
    @Override
    protected List<Layer> getLayers() {
        return Arrays.asList(
            new MecanumDrive(),
            new PathlessStrategy(),
            new WinLayer()
        );
    }
}

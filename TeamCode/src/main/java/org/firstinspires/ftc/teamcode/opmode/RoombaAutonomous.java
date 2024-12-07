package org.firstinspires.ftc.teamcode.opmode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import java.util.Arrays;
import java.util.List;
import org.firstinspires.ftc.teamcode.layer.Layer;
import org.firstinspires.ftc.teamcode.layer.drive.MecanumDrive;
import org.firstinspires.ftc.teamcode.layer.strategy.PathlessStrategy;
import org.firstinspires.ftc.teamcode.layer.WinLayer;

/**
 * Autonomous that makes no use of anything but the drive system and scoots around like a roomba.
 * Currently uses PathlessStrategy, which scores the Alliance-neutral sample on our side of the
 * field.
 */
@Autonomous(name="Roomba")
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

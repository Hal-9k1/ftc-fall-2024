package org.firstinspires.ftc.teamcode.opmode;

import java.util.Arrays;
import java.util.List;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.layer.Layer;
import org.firstinspires.ftc.teamcode.layer.WinLayer;
import org.firstinspires.ftc.teamcode.layer.drive.TwoWheelDrive;
import org.firstinspires.ftc.teamcode.layer.strategy.PathlessStrategy;

/**
 * Autonomous that makes no use of anything but the drive system and scoots around like a roomba.
 * Currently uses PathlessStrategy, which scores the Alliance-neutral sample on our side of the
 * field.
 */
@Autonomous(name="Roomba")
public final class RoombaAutonomous extends LayerOpMode {
    public RoombaAutonomous() { }

    @Override
    protected List<Layer> getLayers() {
        return Arrays.asList(
            new TwoWheelDrive(),
            new PathlessStrategy(),
            new WinLayer()
        );
    }
}

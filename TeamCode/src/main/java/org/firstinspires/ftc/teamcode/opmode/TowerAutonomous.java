package org.firstinspires.ftc.teamcode.opmode;

import java.util.Arrays;
import java.util.List;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.layer.Layer;
import org.firstinspires.ftc.teamcode.layer.MultiplexLayer;
import org.firstinspires.ftc.teamcode.layer.TopLayerSequence;
import org.firstinspires.ftc.teamcode.layer.WinLayer;
import org.firstinspires.ftc.teamcode.layer.drive.MecanumDrive;
import org.firstinspires.ftc.teamcode.layer.manipulator.IntakeLayer;
import org.firstinspires.ftc.teamcode.layer.manipulator.TowerLayer;
import org.firstinspires.ftc.teamcode.layer.strategy.TowerStrategy;

/**
 * Autonomous that uses the tower and intake to score a preloaded sample and
 * the alliance-neutral samples on the spike marks.
 */
@Autonomous(name="Tower")
public final class TowerAutonomous extends AbstractLayerOpMode {
    /**
     * Constructs a TowerAutonomous.
     */
    public TowerAutonomous() { }

    @Override
    protected List<Layer> getLayers() {
        TowerLayer towerLayer = new TowerLayer();
        return Arrays.asList(
            new MultiplexLayer(Arrays.asList(
                new MecanumDrive(),
                new IntakeLayer(),
                towerLayer
            )),
            new TowerStrategy(),
            new TopLayerSequence(Arrays.asList(
                towerLayer.new InitLayer(),
                new WinLayer()
            ))
        );
    }
}

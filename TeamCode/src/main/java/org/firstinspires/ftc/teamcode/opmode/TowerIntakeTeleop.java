package org.firstinspires.ftc.teamcode.opmode;

import java.util.Arrays;
import java.util.List;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.layer.BlockingMultiplexLayer;
import org.firstinspires.ftc.teamcode.layer.Layer;
import org.firstinspires.ftc.teamcode.layer.MultiplexLayer;
import org.firstinspires.ftc.teamcode.layer.TopLayerSequence;
import org.firstinspires.ftc.teamcode.layer.drive.MecanumDrive;
import org.firstinspires.ftc.teamcode.layer.input.GamepadInputGenerator;
import org.firstinspires.ftc.teamcode.layer.input.mapping.DpadTowerMapping;
import org.firstinspires.ftc.teamcode.layer.input.mapping.JoystickHoloDriveMapping;
import org.firstinspires.ftc.teamcode.layer.input.mapping.TriggerIntakeMapping;
import org.firstinspires.ftc.teamcode.layer.manipulator.IntakeLayer;
import org.firstinspires.ftc.teamcode.layer.manipulator.TowerLayer;

/**
 * Teleop opmode that drives the robot and controls a tower with an intake.
 * See {@link TowerLayer} for what a "tower" is.
 */
@TeleOp(name="Drive Tower Intake")
public final class TowerIntakeTeleop extends AbstractLayerOpMode {
    /**
     * Constructs a TowerIntakeTeleop opmode.
     */
    public TowerIntakeTeleop() { }

    @Override
    protected List<Layer> getLayers() {
        return Arrays.asList(
            new BlockingMultiplexLayer(Arrays.asList(
                new MecanumDrive(),
                new TowerLayer(),
                new IntakeLayer()
            )),
            new MultiplexLayer(Arrays.asList(
                new JoystickHoloDriveMapping(),
                new DpadTowerMapping(),
                new TriggerIntakeMapping()
            )),
            new TopLayerSequence(Arrays.asList(
                new TowerLayer.InitLayer(),
                new GamepadInputGenerator()
            ))
        );
    }
}

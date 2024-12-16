package org.firstinspires.ftc.teamcode.opmode;

import java.util.Arrays;
import java.util.List;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.layer.Layer;
import org.firstinspires.ftc.teamcode.layer.MultiplexLayer;
import org.firstinspires.ftc.teamcode.layer.drive.MecanumDrive;
import org.firstinspires.ftc.teamcode.layer.input.GamepadInputGenerator;
import org.firstinspires.ftc.teamcode.layer.input.mapping.DpadTowerMapping;
import org.firstinspires.ftc.teamcode.layer.input.mapping.JoystickHoloDriveMapping;
import org.firstinspires.ftc.teamcode.layer.manipulator.TowerLayer;

/**
 * Teleop opmode that drives the robot and controls a tower.
 * See {@link TowerLayer} for what a "tower" is.
 */
@TeleOp(name="Drive Tower")
public final class DriveTowerTeleop extends AbstractLayerOpMode {
    /**
     * Constructs a DriveTowerTeleop opmode.
     */
    public DriveTowerTeleop() { }

    @Override
    protected List<Layer> getLayers() {
        return Arrays.asList(
            new MultiplexLayer(Arrays.asList(
                new MecanumDrive(),
                new TowerLayer()
            )),
            new MultiplexLayer(Arrays.asList(
                new JoystickHoloDriveMapping(),
                new DpadTowerMapping()
            )),
            new GamepadInputGenerator()
        );
    }
}

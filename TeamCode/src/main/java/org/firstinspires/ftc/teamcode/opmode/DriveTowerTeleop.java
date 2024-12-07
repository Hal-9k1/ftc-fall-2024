package org.firstinspires.ftc.teamcode.opmode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.layer.Layer;
import org.firstinspires.ftc.teamcode.layer.MultiplexLayer;
import org.firstinspires.ftc.teamcode.layer.drive.MecanumDrive;
import org.firstinspires.ftc.teamcode.layer.input.GamepadInputGenerator;
import org.firstinspires.ftc.teamcode.layer.input.mapping.JoystickHoloDriveMapping;
import org.firstinspires.ftc.teamcode.layer.input.mapping.DpadTowerMapping;
import org.firstinspires.ftc.teamcode.layer.input.mapping.TriggerIntakeMapping;
import org.firstinspires.ftc.teamcode.layer.manipulator.IntakeLayer;
import org.firstinspires.ftc.teamcode.layer.manipulator.TowerLayer;

import java.util.Arrays;
import java.util.List;

@TeleOp(name="Drive Tower")
public class DriveTowerTeleop extends LayerOpMode {
    @Override
    protected List<Layer> getLayers() {
        return Arrays.asList(
            new MultiplexLayer(Arrays.asList(
                new MecanumDrive(),
                new TowerLayer(),
                new IntakeLayer()
            )),
            new MultiplexLayer(Arrays.asList(
                new JoystickHoloDriveMapping(),
                new DpadTowerMapping(),
                new TriggerIntakeMapping()
            )),
            new GamepadInputGenerator()
        );
    }
}

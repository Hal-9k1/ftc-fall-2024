package org.firstinspires.ftc.teamcode.opmode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.layer.Layer;
import org.firstinspires.ftc.teamcode.layer.drive.TwoWheelDrive;
import org.firstinspires.ftc.teamcode.layer.input.GamepadInputGenerator;
import org.firstinspires.ftc.teamcode.layer.input.mapping.TankDriveMapping;

import java.util.Arrays;
import java.util.List;

@TeleOp(name="Drive")
public class DriveTeleop extends LayerOpMode {
    @Override
    protected List<Layer> getLayers() {
        return Arrays.asList(
            new TwoWheelDrive(),
            new TankDriveMapping(),
            new GamepadInputGenerator()
        );
    }
}

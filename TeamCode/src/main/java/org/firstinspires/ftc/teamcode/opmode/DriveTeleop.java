package org.firstinspires.ftc.teamcode.opmode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.layer.Layer;
import org.firstinspires.ftc.teamcode.layer.drive.MecanumDrive;
import org.firstinspires.ftc.teamcode.layer.input.GamepadInputGenerator;
import org.firstinspires.ftc.teamcode.layer.input.mapping.JoystickHoloDriveMapping;

import java.util.Arrays;
import java.util.List;

/**
 * Teleop opmode that just drives the robot.
 * No manipulators or anything else at all are initialized.
 */
@TeleOp(name="Drive")
public class DriveTeleop extends LayerOpMode {
    @Override
    protected List<Layer> getLayers() {
        return Arrays.asList(
            new MecanumDrive(),
            new JoystickHoloDriveMapping(),
            new GamepadInputGenerator()
        );
    }
}

package org.firstinspires.ftc.teamcode.opmode;

import java.util.Arrays;
import java.util.List;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.layer.Layer;
import org.firstinspires.ftc.teamcode.layer.drive.MecanumDrive;
import org.firstinspires.ftc.teamcode.layer.input.GamepadInputGenerator;
import org.firstinspires.ftc.teamcode.layer.input.mapping.JoystickHoloDriveMapping;

/**
 * Teleop opmode that just drives the robot.
 * No manipulators or anything else at all are initialized.
 */
@TeleOp(name="Drive")
public final class DriveTeleop extends LayerOpMode {
    public DriveTeleop() { }

    @Override
    protected List<Layer> getLayers() {
        return Arrays.asList(
            new MecanumDrive(),
            new JoystickHoloDriveMapping(),
            new GamepadInputGenerator()
        );
    }
}

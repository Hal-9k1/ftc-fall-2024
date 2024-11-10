package org.firstinspires.ftc.teamcode.opmode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RobotController;
import org.firstinspires.ftc.teamcode.layer.Layer;
import org.firstinspires.ftc.teamcode.layer.drive.TwoWheelDrive;
import org.firstinspires.ftc.teamcode.layer.input.GamepadInputGenerator;
import org.firstinspires.ftc.teamcode.layer.input.mapping.TankDriveMapping;

import java.util.Arrays;
import java.util.List;

@TeleOp(name="Layer TeleOp (broken)")
public class LayerTeleop extends OpMode {
    private RobotController controller;

    @Override
    public void init() {
        List<Layer> layers = Arrays.asList(
            new TwoWheelDrive(),
            new TankDriveMapping(),
            new GamepadInputGenerator()
        );
        controller = new RobotController();
        controller.setup(hardwareMap, layers, gamepad1, gamepad2);
    }

    @Override
    public void loop() {
        controller.update();
    }
}

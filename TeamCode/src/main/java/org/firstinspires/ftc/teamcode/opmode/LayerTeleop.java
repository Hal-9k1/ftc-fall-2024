package org.firstinspires.ftc.teamcode.opmode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import java.util.List;
import org.firstinspires.ftc.teamcode.layer.Layer;
import org.firstinspires.ftc.teamcode.RobotController;

//@TeleOp(name="Layer TeleOp (broken)", group="Iterative OpMode")
@TeleOp(name="Layer TeleOp (broken)")
public class LayerTeleop extends OpMode {
    private RobotController controller;

    @Override
    public void init() {
        List<Layer> layers = Arrays.asList(
            TwoWheelDrive,
            TankDriveMapping,
            GamepadInputGenerator
        );
        controller = new RobotController();
        controller.setup(hardwareMap, layers, gamepad1, gamepad2);
    }

    @Override
    public void loop() {
        controller.update();
    }
}

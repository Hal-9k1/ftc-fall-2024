package org.firstinspires.ftc.teamcode.opmode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import java.util.List;
import org.firstinspires.ftc.teamcode.layer.Layer;
import org.firstinspires.ftc.teamcode.RobotController;

//@Autonomous(name="Layer Autonomous (broken)", group="Iterative OpMode")
@Autonomous(name="Layer Autonomous (broken)")
public class LayerAutonomous extends OpMode {
    private RobotController controller;

    @Override
    public void init() {
        List<Layer> layers = Arrays.asList(
            TwoWheelDrive,
            // Strategy layer here
        );
        controller = new RobotController();
        controller.setup(hardwareMap, layers, gamepad1, gamepad2);
    }

    @Override
    public void loop() {
        controller.update();
    }
}

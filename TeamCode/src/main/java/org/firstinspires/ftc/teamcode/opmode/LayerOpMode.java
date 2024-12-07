package org.firstinspires.ftc.teamcode.opmode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.RobotController;
import org.firstinspires.ftc.teamcode.layer.Layer;

import java.util.List;

/**
 * Base class for opmodes that use a RobotController to execute Layers.
 */
public abstract class LayerOpMode extends OpMode {
    /**
     * The RobotController used to execute the layer stack.
     */
    private RobotController controller;

    @Override
    public void init() {
        controller = new RobotController();
        controller.setup(hardwareMap, getLayers(), telemetry, gamepad1, gamepad2);
    }

    @Override
    public void loop() {
        controller.update();
        telemetry.update();
    }

    /**
     * Gets the list of layers to execute for this opmode.
     * @return The list of layers to execute, in order from lowest to highest.
     */
    protected abstract List<Layer> getLayers();
}

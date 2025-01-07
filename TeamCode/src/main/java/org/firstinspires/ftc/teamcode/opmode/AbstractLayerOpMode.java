package org.firstinspires.ftc.teamcode.opmode;

import java.util.List;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.RobotController;
import org.firstinspires.ftc.teamcode.layer.Layer;
import org.firstinspires.ftc.teamcode.logging.LoggerProvider;

/**
 * Base class for opmodes that use a RobotController to execute Layers.
 */
public abstract class AbstractLayerOpMode extends OpMode {
    /**
     * The RobotController used to execute the layer stack.
     */
    private RobotController controller;

    @Override
    public final void init() {
        controller = new RobotController();
        LoggerProvider loggerProvider = new LoggerProvider();
        controller.setup(hardwareMap, getLayers(), gamepad1, gamepad2, loggerProvider);
    }

    @Override
    public final void loop() {
        controller.update();
    }

    /**
     * Gets the list of layers to execute for this opmode.
     *
     * @return The list of layers to execute, in order from lowest to highest.
     */
    protected abstract List<Layer> getLayers();
}

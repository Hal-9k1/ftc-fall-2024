package org.firstinspires.ftc.teamcode.opmode;

import java.util.List;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.RobotController;
import org.firstinspires.ftc.teamcode.layer.Layer;
import org.firstinspires.ftc.teamcode.logging.LoggerProvider;
import org.firstinspires.ftc.teamcode.localization.RobotLocalizer;

/**
 * Base class for opmodes that use a RobotController to execute Layers.
 */
public abstract class AbstractLayerOpMode extends OpMode {
    /**
     * The RobotController used to execute the layer stack.
     */
    private RobotController controller;

    private boolean finished;

    @Override
    public final void init() {
        //telemetry.setAutoClear(true);
        controller = new RobotController();
        finished = false;
        LoggerProvider loggerProvider = new LoggerProvider();
        controller.setup(hardwareMap, getLocalizer(), getLayers(), gamepad1, gamepad2, loggerProvider);
    }

    @Override
    public final void loop() {
        if (finished) {
            return;
        }
        if (controller.update()) {
            //telemetry.log().add("Finished!");
            finished = true;
        }
        //telemetry.update();
    }

    /**
     * Gets the list of layers to execute for this opmode.
     *
     * @return The list of layers to execute, in order from lowest to highest.
     */
    protected abstract List<Layer> getLayers();

    /**
     * Gets the robot localizer to use for this opmode.
     * The default implementation returns null. If an opmode's layers expect a localizer, override
     * this method and return a RobotLocalizer implementation.
     *
     * @return The RobotLocalizer to use to determine the robot's transform during execution.
     */
    protected RobotLocalizer getLocalizer() {
        return null;
    }
}

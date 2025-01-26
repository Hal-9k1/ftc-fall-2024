package org.firstinspires.ftc.teamcode.opmode;

import java.util.List;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.RobotController;
import org.firstinspires.ftc.teamcode.layer.Layer;

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
        telemetry.setAutoClear(true);
        controller = new RobotController();
        finished = false;
        controller.setup(hardwareMap, getLayers(), telemetry, gamepad1, gamepad2);
    }

    @Override
    public final void loop() {
        if (finished) {
            return;
        }
        if (controller.update()) {
            telemetry.log().add("Finished!");
            finished = true;
        }
        telemetry.update();
    }

    /**
     * Gets the list of layers to execute for this opmode.
     *
     * @return The list of layers to execute, in order from lowest to highest.
     */
    protected abstract List<Layer> getLayers();
}

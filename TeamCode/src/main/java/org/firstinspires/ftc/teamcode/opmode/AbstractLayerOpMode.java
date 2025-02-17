package org.firstinspires.ftc.teamcode.opmode;

import java.util.List;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.RobotController;
import org.firstinspires.ftc.teamcode.layer.Layer;
import org.firstinspires.ftc.teamcode.localization.RobotLocalizer;
import org.firstinspires.ftc.teamcode.logging.Logger;
import org.firstinspires.ftc.teamcode.logging.LoggerProvider;

/**
 * Base class for opmodes that use a RobotController to execute Layers.
 */
public abstract class AbstractLayerOpMode extends OpMode {
    /**
     * The RobotController used to execute the layer stack.
     */
    private RobotController controller;

    /**
     * The RobotLocalizer passed to the layer stack during setup.
     * May be null if implementing classes do not override {@link getLocalizer}.
     */
    private RobotLocalizer localizer;

    /**
     * Whether the layer stack is finished processing.
     */
    private boolean finished;

    /**
     * The logger.
     */
    private Logger logger;

    @Override
    public final void init() {
        controller = new RobotController();
        finished = false;
        LoggerProvider loggerProvider = new LoggerProvider();
        configureLogger(loggerProvider);
        localizer = getLocalizer();
        logger = loggerProvider.getLogger("AbstractLayerOpMode");
        controller.setup(hardwareMap, localizer, getLayers(), gamepad1, gamepad2, loggerProvider);
    }

    @Override
    public final void loop() {
        if (!finished) {
            if (localizer != null) {
                localizer.invalidateCache();
            }
            if (controller.update()) {
                logger.log("Finished processing layer stack!");
                finished = true;
            }
        }
    }

    /**
     * Gets the list of layers to execute for this opmode.
     *
     * @return The list of layers to execute, in order from lowest to highest.
     */
    protected abstract List<Layer> getLayers();

    /**
     * Gets the robot localizer to use for this opmode.
     * If an opmode's layers expect a localizer, override this method and return a RobotLocalizer
     * implementation.
     *
     * @return The RobotLocalizer to use to determine the robot's transform during execution.
     */
    protected RobotLocalizer getLocalizer() {
        return null;
    }

    /**
     * Configures the LoggerProvider that will be passed to layers during initialization.
     * Override this method to do opmode-specific configuration for the global LoggerProvider.
     *
     * @param loggerProvider the LoggerProvider to configure.
     */
    protected void configureLogger(LoggerProvider loggerProvider) {
        // Do nothing
    }
}

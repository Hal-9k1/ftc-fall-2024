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
     * The VisionPortal created by {@link #createVisionPortal}.
     * A reference to it is stored so it may be started later after initialization ends.
     */
    private VisionPortal visionPortal;

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
        logger = loggerProvider.getLogger("AbstractLayerOpMode");

        List<CameraModule> cameraModules = getCameraModules();
        if (cameraModules.size() != 0) {
            VisionPortal.Builder visionBuilder = setupVisionPortal();
            for (CameraModule module : cameraModules) {
                module.createVisionProcessor(visionBuilder);
            }
            visionPortal = builder.build();
        } else {
            visionPortal = null;
        }

        localizer = getLocalizer();

        controller.setup(hardwareMap, localizer, getLayers(), gamepad1, gamepad2, loggerProvider);
    }

    @Override
    public final void start() {
        if (visionPortal != null) {
            visionPortal.resumeStreaming();
        }
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

    protected final void logVisionFps(Logger logger) {
        if (visionPortal == null) {
            throw new IllegalStateException(
                "Attempt to log vision FPS when no portal is constructed."
            );
        }
        logger.update("VisionPortal FPS", visionPortal.getFps());
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

    protected List<CameraModule> getCameraModules() {
        return new ArrayList<>();
    }

    private VisionPortal.Builder setupVisionPortal() {
        return new VisionPortal.Builder()
            .setCamera("WEBCAM_NAME_HERE")
            .enableLiveView(true);
    }
}

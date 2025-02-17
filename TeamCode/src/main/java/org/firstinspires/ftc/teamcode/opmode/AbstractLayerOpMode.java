package org.firstinspires.ftc.teamcode.opmode;

import java.util.ArrayList;
import java.util.List;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.firstinspires.ftc.vision.VisionPortal;

import org.firstinspires.ftc.teamcode.RobotController;
import org.firstinspires.ftc.teamcode.layer.Layer;
import org.firstinspires.ftc.teamcode.localization.RobotLocalizer;
import org.firstinspires.ftc.teamcode.logging.Logger;
import org.firstinspires.ftc.teamcode.logging.LoggerProvider;
import org.firstinspires.ftc.teamcode.vision.AbstractCameraModule;

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
     * The VisionPortal created by {@link #setupVisionPortal}, if one is needed.
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

        List<AbstractCameraModule> cameraModules = getCameraModules();
        if (cameraModules.size() != 0) {
            VisionPortal.Builder visionBuilder = setupVisionPortal();
            for (AbstractCameraModule module : cameraModules) {
                module.createVisionProcessor(visionBuilder);
            }
            visionPortal = visionBuilder.build();
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

    /**
     * Updates the AbstractLayerOpMode's internal logger with the frame processing rate of the
     * vision system.
     */
    protected final void logVisionFps() {
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

    /**
     * Retrieves the camera identifier used when creating a VisionPortal.
     * Subclasses only need to override this if also overriding {@link #getCameraModules}.
     *
     * @return The identifier of the camera to connect to the vision system.
     */
    protected CameraName getCameraName() {
        return null;
    }

    /**
     * Gets the list of camera modules to process for this opmode.
     * The default implementation returns an empty list. If a subclass opmode doesn't use vision, it
     * shouldn't override this method.
     *
     * @return The list of camera modules to use.
     */
    protected List<AbstractCameraModule> getCameraModules() {
        return new ArrayList<>();
    }

    /**
     * Creates and configures a vision portal with default settings.
     *
     * @return The configured vision portal.
     */
    private VisionPortal.Builder setupVisionPortal() {
        CameraName name = getCameraName();
        if (name == null) {
            throw new UnsupportedOperationException("If an opmode uses CameraModules, it must"
                + " override getCameraName to return a valid camera identifier.");
        }
        return new VisionPortal.Builder()
            .setCamera(getCameraName())
            .enableLiveView(true);
    }
}

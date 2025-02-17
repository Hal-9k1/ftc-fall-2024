package org.firstinspires.ftc.teamcode.opmode;

/**
 * Convenience base class for AbstractLayerOpModes that use vision processing.
 */
public abstract class AbstractVisionLayerOpMode extends AbstractLayerOpMode {
    /**
     * The VisionPortal created by {@link #createVisionPortal}.
     * A reference to it is stored so it may be started later after initialization ends.
     */
    private VisionPortal portal;

    @Override
    public final void start() {
        portal.resumeStreaming();
    }

    /**
     * Creates a VisionPortal which will begin processing camera data after the OpMode is started.
     */
    protected final void createVisionPortal() {
        VisionPortal.Builder builder = new VisionPortal.Builder()
            .setCamera("WEBCAM_NAME_HERE")
            .enableLiveView(true);
        configureVisionPortal(builder);
        portal = builder.build();
    }

    /**
     * Allows the VisionPortal created by {@link #createVisionPortal} to be further configured.
     * Override this method to add opmode-specific processors or other configuration.
     *
     * @param builder the VisionPortal builder to configure.
     */
    protected void configureVisionPortal(VisionPortal.Builder builder) {
        // Do nothing
    }

    protected void logFps(Logger logger) {
        logger.update("VisionPortal FPS", portal.getFps());
    }
}

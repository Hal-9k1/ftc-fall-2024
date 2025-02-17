
package org.firstinspires.ftc.teamcode.opmode;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.layer.Layer;
import org.firstinspires.ftc.teamcode.layer.LayerSetupInfo;
import org.firstinspires.ftc.teamcode.layer.input.AbstractInputGenerator;
import org.firstinspires.ftc.teamcode.localization.NewtonRobotLocalizer;
import org.firstinspires.ftc.teamcode.localization.RobotLocalizer;
import org.firstinspires.ftc.teamcode.logging.Logger;
import org.firstinspires.ftc.teamcode.task.Task;
import org.firstinspires.ftc.teamcode.vision.AbstractCameraModule;
import org.firstinspires.ftc.teamcode.vision.AprilTagLocalizationSource;

/**
 * An opmode that uses localization and vision-based localization sources and outputs the result to
 * the logger.
 * No drive system or any other peripherals are connected.
 */
@TeleOp(name="Vision Test")
public final class VisionTest extends AbstractLayerOpMode {
    /**
     * The AprilTagLocalizationSource that will be registered to the RobotLocalizer and as a
     * camera module.
     */
    private AprilTagLocalizationSource aprilTagLocalization;

    /**
     * Constructs a VisionTest.
     */
    public VisionTest() {
        aprilTagLocalization = new AprilTagLocalizationSource();
    }

    @Override
    protected List<Layer> getLayers() {
        return Arrays.asList(
            new AbstractInputGenerator() {
                private Logger logger;
                private RobotLocalizer localizer;

                @Override
                public void setup(LayerSetupInfo setupInfo) {
                    logger = setupInfo.getLogger("VisionTest anon layer");
                    localizer = setupInfo.getLocalizer();
                }

                @Override
                public Iterator<Task> update(Iterable<Task> completed) {
                    logger.transform("Localizer guess", null, localizer.resolveTransform());
                    return null;
                }
            }
        );
    }

    @Override
    protected RobotLocalizer getLocalizer() {
        RobotLocalizer localizer = new NewtonRobotLocalizer();
        localizer.registerSource(aprilTagLocalization);
        return localizer;
    }

    @Override
    protected List<AbstractCameraModule> getCameraModules() {
        return Arrays.asList(
            aprilTagLocalization
        );
    }
}

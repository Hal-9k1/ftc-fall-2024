
package org.firstinspires.ftc.teamcode.opmode;

import java.util.Arrays;
import java.util.List;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.layer.Layer;
import org.firstinspires.ftc.teamcode.layer.input.AbstractInputGenerator;

@TeleOp(name="Vision Test")
public final class VisionTest extends AbstractLayerOpMode {
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
                    logger.transform(localizer.resolveTransform());
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
    protected List<CameraModule> getCameraModules() {
        return Arrays.asList(
            aprilTagLocalization
        );
    }
}

package org.firstinspires.ftc.teamcode.layer;

import com.qualcomm.robotcore.hardware.HardwareMap;
import java.util.function.Consumer;
import org.firstinspires.ftc.teamcode.RobotController;

/**
 * Contains the information needed to initialize a layer.
 */
public class LayerSetupInfo {
    private final HardwareMap hardwareMap;
    private final RobotController robotController;

    /**
     * Creates a LayerSetupInfo.
     * @param hardwareMap the source of peripheral interfaces the layer may use to communicate with
     * hardware.
     * @param robotController the RobotController that will run the layer.
     */
    public LayerSetupInfo(HardwareMap hardwareMap, RobotController robotController) {
        this.hardwareMap = hardwareMap;
        this.robotController = robotController;
    }

    /**
     * Returns the HardwareMap.
     * @return A HardwareMap that can retrieve peripheral interfaces for devices connected to the
     * robot.
     */
    public HardwareMap getHardwareMap() {
        return hardwareMap;
    }
    /**
     * Registers a callback to be called on every update of the owning RobotController.
     * @param listener The callback to be called.
     */
    public void addUpdateListener(Consumer<Boolean> listener) {
        robotController.addUpdateListener(listener);
    }
}

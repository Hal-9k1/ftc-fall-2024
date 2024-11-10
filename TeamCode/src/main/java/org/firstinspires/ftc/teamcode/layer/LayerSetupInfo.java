package org.firstinspires.ftc.teamcode.layer;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import java.util.function.Consumer;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.RobotController;

/**
 * Contains the information needed to initialize a layer.
 */
public class LayerSetupInfo {
    private final HardwareMap hardwareMap;
    private final Telemetry telemetry;
    private final RobotController robotController;
    private final Gamepad gamepad0;
    private final Gamepad gamepad1;

    /**
     * Creates a LayerSetupInfo.
     * @param hardwareMap the source of peripheral interfaces the layer may use to communicate with
     * hardware.
     * @param robotController the RobotController that will run the layer.
     * @param gamepad0 the Gamepad connected to the first slot, or null if no such gamepad is
     * available or connected.
     * @param gamepad1 the Gamepad connected to the second slot, or null if no such gamepad is
     * available or connected.
     */
    public LayerSetupInfo(HardwareMap hardwareMap, Telemetry telemetry,
        RobotController robotController, Gamepad gamepad0, Gamepad gamepad1) {
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;
        this.robotController = robotController;
        this.gamepad0 = gamepad0;
        this.gamepad1 = gamepad1;
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
     * Returns the Telemetry.
     * @return The Telemetry used to communicate with the Driver Hub.
     */
    public Telemetry getTelemetry() {
        return telemetry;
    }
    /**
     * Returns the Gamepad connected to the first slot, or null if no such gamepad is available or
     * connected.
     * @return The first gamepad.
     */
    public Gamepad getGamepad0() {
        return gamepad0;
    }
    /**
     * Returns the Gamepad connected to the second slot, or null if no such gamepad is available or
     * connected.
     * @return The second gamepad.
     */
    public Gamepad getGamepad1() {
        return gamepad1;
    }
    /**
     * Registers a callback to be called on every update of the owning RobotController.
     * @param listener - the callback to be called. Passed with a value of true during teardown, and
     * false otherwise.
     */
    public void addUpdateListener(Consumer<Boolean> listener) {
        robotController.addUpdateListener(listener);
    }
}

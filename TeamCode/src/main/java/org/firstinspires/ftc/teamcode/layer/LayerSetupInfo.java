package org.firstinspires.ftc.teamcode.layer;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;

import org.firstinspires.ftc.teamcode.RobotController;

/**
 * Contains the information needed to initialize a layer.
 */
public class LayerSetupInfo {
    /**
     * The HardwareMap for the robot, where peripheral interfaces can be retrieved.
     */
    private final HardwareMap hardwareMap;

    /**
     * The RobotController setting up the layer.
     */
    private final RobotController robotController;

    /**
     * The gamepad connected to the first port.
     * Null if none is connected or available (e.g. we're in teleop).
     */
    private final Gamepad gamepad0;

    /**
     * The gamepad connected to the second port.
     * Null if none is connected or available (e.g. we're in teleop).
     */
    private final Gamepad gamepad1;

    /**
     * Telemetry.
     */
    private final Telemetry telemetry;

    /**
     * Creates a LayerSetupInfo.
     *
     * @param hardwareMap - the source of peripheral interfaces the layer may use to communicate with
     * hardware.
     * @param robotController - the RobotController that will run the layer.
     * @param telemetry - Telemetry used to report debugging info.
     * @param gamepad0 - the Gamepad connected to the first slot, or null if no such gamepad is
     * available or connected.
     * @param gamepad1 - the Gamepad connected to the second slot, or null if no such gamepad is
     * available or connected.
     */
    public LayerSetupInfo(HardwareMap hardwareMap, RobotController robotController,
        Telemetry telemetry, Gamepad gamepad0, Gamepad gamepad1
    ) {
        this.hardwareMap = hardwareMap;
        this.robotController = robotController;
        this.telemetry = telemetry;
        this.gamepad0 = gamepad0;
        this.gamepad1 = gamepad1;
    }

    /**
     * Returns the HardwareMap.
     *
     * @return A HardwareMap that can retrieve peripheral interfaces for devices connected to the
     * robot.
     */
    public HardwareMap getHardwareMap() {
        return hardwareMap;
    }

    /**
     * Gets the Telemetry.
     *
     * @return the Telemetry.
     */
    public Telemetry getTelemetry() {
        return telemetry;
    }

    /**
     * Returns the Gamepad connected to the first slot, or null if no such gamepad is available or
     * connected.
     *
     * @return The first gamepad.
     */
    public Gamepad getGamepad0() {
        return gamepad0;
    }

    /**
     * Returns the Gamepad connected to the second slot, or null if no such gamepad is available or
     * connected.
     *
     * @return The second gamepad.
     */
    public Gamepad getGamepad1() {
        return gamepad1;
    }

    /**
     * Registers a callback to be called on every update of the owning RobotController.
     *
     * @param listener - the callback to be called.
     */
    public void addUpdateListener(Runnable listener) {
        robotController.addUpdateListener(listener);
    }

    /**
     * Registers a callback to be called after the layer stack finishes executing.
     *
     * @param listener - the callback to be called.
     */
    public void addTeardownListener(Runnable listener) {
        robotController.addTeardownListener(listener);
    }
}

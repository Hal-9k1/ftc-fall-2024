package org.firstinspires.ftc.teamcode.localization;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

import org.firstinspires.ftc.teamcode.matrix.Mat2;
import org.firstinspires.ftc.teamcode.matrix.Mat3;
import org.firstinspires.ftc.teamcode.matrix.Vec2;

/**
 * Uses the inertial measurement unit (IMU) embedded in the Control Hub to estimate the robot's
 * orientation.
 * Yaw drift inherent to using an IMU without an absolute reference (e.g. a magnet) is reflected in
 * the decaying reported accuracy of the localization data produced by this source.
 */
public final class ImuLocalizationSource implements LocalizationSource {
    /**
     * The hardware identifier of the IMU as given in the robot configuration menu.
     */
    private static final String IMU_NAME = "imu 1";

    /**
     * The decay constant for the accuracy of the collected localization data.
     * The accuracy of the IMU measurement decreases with time due to yaw drift, so the reported
     * accuracy also decreases after initialization to reflect this in localization data weighting.
     * This weight is the reciprocal of the number of nanoseconds after initialization the reported
     * accuracy should drop to 1/e (about %40). In other words, higher value = faster decay.
     */
    private static final double K = 0.00001;

    /**
     * The interface used to communicate with the IMU.
     */
    private IMU imu;

    /**
     * The nanosecond timestamp of the last time the IMU's yaw was reset.
     */
    private long lastResetTime;

    /**
     * The robot's field space yaw in radians when the IMU's yaw was last reset.
     */
    private double initialYaw;

    /**
     * Constructs an ImuLocalizationSource.
     *
     * @param hardwareMap the hardware map to retrieve the IMU interface from.
     */
    public ImuLocalizationSource(HardwareMap hardwareMap) {
        imu = hardwareMap.get(IMU.class, IMU_NAME);
        imu.initialize(new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.LEFT,
                RevHubOrientationOnRobot.UsbFacingDirection.UP)
        ));
    }

    /**
     * Asserts that the robot currently has the given rotation and resets the accuracy decay.
     *
     * @param yaw the robot's current field space rotation in radians.
     */
    public void reset(double yaw) {
        initialYaw = initialYaw;
        lastResetTime = System.nanoTime();
        imu.resetYaw();
    }

    @Override
    public boolean canLocalizePosition() {
        return false;
    }

    @Override
    public boolean canLocalizeRotation() {
        return true;
    }

    @Override
    public LocalizationData collectData() {
        double imuYaw = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS) + initialYaw;
        return new SqFalloffLocalizationData(
            Mat3.fromTransform(Mat2.fromAngle(imuYaw), new Vec2(0, 0)),
            Math.exp(-K * (System.nanoTime() - lastResetTime)),
            0,
            1
        );
    }
}

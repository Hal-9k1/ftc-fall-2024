package org.firstinspires.ftc.teamcode.localization;

public interface RobotLocalizer {
    void invalidateCache();
    void registerSource(LocalizationSource source);
    Mat3 resolveTransform();
    Vec2 resolvePosition();
    double resolveAngle();
}

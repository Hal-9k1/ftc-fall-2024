package org.firstinspires.ftc.teamcode.localization;

import org.firstinspires.ftc.teamcode.matrix.Mat3;
import org.firstinspires.ftc.teamcode.matrix.Vec2;

public interface RobotLocalizer {
    void invalidateCache();

    void registerSource(LocalizationSource source);

    Mat3 resolveTransform();

    Vec2 resolvePosition();

    double resolveRotation();
}

package org.firstinspires.ftc.teamcode.localization;

import java.util.List;

public interface LocalizationData {
    double getPositionProbability(Vec2 pos);

    double getPositionProbabilityDx(Vec2 pos, List<Vec2> ignoreRoots);

    double getPositionProbabilityDy(Vec2 pos, List<Vec2> ignoreRoots);

    Vec2 getPositionProbabilityDxGradient(Vec2 pos, List<Vec2> ignoreRoots);

    Vec2 getPositionProbabilityDyGradient(Vec2 pos, List<Vec2> ignoreRoots);

    double getRotationProbability(double rot);

    double getRotationProbabilityDx(double rot, List<Double> ignoreRoots);

    double getRotationProbabilityDx2(double rot, List<Double> ignoreRoots);
}

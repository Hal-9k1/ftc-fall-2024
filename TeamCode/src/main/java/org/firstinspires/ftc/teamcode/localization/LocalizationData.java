package org.firstinspires.ftc.teamcode.localization;

public interface LocalizationData {
    double getPositionProbability(Vec2 pos);

    Vec2 getPositionProbabilityGradient(Vec2 pos, List<Double> ignoreRoots);

    double getRotationProbability(double rot);

    double getRotationProbabilityDerivative(double rot, List<Double> ignoreRoots);

    public double getPositionPrecision() {
        return positionPrecision;
    }

    public double getRotationPrecision() {
        return rotationPrecision;
    }
}
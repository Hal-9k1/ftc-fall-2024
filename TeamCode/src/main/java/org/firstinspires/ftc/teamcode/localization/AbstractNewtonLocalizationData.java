package org.firstinspires.ftc.teamcode.localization;

public abstract class AbstractNewtonLocalizationData implements LocalizationData {
    Vec2 getPositionProbabilityGradient(Vec2 pos, List<Double> ignoreRoots) {

    }

    double getRotationProbabilityDerivative(double rot, List<Double> ignoreRoots) {

    }

    public double getPositionPrecision() {
        return positionPrecision;
    }

    public double getRotationPrecision() {
        return rotationPrecision;
    }
}

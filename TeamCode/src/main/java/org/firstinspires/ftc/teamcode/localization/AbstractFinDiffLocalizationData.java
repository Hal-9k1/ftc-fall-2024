package org.firstinspires.ftc.teamcode.localization;

public abstract class AbstractFinDiffLocalizationData implements LocalizationData {
    private final double epsilon;

    public AbstractFinDiffLocalizationData(double epsilon) {
        this.epsilon = epsilon;
    }

    Vec2 getPositionProbabilityGradient(Vec2 pos, List<Vec2> ignoreRoots) {
        return getPositionProbability(pos).mul(-1).add(getPositionProbability(pos.add(new Vec2(epsilon, epsilon)))).mul(1 / pos);
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

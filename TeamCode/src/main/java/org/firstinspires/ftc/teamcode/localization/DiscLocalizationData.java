package org.firstinspires.ftc.teamcode.localization;

public final class DiscLocalizationData extends AbstractFinDiffLocalizationData {
    private static final double EPSILON = 0.001;

    private Mat3 transform;

    private double accuracy;

    private double positionPrecision;

    private double rotationPrecision;

    public DiscLocalizationData(
        Mat3 transform,
        double accuracy,
        double positionPrecision,
        double rotationPrecision
    ) {
        super(EPSILON);
        this.transform = transform;
        this.accuracy = accuracy;
        this.positionPrecision = positionPrecision;
        this.rotationPrecision = rotationPrecision;
    }

    public Mat3 getTransform() {
        return transform;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public double getPositionProbability(Vec2 pos) {
        Vec2 diff = transform.getTranslation().mul(-1).add(pos);
        return accuracy / ((diff.dot(diff) * positionPrecision) + 1);
    }

    public double getRotationProbability(double rot) {
        double diff = rot - transform.getRotation();
        return accuracy / (diff * diff * rotationPrecision + 1);
    }

    public double getPositionPrecision() {
        return positionPrecision;
    }

    public double getRotationPrecision() {
        return rotationPrecision;
    }
}

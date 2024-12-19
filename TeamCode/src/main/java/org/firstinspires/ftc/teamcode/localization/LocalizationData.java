package org.firstinspires.ftc.teamcode.localization;

public final class LocalizationData {
    private Mat3 transform;
    private double accuracy;
    private double positionPrecision;
    private double rotationPrecision;

    public LocalizationData(
        Mat3 transform,
        double accuracy,
        double positionPrecision,
        double rotationPrecision
    ) {
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

    public double getPositionPrecision() {
        return positionPrecision;
    }

    public double getRotationPrecision() {
        return rotationPrecision;
    }
}

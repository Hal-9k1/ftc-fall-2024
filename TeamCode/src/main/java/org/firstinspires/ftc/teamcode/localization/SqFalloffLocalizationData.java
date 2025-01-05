package org.firstinspires.ftc.teamcode.localization;

import org.firstinspires.ftc.teamcode.matrix.Mat3;
import org.firstinspires.ftc.teamcode.matrix.Vec2;

/**
 * LocalizationData suggesting the robot's transform is "near" a position and/or rotation with a
 * square distance falloff.
 */
public final class SqFalloffLocalizationData extends AbstractFinDiffLocalizationData {
    private static final double EPSILON = 0.001;

    private Mat3 transform;

    private double accuracy;

    private double positionPrecision;

    private double rotationPrecision;

    public SqFalloffLocalizationData(
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

    public double getPositionProbability(Vec2 pos) {
        Vec2 diff = transform.getTranslation().mul(-1).add(pos);
        return accuracy / ((diff.dot(diff) * positionPrecision) + 1);
    }

    public double getRotationProbability(double rot) {
        double diff = rot - transform.getRotation();
        return accuracy / (diff * diff * rotationPrecision + 1);
    }
}

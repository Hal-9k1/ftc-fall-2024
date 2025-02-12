package org.firstinspires.ftc.teamcode.localization;

import java.util.List;

/**
 * Represents an uncertain clue about the robot's position, rotation, or both, collected from a
 * {@link LocalizationSource}.
 */
public interface LocalizationData {
    /**
     * Gets the probability according to this datum of the robot being at the given field position.
     *
     * @param pos a field position in meters.
     * @return The probability of the robot being at the given position.
     */
    double getPositionProbability(Vec2 pos);

    /**
     * Returns the partial derivative of the position probability with respect to x.
     */
    double getPositionProbabilityDx(Vec2 pos, List<Vec2> ignoreRoots);

    /**
     * Returns the partial derivative of the position probability with respect to y.
     */
    double getPositionProbabilityDy(Vec2 pos, List<Vec2> ignoreRoots);

    /**
     * Returns the gradient of the partial derivative of the position probability with respect to x.
     */
    Vec2 getPositionProbabilityDxGradient(Vec2 pos, List<Vec2> ignoreRoots);

    /**
     * Returns the gradient of the partial derivative of the position probability with respect to y.
     */
    Vec2 getPositionProbabilityDyGradient(Vec2 pos, List<Vec2> ignoreRoots);

    /**
     * Gets the probability of the robot having the given rotation.
     *
     * @param rot a rotation in radians. Zero indicates the positive x direction, and increasing
     * values are counterclockwise.
     * @return The probability of the robot having the given rotation.
     */
    double getRotationProbability(double rot);

    /**
     * Gets the first derivative of the rotation probability.
     */
    double getRotationProbabilityDx(double rot, List<Double> ignoreRoots);

    /**
     * Gets the second derivative of the rotation probability.
     */
    double getRotationProbabilityDx2(double rot, List<Double> ignoreRoots);
}

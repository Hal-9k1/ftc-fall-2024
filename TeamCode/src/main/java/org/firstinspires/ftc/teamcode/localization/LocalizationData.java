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
     *
     * @param pos the position at which to get the partial derivative of the position probability.
     * @param ignoreRoots a list of already known point roots of the derivative to divide out. This
     * affects the behavior of the derivative around these roots and creates points of discontinuity
     * at the roots.
     * @return The partial derivative of the position probability with respect to x at the given
     * position. This is guarenteed to be finite.
     */
    double getPositionProbabilityDx(Vec2 pos, List<Vec2> ignoreRoots);

    /**
     * Returns the partial derivative of the position probability with respect to y.
     *
     * @param pos the position at which to get the partial derivative of the position probability.
     * @param ignoreRoots a list of already known point roots of the derivative to divide out. This
     * affects the behavior of the derivative around these roots and creates points of discontinuity
     * at the roots.
     * @return The partial derivative of the position probability with respect to y at the given
     * position. This is guarenteed to be finite.
     */
    double getPositionProbabilityDy(Vec2 pos, List<Vec2> ignoreRoots);

    /**
     * Returns the gradient of the partial derivative of the position probability with respect to x.
     *
     * @param pos the position at which to get the gradient of the partial derivative of the
     * position probability.
     * @param ignoreRoots a list of already known point roots of the original partial derivative to
     * divide out. The gradient will reflect the removal of these roots.
     * @return The gradient of the partial derivative of the position probability with respect to x
     * at the given position. This is guarenteed to be finite.
     */
    Vec2 getPositionProbabilityDxGradient(Vec2 pos, List<Vec2> ignoreRoots);

    /**
     * Returns the gradient of the partial derivative of the position probability with respect to y.
     *
     * @param pos the position at which to get the gradient of the partial derivative of the
     * position probability.
     * @param ignoreRoots a list of already known point roots of the original partial derivative to
     * divide out. The gradient will reflect the removal of these roots.
     * @return The gradient of the partial derivative of the position probability with respect to y
     * at the given position. This is guarenteed to be finite.
     */
    Vec2 getPositionProbabilityDyGradient(Vec2 pos, List<Vec2> ignoreRoots);

    /**
     * Gets the probability of the robot having the given rotation.
     *
     * @param rot a rotation in radians. Zero indicates the positive x direction, and increasing
     * values are counterclockwise.
     * @return The probability of the robot having the given rotation. This is guarenteed to be
     * finite.
     */
    double getRotationProbability(double rot);

    /**
     * Evaluates the first derivative of the rotation probability.
     *
     * @param rot the rotation to evaluate the derivative at.
     * @param ignoreRoots a list of already known roots of the derivative to divide out. This
     * affects the behavior of the derivative around these roots and creates points of discontinuity
     * at the roots.
     * @return The first derivative of the rotation probability at the given rotation. This is
     * guarenteed to be finite.
     */
    double getRotationProbabilityDx(double rot, List<Double> ignoreRoots);

    /**
     * Gets the second derivative of the rotation probability.
     *
     * @param rot the rotation to evaluate the derivative at.
     * @param ignoreRoots a list of already known roots of the first derivative to divide out. The
     * second derivative will reflect the removal of these roots.
     * @return The second derivative of the rotation probability at the given rotation.
     */
    double getRotationProbabilityDx2(double rot, List<Double> ignoreRoots);
}

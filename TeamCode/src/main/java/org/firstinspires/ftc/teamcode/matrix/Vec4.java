package org.firstinspires.ftc.teamcode.matrix;

/**
 * Represents a 4D vector.
 */
public final class Vec4 {
    /**
     * An array holding the components of the vector.
     */
    private final double[] vec;

    /**
     * Constructs a Vec4.
     *
     * @param x the x component of the vector.
     * @param y the y component of the vector.
     * @param z the z component of the vector.
     * @param w the w component of the vector.
     */
    public Vec4(double x, double y, double z, double w) {
        vec = new double[] {x, y, z, w};
    }

    /**
     * Gets the x component of the vector.
     *
     * @return The x component of the vector.
     */
    public double getX() {
        return vec[0];
    }

    /**
     * Gets the y component of the vector.
     *
     * @return The y component of the vector.
     */
    public double getY() {
        return vec[1];
    }

    /**
     * Gets the z component of the vector.
     *
     * @return The z component of the vector.
     */
    public double getZ() {
        return vec[2];
    }

    /**
     * Gets the w component of the vector.
     *
     * @return The w component of the vector.
     */
    public double getW() {
        return vec[3];
    }

    /**
     * Calculates the dot product of this and a given vector.
     *
     * @param other the factor to compute the dot product with.
     * @return The dot product.
     */
    public double dot(Vec4 other) {
        return vec[0] * other.vec[0]
            + vec[1] * other.vec[1]
            + vec[2] * other.vec[2]
            + vec[3] * other.vec[3];
    }

    /**
     * Gets a component of the vector.
     *
     * @param index which component to return: the x coordinate if 0, y if 1, z if 2/
     * @return The indicated component of the vector.
     */
    public double get(int index) {
        if (index < 0 || index > 2) {
            throw new IllegalArgumentException("Bad index " + index);
        }
        return vec[index];
    }
}

package org.firstinspires.ftc.teamcode.localization;

/**
 * Represents a 2D vector.
 */
public final class Vec2 {
    /**
     * The x component of the vector.
     */
    private final double x;

    /**
     * The y component of the vector.
     */
    private final double y;

    /**
     * Constructs a Vec2.
     *
     * @param x the x component of the vector.
     * @param y the y component of the vector.
     */
    public Vec2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Gets the x component of the vector.
     *
     * @return The x component of the vector.
     */
    public double getX() {
        return x;
    }

    /**
     * Gets the y component of the vector.
     *
     * @return The y component of the vector.
     */
    public double getY() {
        return y;
    }

    /**
     * Gets the sum of this and a given vector.
     *
     * @param other the addend.
     * @return A new vector that is the sum of this and the given vector.
     */
    public Vec2 add(Vec2 other) {
        return new Vec2(x + other.x, y + other.y);
    }

    /**
     * Gets the produce of this vector with a given scalar.
     *
     * @param scalar the scalar factor.
     * @return A new vector that is the scalar product of this vector and the given scalar.
     */
    public Vec2 mul(double scalar) {
        return new Vec2(x * scalar, y * scalar);
    }

    /**
     * Calculates the dot product of this and a given vector.
     *
     * @param other the factor to compute the dot product with.
     * @return The dot product.
     */
    public double dot(Vec2 other) {
        return x * other.x + y * other.y;
    }

    /**
     * Finds the length of this vector.
     *
     * @return The length of this vector.
     */
    public double len() {
        return Math.sqrt(dot(this));
    }

    /**
     * Checks whether both components of the vector are finite.
     *
     * @return Whether both components of this vector are finite.
     */
    public boolean isFinite() {
        return Double.isFinite(x) && !Double.isNaN(x) && Double.isFinite(y) && !Double.isNaN(y);
    }
}

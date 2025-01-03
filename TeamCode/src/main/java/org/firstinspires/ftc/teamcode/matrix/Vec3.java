package org.firstinspires.ftc.teamcode.matrix;

public final class Vec3 {
    private final double[] vec;

    public Vec3(double x, double y, double z) {
        vec = new double[] { x, y, z };
    }

    public double getX() {
        return vec[0];
    }

    public double getY() {
        return vec[1];
    }

    public double getZ() {
        return vec[2];
    }

    public double dot(Vec3 other) {
        return vec[0] * other.vec[0] + vec[1] * other.vec[1] + vec[2] * other.vec[2];
    }

    public double get(int index) {
        if (index < 0 || index > 2) {
            throw new IllegalArgumentException("Bad index " + index);
        }
        return vec[index];
    }
}

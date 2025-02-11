package org.firstinspires.ftc.teamcode.localization;

public final class Vec2 {
    private final double x;

    private final double y;

    public Vec2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Vec2 add(Vec2 other) {
        return new Vec2(x + other.x, y + other.y);
    }

    public Vec2 mul(double scalar) {
        return new Vec2(x * scalar, y * scalar);
    }

    public double dot(Vec2 other) {
        return x * other.x + y * other.y;
    }

    public double len() {
        return Math.sqrt(dot(this));
    }

    public double getAngle() {
        return Math.atan2(y, x);
    }

    public boolean isFinite() {
        return Double.isFinite(x) && !Double.isNaN(x) && Double.isFinite(y) && !Double.isNaN(y);
    }
}

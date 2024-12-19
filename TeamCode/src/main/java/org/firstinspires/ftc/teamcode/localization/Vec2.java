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

    public double dot(Vec2 other) {
        return x * other.x + y * other.y;
    }
}

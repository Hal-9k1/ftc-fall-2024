package org.firstinspires.ftc.teamcode.localization;

public final class Mat2 {
    // CSOFF:MagicNumber
    private final double[] mat;

    public Mat2(double m00, double m10, double m01, double m11) {
        mat = new double[] {m00, m10, m01, m11};
    }

    public static Mat2 fromAngle(double angle) {
        return new Mat2(Math.cos(angle), -Math.sin(angle), Math.sin(angle), Math.cos(angle));
    }

    public Mat2 mul(Mat2 other) {
        return new Mat2(
            mat[0] * other.mat[0] + mat[1] * other.mat[2],
            mat[0] * other.mat[1] + mat[1] * other.mat[3],
            mat[2] * other.mat[0] + mat[3] * other.mat[2],
            mat[2] * other.mat[1] + mat[3] * other.mat[3]
        );
    }

    public Vec2 mul(Vec2 other) {
        return new Vec2(
            mat[0] * other.getX() + mat[1] * other.getY(),
            mat[2] * other.getX() + mat[3] * other.getY()
        );
    }

    public Mat2 mul(double other) {
        return new Mat2(
            mat[0] * other,
            mat[1] * other,
            mat[2] * other,
            mat[3] * other
        );
    }

    public double det() {
        return mat[0] * mat[3] - mat[1] * mat[2];
    }

    public Mat2 inv() {
        double d = det();
        return new Mat2(
            mat[3] / d,
            -mat[2] / d,
            -mat[1] / d,
            mat[0] / d
        );
    }

    public Vec2 col(int num) {
        if (num == 0) {
            return new Vec2(mat[0], mat[2]);
        } else if (num == 1) {
            return new Vec2(mat[1], mat[3]);
        } else {
            throw new IllegalArgumentException("Bad column number " + num);
        }
    }

    public Vec2 row(int num) {
        if (num == 0) {
            return new Vec2(mat[0], mat[1]);
        } else if (num == 1) {
            return new Vec2(mat[2], mat[3]);
        } else {
            throw new IllegalArgumentException("Bad row number " + num);
        }
    }

    public double elem(int x, int y) {
        return mat[y * 2 + x];
    }
}

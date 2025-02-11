package org.firstinspires.ftc.teamcode.localization;

public final class Mat3 {
    private final double[] mat;

    public Mat3(
        double m00, double m10, double m20,
        double m01, double m11, double m21,
        double m02, double m12, double m22
    ) {
        mat = new double[] { m00, m10, m20, m01, m11, m21, m02, m12, m22 };
    }

    public static Mat3 fromTransform(Mat2 rot, Vec2 pos) {
        return new Mat3(
            rot.elem(0, 0), rot.elem(1, 0), pos.getX(),
            rot.elem(0, 1), rot.elem(1, 1), pos.getY(),
            0.0, 0.0, 1.0
        );
    }

    public Mat3 mul(Mat3 other) {
        return new Mat3(
            row(0).dot(other.col(0)),
            row(0).dot(other.col(1)),
            row(0).dot(other.col(2)),
            row(1).dot(other.col(0)),
            row(1).dot(other.col(1)),
            row(1).dot(other.col(2)),
            row(2).dot(other.col(0)),
            row(2).dot(other.col(1)),
            row(2).dot(other.col(2))
        );
    }

    public Vec2 mul(Vec2 other) {
        Vec3 extended = new Vec3(other.getX(), other.getY(), 0.0);
        return new Vec2(
            row(0).dot(extended),
            row(1).dot(extended)
        );
    }

    public Vec3 mul(Vec3 other) {
        return new Vec3(
            row(0).dot(other),
            row(1).dot(other),
            row(2).dot(other)
        );
    }

    public Mat3 mul(double other) {
        return new Mat3(
            mat[0] * other,
            mat[1] * other,
            mat[2] * other,
            mat[3] * other,
            mat[4] * other,
            mat[5] * other,
            mat[6] * other,
            mat[7] * other,
            mat[8] * other
        );
    }

    public double det() {
        return mat[0] * mat[4] * mat[8]
            + mat[1] * mat[5] * mat[6]
            + mat[2] * mat[3] * mat[7]
            - mat[2] * mat[4] * mat[6]
            - mat[0] * mat[5] * mat[7]
            - mat[1] * mat[3] * mat[8];
    }

    public Mat3 inv() {
        return cofactor().transpose().mul(1 / det());
    }

    public Vec3 col(int num) {
        checkDim(num, true);
        return new Vec3(mat[num], mat[num + 3], mat[num + 6]);
    }

    public Vec3 row(int num) {
        checkDim(num, false);
        return new Vec3(mat[num * 3], mat[num * 3 + 1], mat[num * 3 + 2]);
    }

    public Mat3 transpose() {
        return new Mat3(
            mat[0], mat[3], mat[6],
            mat[1], mat[4], mat[7],
            mat[2], mat[5], mat[8]
        );
    }

    public Mat2 minor(int col, int row) {
        checkDim(row, false);
        checkDim(col, true);
        int startCol = col == 0 ? 1 : 0;
        int colDiff = col == 1 ? 2 : 1;
        int startRow = row == 0 ? 1 : 0;
        int rowDiff = row == 1 ? 2 : 1;
        return new Mat2(
            col(startCol).get(startRow),
            col(startCol + colDiff).get(startRow),
            col(startCol).get(startRow + rowDiff),
            col(startCol + colDiff).get(startRow + rowDiff)
        );
    }

    public Mat3 cofactor() {
        return new Mat3(
            minor(0, 0).det(),
            -minor(1, 0).det(),
            minor(2, 0).det(),
            -minor(0, 1).det(),
            minor(1, 1).det(),
            -minor(2, 1).det(),
            minor(0, 2).det(),
            -minor(1, 2).det(),
            minor(2, 2).det()
        );
    }

    public double elem(int col, int row) {
        checkDim(col, true);
        checkDim(row, false);
        return mat[row * 3 + col];
    }

    public Vec2 getTranslation() {
        return new Vec2(mat[2], mat[5]);
    }

    public Vec2 getDirection() {
        return Mat3.fromTransform(minor(2, 2), new Vec2()).mul(new Vec2(1, 0));
    }

    private void checkDim(int num, boolean col) {
        if (num < 0 || num > 2) {
            throw new IllegalArgumentException("Bad " + (col ? "column" : "row") + " number "
                + num);
        }
    }
}

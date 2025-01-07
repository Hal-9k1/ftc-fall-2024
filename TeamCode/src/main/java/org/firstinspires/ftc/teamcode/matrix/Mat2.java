package org.firstinspires.ftc.teamcode.matrix;

// CSOFF type:MagicNumber

/**
 * Represents an immutable 2x2 matrix of double-precision floating point numbers.
 */
public final class Mat2 {
    /**
     * The underlying array storing matrix elements.
     */
    private final double[] mat;

    /**
     * Constructs a Mat2.
     *
     * @param m00 - the element in the 0th row and 0th column
     * @param m10 - the element in the 1st row and 0th column
     * @param m01 - the element in the 0th row and 1st column
     * @param m11 - the element in the 1st row and 1st column
     */
    public Mat2(double m00, double m10, double m01, double m11) {
        mat = new double[]{m00, m10, m01, m11};
    }

    /**
     * Creates a rotation matrix for the given angle.
     *
     * @param angle - the counterclockwise angle in radians to create a rotation matrix for.
     * @return the created rotation matrix.
     */
    public static Mat2 fromAngle(double angle) {
        return new Mat2(Math.cos(angle), -Math.sin(angle), Math.sin(angle), Math.cos(angle));
    }

    /**
     * Returns a matrix which is the product of this and another matrix.
     * If the matrices represent transformations, their product is the transformation matrix
     * equivalent to applying the first factor's transformation and then the second one's.
     *
     * @param other - the matrix to multiply by.
     * @return the product.
     */
    public Mat2 mul(Mat2 other) {
        return new Mat2(
            mat[0] * other.mat[0] + mat[1] * other.mat[2],
            mat[0] * other.mat[1] + mat[1] * other.mat[3],
            mat[2] * other.mat[0] + mat[3] * other.mat[2],
            mat[2] * other.mat[1] + mat[3] * other.mat[3]
        );
    }

    /**
     * Returns a vector which is the product of this and another matrix.
     * If the matrix represents a rotation, their product is the vector rotated about the origin by
     * this rotation. This product could also represent the transformation of a 1D point, but this
     * has no obvious use on our robot.
     *
     * @param other - the vector to multiply by.
     * @return the product.
     */
    public Vec2 mul(Vec2 other) {
        return new Vec2(
            mat[0] * other.getX() + mat[1] * other.getY(),
            mat[2] * other.getX() + mat[3] * other.getY()
        );
    }

    /**
     * Returns a matrix which is the elementwise product of this matrix and a scalar.
     *
     * @param other - the scalar to multiply each element by.
     * @return the scalar product.
     */
    public Mat2 mul(double other) {
        return new Mat2(
            mat[0] * other,
            mat[1] * other,
            mat[2] * other,
            mat[3] * other
        );
    }

    /**
     * Computes the determinant of the matrix.
     *
     * @return the determinant of this matrix.
     */
    public double det() {
        return mat[0] * mat[3] - mat[1] * mat[2];
    }

    /**
     * Computes the inverse of the matrix.
     *
     * @return the inverse of this matrix. If {@link #det} returns 0, this may return a matrix where
     * {@link #isFinite} is false.
     */
    public Mat2 inv() {
        double d = det();
        return new Mat2(
            mat[3] / d,
            -mat[2] / d,
            -mat[1] / d,
            mat[0] / d
        );
    }

    /**
     * Returns whether all matrix elements {@link Double#isFinite are finite}.
     *
     * @return whether all matrix elements are a valid, real, nonexceptional number.
     */
    public boolean isFinite() {
        return Double.isFinite(mat[0])
            && Double.isFinite(mat[1])
            && Double.isFinite(mat[2])
            && Double.isFinite(mat[3]);
    }

    /**
     * Returns the given column of the matrix.
     *
     * @param num - the number of column to return.
     * @return the specified column as a vector.
     */
    public Vec2 col(int num) {
        if (num == 0) {
            return new Vec2(mat[0], mat[2]);
        } else if (num == 1) {
            return new Vec2(mat[1], mat[3]);
        } else {
            throw new IllegalArgumentException("Bad column number " + num);
        }
    }

    /**
     * Returns the given row of the matrix.
     *
     * @param num - the number of row to return.
     * @return the specified row as a vector.
     */
    public Vec2 row(int num) {
        if (num == 0) {
            return new Vec2(mat[0], mat[1]);
        } else if (num == 1) {
            return new Vec2(mat[2], mat[3]);
        } else {
            throw new IllegalArgumentException("Bad row number " + num);
        }
    }

    /**
     * Returns the element at the given column and row.
     *
     * @param col - the column number.
     * @param row - the row number.
     * @return the element at the given position.
     */
    public double elem(int col, int row) {
        return mat[row * 2 + col];
    }
}

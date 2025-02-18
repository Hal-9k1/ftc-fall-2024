package org.firstinspires.ftc.teamcode.matrix;

/**
 * Represents an immutable 3x3 matrix of double-precision floating point numbers.
 * Commonly used to express 3D rotations or 2D transformations (combined translation and rotation).
 */
public final class Mat3 {
    // CSOFF:MagicNumber
    /**
     * The underlying array storing matrix elements.
     */
    private final double[] mat;

    /**
     * Constructs a Mat3.
     * The top-left element is has position (0, 0). x positions increase rightward and y positions
     * downward.
     *
     * @param m00 the element at position (0, 0).
     * @param m10 the element at position (1, 0).
     * @param m20 the element at position (2, 0).
     * @param m01 the element at position (0, 1).
     * @param m11 the element at position (1, 1).
     * @param m21 the element at position (2, 1).
     * @param m02 the element at position (0, 2).
     * @param m12 the element at position (1, 2).
     * @param m22 the element at position (2, 2).
     */
    public Mat3(
        double m00, double m10, double m20,
        double m01, double m11, double m21,
        double m02, double m12, double m22
    ) {
        mat = new double[] {m00, m10, m20, m01, m11, m21, m02, m12, m22};
    }

    /**
     * Creates a zero matrix.
     * Pre- or postmultiplying by a zero matrix results in another zero matrix.
     *
     * @return A new Mat3 populated with zeroes.
     */
    public static Mat3 zero() {
        return new Mat3(
            0, 0, 0,
            0, 0, 0,
            0, 0, 0
        );
    }

    /**
     * Creates an identity matrix.
     * Pre- or postmultiplying by an identity matrix results in the original matrix.
     *
     * @return A new Mat3 populated with ones along the diagonal and zeroes everywhere else.
     */
    public static Mat3 identity() {
        return new Mat3(
            1, 0, 0,
            0, 1, 0,
            0, 0, 1
        );
    }

    /**
     * Creates a Mat3 representing a transformation from a 2D rotation matrix and 2D translation.
     *
     * @param rot a 2D rotation matrix like those returned by {@link Mat2#fromAngle}.
     * @param pos a translation or position.
     * @return A new transformation matrix simultaneously encoding the given rotation and
     * translation.
     */
    public static Mat3 fromTransform(Mat2 rot, Vec2 pos) {
        return new Mat3(
            rot.elem(0, 0), rot.elem(1, 0), pos.getX(),
            rot.elem(0, 1), rot.elem(1, 1), pos.getY(),
            0.0, 0.0, 1.0
        );
    }

    /**
     * Creates a Mat3 representing a 3D rotation from yaw, pitch, and roll angles.
     * Multiplying by this matrix applies the given yaw, then pitch, then roll.
     *
     * @param yaw the yaw angle in radians.
     * @param pitch the pitch angle in radians.
     * @param roll the roll angle in radians.
     * @return A rotation matrix encoding the given angles.
     */
    public static Mat3 fromYawPitchRoll(double yaw, double pitch, double roll) {
        Mat3 yawMat = new Mat3(
            Math.cos(yaw), -Math.sin(yaw), 0,
            Math.sin(yaw), Math.cos(yaw), 0,
            0, 0, 1
        );
        Mat3 pitchMat = new Mat3(
            Math.cos(pitch), 0, Math.sin(pitch),
            0, 1, 0,
            -Math.sin(pitch), 0, Math.cos(pitch)
        );
        Mat3 rollMat = new Mat3(
            1, 0, 0,
            0, Math.cos(roll), -Math.sin(roll),
            0, Math.sin(roll), Math.cos(roll)
        );
        return yawMat.mul(pitchMat).mul(rollMat);
    }

    /**
     * Returns the product of this matrix and the given matrix.
     * If both are transformation matrices, has the effect of composing the two transformations.
     * Pre- or postmultiplying by the resulting matrix will have the same effect as multiplying by
     * this matrix, then by the given matrix.
     *
     * @param other the matrix to postmultiply by.
     * @return A new matrix that is the product of the multiplication.
     */
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

    /**
     * Returns the product of this matrix and the given vector.
     * If this matrix represents a transformation and the vector represents a 2D translation, the
     * product represents the transformation of the vector by the transformation represented by this
     * matrix.
     *
     * @param other the vector factor.
     * @return A new vector that is the product of the multiplication.
     */
    public Vec2 mul(Vec2 other) {
        Vec3 extended = new Vec3(other.getX(), other.getY(), 1.0);
        return new Vec2(
            row(0).dot(extended),
            row(1).dot(extended)
        );
    }

    /**
     * Returns the product of this matrix and the given vector.
     * If this matrix represents a transformation and the vector \(\langle x,y,z\rangle\) represents
     * a 2D translation of \(\langle x,y\rangle\) and a translation factor of z, the product
     * represents the transformation of the vector by the transformation represented by this matrix.
     *
     * @param other the vector factor.
     * @return A new vector that is the product of the multiplication.
     */
    public Vec3 mul(Vec3 other) {
        return new Vec3(
            row(0).dot(other),
            row(1).dot(other),
            row(2).dot(other)
        );
    }

    /**
     * Returns the scalar multiple of this matrix with a number.
     *
     * @param other the scalar factor.
     * @return A new matrix that is the product of the multiplication.
     */
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

    /**
     * Returns the determinant of this matrix.
     *
     * @return The determinant of this matrix.
     */
    public double det() {
        return mat[0] * mat[4] * mat[8]
            + mat[1] * mat[5] * mat[6]
            + mat[2] * mat[3] * mat[7]
            - mat[2] * mat[4] * mat[6]
            - mat[0] * mat[5] * mat[7]
            - mat[1] * mat[3] * mat[8];
    }

    /**
     * Returns the inverse of this matrix.
     * The inverse of a matrix M is M^-1 such that M * M^-1 = M^-1 * M = I, where I is the identity
     * matrix (IM = MI = M for all M). Multiplying by these is useful when converting between
     * spaces.
     *
     * @return A new matrix that is the inverse of this one. If this matrix is non-invertable, the
     * elements of the resulting matrix will be NaN.
     */
    public Mat3 inv() {
        return cofactor().transpose().mul(1 / det());
    }

    /**
     * Returns whether all matrix elements {@link Double#isFinite are finite}.
     *
     * @return whether all matrix elements are a valid, real, nonexceptional number.
     */
    public boolean isFinite() {
        for (int i = 0; i < 9; i++) {
            if (!Double.isFinite(mat[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns a column of the matrix as a vector.
     *
     * @param num the index of column to return.
     * @return The requested column as a new vector.
     * @throws IllegalArgumentException if the index is out of range.
     */
    public Vec3 col(int num) {
        checkDim(num, true);
        return new Vec3(mat[num], mat[num + 3], mat[num + 6]);
    }

    /**
     * Returns a row of the matrix as a vector.
     *
     * @param num the index of row to return.
     * @return The requested row as a new vector.
     * @throws IllegalArgumentException if the index is out of range.
     */
    public Vec3 row(int num) {
        checkDim(num, false);
        return new Vec3(mat[num * 3], mat[num * 3 + 1], mat[num * 3 + 2]);
    }

    /**
     * Returns the transpose of this matrix.
     * This is useful when calculating the inverse of a 3x3 matrix.
     *
     * @return The transpose of the matrix.
     */
    public Mat3 transpose() {
        return new Mat3(
            mat[0], mat[3], mat[6],
            mat[1], mat[4], mat[7],
            mat[2], mat[5], mat[8]
        );
    }

    /**
     * Returns the minor of this matrix at the given column and row.
     * The minor of a matrix at x,y is the matrix with column x and row y removed. This happens to
     * be useful when calculating the matrix cofactor, which is used to calculate the inverse.
     *
     * @param col the column of the minor.
     * @param row the row of the minor.
     * @return The matrix minor at col,row.
     * @throws IllegalArgumentException If either index is out of bounds.
     */
    public Mat2 minor(int col, int row) {
        checkDim(row, false);
        checkDim(col, true);
        int col0 = col == 0 ? 1 : 0;
        int col1 = col0 + (col == 1 ? 1 : 0);
        int row0 = row == 0 ? 1 : 0;
        int row1 = row0 + (row == 1 ? 1 : 0);
        return new Mat2(
            elem(col0, row0), elem(col1, row0),
            elem(col0, row1), elem(col1, row1)
        );
    }

    /**
     * Returns the matrix cofactor of this matrix.
     * This happens to be an important part of calculating the inverse of a 3x3 matrix.
     *
     * @return The matrix cofactor of this matrix.
     */
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

    /**
     * Gets an element of the matrix.
     *
     * @param col the column index of the element to get.
     * @param row the row index of the element to get.
     * @return The requested element.
     * @throws IllegalArgumentException if either index is out of range.
     */
    public double elem(int col, int row) {
        checkDim(col, true);
        checkDim(row, false);
        return mat[row * 3 + col];
    }

    /**
     * Gets the translation this matrix encodes if it represents a transformation.
     *
     * @return The translation represented by this transformation matrix.
     */
    public Vec2 getTranslation() {
        return new Vec2(mat[2], mat[5]);
    }

    /**
     * Gets a unit vector pointing in the direction of this transformation matrix.
     *
     * @return A unit vector pointing from the origin in the direction of this matrix, if it
     * represents a transformation.
     */
    public Vec2 getDirection() {
        return minor(2, 2).mul(new Vec2(1, 0));
    }

    /**
     * Bounds-checks a row or column index, throwing an exception if out of range.
     *
     * @param num the index to check.
     * @param col whether the index indicates a column, used to generate an error message.
     * @throws IllegalArgumentException If the index is out of bounds.
     */
    private void checkDim(int num, boolean col) {
        if (num < 0 || num > 2) {
            throw new IllegalArgumentException("Bad " + (col ? "column" : "row") + " number "
                + num);
        }
    }
}

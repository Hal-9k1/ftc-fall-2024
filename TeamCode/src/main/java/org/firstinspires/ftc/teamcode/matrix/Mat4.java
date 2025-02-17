package org.firstinspires.ftc.teamcode.matrix;

/**
 * Represents an immutable 3x3 matrix of double-precision floating point numbers.
 * Commonly used to express 3D rotations or 2D transformations (combined translation and rotation).
 */
public final class Mat4 {
    // CSOFF:MagicNumber
    /**
     * The underlying array storing matrix elements.
     */
    private final double[] mat;

    /**
     * Constructs a Mat4.
     * The top-left element is has position (0, 0). x positions increase rightward and y positions
     * downward.
     *
     * @param m00 the element at position (0, 0).
     * @param m10 the element at position (1, 0).
     * @param m20 the element at position (2, 0).
     * @param m30 the element at position (3, 0).
     * @param m01 the element at position (0, 1).
     * @param m11 the element at position (1, 1).
     * @param m21 the element at position (2, 1).
     * @param m31 the element at position (3, 1).
     * @param m02 the element at position (0, 2).
     * @param m12 the element at position (1, 2).
     * @param m22 the element at position (2, 2).
     * @param m32 the element at position (3, 2).
     * @param m03 the element at position (0, 3).
     * @param m13 the element at position (1, 3).
     * @param m23 the element at position (2, 3).
     * @param m33 the element at position (3, 3).
     */
    public Mat4(
        double m00, double m10, double m20, double m30,
        double m01, double m11, double m21, double m31,
        double m02, double m12, double m22, double m32,
        double m03, double m13, double m23, double m33
    ) {
        mat = new double[] {m00, m10, m20, m01, m11, m21, m02, m12, m22, m03, m13, m23, m33};
    }

    /**
     * Creates a Mat4 representing a transformation from a 3D rotation matrix and 3D translation.
     *
     * @param rot a 3D rotation matrix like those returned by {@link Mat3#fromYawPitchRoll}.
     * @param pos a 3D translation or position.
     * @return A new transformation matrix simultaneously encoding the given rotation and
     * translation.
     */
    public static Mat4 fromTransform(Mat3 rot, Vec3 pos) {
        return new Mat4(
            rot.elem(0, 0), rot.elem(1, 0), rot.elem(2, 0), pos.getX(),
            rot.elem(0, 1), rot.elem(1, 1), rot.elem(2, 1), pos.getY(),
            rot.elem(0, 2), rot.elem(1, 2), rot.elem(2, 2), pos.getZ(),
            0.0, 0.0, 0.0, 1.0
        );
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
    public Mat4 mul(Mat4 other) {
        return new Mat4(
            row(0).dot(other.col(0)),
            row(0).dot(other.col(1)),
            row(0).dot(other.col(2)),
            row(0).dot(other.col(3)),
            row(1).dot(other.col(0)),
            row(1).dot(other.col(1)),
            row(1).dot(other.col(2)),
            row(1).dot(other.col(3)),
            row(2).dot(other.col(0)),
            row(2).dot(other.col(1)),
            row(2).dot(other.col(2)),
            row(2).dot(other.col(3)),
            row(3).dot(other.col(0)),
            row(3).dot(other.col(1)),
            row(3).dot(other.col(2)),
            row(3).dot(other.col(3))
        );
    }

    /**
     * Returns the product of this matrix and the given vector.
     * If this matrix represents a transformation and the vector represents a 3D translation, the
     * product represents the transformation of the vector by the transformation represented by this
     * matrix.
     *
     * @param other the vector factor.
     * @return A new vector that is the product of the multiplication.
     */
    public Vec3 mul(Vec3 other) {
        Vec4 extended = new Vec4(other.getX(), other.getY(), other.getZ(), 1.0);
        return new Vec3(
            row(0).dot(extended),
            row(1).dot(extended),
            row(2).dot(extended)
        );
    }

    /**
     * Returns the product of this matrix and the given vector.
     * If this matrix represents a transformation and the vector \(\langle x,y,z\rangle\) represents
     * a 3D translation of \(\langle x,y\rangle\) and a translation factor of z, the product
     * represents the transformation of the vector by the transformation represented by this matrix.
     *
     * @param other the vector factor.
     * @return A new vector that is the product of the multiplication.
     */
    public Vec4 mul(Vec4 other) {
        return new Vec4(
            row(0).dot(other),
            row(1).dot(other),
            row(2).dot(other),
            row(3).dot(other)
        );
    }

    /**
     * Returns the scalar multiple of this matrix with a number.
     *
     * @param other the scalar factor.
     * @return A new matrix that is the product of the multiplication.
     */
    public Mat4 mul(double other) {
        return new Mat4(
            mat[0] * other,
            mat[1] * other,
            mat[2] * other,
            mat[3] * other,
            mat[4] * other,
            mat[5] * other,
            mat[6] * other,
            mat[7] * other,
            mat[8] * other,
            mat[9] * other,
            mat[10] * other,
            mat[11] * other,
            mat[12] * other,
            mat[13] * other,
            mat[14] * other,
            mat[15] * other
        );
    }

    /**
     * Returns the determinant of this matrix.
     *
     * @return The determinant of this matrix.
     */
    public double det() {
        return mat[0] * minor(0, 0).det()
            - mat[1] * minor(1, 0).det()
            + mat[2] * minor(2, 0).det()
            - mat[3] * minor(3, 0).det();
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
    public Mat4 inv() {
        return cofactor().transpose().mul(1 / det());
    }

    /**
     * Returns a column of the matrix as a vector.
     *
     * @param num the index of column to return.
     * @return The requested column as a new vector.
     * @throws IllegalArgumentException if the index is out of range.
     */
    public Vec4 col(int num) {
        checkDim(num, true);
        return new Vec4(mat[num], mat[num + 4], mat[num + 8], mat[num + 12]);
    }

    /**
     * Returns a row of the matrix as a vector.
     *
     * @param num the index of row to return.
     * @return The requested row as a new vector.
     * @throws IllegalArgumentException if the index is out of range.
     */
    public Vec4 row(int num) {
        checkDim(num, false);
        return new Vec4(mat[num * 4], mat[num * 4 + 1], mat[num * 4 + 2], mat[num * 4 + 3]);
    }

    /**
     * Returns the transpose of this matrix.
     * This is useful when calculating the inverse of a 3x3 matrix.
     *
     * @return The transpose of the matrix.
     */
    public Mat4 transpose() {
        return new Mat4(
            mat[0], mat[4], mat[8], mat[12],
            mat[1], mat[5], mat[9], mat[13],
            mat[2], mat[6], mat[10], mat[14],
            mat[3], mat[7], mat[11], mat[15]
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
    public Mat3 minor(int col, int row) {
        checkDim(row, false);
        checkDim(col, true);
        int col0 = col == 0 ? 1 : 0;
        int col1 = col0 + (col == 1 ? 1 : 0);
        int col2 = col1 + (col == 2 ? 1 : 0);
        int row0 = row == 0 ? 1 : 0;
        int row1 = row0 + (row == 1 ? 1 : 0);
        int row2 = row1 + (row == 2 ? 1 : 0);
        return new Mat3(
            elem(col0, row0), elem(col1, row0), elem(col2, row0),
            elem(col0, row1), elem(col1, row1), elem(col2, row1),
            elem(col0, row2), elem(col1, row2), elem(col2, row2)
        );
    }

    /**
     * Returns the matrix cofactor of this matrix.
     * This happens to be an important part of calculating the inverse of a 3x3 matrix.
     *
     * @return The matrix cofactor of this matrix.
     */
    public Mat4 cofactor() {
        return new Mat4(
            minor(0, 0).det(),
            -minor(1, 0).det(),
            minor(2, 0).det(),
            -minor(3, 0).det(),
            minor(0, 1).det(),
            -minor(1, 1).det(),
            minor(2, 1).det(),
            -minor(3, 1).det(),
            minor(0, 2).det(),
            -minor(1, 2).det(),
            minor(2, 2).det(),
            -minor(3, 2).det(),
            minor(0, 3).det(),
            -minor(1, 3).det(),
            minor(2, 3).det(),
            -minor(3, 3).det()
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
        return mat[row * 4 + col];
    }

    /**
     * Gets the translation this matrix encodes if it represents a transformation.
     *
     * @return The translation represented by this transformation matrix.
     */
    public Vec3 getTranslation() {
        return new Vec3(mat[3], mat[7], mat[11]);
    }

    /**
     * Gets a unit vector pointing in the direction of this transformation matrix.
     *
     * @return A unit vector pointing from the origin in the direction of this matrix, if it
     * represents a transformation.
     */
    public Vec3 getDirection() {
        return minor(3, 3).mul(new Vec3(1, 0, 0));
    }

    /**
     * Bounds-checks a row or column index, throwing an exception if out of range.
     *
     * @param num the index to check.
     * @param col whether the index indicates a column, used to generate an error message.
     * @throws IllegalArgumentException If the index is out of bounds.
     */
    private void checkDim(int num, boolean col) {
        if (num < 0 || num > 3) {
            throw new IllegalArgumentException("Bad " + (col ? "column" : "row") + " number "
                + num);
        }
    }
}

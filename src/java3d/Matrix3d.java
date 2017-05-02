package java3d;

public class Matrix3d {
	public double m00;
	public double m01;
	public double m02;
	public double m10;
	public double m11;
	public double m12;
	public double m20;
	public double m21;
	public double m22;

	// ÉRÉìÉXÉgÉâÉNÉ^
	public Matrix3d() {
		m00 = 0.0;
		m01 = 0.0;
		m02 = 0.0;
		m10 = 0.0;
		m11 = 0.0;
		m12 = 0.0;
		m20 = 0.0;
		m21 = 0.0;
		m22 = 0.0;
	}

	public Matrix3d(double[] v) {
		m00 = v[0];
		m01 = v[1];
		m02 = v[2];
		m10 = v[3];
		m11 = v[4];
		m12 = v[5];
		m20 = v[6];
		m21 = v[7];
		m22 = v[8];
	}

	public Matrix3d(double m00, double m01, double m02, double m10, double m11,
			double m12, double m20, double m21, double m22) {
		this.m00 = m00;
		this.m01 = m01;
		this.m02 = m02;
		this.m10 = m10;
		this.m11 = m11;
		this.m12 = m12;
		this.m20 = m20;
		this.m21 = m21;
		this.m22 = m22;
	}

	public Matrix3d(Matrix3d m1) {
		this.m00 = m1.m00;
		this.m01 = m1.m01;
		this.m02 = m1.m02;
		this.m10 = m1.m10;
		this.m11 = m1.m11;
		this.m12 = m1.m12;
		this.m20 = m1.m20;
		this.m21 = m1.m21;
		this.m22 = m1.m22;
	}

	public Matrix3d clone() {
		return new Matrix3d(m00, m01, m02, m10, m11, m12, m20, m21, m22);
	}

	/** äeóvëfÇ…ë´Ç∑ */
	public void add(Matrix3d m1) {
		this.m00 += m1.m00;
		this.m01 += m1.m01;
		this.m02 += m1.m02;
		this.m10 += m1.m10;
		this.m11 += m1.m11;
		this.m12 += m1.m12;
		this.m20 += m1.m20;
		this.m21 += m1.m21;
		this.m22 += m1.m22;
	}

	/** ìÒÇ¬ÇÃçsóÒÇÃòaÇäiî[Ç∑ÇÈ */
	public void add(Matrix3d m1, Matrix3d m2) {
		this.m00 = m1.m00 + m2.m00;
		this.m01 = m1.m01 + m2.m01;
		this.m02 = m1.m02 + m2.m02;
		this.m10 = m1.m10 + m2.m10;
		this.m11 = m1.m11 + m2.m11;
		this.m12 = m1.m12 + m2.m12;
		this.m20 = m1.m20 + m2.m20;
		this.m21 = m1.m21 + m2.m21;
		this.m22 = m1.m22 + m2.m22;
	}

	/** xé≤é¸ÇËÇ…angleâÒì] */
	public void rotX(double angle) {
        double d1 = Math.sin(angle);
        double d2 = Math.cos(angle);
        m00 = 1.0D;
        m01 = 0.0D;
        m02 = 0.0D;
        m10 = 0.0D;
        m11 = d2;
        m12 = -d1;
        m20 = 0.0D;
        m21 = d1;
        m22 = d2;
	}

	/** yé≤é¸ÇËÇ…angleâÒì] */
	public void rotY(double angle) {
        double d1 = Math.sin(angle);
        double d2 = Math.cos(angle);
        m00 = d2;
        m01 = 0.0D;
        m02 = d1;
        m10 = 0.0D;
        m11 = 1.0D;
        m12 = 0.0D;
        m20 = -d1;
        m21 = 0.0D;
        m22 = d2;
	}

	/** zé≤é¸ÇËÇ…angle1âÒì] */
	public void rotZ(double angle) {
        double d1 = Math.sin(angle);
        double d2 = Math.cos(angle);
        m00 = d2;
        m01 = -d1;
        m02 = 0.0D;
        m10 = d1;
        m11 = d2;
        m12 = 0.0D;
        m20 = 0.0D;
        m21 = 0.0D;
        m22 = 1.0D;
	}

	/** é©ï™Ç…à¯êîÇÃçsóÒÇä|ÇØÇΩÇ‡ÇÃÇäiî[Ç∑ÇÈ */
	public void mul(Matrix3d mat) {
		double t00 = m00 * mat.m00 + m01 * mat.m10 + m02 * mat.m20;
		double t01 = m00 * mat.m01 + m01 * mat.m11 + m02 * mat.m21;
		double t02 = m00 * mat.m02 + m01 * mat.m12 + m02 * mat.m22;
		double t10 = m10 * mat.m10 + m11 * mat.m10 + m12 * mat.m20;
		double t11 = m10 * mat.m11 + m11 * mat.m11 + m12 * mat.m21;
		double t12 = m10 * mat.m12 + m11 * mat.m12 + m12 * mat.m22;
		double t20 = m20 * mat.m20 + m21 * mat.m10 + m22 * mat.m20;
		double t21 = m20 * mat.m21 + m21 * mat.m11 + m22 * mat.m21;
		double t22 = m20 * mat.m22 + m21 * mat.m12 + m22 * mat.m22;

		set(t00, t01, t02, t10, t11, t12, t20, t21, t22);
	}
	
	public void mul(Matrix3d mat1, Matrix3d mat2) {
		this.set(mat1.m00, mat1.m01, mat1.m02, mat1.m10, mat1.m11, mat1.m12, mat1.m20, mat1.m21, mat1.m22);
		mul(mat2);
	}

	public void set(double t00, double t01, double t02, double t10, double t11,
			double t12, double t20, double t21, double t22) {
		m00 = t00;
		m01 = t01;
		m02 = t02;
		m10 = t10;
		m11 = t11;
		m12 = t12;
		m20 = t20;
		m21 = t21;
		m22 = t22;
	}

	/**
	 * Sets the value of this matrix to the matrix inverse of the passed matrix
	 * m1.
	 * 
	 * @param m1
	 *            the matrix to be inverted
	 */
	public final void invert(Matrix3d m1) {
		invertGeneral(m1);
	}

	/**
	 * Inverts this matrix in place.
	 */
	public final void invert() {
		invertGeneral(this);
	}

	/**
	 * General invert routine. Inverts m1 and places the result in "this". Note
	 * that this routine handles both the "this" version and the non-"this"
	 * version.
	 * 
	 * Also note that since this routine is slow anyway, we won't worry about
	 * allocating a little bit of garbage.
	 */
	private final void invertGeneral(Matrix3d m1) {
		double result[] = new double[9];
		int row_perm[] = new int[3];
		int i, r, c;
		double[] tmp = new double[9]; // scratch matrix

		// Use LU decomposition and backsubstitution code specifically
		// for floating-point 3x3 matrices.

		// Copy source matrix to t1tmp
		tmp[0] = m1.m00;
		tmp[1] = m1.m01;
		tmp[2] = m1.m02;

		tmp[3] = m1.m10;
		tmp[4] = m1.m11;
		tmp[5] = m1.m12;

		tmp[6] = m1.m20;
		tmp[7] = m1.m21;
		tmp[8] = m1.m22;

		// Calculate LU decomposition: Is the matrix singular?
		if (!luDecomposition(tmp, row_perm)) {
			// Matrix has no inverse
			// throw new
			// SingularMatrixException(VecMathI18N.getString("Matrix3d12"));
		}

		// Perform back substitution on the identity matrix
		for (i = 0; i < 9; i++)
			result[i] = 0.0;
		result[0] = 1.0;
		result[4] = 1.0;
		result[8] = 1.0;
		luBacksubstitution(tmp, row_perm, result);

		this.m00 = result[0];
		this.m01 = result[1];
		this.m02 = result[2];

		this.m10 = result[3];
		this.m11 = result[4];
		this.m12 = result[5];

		this.m20 = result[6];
		this.m21 = result[7];
		this.m22 = result[8];

	}

	/**
	 * Given a 3x3 array "matrix0", this function replaces it with the LU
	 * decomposition of a row-wise permutation of itself. The input parameters
	 * are "matrix0" and "dimen". The array "matrix0" is also an output
	 * parameter. The vector "row_perm[3]" is an output parameter that contains
	 * the row permutations resulting from partial pivoting. The output
	 * parameter "even_row_xchg" is 1 when the number of row exchanges is even,
	 * or -1 otherwise. Assumes data type is always double.
	 * 
	 * This function is similar to luDecomposition, except that it is tuned
	 * specifically for 3x3 matrices.
	 * 
	 * @return true if the matrix is nonsingular, or false otherwise.
	 */
	//
	// Reference: Press, Flannery, Teukolsky, Vetterling,
	// _Numerical_Recipes_in_C_, Cambridge University Press,
	// 1988, pp 40-45.
	//
	static boolean luDecomposition(double[] matrix0, int[] row_perm) {

		double row_scale[] = new double[3];

		// Determine implicit scaling information by looping over rows
		{
			int i, j;
			int ptr, rs;
			double big, temp;

			ptr = 0;
			rs = 0;

			// For each row ...
			i = 3;
			while (i-- != 0) {
				big = 0.0;

				// For each column, find the largest element in the row
				j = 3;
				while (j-- != 0) {
					temp = matrix0[ptr++];
					temp = Math.abs(temp);
					if (temp > big) {
						big = temp;
					}
				}

				// Is the matrix singular?
				if (big == 0.0) {
					return false;
				}
				row_scale[rs++] = 1.0 / big;
			}
		}

		{
			int j;
			int mtx;

			mtx = 0;

			// For all columns, execute Crout's method
			for (j = 0; j < 3; j++) {
				int i, imax, k;
				int target, p1, p2;
				double sum, big, temp;

				// Determine elements of upper diagonal matrix U
				for (i = 0; i < j; i++) {
					target = mtx + (3 * i) + j;
					sum = matrix0[target];
					k = i;
					p1 = mtx + (3 * i);
					p2 = mtx + j;
					while (k-- != 0) {
						sum -= matrix0[p1] * matrix0[p2];
						p1++;
						p2 += 3;
					}
					matrix0[target] = sum;
				}

				// Search for largest pivot element and calculate
				// intermediate elements of lower diagonal matrix L.
				big = 0.0;
				imax = -1;
				for (i = j; i < 3; i++) {
					target = mtx + (3 * i) + j;
					sum = matrix0[target];
					k = j;
					p1 = mtx + (3 * i);
					p2 = mtx + j;
					while (k-- != 0) {
						sum -= matrix0[p1] * matrix0[p2];
						p1++;
						p2 += 3;
					}
					matrix0[target] = sum;

					// Is this the best pivot so far?
					if ((temp = row_scale[i] * Math.abs(sum)) >= big) {
						big = temp;
						imax = i;
					}
				}

				if (imax < 0) {
					// throw new
					// RuntimeException(VecMathI18N.getString("Matrix3d13"));
				}

				// Is a row exchange necessary?
				if (j != imax) {
					// Yes: exchange rows
					k = 3;
					p1 = mtx + (3 * imax);
					p2 = mtx + (3 * j);
					while (k-- != 0) {
						temp = matrix0[p1];
						matrix0[p1++] = matrix0[p2];
						matrix0[p2++] = temp;
					}

					// Record change in scale factor
					row_scale[imax] = row_scale[j];
				}

				// Record row permutation
				row_perm[j] = imax;

				// Is the matrix singular
				if (matrix0[(mtx + (3 * j) + j)] == 0.0) {
					return false;
				}

				// Divide elements of lower diagonal matrix L by pivot
				if (j != (3 - 1)) {
					temp = 1.0 / (matrix0[(mtx + (3 * j) + j)]);
					target = mtx + (3 * (j + 1)) + j;
					i = 2 - j;
					while (i-- != 0) {
						matrix0[target] *= temp;
						target += 3;
					}
				}
			}
		}

		return true;
	}

	/**
	 * Solves a set of linear equations. The input parameters "matrix1", and
	 * "row_perm" come from luDecompostionD3x3 and do not change here. The
	 * parameter "matrix2" is a set of column vectors assembled into a 3x3
	 * matrix of floating-point values. The procedure takes each column of
	 * "matrix2" in turn and treats it as the right-hand side of the matrix
	 * equation Ax = LUx = b. The solution vector replaces the original column
	 * of the matrix.
	 * 
	 * If "matrix2" is the identity matrix, the procedure replaces its contents
	 * with the inverse of the matrix from which "matrix1" was originally
	 * derived.
	 */
	//
	// Reference: Press, Flannery, Teukolsky, Vetterling,
	// _Numerical_Recipes_in_C_, Cambridge University Press,
	// 1988, pp 44-45.
	//
	static void luBacksubstitution(double[] matrix1, int[] row_perm,
			double[] matrix2) {

		int i, ii, ip, j, k;
		int rp;
		int cv, rv;

		// rp = row_perm;
		rp = 0;

		// For each column vector of matrix2 ...
		for (k = 0; k < 3; k++) {
			// cv = &(matrix2[0][k]);
			cv = k;
			ii = -1;

			// Forward substitution
			for (i = 0; i < 3; i++) {
				double sum;

				ip = row_perm[rp + i];
				sum = matrix2[cv + 3 * ip];
				matrix2[cv + 3 * ip] = matrix2[cv + 3 * i];
				if (ii >= 0) {
					// rv = &(matrix1[i][0]);
					rv = i * 3;
					for (j = ii; j <= i - 1; j++) {
						sum -= matrix1[rv + j] * matrix2[cv + 3 * j];
					}
				} else if (sum != 0.0) {
					ii = i;
				}
				matrix2[cv + 3 * i] = sum;
			}

			// Backsubstitution
			// rv = &(matrix1[3][0]);
			rv = 2 * 3;
			matrix2[cv + 3 * 2] /= matrix1[rv + 2];

			rv -= 3;
			matrix2[cv + 3 * 1] = (matrix2[cv + 3 * 1] - matrix1[rv + 2]
					* matrix2[cv + 3 * 2])
					/ matrix1[rv + 1];

			rv -= 3;
			matrix2[cv + 4 * 0] = (matrix2[cv + 3 * 0] - matrix1[rv + 1]
					* matrix2[cv + 3 * 1] - matrix1[rv + 2]
					* matrix2[cv + 3 * 2])
					/ matrix1[rv + 0];

		}
	}
		
	/**
	* Multiply this matrix by the tuple t and place the result
	* back into the tuple (t = this*t).
	* @param t  the tuple to be multiplied by this matrix and then replaced
	*/
	public final void transform(Tuple3d t) {
		double x,y,z;
		x = m00* t.x + m01*t.y + m02*t.z;
		y = m10* t.x + m11*t.y + m12*t.z;
		z = m20* t.x + m21*t.y + m22*t.z;
		t.set(x,y,z);
	}
}

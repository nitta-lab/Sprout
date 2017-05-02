package java3d;

public class Matrix4d {
	public double m00;
	public double m01;
	public double m02;
	public double m03;
	public double m10;
	public double m11;
	public double m12;
	public double m13;
	public double m20;
	public double m21;
	public double m22;
	public double m23;
	public double m30;
	public double m31;
	public double m32;
	public double m33;
	
	// ÉRÉìÉXÉgÉâÉNÉ^
	public Matrix4d() {
		m00 = 1.0;
		m01 = 0.0;
		m02 = 0.0;
		m03 = 0.0;
		m10 = 0.0;
		m11 = 1.0;
		m12 = 0.0;
		m13 = 0.0;
		m20 = 0.0;
		m21 = 0.0;
		m22 = 1.0;
		m23 = 0.0;
		m30 = 0.0;
		m31 = 0.0;
		m32 = 0.0;
		m33 = 1.0;
	}
	
	public Matrix4d(double[] v) {
		m00 = v[0];
		m01 = v[1];
		m02 = v[2];
		m03 = v[3];
		m10 = v[4];
		m11 = v[5];
		m12 = v[6];
		m13 = v[7];
		m20 = v[8];
		m21 = v[9];
		m22 = v[10];
		m23 = v[11];
		m30 = v[12];
		m31 = v[13];
		m32 = v[14];
		m33 = v[15];
	}
	
	public Matrix4d(double m00, double m01, double m02, double m03, double m10,
			double m11, double m12, double m13, double m20, double m21,
			double m22, double m23, double m30, double m31, double m32,
			double m33) {
		this.m00 = m00;
		this.m01 = m01;
		this.m02 = m02;
		this.m03 = m03;
		this.m10 = m10;
		this.m11 = m11;
		this.m12 = m12;
		this.m13 = m13;
		this.m20 = m20;
		this.m21 = m21;
		this.m22 = m22;
		this.m23 = m23;
		this.m30 = m30;
		this.m31 = m31;
		this.m32 = m32;
		this.m33 = m33;
	}
	
	public Matrix4d clone() {
		return new Matrix4d(m00, m01, m02, m03, m10, m11, m12, m13, m20, m21, m22, m23, m30, m31, m32, m33);
	}
	
	public Matrix4d(Matrix4d m1) {
		this.m00 = m1.m00;
		this.m01 = m1.m01;
		this.m02 = m1.m02;
		this.m03 = m1.m03;
		this.m10 = m1.m10;
		this.m11 = m1.m11;
		this.m12 = m1.m12;
		this.m13 = m1.m13;
		this.m20 = m1.m20;
		this.m21 = m1.m21;
		this.m22 = m1.m22;
		this.m23 = m1.m23;
		this.m30 = m1.m30;
		this.m31 = m1.m31;
		this.m32 = m1.m32;
		this.m33 = m1.m33;
	}

	/** äeóvëfÇ…ë´Ç∑ */
	public void add(Matrix4d m1) {
		this.m00 += m1.m00;
		this.m01 += m1.m01;
		this.m02 += m1.m02;
		this.m03 += m1.m03;
		this.m10 += m1.m10;
		this.m11 += m1.m11;
		this.m12 += m1.m12;
		this.m13 += m1.m13;
		this.m20 += m1.m20;
		this.m21 += m1.m21;
		this.m22 += m1.m22;
		this.m23 += m1.m23;
		this.m30 += m1.m30;
		this.m31 += m1.m31;
		this.m32 += m1.m32;
		this.m33 += m1.m33;
	}
	
	/** ìÒÇ¬ÇÃçsóÒÇÃòaÇäiî[Ç∑ÇÈ */
	public void add(Matrix4d m1, Matrix4d m2) {
		this.m00 = m1.m00 + m2.m00;
		this.m01 = m1.m01 + m2.m01;
		this.m02 = m1.m02 + m2.m02;
		this.m03 = m1.m03 + m2.m03;
		this.m10 = m1.m10 + m2.m10;
		this.m11 = m1.m11 + m2.m11;
		this.m12 = m1.m12 + m2.m12;
		this.m13 = m1.m13 + m2.m13;
		this.m20 = m1.m20 + m2.m20;
		this.m21 = m1.m21 + m2.m21;
		this.m22 = m1.m22 + m2.m22;
		this.m23 = m1.m23 + m2.m23;
		this.m30 = m1.m30 + m2.m30;
		this.m31 = m1.m31 + m2.m31;
		this.m32 = m1.m32 + m2.m32;
		this.m33 = m1.m33 + m2.m33;
	}
	
	/** é©ï™Ç…à¯êîÇÃçsóÒÇâEÇ©ÇÁä|ÇØÇΩÇ‡ÇÃÇäiî[Ç∑ÇÈ */
	public void mul(Matrix4d right) {
		double _m00 = m00 * right.m00 + m01 * right.m10 + m02 * right.m20 + m03 * right.m30;
		double _m01 = m00 * right.m01 + m01 * right.m11 + m02 * right.m21 + m03 * right.m31;
		double _m02 = m00 * right.m02 + m01 * right.m12 + m02 * right.m22 + m03 * right.m32;
		double _m03 = m00 * right.m03 + m01 * right.m13 + m02 * right.m23 + m03 * right.m33;
		
		double _m10 = m10 * right.m00 + m11 * right.m10 + m12 * right.m20 + m13 * right.m30;
		double _m11 = m10 * right.m01 + m11 * right.m11 + m12 * right.m21 + m13 * right.m31;
		double _m12 = m10 * right.m02 + m11 * right.m12 + m12 * right.m22 + m13 * right.m32;
		double _m13 = m10 * right.m03 + m11 * right.m13 + m12 * right.m23 + m13 * right.m33;
		
		double _m20 = m20 * right.m00 + m21 * right.m10 + m22 * right.m20 + m23 * right.m30;
		double _m21 = m20 * right.m01 + m21 * right.m11 + m22 * right.m21 + m23 * right.m31;
		double _m22 = m20 * right.m02 + m21 * right.m12 + m22 * right.m22 + m23 * right.m32;
		double _m23 = m20 * right.m03 + m21 * right.m13 + m22 * right.m23 + m23 * right.m33;
		
		double _m30 = m30 * right.m00 + m31 * right.m10 + m32 * right.m20 + m33 * right.m30;
		double _m31 = m30 * right.m01 + m31 * right.m11 + m32 * right.m21 + m33 * right.m31;
		double _m32 = m30 * right.m02 + m31 * right.m12 + m32 * right.m22 + m33 * right.m32;
		double _m33 = m30 * right.m03 + m31 * right.m13 + m32 * right.m23 + m33 * right.m33;
		
		m00 = _m00; m01 = _m01; m02 = _m02; m03 = _m03;
		m10 = _m10; m11 = _m11; m12 = _m12; m13 = _m13;
		m20 = _m20; m21 = _m21; m22 = _m22; m23 = _m23;
		m30 = _m30; m31 = _m31; m32 = _m32; m33 = _m33;
	}
	
	public void invert() {
		double det = determinant();
		if (det == 0) {
			return;
		}
		double b00 = determinant3x3(m11,m12,m13,m21,m22,m23,m31,m32,m33) / det;
		double b01 = -determinant3x3(m01,m02,m03,m21,m22,m23,m31,m32,m33) / det;
		double b02 = determinant3x3(m01,m02,m03,m11,m12,m13,m31,m32,m33) / det;
		double b03 = -determinant3x3(m01,m02,m03,m11,m12,m13,m21,m22,m23) / det;
		
		double b10 = -determinant3x3(m10,m12,m13,m20,m22,m23,m30,m32,m33) / det;
		double b11 = determinant3x3(m00,m02,m03,m20,m22,m23,m30,m32,m33) / det;
		double b12 = -determinant3x3(m00,m02,m03,m10,m12,m13,m30,m32,m33) / det;
		double b13 = determinant3x3(m00,m02,m03,m10,m12,m13,m20,m22,m23) / det;
		
		double b20 = determinant3x3(m10,m11,m13,m20,m21,m23,m30,m31,m33) / det;
		double b21 = -determinant3x3(m00,m01,m03,m20,m21,m23,m30,m31,m33) / det;
		double b22 = determinant3x3(m00,m01,m03,m10,m11,m13,m30,m31,m33) / det;
		double b23 = -determinant3x3(m00,m01,m03,m10,m11,m13,m20,m21,m23) / det;
		
		double b30 = -determinant3x3(m10,m11,m12,m20,m21,m22,m30,m31,m32) / det;
		double b31 = determinant3x3(m00,m01,m02,m20,m21,m22,m30,m31,m32) / det;
		double b32 = -determinant3x3(m00,m01,m02,m10,m11,m12,m30,m31,m32) / det;
		double b33 = determinant3x3(m00,m01,m02,m10,m11,m12,m20,m21,m22) / det;
		
		m00 = b00; m01 = b01; m02 = b02; m03 = b03;
		m10 = b10; m11 = b11; m12 = b12; m13 = b13;
		m20 = b20; m21 = b21; m22 = b22; m23 = b23;
		m30 = b30; m31 = b31; m32 = b32; m33 = b33;
	}
	
	private static double determinant3x3(double m11, double m12, double m13,double m21,double m22, double m23,double m31,double m32,double m33) {
		return m11*m22*m33+m12*m23*m31+m13*m21*m32-m11*m23*m32-m12*m21*m33-m13*m22*m31;
	}
	
	/** çsóÒéÆÇãÅÇﬂÇÈ */
	public double determinant() {
		return m00 * determinant3x3(m11,m12,m13,m21,m22,m23,m31,m32,m33)-
		m01 * determinant3x3(m10,m12,m13,m20,m22,m23,m30,m32,m33)+
		m02 * determinant3x3(m10,m11,m13,m20,m21,m23,m30,m31,m33)-
		m03 * determinant3x3(m10,m11,m12,m20,m21,m22,m30,m31,m32);
	}
	
}

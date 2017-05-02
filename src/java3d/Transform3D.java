package java3d;


public class Transform3D {
	double[][] mat;
	double[] rot;
	double[] scales = new double[] {1.0,1.0,1.0};
	
	 static final double EPSILON_ABSOLUTE = 1.0e-5;
	
	public Transform3D() {
		mat = new double[][]{{1.0,0.0,0.0,0.0},{0.0,1.0,0.0,0.0},{0.0,0.0,1.0,0.0},{0.0,0.0,0.0,1.0}};
	}
	
	public Transform3D(Matrix4d m1) {
		mat = new double[][]{{m1.m00,m1.m01,m1.m02,m1.m03},{m1.m10,m1.m11,m1.m12,m1.m13},{m1.m20,m1.m21,m1.m22,m1.m23},{m1.m30,m1.m31,m1.m32,m1.m33}};
	}
	
	public Transform3D(Transform3D transform) {
		mat = new double[4][4];
		set(transform);
	}
	
	public void set(Transform3D transform) {
		for (int i = 0;i < 4; i++) {
			for (int j = 0;j < 4; j++) {
				mat[i][j] = transform.mat[i][j];
			}
		}
		scales[0] = transform.scales[0];
		scales[1] = transform.scales[1];
		scales[2] = transform.scales[2];
	}
	
	/**
	 * this = t1 * t2
	 * 
	 * @param t1
	 * @param t2
	 */
	public void mul(Transform3D t1, Transform3D t2) {
		for (int i = 0; i < 4; i++) {
			double[] row = row(i, t1.mat);
			for (int j = 0; j < 4; j++) {
				mat[i][j] = 0.0;
				double[] col = col(j, t2.mat); 
				for (int k = 0; k < 4; k++) {
					mat[i][j] += row[k] * col[k]; 
				}
			}
		}
	}

	/**
	 * this = this * t1
	 * 
	 * @param t1
	 */
	public void mul(Transform3D t1) {
		this.mul(new Transform3D(this), t1);
	}

	/** 4*4s—ñ‚Ìs‚ð•Ô‚· */
	private double[] row(int row, double[][] mat) {
		return new double[] {mat[row][0], mat[row][1], mat[row][2], mat[row][3]};
	}
	
	/** 4*4s—ñ‚Ì—ñ‚ð•Ô‚· */
	private double[] col(int col, double[][] mat) {
		return new double[] {mat[0][col], mat[1][col], mat[2][col], mat[3][col]};
	}

	public void set(Vector3d v) {
		mat[0][3] = v.x;
		mat[1][3] = v.y;
		mat[2][3] = v.z;
	}

	/** 4*4‚Ìs—ñ‚ð16‚Ì1ŽŸŒ³”z—ñ‚Å•Ô‚· */
	public float[] getMatrix() {
		float[] result = new float[16];
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				result[j + 4 * i] = (float)(mat[j][i]);
			}	
		}
		return result;
	}

	/** ‘ÎŠps—ñ‚ðƒXƒJƒ‰”{‚·‚é */
	public void setScale(double s) {
		for (int i = 0; i < 3; i++) {
			mat[i][i] = s;
			scales[i] = s;
		}
	}
	
	/** ‘ÎŠps—ñ‚ðvector‚Ì¬•ª”{‚·‚é */
	public void setScale(Vector3d vector3d) {
		scales[0] = mat[0][0] = vector3d.x;
		scales[1] = mat[1][1] = vector3d.y;
		scales[2] = mat[2][2] = vector3d.z;
	}
	
	/** ”CˆÓ‚Ì’PˆÊƒxƒNƒgƒ‹‚Ü‚í‚è‚É‰ñ“]‚·‚é */
	public void setRotation(AxisAngle4d t) {
		double vx = t.x;
		double vy = t.y;
		double vz = t.z;
		double sin = Math.sin(t.angle);
		double cos = Math.cos(t.angle);

		mat[0][0] = (Math.pow(vx, 2.0)) * (1 - cos) + cos;
		mat[0][1] = ((vx * vy) * (1 - cos)) - (vz * sin);
		mat[0][2] = ((vz * vx) * (1 - cos)) + (vy * sin);
		mat[1][0] = ((vx * vy) * (1 - cos)) + (vz * sin);
		mat[1][1] = (Math.pow(vy, 2.0)) * (1 - cos) + cos;
		mat[1][2] = ((vy * vz) * (1 - cos)) - (vx * sin);
		mat[2][0] = ((vz * vx) * (1 - cos)) - (vy * sin);
		mat[2][1] = ((vy * vz) * (1 - cos)) + (vx * sin);
		mat[2][2] = (Math.pow(vz, 2.0)) * (1 - cos) + cos;
	}
	
	/** ”CˆÓ‚Ì’PˆÊƒxƒNƒgƒ‹‚Ü‚í‚è‚É‰ñ“]‚·‚é */
	public void setRotation(Quat4d q) {
//		double vx = q.x;
//		double vy = q.y;
//		double vz = q.z;
//		double sin = Math.sin(q.w);
//		double cos = Math.cos(q.w);
//		double cosm = 1 - cos;
//
//		mat[0][0] = (vx * vx) * cosm + cos;
//		mat[0][1] = ((vx * vy) * cosm) - (vz * sin);
//		mat[0][2] = ((vz * vx) * cosm) + (vy * sin);
//		mat[1][0] = ((vx * vy) * cosm) + (vz * sin);
//		mat[1][1] = (vy * vy) * cosm + cos;
//		mat[1][2] = ((vy * vz) * cosm) - (vx * sin);
//		mat[2][0] = ((vz * vx) * cosm) - (vy * sin);
//		mat[2][1] = ((vy * vz) * cosm) + (vx * sin);
//		mat[2][2] = (vz * vz) * cosm + cos;

		mat[0][0] = (1.0 - 2.0 * q.y * q.y - 2.0 * q.z * q.z) * scales[0];
		mat[1][0] = (2.0 * (q.x * q.y + q.w * q.z)) * scales[0];
		mat[2][0] = (2.0 * (q.x * q.z - q.w * q.y)) * scales[0];
		mat[0][1] = (2.0 * (q.x * q.y - q.w * q.z)) * scales[1];
		mat[1][1] = (1.0 - 2.0 * q.x * q.x - 2.0 * q.z * q.z) * scales[1];
		mat[2][1] = (2.0 * (q.y * q.z + q.w * q.x)) * scales[1];
		mat[0][2] = (2.0 * (q.x * q.z + q.w * q.y)) * scales[2];
		mat[1][2] = (2.0 * (q.y * q.z - q.w * q.x)) * scales[2];
		mat[2][2] = (1.0 - 2.0 * q.x * q.x - 2.0 * q.y * q.y) * scales[2];
	}
	
	public void rotX(double angle) {
		double sinAngle = Math.sin(angle);
		double cosAngle = Math.cos(angle);
		
		mat[0][0] = 1.0;
		mat[0][1] = 0.0;
		mat[0][2] = 0.0;
		mat[0][3] = 0.0;

		mat[1][0] = 0.0;
		mat[1][1] = cosAngle;
		mat[1][2] = -sinAngle;
		mat[1][3] = 0.0;
		
		mat[2][0] = 0.0;
		mat[2][1] = sinAngle;
		mat[2][2] = cosAngle;
		mat[2][3] = 0.0;

		mat[3][0] = 0.0;
		mat[3][1] = 0.0;
		mat[3][2] = 0.0;
		mat[3][3] = 1.0;
	}
	
	public void rotY(double angle) {
		double sinAngle = Math.sin(angle);
		double cosAngle = Math.cos(angle);
		
		mat[0][0] = cosAngle;
		mat[0][1] =  0.0;
		mat[0][2] = sinAngle;
		mat[0][3] = 0.0;

		mat[1][0] = 0.0;
		mat[1][1] = 1.0;
		mat[1][2] = 0.0;
		mat[1][3] = 0.0;
		
		mat[2][0] = -sinAngle;
		mat[2][1] = 0.0;
		mat[2][2] = cosAngle;
		mat[2][3] = 0.0;

		mat[3][0] = 0.0;
		mat[3][1] = 0.0;
		mat[3][2] = 0.0;
		mat[3][3] = 1.0;
	}

	public void rotZ(double angle) {
		double sinAngle = Math.sin(angle);
		double cosAngle = Math.cos(angle);
		
		mat[0][0] = cosAngle;
		mat[0][1] = -sinAngle;
		mat[0][2] = 0.0;
		mat[0][3] = 0.0;

		mat[1][0] = sinAngle;
		mat[1][1] = cosAngle;
		mat[1][2] = 0.0;
		mat[1][3] = 0.0;
		
		mat[2][0] = 0.0;
		mat[2][1] = 0.0;
		mat[2][2] = 1.0;
		mat[2][3] = 0.0;

		mat[3][0] = 0.0;
		mat[3][1] = 0.0;
		mat[3][2] = 0.0;
		mat[3][3] = 1.0;
	}

	/** ˆø”‚Ì¬•ª‚ðŠi”[‚·‚é */
	public void get(Matrix4d mat4d) {
		mat4d.m00 = mat[0][0];
		mat4d.m01 = mat[0][1];
		mat4d.m02 = mat[0][2];
		mat4d.m03 = mat[0][3];
		mat4d.m10 = mat[1][0];
		mat4d.m11 = mat[1][1];
		mat4d.m12 = mat[1][2];
		mat4d.m13 = mat[1][3];
		mat4d.m20 = mat[2][0];
		mat4d.m21 = mat[2][1];
		mat4d.m22 = mat[2][2];
		mat4d.m23 = mat[2][3];
		mat4d.m30 = mat[3][0];
		mat4d.m31 = mat[3][1];
		mat4d.m32 = mat[3][2];
		mat4d.m33 = mat[3][3];
	}
	

	public void set(double[] matrix) {
		mat[0][0] = matrix[0];
		mat[0][1] = matrix[1];
		mat[0][2] = matrix[2];
		mat[0][3] = matrix[3];
		mat[1][0] = matrix[4];
		mat[1][1] = matrix[5];
		mat[1][2] = matrix[6];
		mat[1][3] = matrix[7];
		mat[2][0] = matrix[8];
		mat[2][1] = matrix[9];
		mat[2][2] = matrix[10];
		mat[2][3] = matrix[11];
		mat[3][0] = matrix[12];
		mat[3][1] = matrix[13];
		mat[3][2] = matrix[14];
		mat[3][3] = matrix[15];
		scales[0] = 1.0;
		scales[1] = 1.0;
		scales[2] = 1.0;
	}

	public void set(AxisAngle4d a1) {
		double mag = Math.sqrt( a1.x*a1.x + a1.y*a1.y + a1.z*a1.z);
		if (almostZero(mag)) {
			setIdentity();
		} else {
			mag = 1.0/mag;
			double ax = a1.x*mag;
			double ay = a1.y*mag;
			double az = a1.z*mag;
			double sinTheta = Math.sin((double)a1.angle);
			double cosTheta = Math.cos((double)a1.angle);
			double t = 1.0 - cosTheta;
			double xz = ax * az;
			double xy = ax * ay;
			double yz = ay * az;
			mat[0][0] = t * ax * ax + cosTheta;
			mat[0][1] = t * xy - sinTheta * az;
			mat[0][2] = t * xz + sinTheta * ay;
			mat[0][3] = 0.0;
			mat[1][0] = t * xy + sinTheta * az;
			mat[1][1] = t * ay * ay + cosTheta;
			mat[1][2] = t * yz - sinTheta * ax;
			mat[1][3] = 0.0;
			mat[2][0] = t * xz - sinTheta * ay;
			mat[2][1] = t * yz + sinTheta * ax;
			mat[2][2] = t * az * az + cosTheta;
			mat[2][3] = 0.0;
			mat[3][0] = 0.0;
			mat[3][1] = 0.0;
			mat[3][2] = 0.0;
			mat[3][3] = 1.0;
		}
	}

	public void set(Quat4d q1) {
		mat[0][0] = (1.0f - 2.0f*q1.y*q1.y - 2.0f*q1.z*q1.z);
		mat[1][0] = (2.0f*(q1.x*q1.y + q1.w*q1.z));
		mat[2][0] = (2.0f*(q1.x*q1.z - q1.w*q1.y));
		 
		mat[0][1] = (2.0f*(q1.x*q1.y - q1.w*q1.z));
		mat[1][1] = (1.0f - 2.0f*q1.x*q1.x - 2.0f*q1.z*q1.z);
		mat[2][1] = (2.0f*(q1.y*q1.z + q1.w*q1.x));
		 
		mat[0][2] = (2.0f*(q1.x*q1.z + q1.w*q1.y));
		mat[1][2] = (2.0f*(q1.y*q1.z - q1.w*q1.x));
		mat[2][2] = (1.0f - 2.0f*q1.x*q1.x - 2.0f*q1.y*q1.y);
		 
		mat[0][3] =  0.0;
		mat[1][3] =  0.0;
		mat[2][3] = 0.0;
		 
		mat[3][0] = 0.0;
		mat[3][1] = 0.0;
		mat[3][2] = 0.0;
		mat[3][3] = 1.0;
		 
		// Issue 253: set all dirty bits if input is infinity or NaN
//		if (isInfOrNaN(q1)) {
//			dirtyBits = ALL_DIRTY;
//			return;
//		}
		 
//		dirtyBits = CLASSIFY_BIT | SCALE_BIT | ROTATION_BIT;
//		type = RIGID | CONGRUENT | AFFINE | ORTHO;
	}

	/** Vector3d‚ðTransform3d‚Å•ÏŠ·‚·‚é */
	public void transform(Vector3d v) {
		double vx =  mat[0][0]*v.x + mat[0][1]*v.y + mat[0][2]*v.z;// + mat[0][3] * 1.0;
		double vy = mat[1][0]*v.x + mat[1][1]*v.y + mat[1][2]*v.z;// + mat[1][3] * 1.0;
		v.z = mat[2][0]*v.x + mat[2][1]*v.y + mat[2][2]*v.z;// + mat[2][3] * 1.0;
		v.x = vx;
		v.y = vy;
	}
		
	private static final boolean almostZero(double a) {
		return ((a < EPSILON_ABSOLUTE) && (a > -EPSILON_ABSOLUTE));
	}
	
	public final void setIdentity() {
		mat[0][0] = 1.0; mat[0][1] = 0.0; mat[0][2] = 0.0; mat[0][3] = 0.0;
		mat[1][0] = 0.0; mat[1][1] = 1.0; mat[1][2] = 0.0; mat[1][3] = 0.0;
		mat[2][0] = 0.0; mat[2][1] = 0.0; mat[2][2] = 1.0; mat[2][3] = 0.0;
		mat[3][0] = 0.0; mat[3][1] = 0.0; mat[3][2] = 0.0; mat[3][3] = 1.0;
		}

	public void invert() {
		Matrix4d m = new Matrix4d();
		m.m00 = mat[0][0]; m.m01 = mat[0][1]; m.m02 = mat[0][2]; m.m03 = mat[0][3];
		m.m10 = mat[1][0]; m.m11 = mat[1][1]; m.m12 = mat[1][2]; m.m13 = mat[1][3];
		m.m20 = mat[2][0]; m.m21 = mat[2][1]; m.m22 = mat[2][2]; m.m23 = mat[2][3];
		m.m30 = mat[3][0]; m.m31 = mat[3][1]; m.m32 = mat[3][2]; m.m33 = mat[3][3];
		m.invert();
		mat = new double[][]{{m.m00,m.m01,m.m02,m.m03},{m.m10,m.m11,m.m12,m.m13},{m.m20,m.m21,m.m22,m.m23},{m.m30,m.m31,m.m32,m.m33}};
	}

	public void transpose() {
		for (int i = 0; i < 3; i++) {
			for (int j = 1 + i; j < 4; j++) {
				double m = mat[j][i];
				mat[j][i] = mat[i][j];
				mat[i][j] = m;
			}
		}
	}

	public void transform(Point3d point) {
		Point3d p = new Point3d();
		p.x = mat[0][0] * point.x + mat[0][1] * point.y + mat[0][2] * point.z;
		p.y = mat[1][0] * point.x + mat[1][1] * point.y + mat[1][2] * point.z;
		p.z = mat[2][0] * point.x + mat[2][1] * point.y + mat[2][2] * point.z;
		point.x = p.x;
		point.y = p.y;
		point.z = p.z;
	}
	
	/**
	 * •½–Ê‚É‘Î‚µ‚Ä‚µ‚©Žg‚Á‚Ä‚Í‚¢‚¯‚È‚¢
	 * @param plane
	 */
	public void transform(Vector4d plane) {
//		double x = mat[0][0] * plane.x + mat[0][1] * plane.y + mat[0][2] * plane.z;
//		double y = mat[1][0] * plane.x + mat[1][1] * plane.y + mat[1][2] * plane.z;
//		double z = mat[2][0] * plane.x + mat[2][1] * plane.y + mat[2][2] * plane.z;
//		
//		double vx = x * plane.w - mat[0][3];
//		double vy = y * plane.w - mat[1][3];
//		double vz = z * plane.w - mat[2][3];
//
//		plane.x = x;
//		plane.y = y;
//		plane.z = z;
//		plane.w = x * vx + y * vy + z * vz;
//		
		double x = (mat[0][0]*plane.x + mat[0][1]*plane.y
				+ mat[0][2]*plane.z + mat[0][3]*plane.w);
		double y = (mat[1][0]*plane.x + mat[1][1]*plane.y
				+ mat[1][2]*plane.z + mat[1][3]*plane.w);
		double z = (mat[2][0]*plane.x + mat[2][1]*plane.y
				+ mat[2][2]*plane.z + mat[2][3]*plane.w);
		plane.w = (mat[3][0]*plane.x + mat[3][1]*plane.y
				+ mat[3][2]*plane.z + mat[3][3]*plane.w);
		plane.x = x;
		plane.y = y;
		plane.z = z;
	}

	public void setTranslation(Vector3d v) {
		mat[3][0] = v.x;
		mat[3][1] = v.y;
		mat[3][2] = v.z;
	}
	
}
package java3d;

public class Vector4d {
	public double x;
	public double y;
	public double z;
	public double w;

	// コンストラクタ
	public Vector4d() {
		x = 0.0;
		y = 0.0;
		z = 0.0;
		w = 0.0;
	}

	// コンストラクタ
	public Vector4d(double px, double py, double pz, double pw) {
		x = px;
		y = py;
		z = pz;
		w = pw;
	}

	public Vector4d(Vector4d v) {
		x = v.x;
		y = v.y;
		z = v.z;
		w = v.w;
	}

	public void set(double x2, double y2, double z2, double w2) {
		x = x2;
		y = y2;
		z = z2;
		w = w2;
	}

	private void set(double[] t) {
		x = t[0];
		y = t[1];
		z = t[2];
		w = t[3];
	}
	
	public Vector4d clone() {
		return new Vector4d(x, y, z, w);
	}

	/** 絶対値を返す */
	final void absolute() {
		x = Math.abs(x);
		y = Math.abs(y);
		z = Math.abs(z);
		w = Math.abs(w);
	}

	final double angle(Vector4d v1) {
		double vDot = this.dot(v1) / (this.length() * v1.length());
		if (vDot < -1.0)
			vDot = -1.0;
		if (vDot > 1.0)
			vDot = 1.0;
		return ((double) (Math.acos(vDot)));
	}

	public final double dot(Vector4d v1) {
		return (this.x * v1.x + this.y * v1.y + this.z * v1.z + this.w * v1.w);
	}

	public final double length() {
		return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z
				+ this.w * this.w);
	}

	final void clamp(double min, double max) {
		if (x > max) {
			x = max;
		} else if (x < min) {
			x = min;
		}

		if (y > max) {
			y = max;
		} else if (y < min) {
			y = min;
		}

		if (z > max) {
			z = max;
		} else if (z < min) {
			z = min;
		}

		if (w > max) {
			w = max;
		} else if (w < min) {
			w = min;
		}
	}

	final void clampMax(double max) {
		if (x > max)
			x = max;
		if (y > max)
			y = max;
		if (z > max)
			z = max;
		if (w > max)
			w = max;
	}

	final void clampMin(double min) {
		if (x < min)
			x = min;
		if (y < min)
			y = min;
		if (z < min)
			z = min;
		if (w < min)
			w = min;
	}

	final void normalize() {
		double norm;

		norm = 1.0 / Math.sqrt(this.x * this.x + this.y * this.y + this.z
				* this.z + this.w * this.w);
		this.x *= norm;
		this.y *= norm;
		this.z *= norm;
		this.w *= norm;
	}

	final void normalize(Vector4d v1) {
		double norm;

		norm = 1.0 / Math.sqrt(v1.x * v1.x + v1.y * v1.y + v1.z * v1.z + v1.w
				* v1.w);
		this.x = v1.x * norm;
		this.y = v1.y * norm;
		this.z = v1.z * norm;
		this.w = v1.w * norm;
	}

	final void get(double[] t) {
		t[0] = this.x;
		t[1] = this.y;
		t[2] = this.z;
		t[3] = this.w;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZ() {
		return z;
	}

	public double getW() {
		return w;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public void setZ(double z) {
		this.z = z;
	}

	public void setW(double w) {
		this.w = w;
	}
}

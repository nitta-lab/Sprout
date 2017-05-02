package java3d;

public class Quat4d implements Cloneable {
	private static final double EPS = 1.0e-12;
	public double x;
	public double y;
	public double z;
	public double w;

	public Quat4d clone() {
		Quat4d q;
		try {
			q = (Quat4d) super.clone();
		} catch (CloneNotSupportedException ce) {
			throw new RuntimeException();
		}
		q.set(x, y, z, w);
		return q;
	}

	// コンストラクタ
	public Quat4d() {
		x = 0.0;
		y = 0.0;
		z = 0.0;
		w = 0.0;
	}

	// コンストラクタ
	public Quat4d(double px, double py, double pz, double pw) {
		double mag = 1.0/Math.sqrt(px*px+py*py+pz*pz+pw*pw);
		x = px*mag;
		y = py*mag;
		z = pz*mag;
		w = pw*mag;
	}

	public Quat4d(Quat4d q) {
		x = q.x;
		y = q.y;
		z = q.z;
		w = q.w;
	}

	public void set(AxisAngle4d a) {
		double mag,amag;
		amag = Math.sqrt( a.x*a.x + a.y*a.y + a.z*a.z);
		if( amag < EPS ) {
		w = 0.0;
		x = 0.0;
		y = 0.0;
		z = 0.0;
		} else {
		amag = 1.0/amag;
		mag = Math.sin(a.angle/2.0);
		w = Math.cos(a.angle/2.0);
		x = a.x*amag*mag;
		y = a.y*amag*mag;
		z = a.z*amag*mag;
		}
	}
	
	private void set(double x2, double y2, double z2, double w2) {
		x = x2;
		y = y2;
		z = z2;
		w = w2;
	}

	public void mul(Quat4d q1) {
		double x, y, w;
		w = this.w * q1.w - this.x * q1.x - this.y * q1.y - this.z * q1.z;
		x = this.w * q1.x + q1.w * this.x + this.y * q1.z - this.z * q1.y;
		y = this.w * q1.y + q1.w * this.y - this.x * q1.z + this.z * q1.x;
		this.z = this.w * q1.z + q1.w * this.z + this.x * q1.y - this.y * q1.x;
		this.w = w;
		this.x = x;
		this.y = y;
	}

	public void interpolate(Quat4d q1, double alpha) {
		double dot, s1, s2, om, sinom;

		dot = x * q1.x + y * q1.y + z * q1.z + w * q1.w;

		if (dot < 0) {
			// negate quaternion
			q1.x = -q1.x;
			q1.y = -q1.y;
			q1.z = -q1.z;
			q1.w = -q1.w;
			dot = -dot;
		}

		if ((1.0 - dot) > EPS) {
			om = Math.acos(dot);
			sinom = Math.sin(om);
			s1 = Math.sin((1.0 - alpha) * om) / sinom;
			s2 = Math.sin(alpha * om) / sinom;
		} else {
			s1 = 1.0 - alpha;
			s2 = alpha;
		}

		w = s1 * w + s2 * q1.w;
		x = s1 * x + s2 * q1.x;
		y = s1 * y + s2 * q1.y;
		z = s1 * z + s2 * q1.z;
	}

	public void normalize() {
		double norm;

		norm = (this.x * this.x + this.y * this.y + this.z * this.z + this.w
				* this.w);

		if (norm > 0.0) {
			norm = 1.0 / Math.sqrt(norm);
			this.x *= norm;
			this.y *= norm;
			this.z *= norm;
			this.w *= norm;
		} else {
			this.x = 0.0;
			this.y = 0.0;
			this.z = 0.0;
			this.w = 0.0;
		}
	}
}

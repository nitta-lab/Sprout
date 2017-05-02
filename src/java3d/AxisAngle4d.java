package java3d;

public class AxisAngle4d {
	public double angle;
	public double x;
	public double y;
	public double z;
	static final double EPS = 1.0e-12;
	
	// コンストラクタ
	public AxisAngle4d() {
		x = 0.0;
		y = 0.0;
		z = 0.0;		
	}

	//　コンストラクタ
	public AxisAngle4d(double px, double py, double pz, double pa) {
		x = px;
		y = py;
		z = pz;
		angle = pa;
	}
	
	public AxisAngle4d(AxisAngle4d a) {
		x = a.x;
		y = a.y;
		z = a.z;
		angle = a.angle;
	}

	public AxisAngle4d(Vector3d axis, double angle) {
		x = axis.x;
		y = axis.y;
		z = axis.z;
		this.angle = angle;
	}
	
	public final void set(Quat4d q1) {
        double mag = q1.x*q1.x + q1.y*q1.y + q1.z*q1.z;

        if( mag > EPS ) {
        	mag = Math.sqrt(mag);
        	double invMag = 1.0/mag;
	
        	x = q1.x*invMag;
        	y = q1.y*invMag;
        	z = q1.z*invMag;
        	angle = 2.0*Math.atan2(mag, q1.w);
		} else {
			x = 0.0d;
			y = 1.0d;
			z = 0.0d;
			angle = 0.0d;
        }
    }
	
	public AxisAngle4d clone() {
		return new AxisAngle4d(x, y, z, angle);
	}
	
}

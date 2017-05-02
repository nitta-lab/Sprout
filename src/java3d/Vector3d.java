package java3d;

public class Vector3d extends Tuple3d {
	
	// ƒRƒ“ƒXƒgƒ‰ƒNƒ^
	public Vector3d() {
		x = 0.0;
		y = 0.0;
		z = 0.0;
	}

	// ƒRƒ“ƒXƒgƒ‰ƒNƒ^
	public Vector3d(double px, double py, double pz) {
		x = px;
		y = py;
		z = pz;
	}

	public Vector3d(Vector3d v) {
		x = v.x;
		y = v.y;
		z = v.z;
	}
	
	public Vector3d(Tuple3d p) {
		x = p.x;
		y = p.y;
		z = p.z;
	}
	
	public Vector3d(double[] coordinate) {
		x = coordinate[0];
		y = coordinate[1];
		z = coordinate[2];
	}
	
	public Vector3d clone() {
		return new Vector3d(this.x, this.y, this.z);
	}
	
	public double dot(Vector3d a) {
		return x*a.x + y*a.y + z*a.z;
	}
	
	public void cross(Vector3d a, Vector3d b) {
		double x = a.y*b.z - a.z*b.y;
		double y = a.z*b.x - a.x*b.z;
		double z = a.x*b.y - a.y*b.x;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public double length() {
		return Math.sqrt(x*x+y*y+z*z);
	}
	
	public void scale(double s) {
		x = x*s;
		y = y*s;
		z = z*s;
	}
	
	public void normalize() {
		double l = length();
		x = x / l;
		y = y / l;
		z = z / l;
	}

	/** v1‚Æv2‚Ì˜a‚ğŠi”[‚·‚é */
	public void add(Tuple3d v1, Tuple3d v2) {
		this.x = v1.x + v2.x;
		this.y = v1.y + v2.y;
		this.z = v1.z + v2.z;
	}
	
	/** v1‚Æ‚Ì˜a‚ğŠi”[‚·‚é */
	public void add(Tuple3d v1) {
		this.x += v1.x;
		this.y += v1.y;
		this.z += v1.z;
	}

	/** v1‚Æv2‚Ì·‚ğŠi”[‚·‚é */
	public void sub(Tuple3d v1, Tuple3d v2) {
		this.x = v1.x - v2.x;
		this.y = v1.y - v2.y;
		this.z = v1.z - v2.z;
	}
	
	/** v1‚Æ‚Ì·‚ğŠi”[‚·‚é */
	public void sub(Tuple3d v1) {
		this.x -= v1.x;
		this.y -= v1.y;
		this.z -= v1.z;
	}
	
	/** Vector3d‚ğƒZƒbƒg‚·‚é */
	public void set(Tuple3d v) {
		this.x = v.x;
		this.y = v.y;
		this.z = v.z;
	}

	/** •„†‚ğ”½“]‚·‚é */
	public void negate() {
		this.x = -x;
		this.y = -y;
		this.z = -z;
	}

	/** this = d * axis + p */
	public void scaleAdd(double d, Vector3d axis, Vector3d p) {
		this.x = d * axis.x + p.x;
		this.y = d * axis.y + p.y;
		this.z = d * axis.z + p.z;
	}	

	public void negate(Vector3d v) {
		this.x = -v.x;
		this.y = -v.y;
		this.z = -v.z;
	}
	
	public double angle(Vector3d v) {
		double d = dot(v) / (length() * v.length());
		if (d < -1D)
			d = -1D;
		if (d > 1.0D)
			d = 1.0D;
		return Math.acos(d);
	}
}

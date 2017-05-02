package java3d;

public class Vector3f {
	public float x;
	public float y;
	public float z;

	// ƒRƒ“ƒXƒgƒ‰ƒNƒ^
	public Vector3f() {
		x = 0.0f;
		y = 0.0f;
		z = 0.0f;
	}

	// ƒRƒ“ƒXƒgƒ‰ƒNƒ^
	public Vector3f(float px, float py, float pz) {
		x = px;
		y = py;
		z = pz;
	}

	public Vector3f(Vector3f v) {
		x = v.x;
		y = v.y;
		z = v.z;
	}
	
	public Vector3f clone() {
		return new Vector3f(this.x, this.y, this.z);
	}
	
	public double dot(Vector3f a) {
		return x*a.x + y*a.y + z*a.z;
	}
	
	public void cross(Vector3f a, Vector3f b) {
		float x = a.y*b.z - a.z*b.y;
		float y = a.z*b.x - a.x*b.z;
		float z = a.x*b.y - a.y*b.x;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public float length() {
		return (float)Math.sqrt(x*x + y*y + z*z);
	}
	
	public void scale(float s) {;
		x = x*s;
		y = y*s;
		z = z*s;
	}
	
	public void normalize() {
		float l = length();
		x = x / l;
		y = y / l;
		z = z / l;
	}

	/** v1‚Æv2‚Ì˜a‚ğŠi”[‚·‚é */
	public void add(Vector3f v1, Vector3f v2) {
		this.x = v1.x + v2.x;
		this.y = v1.y + v2.y;
		this.z = v1.z + v2.z;
	}
	
	/** v1‚Æ‚Ì˜a‚ğŠi”[‚·‚é */
	public void add(Vector3f v1) {
		this.x += v1.x;
		this.y += v1.y;
		this.z += v1.z;
	}

	/** v1‚Æv2‚Ì·‚ğŠi”[‚·‚é */
	public void sub(Vector3f v1, Vector3f v2) {
		this.x = v1.x - v2.x;
		this.y = v1.y - v2.y;
		this.z = v1.z - v2.z;
	}
	
	/** v1‚Æ‚Ì·‚ğŠi”[‚·‚é */
	public void sub(Vector3f v1) {
		this.x -= v1.x;
		this.y -= v1.y;
		this.z -= v1.z;
	}
	
	/** Vector3d‚ğƒZƒbƒg‚·‚é */
	public void set(Vector3f v) {
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
	public void scaleAdd(float d, Vector3f axis, Vector3f p) {
		this.x = d * axis.x + p.x;
		this.y = d * axis.y + p.y;
		this.z = d * axis.z + p.z;
	}

	public void setX(float x) {
		this.x = x;
	}
	
	public void setY(float y) {
		this.y = y;
	}

	public void setZ(float z) {
		this.z = z;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public float getZ() {
		return z;
	}
}

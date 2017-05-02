package java3d;

public class Point3f  {
	public float x;
	public float y;
	public float z;
	
	// コンストラクタ
	public Point3f() {
		this.x = 0;
		this.y = 0;
		this.z = 0;
	}
	
	public Point3f(float[] p) {
		this.x = p[0];
		this.y = p[1];
		this.z = p[2];
	}
	
	public Point3f(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Point3f(Point3f p1) {
		this.x = p1.x;
		this.y = p1.y;
		this.z = p1.z;
	}
	
	public void scale(float s) {
		this.x = x * s;
		this.y = y * s;
		this.z = z * s;
	}
	
	public void add(Point3f p) {
		this.x += p.x;
		this.y += p.y;
		this.z += p.z;
	}
	
	public void sub(Point3f p) {
		this.x -= p.x;
		this.y -= p.y;
		this.z -= p.z;
	}
	
	public void set(Point3f v) {
		this.x = v.x;
		this.y = v.y;
		this.z = v.z;
	}

	/** 引数の点との距離を返す */
	public double distance(Point3f p) {
		return Math.sqrt(distanceSquared(p));
	}

	public double distanceSquared(Point3f p) {
		return (this.x - p.x) * (this.x - p.x) + (this.y - p.y) * (this.y - p.y) + (this.z - p.z) * (this.z - p.z);
	}
}

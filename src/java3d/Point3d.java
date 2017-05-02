package java3d;

public class Point3d extends Tuple3d {
	
	// コンストラクタ
	public Point3d() {
		this.x = 0;
		this.y = 0;
		this.z = 0;
	}
	
	public Point3d(double[] p) {
		this.x = p[0];
		this.y = p[1];
		this.z = p[2];
	}
	
	public Point3d(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Point3d(Point3d p1) {
		this.x = p1.x;
		this.y = p1.y;
		this.z = p1.z;
	}
	
	public Point3d(Tuple3d v) {
		this.x = v.x;
		this.y = v.y;
		this.z = v.z;
	}
	
	public void scale(double s) {
		this.x = x * s;
		this.y = y * s;
		this.z = z * s;
	}
	
	public void add(Tuple3d p) {
		this.x += p.x;
		this.y += p.y;
		this.z += p.z;
	}
	
	public void sub(Tuple3d p) {
		this.x -= p.x;
		this.y -= p.y;
		this.z -= p.z;
	}
	
	public void set(Tuple3d v) {
		this.x = v.x;
		this.y = v.y;
		this.z = v.z;
	}

	/** 引数の点との距離を返す */
	public double distance(Point3d p) {
		return Math.sqrt(distanceSquared(p));
	}

	public double distanceSquared(Point3d p) {
		return (this.x - p.x) * (this.x - p.x) + (this.y - p.y) * (this.y - p.y) + (this.z - p.z) * (this.z - p.z);
	}
}

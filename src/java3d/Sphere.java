package java3d;

public class Sphere extends Primitive {
	public static final int BODY = 0;
	static final int MID_REZ_DIV = 16;
	
	private float radius;
	int divisions;
	
	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

	@Override
	public Shape3D getShape(int partid) {
		return null;
	}
}

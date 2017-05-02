package java3d;

public class BoundingBox extends Bounds {
	Point3d lower;
	Point3d upper;
	
	public BoundingBox(Point3d lower, Point3d upper) {
		boundId = BOUNDING_BOX;
		this.lower = lower;
		this.upper = upper;
	}

	@Override
	public Object clone() {
		// TODO Auto-generated method stub
		return new BoundingBox(new Point3d(lower), new Point3d(upper));
	}

	@Override
	public void combine(Bounds boundsObject) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void transform(Transform3D trans) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean intersect(Bounds boundsObject) {
		// TODO Auto-generated method stub
		return false;
	}

}

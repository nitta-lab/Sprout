package java3d;

public class BoundingSphere extends Bounds {
	public Point3d center = new Point3d();
	public double radius;
	
	public BoundingSphere(Point3d center, double r) {
		boundId = BOUNDING_SPHERE;
		this.center = center;
		this.radius = r;
	}
	
	public BoundingSphere() {
		boundId = BOUNDING_SPHERE;
		center.x = 0;
		center.y = 0;
		center.z = 0;
		radius = 1;
	}

	public void combine(BoundingSphere bs) {
		double r1 = this.radius;
		double r2 = bs.radius;

		Point3d c1 = new Point3d(center);
		Point3d c2 = new Point3d(bs.center);

		double d = center.distance(bs.center);

		if (d + r2 <= r1) {
			// this が bs を包含する場合、何もしない
		} else if (d + r1 < r2) {
			// bs が this を包含する場合、bsをコピー
			this.radius = bs.radius;
			this.center.set(bs.center);
		} else {
			// this と 　bs のいずれも他方を包含しない場合
			this.radius = (r1 + r2 + d) / 2.0;
	
			double a1 = this.radius - r1;
			double a2 = this.radius - r2;
	
			c1.scale(a2);
			c2.scale(a1);
			c1.add(c2);
			c1.scale(1 / d);
			center.set(c1);
		}
	}

	public void transform(Transform3D trans) {
		 Vector4d v = new Vector4d(center.x, center.y, center.z, 1.0);
		 trans.transform(v);
		 center.set(v.x, v.y, v.z);
		 
		 double max = 0.0;
		 for(int i = 0; i < trans.scales.length; i++) {
			  if (max < trans.scales[i]) {
				  max = trans.scales[i];
			  }
		 }
		 radius *= max;
	 }

	@Override
	public Object clone() {
		return new BoundingSphere(new Point3d(this.center), this.radius);
	}

	@Override
	public void combine(Bounds boundsObject) {
		if( boundsObject instanceof BoundingSphere) {
			combine((BoundingSphere) boundsObject);
		}
	}

	@Override
	public boolean intersect(Bounds boundsObject) {
		if (boundsObject instanceof BoundingSphere) {
			BoundingSphere bs = (BoundingSphere) boundsObject;
			double r = this.radius + bs.radius;
			double x = center.x - bs.center.x;
			double y = center.y - bs.center.y;
			double z = center.z - bs.center.z;
			double d = x * x + y * y + z * z;
			if (d <= r * r) {
				return true;
			}
		}
		return false;
	}
	
}

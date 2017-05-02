package java3d;

public class Shape3D extends Leaf {
	private Geometry geometry;
	private Appearance ap;
	
	public Shape3D(Geometry g, Appearance a) {
		this.geometry = g;
		this.ap = a;
	}

	public Appearance getAppearance() {
		return ap;
	}

	public void setAppearance(Appearance ap) {
		this.ap = ap;
	}

	public Geometry getGeometry() {
		return geometry;
	}

	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}

	@Override
	public Node cloneTree() {
		Shape3D shape = new Shape3D(geometry, (Appearance)ap.cloneNodeComponent());
		return shape;
	}
}

package java3d;

public abstract class Primitive extends Group {
	private Appearance ap = null;

	public Appearance getAppearance() {
		return ap;
	}

	public void setAppearance(Appearance ap) {
		this.ap = ap;
	}

	public abstract Shape3D getShape(int partid);	
}

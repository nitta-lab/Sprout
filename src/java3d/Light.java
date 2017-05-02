package java3d;

public abstract class Light extends Leaf {
	protected Color3f color;
	
	public Light(Color3f c) {
		this.color = c;
	}
	
	public void setColor(Color3f c) {
		color = c;
	}
	
	public Color3f getColor() {
		return color;
	}
}

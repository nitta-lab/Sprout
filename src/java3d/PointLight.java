package java3d;

public class PointLight extends Light {
	private Point3f position;
	private Point3f attenuation;
	
	public PointLight() {
		super(new Color3f(1.0f, 1.0f, 1.0f));
		position = new Point3f(0.0f, 0.0f, 0.0f);
	}
	
	public PointLight(Color3f color, Point3f position, Point3f attenuation){
		super(color);
		this.position = position;
		this.attenuation = attenuation;
	}

	@Override
	public Node cloneTree() {
		return new PointLight(color, position, attenuation);
	}
	
	public void setPosition(Point3f p){
		this.position = p;
	}
	
	public Point3f getPosition(){
		return position;
	}

}

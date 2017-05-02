package java3d;

public class DirectionalLight extends Light {
	private Vector3f direction;

	@Override
	public Node cloneTree() {
		return new DirectionalLight(color, direction);
	}
	
	public DirectionalLight(Color3f c, Vector3f d){
		super(c);
		d.normalize();
		this.direction = d;
	}
	
	public void setDirection(Vector3f d){
		d.normalize();
		this.direction = d;
	}
	
	public Vector3f getDirection(){
		return direction;
	}

}

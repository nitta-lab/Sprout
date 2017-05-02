package java3d;

public class AmbientLight extends Light {

	@Override
	public Node cloneTree() {
		return new AmbientLight(color);
	}
	
	public AmbientLight(Color3f c){
		super(c);
	}
}

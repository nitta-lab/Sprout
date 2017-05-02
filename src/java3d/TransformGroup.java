package java3d;

import java.util.Enumeration;

public class TransformGroup extends Group {

	private Transform3D transform;	
	
	public TransformGroup() {
		this(new Transform3D());
	}
	
	public TransformGroup(Transform3D transform) {
		this.transform = transform;
	}
	
	public void setTransform(Transform3D transform) {
		this.transform = transform;
	}
	
	public void getTransform(Transform3D transform) {
		transform.set(this.transform);
	}
	
	public Node cloneTree() {
		TransformGroup newInstance = new TransformGroup(new Transform3D(transform));
		return newInstance;		
	}
}

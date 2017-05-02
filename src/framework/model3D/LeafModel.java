package framework.model3D;

import java3d.Appearance;
import java3d.Geometry;

public class LeafModel extends Model3D {
	
	private Geometry g;
	private Appearance a;
	
	public LeafModel(String name,Geometry g,Appearance a){
		this.name = name;
		this.g = g;
		this.a = a;
	}
	
	public Object3D createObject(){
		Appearance a2 = (Appearance)a.cloneNodeComponent();
		Object3D obj = new Object3D(name, g, a2);
		return obj;
	}

	public Object3D createObjectSharingAppearance(){
		Object3D obj = new Object3D(name, g, a);
		return obj;
	}
}

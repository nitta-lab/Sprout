package framework.model3D;

import java.util.ArrayList;

import framework.physics.Ground;
import java3d.BranchGroup;
import java3d.Group;
import java3d.Leaf;
import java3d.Light;
import java3d.Node;
import java3d.Primitive;
import java3d.Shape3D;
import java3d.Transform3D;
import java3d.TransformGroup;

public class Universe {
	private BranchGroup root = null;
	private ArrayList<Light> lights = new ArrayList<Light>();
	private BackgroundBox skyBox = null;
	private Ground ground = null;
	private ArrayList<Movable> movableList = new ArrayList<Movable>();
	
	public Universe() {
		root = new BranchGroup();
	}
	
	public void render(IViewer3D viewer) {
		viewer.update(lights, skyBox);
		render(viewer, root);
	}
	
	private void render(IViewer3D viewer, Node node) {
		if (node instanceof Group) {
			if (node instanceof Primitive) {
				viewer.draw((Primitive) node);
			} else {
				if (node instanceof TransformGroup) {
					Transform3D transform = new Transform3D();
					((TransformGroup) node).getTransform(transform);
					//
//					Vector3d vec = new Vector3d(1.0, 1.1, 1.0);
//					transform.setScale(vec);
//					AxisAngle4d aa = new AxisAngle4d(0.0, 0.0, 1.0, 1.0);
//					transform.setRotation(aa);
					//
					viewer.pushTransform(transform);
					for (int i = 0; i < ((Group) node).numChildren(); i++) {
						Node node2 = ((Group) node).getChild(i);
						render(viewer, node2);
					}
					viewer.popTransform();
				} else {
					for (int i = 0; i < ((Group) node).numChildren(); i++) {
						Node node2 = ((Group) node).getChild(i);
						render(viewer, node2);
					}
				}
			}
		} else if (node instanceof Leaf) {
			viewer.draw(node);
		}
	}
	
	public void update(long interval){
		for(int i = 0; i < movableList.size(); i++){
			Movable movable = movableList.get(i);
			movable.motion(interval,ground);			
		}
	}

	/**
	 * オブジェクトを配置する
	 * 
	 * @param obj
	 *            配置するオブジェクト
	 */
	public void place(Placeable obj) {
		if(obj instanceof Ground){
			ground = (Ground)obj;
		}
		if(obj instanceof Movable){
			movableList.add((Movable)obj);
		}			
		place(obj.getTransformGroupToPlace());
	}

	private void place(Node node) {
		root.addChild(node);
	}

	/**
	 * 後で取り除けるようにオブジェクトを配置する
	 * 
	 * @param obj
	 *            配置するオブジェクト
	 */
	public void placeDisplacable(Placeable obj) {
		placeDisplacable(obj.getTransformGroupToPlace());
	}

	private void placeDisplacable(Node node) {
		root.addChild(node);
	}

	/**
	 * 光源の追加
	 * 
	 * @param light
	 *            追加する光源
	 */
	public void placeLight(Light light) {
		root.addChild(light);
		getLights().add(light);
	}
	
	/**
	 * スカイボックスの追加
	 * @param skyBox 追加するスカイボックス
	 */
	public void placeSkyBox(BackgroundBox skyBox) {
		root.addChild(skyBox);		
		this.skyBox = skyBox;
	}

	/**
	 * オブジェクトを可能ならば取り除く
	 * 
	 * @param obj
	 *            取り除くオブジェクト
	 */
	public void displace(Placeable obj) {
		displace(obj.getTransformGroupToPlace());
	}

	private void displace(Node node) {
		root.removeChild(node);
	}

	public ArrayList<Light> getLights() {
		return lights;
	}

}

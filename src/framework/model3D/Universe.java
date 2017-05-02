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
	 * �I�u�W�F�N�g��z�u����
	 * 
	 * @param obj
	 *            �z�u����I�u�W�F�N�g
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
	 * ��Ŏ�菜����悤�ɃI�u�W�F�N�g��z�u����
	 * 
	 * @param obj
	 *            �z�u����I�u�W�F�N�g
	 */
	public void placeDisplacable(Placeable obj) {
		placeDisplacable(obj.getTransformGroupToPlace());
	}

	private void placeDisplacable(Node node) {
		root.addChild(node);
	}

	/**
	 * �����̒ǉ�
	 * 
	 * @param light
	 *            �ǉ��������
	 */
	public void placeLight(Light light) {
		root.addChild(light);
		getLights().add(light);
	}
	
	/**
	 * �X�J�C�{�b�N�X�̒ǉ�
	 * @param skyBox �ǉ�����X�J�C�{�b�N�X
	 */
	public void placeSkyBox(BackgroundBox skyBox) {
		root.addChild(skyBox);		
		this.skyBox = skyBox;
	}

	/**
	 * �I�u�W�F�N�g���\�Ȃ�Ύ�菜��
	 * 
	 * @param obj
	 *            ��菜���I�u�W�F�N�g
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

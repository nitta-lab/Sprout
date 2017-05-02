package framework.view3D;

import java.util.ArrayList;
import java3d.Box;
import java3d.Cone;
import java3d.Cylinder;
import java3d.GraphicsContext3D;
import java3d.Light;
import java3d.Node;
import java3d.Shape3D;
import java3d.Sphere;
import java3d.Transform3D;
import java3d.Vector3d;

import framework.model3D.BackgroundBox;
import framework.model3D.IViewer3D;
import framework.model3D.Position3D;

public class Viewer3D implements IViewer3D {
	private GraphicsContext3D gc3D = null;
	private ArrayList<Light> lights = null;
	private BackgroundBox skyBox = null;
	private Camera3D camera = null;
	
	public Viewer3D(Camera3D camera) {
		this.camera = camera;
	}

	@Override
	public void setGraphicsContext3D(GraphicsContext3D gc3D) {
		if (this.gc3D != gc3D) {
			this.gc3D = gc3D;
		}
	}

	@Override
	public void surfaceChanged(int width, int height) {
		gc3D.update(width, height, (float)camera.getFieldOfView(), (float)camera.getFrontClipDistance(), (float)camera.getBackClipDistance(), camera.isParallel());
	}

	@Override
	public void onDrawFrame() {
		Position3D eye = camera.getViewPoint();
		Position3D center = eye.clone().add(camera.getViewLine());
		Vector3d up = camera.getViewUp();
		gc3D.update((float)camera.getFieldOfView(), (float)camera.getFrontClipDistance(), (float)camera.getBackClipDistance(), eye, center, up, camera.isParallel());
	}
	
	@Override
	public void update(ArrayList<Light> lights, BackgroundBox skyBox) {
		// 光源の更新
		if (this.lights != lights) {
			this.lights = lights;
		}
		
		// スカイボックスの更新
		if (this.skyBox != skyBox) {
			this.skyBox = skyBox;
		}
	}
		
	@Override
	public void draw(Node node) {
		if (node instanceof Box) {
			gc3D.draw(((Box)node).getShape(Box.FRONT));
			gc3D.draw(((Box)node).getShape(Box.BACK));
			gc3D.draw(((Box)node).getShape(Box.LEFT));
			gc3D.draw(((Box)node).getShape(Box.RIGHT));
			gc3D.draw(((Box)node).getShape(Box.TOP));
			gc3D.draw(((Box)node).getShape(Box.BOTTOM));
		} else if (node instanceof Cone) {
			gc3D.draw(((Cone)node).getShape(Cone.BODY));			
			gc3D.draw(((Cone)node).getShape(Cone.CAP));			
		} else if (node instanceof Cylinder) {
			gc3D.draw(((Cylinder)node).getShape(Cylinder.BODY));			
			gc3D.draw(((Cylinder)node).getShape(Cylinder.TOP));			
			gc3D.draw(((Cylinder)node).getShape(Cylinder.BOTTOM));			
		} else if (node instanceof Sphere) {
			gc3D.draw(((Sphere)node).getShape(Sphere.BODY));			
		} else if (node instanceof Shape3D) {
			gc3D.draw((Shape3D)node);
		} else if (node instanceof Light){
			gc3D.updateLightState((Light)node);
		}
	}

	@Override
	public void pushTransform(Transform3D transform) {
		gc3D.pushMatrix();
		gc3D.multMatrix(transform);
	}

	@Override
	public void popTransform() {
		gc3D.popMatrix();
	}
}

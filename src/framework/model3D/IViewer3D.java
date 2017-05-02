package framework.model3D;

import java.util.ArrayList;

import java3d.GraphicsContext3D;
import java3d.Light;
import java3d.Node;
import java3d.Transform3D;

import framework.model3D.BackgroundBox;

public interface IViewer3D {
	public abstract void setGraphicsContext3D(GraphicsContext3D gc3D);
	public abstract void surfaceChanged(int width, int height);
	public abstract void onDrawFrame();
	public abstract void update(ArrayList<Light> lights, BackgroundBox skyBox);
	public abstract void pushTransform(Transform3D t);
	public abstract void popTransform();
	public abstract void draw(Node obj);
}

package framework.RWT;

import java3d.GraphicsContext3D;
import java3d.Light;

import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView.Renderer;
import framework.view3D.Camera3D;
import framework.view3D.Viewer3D;

public class RWTRenderer implements Renderer {
	protected Viewer3D viewer;
	protected Camera3D camera;
	protected GraphicsContext3D gc3D = null;

	public RWTRenderer() {
		super();
	}
	
	public void attachCamera(Camera3D camera) {
		// onSurfaceCreated()ÇÊÇËêÊÇ…åƒÇŒÇÍÇÈ
		this.camera = camera;
		viewer = new Viewer3D(camera);
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		gc3D = new GraphicsContext3D(gl);
		ArrayList<Light> lights = camera.getUniverse().getLights();
		for (int i = 0; i < lights.size(); i++){
			Light l = lights.get(i);
			gc3D.setLight(l,i);
		}
		viewer.setGraphicsContext3D(gc3D);
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		if (gc3D == null) {
			gc3D = new GraphicsContext3D(gl);
			viewer.setGraphicsContext3D(gc3D);
		} else {
			viewer.setGraphicsContext3D(gc3D.setGL10(gl));
		}
		viewer.surfaceChanged(width, height);
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		if (gc3D == null) {
			gc3D = new GraphicsContext3D(gl);
			viewer.setGraphicsContext3D(gc3D);
		} else {
			viewer.setGraphicsContext3D(gc3D.setGL10(gl));
		}
		viewer.onDrawFrame();

		// 3DÉÇÉfÉãÇÃÉåÉìÉ_ÉäÉìÉO
		gc3D.pushMatrix();
		camera.getUniverse().render(viewer);
		gc3D.popMatrix();
	}
}

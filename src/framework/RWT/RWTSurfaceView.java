package framework.RWT;

import framework.view3D.Camera3D;
import android.content.Context;
import android.opengl.GLSurfaceView;

public class RWTSurfaceView extends GLSurfaceView {
	protected RWTRenderer renderer;

	public RWTSurfaceView(Context context) {
		this(context, true);
	}
	
	public RWTSurfaceView(Context context, boolean bInitRenderer) {
		super(context);
		if (bInitRenderer) {
			renderer = new RWTRenderer();
			setEGLConfigChooser(8, 8, 8, 8, 16, 0);
			this.setRenderer(renderer);			
		}
	}

	public void attachCamera(Camera3D camera) {
		renderer.attachCamera(camera);
	}

}

package framework.gameMain;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import framework.RWT.RWTSurfaceView;
import framework.model3D.Universe;
import framework.view3D.Camera3D;

public abstract class RealTime3DActivity extends RealTimeActivity {
	protected Universe universe;
	protected Camera3D camera;
	protected RWTSurfaceView view;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		universe = new Universe();
			
		camera = new Camera3D(universe);
		
		view = new RWTSurfaceView(this);
		view.setRenderMode(RWTSurfaceView.RENDERMODE_WHEN_DIRTY);
		view.attachCamera(camera);
		setContentView(view);
	}

	protected void update(long interval) {
		progress(interval);
		universe.update(interval);
		camera.adjust(interval);
		view.requestRender();
	}
	
	abstract protected void progress(long interval);
}

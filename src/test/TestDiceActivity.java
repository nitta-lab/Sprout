package test;

import java3d.Appearance;
import java3d.Box;
import java3d.Material;
import java3d.Texture;
import java3d.TextureCubeMap;
import java3d.TextureLoader;
import java3d.Transform3D;

import com.example.sprout.R;
import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import framework.RWT.RWTSurfaceView;
import framework.gameMain.RealTimeActivity;
import framework.model3D.Object3D;
import framework.model3D.Position3D;
import framework.model3D.Universe;
import framework.physics.AngularVelocity3D;
import framework.physics.Force3D;
import framework.physics.PhysicalSystem;
import framework.physics.Solid3D;
import framework.physics.Velocity3D;
import framework.view3D.Camera3D;

public class TestDiceActivity extends RealTimeActivity {
	private PhysicalSystem physicalSystem;
	private Object3D groundObj;
	private Solid3D ground;
	private Transform3D initTrans;
	private Solid3D diceObj;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// 3Dモデルの作成
		Universe universe = new Universe();
		
		// サイコロの作成
		Appearance ap1 = new Appearance();
		TextureCubeMap tex1 = new TextureCubeMap(TextureCubeMap.BASE_LEVEL, TextureCubeMap.RGB, 0);
		TextureLoader loader = new TextureLoader(getResources(), R.drawable.dice_1, TextureLoader.BY_REFERENCE | TextureLoader.Y_UP);
		tex1.setImage(0, TextureCubeMap.POSITIVE_Z, loader.getImage());
		loader = new TextureLoader(getResources(), R.drawable.dice_6, TextureLoader.BY_REFERENCE | TextureLoader.Y_UP);
		tex1.setImage(0, TextureCubeMap.NEGATIVE_Z, loader.getImage());
		loader = new TextureLoader(getResources(), R.drawable.dice_4, TextureLoader.BY_REFERENCE | TextureLoader.Y_UP);
		tex1.setImage(0, TextureCubeMap.POSITIVE_X, loader.getImage());
		loader = new TextureLoader(getResources(), R.drawable.dice_3, TextureLoader.BY_REFERENCE | TextureLoader.Y_UP);
		tex1.setImage(0, TextureCubeMap.NEGATIVE_X, loader.getImage());
		loader = new TextureLoader(getResources(), R.drawable.dice_5, TextureLoader.BY_REFERENCE | TextureLoader.Y_UP);
		tex1.setImage(0, TextureCubeMap.POSITIVE_Y, loader.getImage());
		loader = new TextureLoader(getResources(), R.drawable.dice_2, TextureLoader.BY_REFERENCE | TextureLoader.Y_UP);
		tex1.setImage(0, TextureCubeMap.NEGATIVE_Y, loader.getImage());
		ap1.setTexture(tex1);
		physicalSystem = new PhysicalSystem();
		Box b1 = new Box(1.0f, 1.0f, 1.0f, ap1);
		Object3D obj1 = new Object3D("box", b1);
		obj1.apply(new Position3D(0.0 , 30.0, 0.0), false);
		diceObj = new Solid3D(obj1);
		diceObj.scale(2.0, 2.0, 2.0);
		diceObj.apply(new AngularVelocity3D(-0.4, 0.0, -0.6), false);
		universe.place(diceObj);
		physicalSystem.add(diceObj);
		initTrans = new Transform3D();
		obj1.rot.getTransform(initTrans);
		
		// 地面の作成
		Appearance ap2 = new Appearance();
		Material m = new Material();
		m.setDiffuseColor(1.0f, 1.0f, 1.0f);
		m.setAmbientColor(0.5f, 0.5f, 0.5f);
		m.setSpecularColor(0.0f, 0.0f, 0.0f);
		m.setShininess(1.0f);
		ap2.setMaterial(m);
		
		Box b2 = new Box(1.0f, 1.0f, 1.0f, ap1);		
		groundObj = new Object3D("ground", b2);
		groundObj.apply(new Position3D(0.0, -3.0, 0.0), false);
		groundObj.scale(100.0, 3.0, -50.0);
		ground = new Solid3D(groundObj);
		ground.setMass(100000000);
		universe.place(ground);
		physicalSystem.add(ground);
		
		// カメラの作成
		Camera3D camera = new Camera3D(universe);
		camera.setViewPoint(new Position3D(0.0, 30, -30.0));
		camera.addTarget(diceObj);
		
		// 画面との関連付け
		RWTSurfaceView view = new RWTSurfaceView(this);
		view.attachCamera(camera);
		setContentView(view);
		
		start(10);
	}

	@Override
	protected void update(long interval) {
		ground.apply(new Position3D(0.0, -3.0, 0.0), false);
		ground.apply(new Velocity3D(), false);
		groundObj.rot.setTransform(initTrans);
		
		long newTime = System.nanoTime();
		physicalSystem.motion(0, 10, Force3D.ZERO, physicalSystem.objects.get(0).getGravityCenter(), null);
		Log.v("moji", "" + ((System.nanoTime()-newTime)/1000.0));
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event){
		physicalSystem.objects.get(0).setPosition(new Position3D(0.0 , 30.0, 0.0));
		return true;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}

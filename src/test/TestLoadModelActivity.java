package test;

import java3d.IndexedTriangleArray;

import java3d.Appearance;
import java3d.Box;
import java3d.Color3f;
import java3d.DirectionalLight;
import java3d.Material;
import java3d.Point3d;
import java3d.TextureCubeMap;
import java3d.TextureLoader;
import java3d.Transform3D;
import java3d.Vector3d;
import java3d.Vector3f;

import com.example.sprout.R;

import java.io.FileNotFoundException;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import framework.RWT.RWTSurfaceView;
import framework.gameMain.OvergroundActor;
import framework.gameMain.RealTime3DActivity;
import framework.model3D.BaseObject3D;
import framework.model3D.CollisionResult;
import framework.model3D.Model3D;
import framework.model3D.ModelFactory;
import framework.model3D.Object3D;
import framework.model3D.Position3D;
import framework.model3D.Universe;
import framework.physics.AngularVelocity3D;
import framework.physics.Force3D;
import framework.physics.Ground;
import framework.physics.PhysicalSystem;
import framework.physics.PhysicsUtility;
import framework.physics.Solid3D;
import framework.physics.Velocity3D;
import framework.view3D.Camera3D;

public class TestLoadModelActivity extends RealTime3DActivity {
	private OvergroundActor actor;

	@Override
	protected void progress(long interval) {
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// 3Dモデルの作成
		
		// サイコロの作成
		Appearance ap1 = new Appearance();
		Material m = new Material();
		m.setDiffuseColor(0.3f, 1.0f, 0.0f);
		m.setAmbientColor(0.0f, 0.0f, 0.0f);
		m.setEmissiveColor(0.0f, 0.0f, 0.0f);
		m.setSpecularColor(0.0f, 0.0f, 0.0f);
		m.setShininess(0.0f);
		ap1.setMaterial(m);
//		TextureCubeMap tex1 = new TextureCubeMap(TextureCubeMap.BASE_LEVEL, TextureCubeMap.RGB, 0);
//		TextureLoader loader = new TextureLoader(getResources(), R.drawable.dice_1, TextureLoader.BY_REFERENCE | TextureLoader.Y_UP);
//		tex1.setImage(0, TextureCubeMap.POSITIVE_Z, loader.getImage());
//		loader = new TextureLoader(getResources(), R.drawable.dice_6, TextureLoader.BY_REFERENCE | TextureLoader.Y_UP);
//		tex1.setImage(0, TextureCubeMap.NEGATIVE_Z, loader.getImage());
//		loader = new TextureLoader(getResources(), R.drawable.dice_4, TextureLoader.BY_REFERENCE | TextureLoader.Y_UP);
//		tex1.setImage(0, TextureCubeMap.POSITIVE_X, loader.getImage());
//		loader = new TextureLoader(getResources(), R.drawable.dice_3, TextureLoader.BY_REFERENCE | TextureLoader.Y_UP);
//		tex1.setImage(0, TextureCubeMap.NEGATIVE_X, loader.getImage());
//		loader = new TextureLoader(getResources(), R.drawable.dice_5, TextureLoader.BY_REFERENCE | TextureLoader.Y_UP);
//		tex1.setImage(0, TextureCubeMap.POSITIVE_Y, loader.getImage());
//		loader = new TextureLoader(getResources(), R.drawable.dice_2, TextureLoader.BY_REFERENCE | TextureLoader.Y_UP);
//		tex1.setImage(0, TextureCubeMap.NEGATIVE_Y, loader.getImage());
//		ap1.setTexture(tex1);
		Model3D model = null;
		try {
			model = ModelFactory.loadModel(getResources(), "cube-binary.stl", ap1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Object3D obj1 = model.createObject();
		obj1.apply(new Position3D(0.0, 100.0, 250.0), false);
		actor = new OvergroundActor(obj1, null);
		universe.place(actor);
				
		// 地面の作成
		Appearance ap2 = new Appearance();
		Material m2 = new Material();
		m2.setDiffuseColor(0.1f, 0.0f, 0.02f);
		m2.setAmbientColor(0.1f, 0.1f, 0.1f);
		m2.setEmissiveColor(0.0f, 0.0f, 0.0f);
		m2.setSpecularColor(0.2f, 0.2f, 0.2f);
		m2.setShininess(0.0f);
		ap2.setMaterial(m2);
		Model3D groundModel = null;
		try {
			groundModel = ModelFactory.loadModel(getResources(), "konan/konan.stl", ap2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Object3D groundObj = groundModel.createObject();
		groundObj.scale(0.1, 0.1, 0.1);
		groundObj.setPosition(new Position3D(0.0, 0.0, 0.0));
		Ground ground = new Ground(groundObj);
		universe.place(ground);
		
		// 光源の設定
		universe.placeLight(new DirectionalLight(new Color3f(1.0f, 1.0f, 1.0f), new Vector3f(0.0f, -1.0f, -0.5f)));
		
		// カメラの設定
		camera.setViewPoint(new Position3D(0.0, 100.0, 400.0));
		camera.addTarget(actor);
		camera.setFrontClipDistance(1.0);
		camera.setBackClipDistance(10000.0);
		
		// フレームの間隔（ms）
		start(20);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event){
		actor.setVelocity(new Velocity3D(0.0, 0.0, 0.0));
		actor.setPosition(new Position3D(0.0, 100.0, 250.0));
		return true;
	}
}

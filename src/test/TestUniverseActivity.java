package test;

import java3d.IndexedTriangleArray;

import java3d.Appearance;
import java3d.Box;
import java3d.Material;
import java3d.Point3d;
import java3d.TextureCubeMap;
import java3d.TextureLoader;
import java3d.Transform3D;
import java3d.Vector3f;

import com.example.sprout.R;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import framework.RWT.RWTSurfaceView;
import framework.gameMain.OvergroundActor;
import framework.gameMain.RealTime3DActivity;
import framework.model3D.BaseObject3D;
import framework.model3D.CollisionResult;
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

public class TestUniverseActivity extends RealTime3DActivity {

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
		m.setDiffuseColor(1.0f, 1.0f, 1.0f);
		ap1.setMaterial(m);
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
		Box b1 = new Box(1.0f, 1.0f, 1.0f, ap1);
		final Object3D obj1 = new Object3D("box", b1);
		obj1.scale(2.0, 2.0, 2.0);
		obj1.apply(new Position3D(0.0 , 30.0, 0.0), false);
		OvergroundActor actor = new OvergroundActor(obj1, null);
		universe.place(actor);
				
		// 地面の作成
		IndexedTriangleArray groundGeometry = new IndexedTriangleArray(4, 
				IndexedTriangleArray.COORDINATES | IndexedTriangleArray.NORMALS, 6);
		groundGeometry.setCoordinate(0, new Point3d(-20.0, 0.0, -20.0));
		groundGeometry.setCoordinate(1, new Point3d(20.0, 0.0, -20.0));
		groundGeometry.setCoordinate(2, new Point3d(20.0, 0.0, 20.0));
		groundGeometry.setCoordinate(3, new Point3d(-20.0, 0.0, 20.0));
		groundGeometry.setNormal(0, new Vector3f(0.0f, 1.0f, 0.0f));
		groundGeometry.setNormal(1, new Vector3f(0.0f, 1.0f, 0.0f));
		groundGeometry.setNormal(2, new Vector3f(0.0f, 1.0f, 0.0f));
		groundGeometry.setNormal(3, new Vector3f(0.0f, 1.0f, 0.0f));
		groundGeometry.setCoordinateIndices(0, new int[]{0, 3, 2});
		groundGeometry.setCoordinateIndices(3, new int[]{0, 2, 1});
		Appearance ap2 = new Appearance();
		Material m2 = new Material();
		m2.setDiffuseColor(0.5f, 1.0f, 0.2f);
		ap2.setMaterial(m2);
		Ground ground = new Ground(new BaseObject3D(groundGeometry, ap2));
		universe.place(ground);
		
//		final OvergroundActor oga = new OvergroundActor(obj1, null) {
//			@Override
//			public Force3D getGravity() {
//			   return new Force3D(0.0,0.0,0.0);
//			}
//		};
//		universe.place(oga);
		
		// カメラの設定
		camera.setViewPoint(new Position3D(0.0, 10.0, -20.0));
		camera.addTarget(actor);
		
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
}

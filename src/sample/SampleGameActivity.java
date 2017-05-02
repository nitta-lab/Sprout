package sample;

import java.io.IOException;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MotionEvent;
import framework.animation.Animation3D;
import framework.gameMain.OvergroundActor;
import framework.gameMain.RealTime3DActivity;
import framework.model3D.ModelFactory;
import framework.model3D.Object3D;
import framework.model3D.Position3D;
import framework.physics.Ground;
import framework.physics.Velocity3D;
import java3d.AmbientLight;
import java3d.Appearance;
import java3d.Color3f;
import java3d.DirectionalLight;
import java3d.Material;
import java3d.Vector3f;


public class SampleGameActivity extends RealTime3DActivity {
	private OvergroundActor pocha;
	private Ground stage;
	private boolean isTouched = false;
	private float touchX = 0.0f;
	private float touchY = 0.0f;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//環境光
		AmbientLight amblight = new AmbientLight(new Color3f(1.0f, 1.0f, 1.0f));
		
//		amblight.setInfluencingBounds(new BoundingSphere(new Point3d(), 10000.0));
		universe.placeLight(amblight);

		//平行光源
        DirectionalLight dirlight = new DirectionalLight(
                new Color3f(1.0f, 1.0f, 1.0f),  //光の色
                new Vector3f(0.0f, -1.0f, -0.5f) //光の方向ベクトル
        );
//		dirlight.setInfluencingBounds(new BoundingSphere(new Point3d(), 10000.0));
        universe.placeLight(dirlight);
        
		Appearance ap1 = new Appearance();
		Material m = new Material();
		m.setDiffuseColor(0.0f, 0.3f, 1.0f);
		m.setAmbientColor(0.0f, 0.0f, 0.0f);
		m.setEmissiveColor(0.0f, 0.0f, 0.0f);
		m.setSpecularColor(0.0f, 0.0f, 0.0f);
		m.setShininess(5.0f);
		ap1.setMaterial(m);
		
		Object3D pochaBody = null;
		try {
			pochaBody = ModelFactory.loadModel(getResources(), "pocha.stl", ap1).createObject();
			Animation3D pochaAnimation = null;	//AnimationFactory.loadAnimation("data\\pocha\\walk.wrl");
			pocha = new OvergroundActor(pochaBody, pochaAnimation);
			pocha.setPosition(new Position3D(0.0, -100.0, 250.0));
			universe.place(pocha);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Appearance ap2 = new Appearance();
		Material m2 = new Material();
		m2.setDiffuseColor(0.1f, 0.0f, 0.02f);
		m2.setAmbientColor(0.1f, 0.1f, 0.1f);
		m2.setEmissiveColor(0.0f, 0.0f, 0.0f);
		m2.setSpecularColor(0.2f, 0.2f, 0.2f);
		m2.setShininess(5.0f);
		ap2.setMaterial(m2);
		
		Object3D stageObj = null;
		try {
			stageObj = ModelFactory.loadModel(getResources(), "konan/konan.obj").createObject();
			stage = new Ground(stageObj);
			universe.place(stage);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		camera.setViewPoint(pocha.getPosition().add(0.0, 1.5, 0.0));
		camera.setViewLine(pocha.getDirection());
		camera.setFieldOfView(1.5);
		camera.setBackClipDistance(10000.0);
		start(1000L, 50L, true);
	}

	@Override
	public void progress(long interval) {
		Velocity3D curV = pocha.getVelocity();
		if (isTouched) {
			pocha.rotY(0.1 * (0.5f - touchX) * (double)(interval / 15.0));
			curV.setX(pocha.getDirection().getX() * 200.0 * (0.5f - touchY));
			curV.setZ(pocha.getDirection().getZ() * 200.0 * (0.5f - touchY));
			pocha.setVelocity(curV);						
		} else {
			curV.setX(0.0);
			curV.setZ(0.0);
			pocha.setVelocity(curV);						
		}
		camera.setViewPoint(pocha.getPosition().add(0.0, 15.0, 0.0));
		camera.setViewLine(pocha.getDirection());
	}

	@SuppressLint("NewApi")
	@Override
	public boolean onTouchEvent(MotionEvent event){
		super.onTouchEvent(event);
		
		if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
			isTouched = true;		
			float maxX = event.getDevice().getMotionRange(MotionEvent.AXIS_X).getMax();
			float minX = event.getDevice().getMotionRange(MotionEvent.AXIS_X).getMin();
			float maxY = event.getDevice().getMotionRange(MotionEvent.AXIS_Y).getMax();
			float minY = event.getDevice().getMotionRange(MotionEvent.AXIS_Y).getMin();
			touchX = (event.getX() - minX) / (maxX - minX);
			touchY = (event.getY() - minY) / (maxY - minY);
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			isTouched = false;
		}
		return true;
	}
}

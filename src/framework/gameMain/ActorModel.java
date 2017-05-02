package framework.gameMain;
import android.content.res.Resources;
import framework.physics.Solid3D;


public abstract class ActorModel extends GameModel {

	public ActorModel(Resources res, String fileName) {
		super(res, fileName);
	}

	public Solid3D createBody() {
		return new Solid3D(getModel().createObject());
	}
}

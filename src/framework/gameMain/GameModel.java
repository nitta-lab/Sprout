package framework.gameMain;
import java.io.IOException;

import android.content.res.Resources;
import framework.animation.Animation3D;
import framework.model3D.Model3D;
import framework.model3D.ModelFactory;
import framework.model3D.ModelFileFormatException;

public class GameModel {

	private Model3D model = null;
	private Resources res;
	private String fileName;

	public GameModel(Resources res, String fileName) {
		this.res = res;
		this.fileName = fileName;
	}

	public Animation3D getAnimation() {
		return new Animation3D();
	}
	
	public Model3D getModel() {
		if(model == null && fileName != null) {
			try {
				model = ModelFactory.loadModel(res, fileName);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ModelFileFormatException e) {
				e.printStackTrace();
			}
		}
		return model;
	}
	
	public void clearModel(){
		model = null;
	}
}
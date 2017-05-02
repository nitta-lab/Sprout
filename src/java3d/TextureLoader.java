package java3d;

import java.io.InputStream;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class TextureLoader {
	
	public static final int BY_REFERENCE = 2;
	public static final int Y_UP = 4;
	private InputStream in = null;
	private Resources res = null;
	private int id = 0;
	private Bitmap bitmap;

	public TextureLoader(InputStream in, int flags) {
		this.in = in;
	}
	
	public TextureLoader(Resources res, int id, int flags) {
		this.res = res;
		this.id = id;
	}
	
	public ImageComponent2D getImage() {
		if (in != null) {
			bitmap = BitmapFactory.decodeStream(in);
		} else if (res != null) {
			bitmap = BitmapFactory.decodeResource(res, id);
		}
		return new ImageComponent2D(ImageComponent2D.FORMAT_RGB, bitmap);
	}

	public Texture getTexture() {
		ImageComponent2D ic2 = getImage();
		Texture tex = new Texture2D(Texture.BASE_LEVEL, Texture.RGB, bitmap.getWidth(), bitmap.getHeight());
		tex.setImage(0, ic2);
		return tex;
	}

}

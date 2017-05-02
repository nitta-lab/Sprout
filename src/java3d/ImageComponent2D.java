package java3d;

import android.graphics.Bitmap;

public class ImageComponent2D extends ImageComponent {
	private Bitmap image = null;
	
	public ImageComponent2D(int format, Bitmap image) {
		super(format, image.getWidth(), image.getHeight());
		this.image = image;
	}
	
	public Bitmap getBitmap() {
		return image;
	}

	@Override
	public NodeComponent cloneNodeComponent() {
		ImageComponent2D newOne = new ImageComponent2D(format, image);
		return newOne;
	}

}

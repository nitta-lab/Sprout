package java3d;

public abstract class Texture extends NodeComponent {
	public static final int BASE_LEVEL = 1;
	public static final int RGB = 5;
	public static final int RGBA = 6;
	  
	protected ImageComponent[] imageComponents = null;
	protected int mipmapMode;
	protected int format;
	protected int width;
	protected int hight;
	
	public Texture(int mipmapMode, int format, int width, int hight) {
		this.mipmapMode = mipmapMode;
		this.format = format;
		this.width = width;
		this.hight = hight;
	}

	public void setImage(int n, ImageComponent image) {
		imageComponents[n] = image;
	}
	
	public ImageComponent getImage(int n) {
		return imageComponents[n];
	}
	
	public void setImages(ImageComponent[] images) {
		imageComponents = images;
	}
	
	public ImageComponent[] getImages() {
		return imageComponents;
	}
}

package java3d;

public abstract class ImageComponent extends NodeComponent {
	public static final int FORMAT_RGB = 1;
	public static final int FORMAT_RGBA = 2;
	
	protected int format = 0;
	protected int width = 0;
	protected int height = 0;
	
	public ImageComponent(int format, int width, int height) {
		this.format = format;
		this.width = width;
		this.height = height;
	}
}

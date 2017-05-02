package java3d;

public class Texture2D extends Texture {

	public Texture2D(int mipmapMode, int format, int width, int hight) {
		super(mipmapMode, format, width, hight);
		imageComponents = new ImageComponent[1];
	}

	@Override
	public NodeComponent cloneNodeComponent() {
		Texture2D tex2D = new Texture2D(mipmapMode, format, width, hight);
		tex2D.imageComponents = imageComponents.clone();
		return tex2D;
	}
}

package java3d;

public class TextureCubeMap extends Texture {
	  public static final int POSITIVE_X = 0;
	  public static final int NEGATIVE_X = 1;
	  public static final int POSITIVE_Y = 2;
	  public static final int NEGATIVE_Y = 3;
	  public static final int POSITIVE_Z = 4;
	  public static final int NEGATIVE_Z = 5;
	  
	public TextureCubeMap(int mipmapMode, int format, int width) {
		super(mipmapMode, format, width, 0);
		imageComponents = new ImageComponent[6];
	}
            	
	  
	public void setImage(int level, int face, ImageComponent2D image)	{
		imageComponents[face] = image;
		width = image.getBitmap().getWidth();
	}

	@Override
	public NodeComponent cloneNodeComponent() {
		TextureCubeMap texCubeMap = new TextureCubeMap(mipmapMode, format, width);
		texCubeMap.imageComponents = imageComponents.clone();
		return texCubeMap;
	}

}

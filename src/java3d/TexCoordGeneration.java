package java3d;

public class TexCoordGeneration extends NodeComponent {
	public static final int OBJECT_LINEAR = 0;
	public static final int EYE_LINEAR = 1;
	public static final int SPHERE_MAP = 2;
	public static final int NORMAL_MAP = 3;
	public static final int REFLECTION_MAP = 4;
	public static final int TEXTURE_COORDINATE_2 = 0;
	public static final int TEXTURE_COORDINATE_3 = 1;
	public static final int TEXTURE_COORDINATE_4 = 2;
	
	private int genMode;
	private int format;

	public TexCoordGeneration(int genMode, int format)  {
		this.genMode = genMode;
		this.format = format;
	}
	
	public int getGenMode() {
		return genMode;
	}
	
	public void setGenMode(int genMode) {
		this.genMode = genMode;
	}
	
	public int getFormat() {
		return format;
	}
	
	public void setFormat(int format) {
		this.format = format;
	}

	@Override
	public NodeComponent cloneNodeComponent() {
		return new TexCoordGeneration(genMode, format);
	}

}

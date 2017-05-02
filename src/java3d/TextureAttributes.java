package java3d;

public class TextureAttributes extends NodeComponent {
	public static final int FASTEST = 0;
	public static final int NICEST = 1;
	public static final int MODULATE = 2;
	public static final int DECAL = 3;
	public static final int BLEND = 4;
	public static final int REPLACE = 5;
//	public static final int COMBINE = 6;
//	public static final int COMBINE_REPLACE = 0;
//	public static final int COMBINE_MODULATE = 1;
//	public static final int COMBINE_ADD = 2;
//	public static final int COMBINE_ADD_SIGNED = 3;
//	public static final int COMBINE_SUBTRACT = 4;
//	public static final int COMBINE_INTERPOLATE = 5;
//	public static final int COMBINE_DOT3 = 6;
//	public static final int COMBINE_OBJECT_COLOR = 0;
//	public static final int COMBINE_TEXTURE_COLOR = 1;
//	public static final int COMBINE_CONSTANT_COLOR = 2;
//	public static final int COMBINE_PREVIOUS_TEXTURE_UNIT_STATE = 3;
//	public static final int COMBINE_SRC_COLOR = 0;
//	public static final int COMBINE_ONE_MINUS_SRC_COLOR = 1;
//	public static final int COMBINE_SRC_ALPHA = 2;
//	public static final int COMBINE_ONE_MINUS_SRC_ALPHA = 3;
	
	private int textureMode;
	private Transform3D transform;
	private Color4f textureBlendColor;
	private int perspCorrectionMode;
	
	public TextureAttributes() {
		textureMode = TextureAttributes.REPLACE;
		textureBlendColor = new Color4f();
		perspCorrectionMode = TextureAttributes.NICEST;
	}

	public TextureAttributes(int textureMode, Transform3D transform, Color4f textureBlendColor, int perspCorrectionMode) {
		this.textureMode = textureMode;
		this.transform = transform;
		this.textureBlendColor = textureBlendColor;
		this.perspCorrectionMode = perspCorrectionMode;
	}
	
	public int getTextureMode() {
		return textureMode;
	}
	
	public void setTextureMode(int textureMode) {
		this.textureMode = textureMode;
	}	

	public void setTextureTransform(Transform3D transform) {
		this.transform = transform;
	}
	
	public int getPerspectiveCorrectionMode() {
		return perspCorrectionMode;
	}
	
	public void setPerspectiveCorrectionMode(int perspCorrectionMode) {
		this.perspCorrectionMode = perspCorrectionMode;
	}

	@Override
	public NodeComponent cloneNodeComponent() {
		return new TextureAttributes(textureMode, new Transform3D(transform), textureBlendColor.clone(), perspCorrectionMode);
	}

}

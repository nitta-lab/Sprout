package java3d;

public class Appearance extends NodeComponent {
	private Material material = null;
	private Texture texture = null;
	private TextureAttributes textureAttribute = null;
	private TexCoordGeneration texCoordGeneration = null;
	private TextureUnitState[] stateArray;

	public void setMaterial(Material m) {
		this.material = m;
	}

	public Material getMaterial() {
		return material;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}

	public Texture getTexture() {
		return texture;
	}
	
	public int getTextureUnitCount() {
		if (stateArray == null) return 0;
		return stateArray.length;
	}

	public TextureUnitState getTextureUnitState(int textureUnit) {
		if (stateArray == null) return null;
		return stateArray[textureUnit];
	}
	
	public void setTextureUnitState(TextureUnitState[] stateArray) {
		this.stateArray = stateArray;
	}

	public void setTextureUnitState(int textureUnit, TextureUnitState textureUnitState) {
		stateArray[textureUnit] = textureUnitState;
	}

	public TextureAttributes getTextureAttributes() {
		return textureAttribute;
	}

	public void setTextureAttributes(TextureAttributes textureAttribute) {
		this.textureAttribute = textureAttribute;
	}
	
	public TexCoordGeneration getTexCoordGeneration() {
		return texCoordGeneration;
	}
	
	public void setTexCoordGeneration(TexCoordGeneration texCoordGeneration) {
		this.texCoordGeneration = texCoordGeneration;
	}

	@Override
	public NodeComponent cloneNodeComponent() {
		Appearance ap = new Appearance();
		if (material != null) {
			ap.material = (Material)material.cloneNodeComponent();
		}
		if (texture != null) {
			ap.texture = (Texture)texture.cloneNodeComponent();
		}
		if (textureAttribute != null) {
			ap.textureAttribute = (TextureAttributes)textureAttribute.cloneNodeComponent();
		}
		if (texCoordGeneration != null) {
			ap.texCoordGeneration  = (TexCoordGeneration)texCoordGeneration.cloneNodeComponent();
		}
		return ap;
	}
}

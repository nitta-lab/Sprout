package java3d;

public class TextureUnitState extends NodeComponent {
	private Texture texture;
	private TextureAttributes textureAttribute;
	private TexCoordGeneration texCoordGeneration;
	
	public TextureUnitState() {
		this(null, null ,null);
	}
	
	public TextureUnitState(Texture texture, TextureAttributes textureAttribute, TexCoordGeneration texCoordGeneration) {
		this.texture = texture;
		this.textureAttribute = textureAttribute;
		this.texCoordGeneration= texCoordGeneration;
	}

	public Texture getTexture() {
		return texture;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
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
		return new TextureUnitState((Texture)texture.cloneNodeComponent(), 
				(TextureAttributes)textureAttribute.cloneNodeComponent(), 
				(TexCoordGeneration)texCoordGeneration.cloneNodeComponent());
	}
}

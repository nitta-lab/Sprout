package java3d;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Material extends NodeComponent {
	float diffuseR = 1.0f; 
	float diffuseG = 1.0f; 
	float diffuseB = 1.0f; 
	float ambientR = 0.2f; 
	float ambientG = 0.2f; 
	float ambientB = 0.2f; 
	float specularR = 1.0f; 
	float specularG = 1.0f; 
	float specularB = 1.0f; 
	float emissiveR = 0.0f; 
	float emissiveG = 0.0f; 
	float emissiveB = 0.0f; 
	float shininess = 64.0f; 
	float[] diffuse = null;
	float[] ambient = null;
	float[] specular = null;
	float[] emissive = null;
	
	public Material() {
		setDiffuseColor(1.0f, 1.0f, 1.0f);
		setAmbientColor(0.2f, 0.2f, 0.2f);
		setSpecularColor(1.0f, 1.0f, 1.0f);
		setEmissiveColor(0.0f, 0.0f, 0.0f);
		setShininess(64.0f);
	}

	public void setDiffuseColor(float r, float g, float b) {
		this.diffuseR = r;
		this.diffuseG = g;
		this.diffuseB = b;
		diffuse = new float[]{r, g ,b, 1.0f};
	}

	public void setAmbientColor(float r, float g, float b) {
		this.ambientR = r;
		this.ambientG = g;
		this.ambientB = b;
		ambient = new float[]{r, g ,b, 1.0f};
	}

	public void setSpecularColor(float r, float g, float b) {
		this.specularR = r;
		this.specularG = g;
		this.specularB = b;
		specular = new float[]{r, g ,b, 1.0f};
	}

	public void setEmissiveColor(float r, float g, float b) {
		this.emissiveR = r;
		this.emissiveG = g;
		this.emissiveB = b;
		emissive = new float[]{r, g ,b, 1.0f};
	}

	public void setShininess(float shininess) {
		this.shininess = shininess;
	}

	@Override
	public NodeComponent cloneNodeComponent() {
		Material m = new Material();
		m.setDiffuseColor(diffuseR, diffuseG, diffuseB);
		m.setAmbientColor(ambientR, ambientG, ambientB);
		m.setSpecularColor(specularR, specularG, specularB);
		m.setEmissiveColor(emissiveR, emissiveG, emissiveB);
		m.setShininess(shininess);
		return m;
	}

}

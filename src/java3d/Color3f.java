package java3d;

public class Color3f {
	public float r;
	public float g;
	public float b;
	
	// �R���X�g���N�^
	public Color3f() {
		r = 0.0f;
		g = 0.0f;
		b = 0.0f;
	}
	
	// �R���X�g���N�^
	public Color3f(float pr, float pg, float pb) {
		r = pr;
		g = pg;
		b = pb;
	}
	
	public Color3f clone() {
		return new Color3f(r, g, b);
	}

}

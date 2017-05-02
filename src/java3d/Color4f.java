package java3d;

public class Color4f {
	public float r;
	public float g;
	public float b;
	public float a;

	// コンストラクタ
	public Color4f() {
		r = 0.0f;
		g = 0.0f;
		b = 0.0f;
		a = 0.0f;
	}

	// コンストラクタ
	public Color4f(float pr, float pg, float pb, float pa) {
		r = pr;
		g = pg;
		b = pb;
		a = pa;
	}
	
	public Color4f clone() {
		return new Color4f(r, g, b, a);
	}

}

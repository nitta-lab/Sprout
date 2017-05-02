package java3d;

public class Vector2f {
	public float x;
	public float y;

	// �R���X�g���N�^
	public Vector2f() {
		x = 0.0f;
		y = 0.0f;
	}

	// �R���X�g���N�^
	public Vector2f(float px, float py) {
		x = px;
		y = py;
	}

	public Vector2f(Vector2f v) {
		x = v.x;
		y = v.y;
	}
	
	public Vector2f clone() {
		return new Vector2f(this.x, this.y);
	}
	
	public double dot(Vector2f a) {
		return x*a.x + y*a.y;
	}
	
	public float length() {
		return (float)Math.sqrt(x*x + y*y);
	}
	
	public void scale(float s) {;
		x = x*s;
		y = y*s;
	}
	
	public void normalize() {
		float l = length();
		x = x / l;
		y = y / l;
	}

	/** v1��v2�̘a���i�[���� */
	public void add(Vector2f v1, Vector2f v2) {
		this.x = v1.x + v2.x;
		this.y = v1.y + v2.y;
	}
	
	/** v1�Ƃ̘a���i�[���� */
	public void add(Vector2f v1) {
		this.x += v1.x;
		this.y += v1.y;
	}

	/** v1��v2�̍����i�[���� */
	public void sub(Vector2f v1, Vector2f v2) {
		this.x = v1.x - v2.x;
		this.y = v1.y - v2.y;
	}
	
	/** v1�Ƃ̍����i�[���� */
	public void sub(Vector2f v1) {
		this.x -= v1.x;
		this.y -= v1.y;
	}
	
	/** Vector3d���Z�b�g���� */
	public void set(Vector2f v) {
		this.x = v.x;
		this.y = v.y;
	}

	/** �����𔽓]���� */
	public void negate() {
		this.x = -x;
		this.y = -y;
	}

	/** this = d * axis + p */
	public void scaleAdd(float d, Vector2f axis, Vector2f p) {
		this.x = d * axis.x + p.x;
		this.y = d * axis.y + p.y;
	}

	public void setX(float x) {
		this.x = x;
	}
	
	public void setY(float y) {
		this.y = y;
	}

	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
}

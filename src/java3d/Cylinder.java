package java3d;

public class Cylinder extends Primitive {
	public static final int BODY = 0;
	public static final int TOP = 1;
	public static final int BOTTOM = 2;

	static final int MID_REZ_DIV_X = 16;
	static final int MID_REZ_DIV_Y = 1;

	float radius;
	float height;
	int xdivisions;
	int ydivisions;

	private Shape3D bodyShape = null;
	private Shape3D topShape = null;
	private Shape3D bottomShape = null;

	public Cylinder() {
		this(0.5f, 1.0f, null);
	}
	
	public Cylinder(float r, float h) {
		this(r, h, null);
	}

	public Cylinder(float r, float h, Appearance ap) {
		xdivisions = MID_REZ_DIV_X;
		ydivisions = MID_REZ_DIV_Y;
		
		float coordinates[][] = new float[xdivisions * 2][3];

		float coordinates_center[][] = new float[2][3];//��ʂƒ�ʂ̒��S�̍��W
		coordinates_center[0][0] = 0;
		coordinates_center[0][1] = h / 2;
		coordinates_center[0][2] = 0;
		coordinates_center[1][0] = 0;
		coordinates_center[1][1] = -h / 2;
		coordinates_center[1][2] = 0;
		
		for (int i = 0; i < xdivisions; i++) { // TOP(���)���̓_��(x,y,z)���W
			coordinates[i][0] = r * (float) Math.cos(2 * Math.PI * ((double)i / xdivisions));
			coordinates[i][1] = h / 2;
			coordinates[i][2] = r * (float) Math.sin(2 * Math.PI * ((double)i / xdivisions));
		}
		for (int i = 0; i < xdivisions; i++) { // BOTTOM(���)�̓_��(x,y,z)���W
			coordinates[i + xdivisions][0] = r * (float) Math.cos(2 * Math.PI * ((double)i / xdivisions));
			coordinates[i + xdivisions][1] = -h / 2;
			coordinates[i + xdivisions][2] = r * (float) Math.sin(2 * Math.PI * ((double)i / xdivisions));
		}

		float uv[] = { 0, 0, 1, 0, 1, 1, 0, 1, };

		// �e�ʂ̃W�I���g�����쐬
		IndexedTriangleStripArray bodyGeom = new IndexedTriangleStripArray(xdivisions * 2,
				IndexedTriangleStripArray.COORDINATES | IndexedTriangleStripArray.NORMALS | IndexedTriangleStripArray.TEXTURE_COORDINATE_2,
				xdivisions * 2 + 2,
				new int[] { xdivisions * 2 + 2 });
		TriangleFanArray topGeom = new TriangleFanArray(xdivisions + 2,
				TriangleFanArray.COORDINATES | TriangleFanArray.NORMALS | TriangleFanArray.TEXTURE_COORDINATE_2,
				new int[] { xdivisions + 2 });
		TriangleFanArray bottomGeom = new TriangleFanArray(xdivisions + 2,
				TriangleFanArray.COORDINATES | TriangleFanArray.NORMALS | TriangleFanArray.TEXTURE_COORDINATE_2,
				new int[] { xdivisions + 2 });

		// ���_���W�̐ݒ�
			//����
		for (int i = 0; i < xdivisions; i++) {	//��ʂ����ʂ֐����ɍ~��āA��ʂ����ʂ֎΂߂ɏ�鏇�ɍ��W��ݒ�
			bodyGeom.setCoordinate(2 * i    , coordinates[i]);
			bodyGeom.setCoordinate(2 * i + 1, coordinates[i + xdivisions]);
		}

		int coordinateIndices[] = new int[xdivisions * 2 + 2];
		for (int i = 0; i < xdivisions * 2; i++) {
				coordinateIndices[i] = i;
		}
		coordinateIndices[xdivisions * 2] = 0;
		coordinateIndices[xdivisions * 2 + 1] = 1;		
		
		bodyGeom.setCoordinateIndices(0, coordinateIndices);
		
			//���
		topGeom.setCoordinate(0,coordinates_center[0]);
		for(int i = 0; i < xdivisions; i++){
			topGeom.setCoordinate(i + 1, coordinates[i]);					//�ォ�猩�Ĕ����v���̏�
		}
		topGeom.setCoordinate(xdivisions + 1, coordinates[0]);

			//���
		bottomGeom.setCoordinate(0,coordinates_center[1]);
		bottomGeom.setCoordinate(1, coordinates[xdivisions]);
		for(int i = 0; i < xdivisions; i++){	
			bottomGeom.setCoordinate(i + 2, coordinates[2 * xdivisions - i - 1]);	//�ォ�猩�Ď��v���̏�
		}

		 // �e�N�X�`�����W�̐ݒ�
		 bodyGeom.setTextureCoordinates(0, uv);	//
		 topGeom.setTextureCoordinates(0, uv);
		 bottomGeom.setTextureCoordinates(0, uv);
		
		 // �@���̐ݒ�
		 
		for (int i = 0; i < xdivisions * 2; i++) {
			 float[] bodyNorm = new float[]{(float) Math.cos(Math.PI * ((double)i / xdivisions)),0.0f,(float) Math.sin(Math.PI * ((double)i / xdivisions))};
			 bodyGeom.setNormal(i, bodyNorm);
		}
		
		float[] topNorm = new float[]{0.0f, 1.0f, 0.0f};
		for (int i = 0; i < xdivisions + 2; i++) {
			 topGeom.setNormal(i, topNorm);
		}
		
		float[] bottomNorm = new float[]{0.0f, -1.0f, 0.0f};
		for (int i = 0; i < xdivisions + 2; i++) {
			 bottomGeom.setNormal(i, bottomNorm);
		}
		
		// �\�ʑ����̍쐬
		if (ap == null) {
			ap = new Appearance();
		}
		setAppearance(ap);
//		Appearance ap1 = new Appearance();
//		Appearance ap2 = new Appearance();
//		Appearance ap3 = new Appearance();
		// Appearance ap4 = new Appearance();
		// Appearance ap5 = new Appearance();
		// Appearance ap6 = new Appearance();
//		Texture tex = ap.getTexture();
//		if (tex != null && tex instanceof TextureCubeMap) {
		// GL10 �ł� GL_TEXTURE_CUBE_MAP ���g���Ȃ��̂ŁATextureCubeMap �̏ꍇ�� Texture2D
		// �ɕ�������
//		ImageComponent ic1 = tex.getImage(TextureCubeMap.POSITIVE_Z);
//		Texture2D tex1 = new Texture2D(Texture2D.BASE_LEVEL, Texture2D.RGB,
//		ic1.width, ic1.height);
//		tex1.setImage(0, ic1);
//		ap1.setTexture(tex1);
//		ImageComponent ic2 = tex.getImage(TextureCubeMap.NEGATIVE_Z);
//		Texture2D tex2 = new Texture2D(Texture2D.BASE_LEVEL, Texture2D.RGB,
//		ic2.width, ic2.height);
//		tex2.setImage(0, ic2);
//		ap2.setTexture(tex2);
//		ImageComponent ic3 = tex.getImage(TextureCubeMap.POSITIVE_X);
//		Texture2D tex3 = new Texture2D(Texture2D.BASE_LEVEL, Texture2D.RGB,
//		ic3.width, ic3.height);
//		tex3.setImage(0, ic3);
		// ap3.setTexture(tex3);
		// ImageComponent ic4 = tex.getImage(TextureCubeMap.NEGATIVE_X);
		// Texture2D tex4 = new Texture2D(Texture2D.BASE_LEVEL, Texture2D.RGB,
		// ic4.width, ic4.height);
		// tex4.setImage(0, ic4);
		// ap4.setTexture(tex4);
		// ImageComponent ic5 = tex.getImage(TextureCubeMap.POSITIVE_Y);
		// Texture2D tex5 = new Texture2D(Texture2D.BASE_LEVEL, Texture2D.RGB,
		// ic5.width, ic5.height);
		// tex5.setImage(0, ic5);
		// ap5.setTexture(tex5);
		// ImageComponent ic6 = tex.getImage(TextureCubeMap.NEGATIVE_Y);
		// Texture2D tex6 = new Texture2D(Texture2D.BASE_LEVEL, Texture2D.RGB,
		// ic6.width, ic6.height);
		// tex6.setImage(0, ic6);
		// ap6.setTexture(tex6);
//		}
		// �e�ʂ̍쐬			//�S�ē����e�N�X�`���[(ap)��\��t����
		bodyShape = new Shape3D(bodyGeom, ap);
		topShape = new Shape3D(topGeom, ap);
		bottomShape = new Shape3D(bottomGeom, ap);
		//
		radius = r;
		height = h;
	}
	
	public double getRadius() {
		return radius;
	}
	public double getHeight() {
		return height;
	}


	@Override
	public Shape3D getShape(int partid) {
		switch (partid) {
		case BODY:
			return bodyShape;
		case TOP:
			return topShape;
		case BOTTOM:
			return bottomShape;
		}
		return null;
	}
	
	public Node cloneTree() {
		Appearance ap = getAppearance();
		if (ap != null) {
			ap = (Appearance)ap.cloneNodeComponent();
		}		
		Cylinder c = new Cylinder(radius, height, ap);
	    return c;
	}
}
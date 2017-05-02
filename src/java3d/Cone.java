package java3d;

public class Cone extends Primitive {
	public static final int BODY = 0;
	public static final int CAP = 1;
	
	static final int MID_REZ_DIV_X = 16;
	static final int MID_REZ_DIV_Y = 1;
	
	float radius;
	float height;
	int xdivisions;
	int ydivisions;
	

	private Shape3D bodyShape = null;
	private Shape3D capShape = null;

	public Cone() {
		this(0.5f, 1.0f, null);
	}

	public Cone(float r, float h, Appearance ap) {
		xdivisions = MID_REZ_DIV_X;
		ydivisions = MID_REZ_DIV_Y;
			
		float coordinates[][] = new float[xdivisions][3];
	
		for (int i = 0; i < xdivisions; i++) { // BOTTOM(底面)の点の(x,y,z)座標
			coordinates[i][0] = r * (float) Math.cos(2 * Math.PI * ((double)i / xdivisions));
			coordinates[i][1] = -h / 2;
			coordinates[i][2] = r * (float) Math.sin(2 * Math.PI * ((double)i / xdivisions));
		}
			
		float coordinates_center[][] = new float[2][3];//頂点と,底面の中心,の座標
		coordinates_center[0][0] = 0;
		coordinates_center[0][1] = h / 2;
		coordinates_center[0][2] = 0;
		coordinates_center[1][0] = 0;
		coordinates_center[1][1] = -h / 2;
		coordinates_center[1][2] = 0;

		float uv[] = { 0, 0, 1, 0, 1, 1, 0, 1, };

		// 各面のジオメトリを作成
		TriangleFanArray bodyGeom = new TriangleFanArray(xdivisions + 2,
				TriangleFanArray.COORDINATES | TriangleFanArray.NORMALS | TriangleFanArray.TEXTURE_COORDINATE_2,
				new int[] { xdivisions + 2 });
		TriangleFanArray capGeom = new TriangleFanArray(xdivisions + 2,
				TriangleFanArray.COORDINATES | TriangleFanArray.NORMALS | TriangleFanArray.TEXTURE_COORDINATE_2,
				new int[] { xdivisions + 2 });

		// 頂点座標の設定	
			//側面
		bodyGeom.setCoordinate(0,coordinates_center[0]);
		for (int i = 0; i < xdivisions; i++){
			bodyGeom.setCoordinate(i + 1, coordinates[i]);					//上から見て反時計回りの順
		}
		bodyGeom.setCoordinate(xdivisions + 1, coordinates[0]);
			//底面
		capGeom.setCoordinate(0,coordinates_center[1]);
		capGeom.setCoordinate(1, coordinates[0]);
		for (int i = 0; i < xdivisions; i++){	
			capGeom.setCoordinate(i + 2, coordinates[xdivisions - i - 1]);	//上から見て時計回りの順
		}

		 // テクスチャ座標の設定
		 bodyGeom.setTextureCoordinates(0, uv);	//
		 capGeom.setTextureCoordinates(0, uv);

		 // 法線の設定
		 double theta;
		 theta = Math.atan(h / r);
		 for (int i = 0; i < xdivisions; i++) {
			 float[] bodyNorm = new float[]{
					 (float)(r * Math.sin(theta) * Math.cos(90 - theta) * Math.cos(2 * Math.PI * ((double)i / xdivisions))),
					 (float)(r * Math.sin(theta) * Math.sin(90 - theta)),
					 (float) Math.sin(2 * Math.PI * ((double)i / xdivisions))
			 };
			 bodyGeom.setNormal(i, bodyNorm);
		}

		float[] bottomNorm = new float[]{0.0f, -1.0f, 0.0f};
		for (int i = 0; i < xdivisions + 2; i++) {
			capGeom.setNormal(i, bottomNorm);
		}
			
		// 表面属性の作成
		if (ap == null) {
			ap = new Appearance();
		}
		setAppearance(ap);

		// 各面の作成			//全て同じテクスチャー(ap)を貼り付ける
		bodyShape = new Shape3D(bodyGeom, ap);
		capShape = new Shape3D(capGeom, ap);

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
		case CAP:
			return capShape;
		}
		return null;
	}
	
	public Node cloneTree() {
		Appearance ap = getAppearance();
		if (ap != null) {
			ap = (Appearance)ap.cloneNodeComponent();
		}		
		Cone c = new Cone(radius, height, ap);
	    return c;
	}
}


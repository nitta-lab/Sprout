package java3d;

public class Box extends Primitive {
	public static final int FRONT = 0;
	public static final int BACK = 1;
	public static final int RIGHT = 2;
	public static final int LEFT = 3;
	public static final int TOP = 4;
	public static final int BOTTOM = 5;	
	
	float xDim = 1.0f;
	float yDim = 1.0f;
	float zDim = 1.0f;
	
	private Shape3D frontShape = null;
	private Shape3D backShape = null;
	private Shape3D rightShape = null;
	private Shape3D leftShape = null;
	private Shape3D topShape = null;
	private Shape3D bottomShape = null;
	
	public Box() {
		this(1.0f, 1.0f, 1.0f, null);
	}
	
	public Box(float x, float y, float z, Appearance ap) {
	    float coordinates[][] = {
	  		{-x, -y,  z},
	  		{ x, -y,  z},
	  		{ x,  y,  z},
			{-x,  y,  z},
			{-x, -y, -z},
			{ x, -y, -z},
			{ x,  y, -z},
			{-x,  y, -z}
	    };
	    
		float uv[] = {
			0, 0, 1, 0, 1, 1 , 0, 1,
		};	
		
		// 各面のジオメトリを作成
		TriangleFanArray frontGeom = new TriangleFanArray(4, 
				TriangleFanArray.COORDINATES | TriangleFanArray.NORMALS | TriangleFanArray.TEXTURE_COORDINATE_2, 
				new int[]{4});
		TriangleFanArray backGeom = new TriangleFanArray(4, 
				TriangleFanArray.COORDINATES | TriangleFanArray.NORMALS | TriangleFanArray.TEXTURE_COORDINATE_2, 
				new int[]{4});
		TriangleFanArray rightGeom = new TriangleFanArray(4, 
				TriangleFanArray.COORDINATES | TriangleFanArray.NORMALS | TriangleFanArray.TEXTURE_COORDINATE_2, 
				new int[]{4});
		TriangleFanArray leftGeom = new TriangleFanArray(4, 
				TriangleFanArray.COORDINATES | TriangleFanArray.NORMALS | TriangleFanArray.TEXTURE_COORDINATE_2, 
				new int[]{4});
		TriangleFanArray topGeom = new TriangleFanArray(4, 
				TriangleFanArray.COORDINATES | TriangleFanArray.NORMALS | TriangleFanArray.TEXTURE_COORDINATE_2, 
				new int[]{4});
		TriangleFanArray bottomGeom = new TriangleFanArray(4, 
				TriangleFanArray.COORDINATES | TriangleFanArray.NORMALS | TriangleFanArray.TEXTURE_COORDINATE_2, 
				new int[]{4});		
		
		// 頂点座標の設定
		frontGeom.setCoordinate(0, coordinates[0]);
		frontGeom.setCoordinate(1, coordinates[1]);
		frontGeom.setCoordinate(2, coordinates[2]);
		frontGeom.setCoordinate(3, coordinates[3]);
		
		backGeom.setCoordinate(0, coordinates[5]);
		backGeom.setCoordinate(1, coordinates[4]);
		backGeom.setCoordinate(2, coordinates[7]);
		backGeom.setCoordinate(3, coordinates[6]);
		
		rightGeom.setCoordinate(0, coordinates[1]);
		rightGeom.setCoordinate(1, coordinates[5]);
		rightGeom.setCoordinate(2, coordinates[6]);
		rightGeom.setCoordinate(3, coordinates[2]);
		
		leftGeom.setCoordinate(0, coordinates[4]);
		leftGeom.setCoordinate(1, coordinates[0]);
		leftGeom.setCoordinate(2, coordinates[3]);
		leftGeom.setCoordinate(3, coordinates[7]);
		
		topGeom.setCoordinate(0, coordinates[3]);
		topGeom.setCoordinate(1, coordinates[2]);
		topGeom.setCoordinate(2, coordinates[6]);
		topGeom.setCoordinate(3, coordinates[7]);
		
		bottomGeom.setCoordinate(0, coordinates[4]);
		bottomGeom.setCoordinate(1, coordinates[5]);
		bottomGeom.setCoordinate(2, coordinates[1]);
		bottomGeom.setCoordinate(3, coordinates[0]);
		
		// テクスチャ座標の設定
		frontGeom.setTextureCoordinates(0, uv);
		backGeom.setTextureCoordinates(0, uv);
		rightGeom.setTextureCoordinates(0, uv);
		leftGeom.setTextureCoordinates(0, uv);
		topGeom.setTextureCoordinates(0, uv);
		bottomGeom.setTextureCoordinates(0, uv);
				
		// 法線の設定
		float[] frontNorm = new float[]{0.0f, 0.0f, 1.0f};
		frontGeom.setNormal(0, frontNorm);
		frontGeom.setNormal(1, frontNorm);
		frontGeom.setNormal(2, frontNorm);
		frontGeom.setNormal(3, frontNorm);
		
		float[] backNorm = new float[]{0.0f, 0.0f, -1.0f};
		backGeom.setNormal(0, backNorm);
		backGeom.setNormal(1, backNorm);
		backGeom.setNormal(2, backNorm);
		backGeom.setNormal(3, backNorm);
		
		float[] rightNorm = new float[]{1.0f, 0.0f, 0.0f};
		rightGeom.setNormal(0, rightNorm);
		rightGeom.setNormal(1, rightNorm);
		rightGeom.setNormal(2, rightNorm);
		rightGeom.setNormal(3, rightNorm);

		float[] leftNorm = new float[]{-1.0f, 0.0f, 0.0f};
		leftGeom.setNormal(0, leftNorm);
		leftGeom.setNormal(1, leftNorm);
		leftGeom.setNormal(2, leftNorm);
		leftGeom.setNormal(3, leftNorm);

		float[] topNorm = new float[]{0.0f, 1.0f, 0.0f};
		topGeom.setNormal(0, topNorm);
		topGeom.setNormal(1, topNorm);
		topGeom.setNormal(2, topNorm);
		topGeom.setNormal(3, topNorm);

		float[] bottomNorm = new float[]{0.0f, -1.0f, 0.0f};
		bottomGeom.setNormal(0, bottomNorm);
		bottomGeom.setNormal(1, bottomNorm);
		bottomGeom.setNormal(2, bottomNorm);
		bottomGeom.setNormal(3, bottomNorm);

		// 表面属性の作成
		if (ap == null) {
			ap = new Appearance();
		}
	    setAppearance(ap);
	    Appearance ap1;
	    Appearance ap2;
	    Appearance ap3;
	    Appearance ap4;
	    Appearance ap5;
	    Appearance ap6;
	    Texture tex = ap.getTexture();
	    if (tex != null && tex instanceof TextureCubeMap) {
	    	// GL10 では GL_TEXTURE_CUBE_MAP が使えないので、TextureCubeMap の場合は Texture2D に分解する
		    ap1 = (Appearance)ap.cloneNodeComponent();
		    ap2 = (Appearance)ap.cloneNodeComponent();
		    ap3 = (Appearance)ap.cloneNodeComponent();
		    ap4 = (Appearance)ap.cloneNodeComponent();
		    ap5 = (Appearance)ap.cloneNodeComponent();
		    ap6 = (Appearance)ap.cloneNodeComponent();
	    	ImageComponent ic1 = tex.getImage(TextureCubeMap.POSITIVE_Z);
	    	Texture2D tex1 = new Texture2D(Texture2D.BASE_LEVEL, Texture2D.RGB, ic1.width, ic1.height);
	    	tex1.setImage(0, ic1);
	    	ap1.setTexture(tex1);
	    	ImageComponent ic2 = tex.getImage(TextureCubeMap.NEGATIVE_Z);
	    	Texture2D tex2 = new Texture2D(Texture2D.BASE_LEVEL, Texture2D.RGB, ic2.width, ic2.height);
	    	tex2.setImage(0, ic2);
	    	ap2.setTexture(tex2);
	    	ImageComponent ic3 = tex.getImage(TextureCubeMap.POSITIVE_X);
	    	Texture2D tex3 = new Texture2D(Texture2D.BASE_LEVEL, Texture2D.RGB, ic3.width, ic3.height);
	    	tex3.setImage(0, ic3);
	    	ap3.setTexture(tex3);
	    	ImageComponent ic4 = tex.getImage(TextureCubeMap.NEGATIVE_X);
	    	Texture2D tex4 = new Texture2D(Texture2D.BASE_LEVEL, Texture2D.RGB, ic4.width, ic4.height);
	    	tex4.setImage(0, ic4);
	    	ap4.setTexture(tex4);
	    	ImageComponent ic5 = tex.getImage(TextureCubeMap.POSITIVE_Y);
	    	Texture2D tex5 = new Texture2D(Texture2D.BASE_LEVEL, Texture2D.RGB, ic5.width, ic5.height);
	    	tex5.setImage(0, ic5);
	    	ap5.setTexture(tex5);
	    	ImageComponent ic6 = tex.getImage(TextureCubeMap.NEGATIVE_Y);
	    	Texture2D tex6 = new Texture2D(Texture2D.BASE_LEVEL, Texture2D.RGB, ic6.width, ic6.height);
	    	tex6.setImage(0, ic6);
	    	ap6.setTexture(tex6);
	    } else {
	    	ap1 = ap2 = ap3 = ap4 = ap5 = ap6 = ap;
	    }

		// 各面の作成
		frontShape = new Shape3D(frontGeom, ap1);
		backShape = new Shape3D(backGeom, ap2);
		rightShape = new Shape3D(rightGeom, ap3);
		leftShape = new Shape3D(leftGeom, ap4);
		topShape = new Shape3D(topGeom, ap5);
		bottomShape = new Shape3D(bottomGeom, ap6);
			    
	    xDim = x;
	    yDim = y;
	    zDim = z;
	}

	public double getXdimension() {
		return xDim;
	}

	public double getYdimension() {
		return yDim;
	}

	public double getZdimension() {
		return zDim;
	}

	@Override
	public Shape3D getShape(int partid) {
		switch (partid) {
		case FRONT:
			return frontShape;
		case BACK:
			return backShape;
		case RIGHT:
			return rightShape;
		case LEFT:
			return leftShape;
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
		Box b = new Box(xDim, yDim, zDim, ap);
	    return b;
	}
}

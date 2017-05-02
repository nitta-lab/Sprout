package java3d;


public class BoundingPolytope extends Bounds {
	Vector4d[] planes;
	double[] mag; // magnitude of plane vector
	double[] pDotN; // point on plane dotted with normal
	Point3d[] verts; // vertices of polytope
	int nVerts; // number of verts in polytope
	Point3d centroid = new Point3d(); // centroid of polytope
	Point3d boxVerts[];
	boolean allocBoxVerts = false;

	/**
	 * Constructs a BoundingPolytope using the specified planes.
	 * 
	 * @param planes
	 *            a set of planes defining the polytope.
	 * @exception IllegalArgumentException
	 *                if the length of the specified array of planes is less
	 *                than 4.
	 */
	public BoundingPolytope(Vector4d[] planes) {
		boundId = BOUNDING_POLYTOPE;
		int i;
		double invMag;
		this.planes = new Vector4d[planes.length];
		mag = new double[planes.length];
		pDotN = new double[planes.length];
		for (i = 0; i < planes.length; i++) {
			// normalize the plane normals
			mag[i] = Math.sqrt(planes[i].x * planes[i].x + planes[i].y
					* planes[i].y + planes[i].z * planes[i].z);
			invMag = 1.0 / mag[i];
			this.planes[i] = new Vector4d(planes[i].x * invMag, planes[i].y
					* invMag, planes[i].z * invMag, planes[i].w * invMag);
		}
		computeAllVerts(); // XXXX: lazy evaluate
	}

	/**
	 * Constructs a BoundingPolytope and initializes it to a set of 6 planes
	 * that defines a cube such that -1 <= x,y,z <= 1. The values of the planes
	 * are as follows:
	 * <ul>
	 * planes[0] : x <= 1 (1,0,0,-1)<br>
	 * planes[1] : -x <= 1 (-1,0,0,-1)<br>
	 * planes[2] : y <= 1 (0,1,0,-1)<br>
	 * planes[3] : -y <= 1 (0,-1,0,-1)<br>
	 * planes[4] : z <= 1 (0,0,1,-1)<br>
	 * planes[5] : -z <= 1 (0,0,-1,-1)<br>
	 * </ul>
	 */
	public BoundingPolytope() {
		boundId = BOUNDING_POLYTOPE;
		planes = new Vector4d[6];
		mag = new double[planes.length];
		pDotN = new double[planes.length];
		planes[0] = new Vector4d(1.0, 0.0, 0.0, -1.0);
		planes[1] = new Vector4d(-1.0, 0.0, 0.0, -1.0);
		planes[2] = new Vector4d(0.0, 1.0, 0.0, -1.0);
		planes[3] = new Vector4d(0.0, -1.0, 0.0, -1.0);
		planes[4] = new Vector4d(0.0, 0.0, 1.0, -1.0);
		planes[5] = new Vector4d(0.0, 0.0, -1.0, -1.0);
		mag[0] = 1.0;
		mag[1] = 1.0;
		mag[2] = 1.0;
		mag[3] = 1.0;
		mag[4] = 1.0;
		mag[5] = 1.0;
		computeAllVerts(); // XXXX: lazy evaluate
	}
	
	public void setPlanes(Vector4d[] planes) {
		int i;
		double invMag;
		this.planes = new Vector4d[planes.length];
		pDotN = new double[planes.length];
		mag = new double[planes.length];
		if (planes.length <= 0) {
			computeAllVerts(); // XXXX: lazy evaluate
			return;
		}
		for (i = 0; i < planes.length; i++) {
			// normalize the plane normals
			mag[i] = Math.sqrt(planes[i].x * planes[i].x + planes[i].y
					* planes[i].y + planes[i].z * planes[i].z);
			invMag = 1.0 / mag[i];
			this.planes[i] = new Vector4d(planes[i].x * invMag, planes[i].y
					* invMag, planes[i].z * invMag, planes[i].w * invMag);
		}
		computeAllVerts(); // XXXX: lazy evaluate
	}

	/**
	 * Returns the equations of the bounding planes for this bounding polytope.
	 * The equations are copied into the specified array. The array must be
	 * large enough to hold all of the vectors. The individual array elements
	 * must be allocated by the caller.
	 * 
	 * @param planes
	 *            an array Vector4d to receive the bounding planes
	 */
	public void getPlanes(Vector4d[] planes) {
		int i;
		for (i = 0; i < planes.length; i++) {
			planes[i].x = this.planes[i].x * mag[i];
			planes[i].y = this.planes[i].y * mag[i];
			planes[i].z = this.planes[i].z * mag[i];
			planes[i].w = this.planes[i].w * mag[i];
		}
	}

	public int getNumPlanes() {
		return planes.length;
	}
	
	/**
	* Sets the planes for this BoundingPolytope by keeping its current
	* number and position of planes and computing new planes positions
	* to enclose the given bounds object.
	* @param boundsObject another bounds object
	*/
	public void set(Bounds  boundsObject) {
		int i,k;
		double dis;
		
		// no polytope exists yet so initialize one using the boundsObject
		if( boundsObject == null )  {
			boundsIsEmpty = true;
			boundsIsInfinite = false;
			computeAllVerts(); // XXXX: lazy evaluate
		}else if( boundsObject.boundId == BOUNDING_SPHERE ) {
			BoundingSphere sphere = (BoundingSphere)boundsObject;
			if( boundsIsEmpty) {
				initEmptyPolytope();  // no ptope exist so must initialize to default
				computeAllVerts();
			}
			for(i=0;i<planes.length;i++) { // D = -(N dot C + radius)
				planes[i].w = -(sphere.center.x*planes[i].x +
				sphere.center.y*planes[i].y +
				sphere.center.z*planes[i].z + sphere.radius);
			}
			boundsIsEmpty = boundsObject.boundsIsEmpty;
			boundsIsInfinite = boundsObject.boundsIsInfinite;
			computeAllVerts(); // XXXX: lazy evaluate
		} else if( boundsObject.boundId == BOUNDING_BOX){
			BoundingBox box = (BoundingBox)boundsObject;
			double ux,uy,uz,lx,ly,lz,newD;
			if( boundsIsEmpty) {
				initEmptyPolytope();  // no ptope exist so must initialize to default
				computeAllVerts();
			}
			for(i=0;i<planes.length;i++) {
				ux = box.upper.x*planes[i].x; 
				uy = box.upper.y*planes[i].y;
				uz = box.upper.z*planes[i].z;
				lx = box.lower.x*planes[i].x; 
				ly = box.lower.y*planes[i].y;
				lz = box.lower.z*planes[i].z;
				planes[i].w = -(ux + uy + uz ); // initalize plane to upper vert
				if( (newD = ux + uy + lz ) + planes[i].w > 0.0) planes[i].w = -newD;
				if( (newD = ux + ly + uz ) + planes[i].w > 0.0) planes[i].w = -newD;
				if( (newD = ux + ly + lz ) + planes[i].w > 0.0) planes[i].w = -newD;
				if( (newD = lx + uy + uz ) + planes[i].w > 0.0) planes[i].w = -newD;
				if( (newD = lx + uy + lz ) + planes[i].w > 0.0) planes[i].w = -newD;
				if( (newD = lx + ly + uz ) + planes[i].w > 0.0) planes[i].w = -newD;
				if( (newD = lx + ly + lz ) + planes[i].w > 0.0) planes[i].w = -newD;
			}
			boundsIsEmpty = boundsObject.boundsIsEmpty;
			boundsIsInfinite = boundsObject.boundsIsInfinite;
			computeAllVerts(); // XXXX: lazy evaluate
		} else if(boundsObject.boundId == BOUNDING_POLYTOPE) {
			BoundingPolytope polytope = (BoundingPolytope)boundsObject;
			if( planes.length != polytope.planes.length) {
				planes = new Vector4d[polytope.planes.length];
				for(k=0;k<polytope.planes.length;k++) planes[k] = new Vector4d();
				mag    = new double[polytope.planes.length];
				pDotN  = new double[polytope.planes.length];
			}
			for(i=0;i<polytope.planes.length;i++) {
				planes[i].x = polytope.planes[i].x;
				planes[i].y = polytope.planes[i].y;
				planes[i].z = polytope.planes[i].z;
				planes[i].w = polytope.planes[i].w;
				mag[i] = polytope.mag[i];
			}
			nVerts = polytope.nVerts;
			verts  = new Point3d[nVerts];
			for (k=0; k<nVerts; k++) {
				verts[k] = new Point3d(polytope.verts[k]);
			}
			boundsIsEmpty = boundsObject.boundsIsEmpty;
			boundsIsInfinite = boundsObject.boundsIsInfinite;
		} else {
		}
	}
	
	/**
	 * Creates a copy of a polytope.
	 * 
	 * @return a new BoundingPolytope
	 */
	@Override
	public Object clone() {
		return new BoundingPolytope(planes);
	}

	/**
	 * Combines this bounding polytope with a bounding object so that the
	 * resulting bounding polytope encloses the original bounding polytope and
	 * the given bounds object.
	 * 
	 * @param boundsObject
	 *            another bounds object
	 */
	@Override
	public void combine(Bounds boundsObject) {
		BoundingSphere sphere;
		if((boundsObject == null) || (boundsObject.boundsIsEmpty)
			|| (boundsIsInfinite))
			return;
		if((boundsIsEmpty) || (boundsObject.boundsIsInfinite)) {
			this.set(boundsObject);
			return;
		}
		boundsIsEmpty = boundsObject.boundsIsEmpty;
		boundsIsInfinite = boundsObject.boundsIsInfinite;
		if( boundsObject.boundId == BOUNDING_SPHERE ) {
			sphere = (BoundingSphere)boundsObject;
			int i;
			double dis;
			for(i = 0; i < planes.length; i++){
				dis = sphere.radius+ sphere.center.x*planes[i].x +
				sphere.center.y*planes[i].y + sphere.center.z *
				planes[i].z + planes[i].w;
				if( dis > 0.0 ) {
					planes[i].w += -dis;
				}
			}
		} else if( boundsObject  instanceof BoundingBox){
			BoundingBox b = (BoundingBox)boundsObject;
			if( !allocBoxVerts){
				boxVerts = new Point3d[8];
				for(int j=0;j<8;j++)boxVerts[j] = new Point3d();
				allocBoxVerts = true;
			}
			boxVerts[0].set(b.lower.x, b.lower.y, b.lower.z );
			boxVerts[1].set(b.lower.x, b.upper.y, b.lower.z );
			boxVerts[2].set(b.upper.x, b.lower.y, b.lower.z );
			boxVerts[3].set(b.upper.x, b.upper.y, b.lower.z );
			boxVerts[4].set(b.lower.x, b.lower.y, b.upper.z );
			boxVerts[5].set(b.lower.x, b.upper.y, b.upper.z );
			boxVerts[6].set(b.upper.x, b.lower.y, b.upper.z );
			boxVerts[7].set(b.upper.x, b.upper.y, b.upper.z );
			this.combine(boxVerts);
		 
		} else if(boundsObject.boundId == BOUNDING_POLYTOPE) {
			BoundingPolytope polytope = (BoundingPolytope)boundsObject;
			this.combine(polytope.verts);
		}   else {
		}
		 
		computeAllVerts();	
	}

	/**
	* Combines this bounding polytope with a point.
	* @param point a 3d point in space
	*/
	public void combine(Point3d point) {
		int i;
		double dis;
		 
		if(boundsIsInfinite) {
			return;
		}
		 
		if( boundsIsEmpty ){
			planes = new Vector4d[6];
			mag = new double[planes.length];
			pDotN  = new double[planes.length];
			nVerts = 1;
			verts = new Point3d[nVerts];
			verts[0] = new Point3d( point.x, point.y, point.z);
			 
			for(i=0;i<planes.length;i++) {
				pDotN[i] =  0.0;
			}
			planes[0] = new Vector4d( 1.0, 0.0, 0.0, -point.x );
			planes[1] = new Vector4d(-1.0, 0.0, 0.0,  point.x );
			planes[2] = new Vector4d( 0.0, 1.0, 0.0, -point.y );
			planes[3] = new Vector4d( 0.0,-1.0, 0.0,  point.y );
			planes[4] = new Vector4d( 0.0, 0.0, 1.0, -point.z );
			planes[5] = new Vector4d( 0.0, 0.0,-1.0,  point.z );
			mag[0] = 1.0;   
			mag[1] = 1.0;
			mag[2] = 1.0;
			mag[3] = 1.0;
			mag[4] = 1.0;
			mag[5] = 1.0;
			centroid.x = point.x;
			centroid.y = point.y;
			centroid.z = point.z;
			boundsIsEmpty = false;
			boundsIsInfinite = false;
		} else {
		 
			for(i = 0; i < planes.length; i++){
				dis = point.x*planes[i].x + point.y*planes[i].y + point.z*
				planes[i].z + planes[i].w;
				if( dis > 0.0 ) {
					planes[i].w += -dis;
				}
			}
			computeAllVerts();
		}
	} 
	 
	/**
	* Combines this bounding polytope with an array of points.
	* @param points an array of 3d points in space
	*/ 
	public void combine(Point3d[] points) {
		int i,j;
		double dis;
		 
		if( boundsIsInfinite) {
			return;
		}
		 
		if( boundsIsEmpty ){
			planes = new Vector4d[6];
			mag = new double[planes.length];
			pDotN  = new double[planes.length];
			nVerts = points.length;
			verts = new Point3d[nVerts];
			verts[0] = new Point3d( points[0].x, points[0].y, points[0].z);
			 
			for(i=0;i<planes.length;i++) {
				pDotN[i] =  0.0;
			}
			planes[0] = new Vector4d( 1.0, 0.0, 0.0, -points[0].x );
			planes[1] = new Vector4d(-1.0, 0.0, 0.0,  points[0].x );
			planes[2] = new Vector4d( 0.0, 1.0, 0.0, -points[0].y );
			planes[3] = new Vector4d( 0.0,-1.0, 0.0,  points[0].y );
			planes[4] = new Vector4d( 0.0, 0.0, 1.0, -points[0].z );
			planes[5] = new Vector4d( 0.0, 0.0,-1.0,  points[0].z );
			mag[0] = 1.0;   
			mag[1] = 1.0;
			mag[2] = 1.0;
			mag[3] = 1.0;
			mag[4] = 1.0;
			mag[5] = 1.0;
			centroid.x = points[0].x;
			centroid.y = points[0].y;
			centroid.z = points[0].z;
			boundsIsEmpty = false;
			boundsIsInfinite = false;
		}
		 
		for(j = 0; j < points.length; j++){
			for(i = 0; i < planes.length; i++){
				dis = points[j].x*planes[i].x + points[j].y*planes[i].y +
				points[j].z*planes[i].z + planes[i].w;
				if( dis > 0.0 ) {
					planes[i].w += -dis;
				}
			}
		}
		 
		computeAllVerts();
	}
	
	/**
	 * Transforms this bounding polytope by the given transformation matrix.
	 * 
	 * @param matrix
	 *            a transformation matrix
	 */
	@Override
	public void transform(Transform3D matrix) {
		int i;
		double invMag;
		Transform3D invTrans = new Transform3D(matrix);
		invTrans.invert();
		invTrans.transpose();
		for (i = 0; i < planes.length; i++) {
			planes[i].x = planes[i].x * mag[i];
			planes[i].y = planes[i].y * mag[i];
			planes[i].z = planes[i].z * mag[i];
			planes[i].w = planes[i].w * mag[i];
			invTrans.transform(planes[i]);
		}
		for (i = 0; i < planes.length; i++) {
			// normalize the plane normals
			mag[i] = Math.sqrt(planes[i].x * planes[i].x + planes[i].y
					* planes[i].y + planes[i].z * planes[i].z);
			invMag = 1.0 / mag[i];
			this.planes[i] = new Vector4d(planes[i].x * invMag, planes[i].y
					* invMag, planes[i].z * invMag, planes[i].w * invMag);
		}
		for (i = 0; i < verts.length; i++) {
			matrix.transform(verts[i]);
		}
	}

	/**
	* Test for intersection with another bounds object.
	* @param boundsObject another bounds object
	* @return true or false indicating if an intersection occured
	*/ 
	public boolean intersect(Bounds boundsObject) {
	 
		if( boundsObject == null ) {
			return false;
		}
		 
		if( boundsIsEmpty || boundsObject.boundsIsEmpty ) {
			return false;
		}
		 
		if( boundsIsInfinite || boundsObject.boundsIsInfinite ) {
			return true;
		}
		 
		if( boundsObject.boundId == BOUNDING_SPHERE ) {
			return intersect_ptope_sphere( this, (BoundingSphere)boundsObject);
		} else if( boundsObject.boundId == BOUNDING_BOX){
			return intersect_ptope_abox( this, (BoundingBox)boundsObject);
		} else if(boundsObject.boundId == BOUNDING_POLYTOPE) {
			return intersect_ptope_ptope( this, (BoundingPolytope)boundsObject);
		} else {
			return false;
		}
	}
	
	private void computeVertex(int a, int b, int c) {
		double det, x, y, z;
		det = planes[a].x * planes[b].y * planes[c].z + planes[a].y
				* planes[b].z * planes[c].x + planes[a].z * planes[b].x
				* planes[c].y - planes[a].z * planes[b].y * planes[c].x
				- planes[a].y * planes[b].x * planes[c].z - planes[a].x
				* planes[b].z * planes[c].y;
		// System.err.println("\n det="+det);
		if (det * det < EPSILON) {
			// System.err.println("parallel planes="+a+" "+b+" "+c);
			return; // two planes are parallel
		}
		det = 1.0 / det;
		x = (planes[b].y * planes[c].z - planes[b].z * planes[c].y) * pDotN[a];
		y = (planes[b].z * planes[c].x - planes[b].x * planes[c].z) * pDotN[a];
		z = (planes[b].x * planes[c].y - planes[b].y * planes[c].x) * pDotN[a];
		x += (planes[c].y * planes[a].z - planes[c].z * planes[a].y) * pDotN[b];
		y += (planes[c].z * planes[a].x - planes[c].x * planes[a].z) * pDotN[b];
		z += (planes[c].x * planes[a].y - planes[c].y * planes[a].x) * pDotN[b];
		x += (planes[a].y * planes[b].z - planes[a].z * planes[b].y) * pDotN[c];
		y += (planes[a].z * planes[b].x - planes[a].x * planes[b].z) * pDotN[c];
		z += (planes[a].x * planes[b].y - planes[a].y * planes[b].x) * pDotN[c];
		x = x * det;
		y = y * det;
		z = z * det;
		if (pointInPolytope(x, y, z)) {
			if (nVerts >= verts.length) {
				Point3d newVerts[] = new Point3d[nVerts << 1];
				for (int i = 0; i < nVerts; i++) {
					newVerts[i] = verts[i];
				}
				verts = newVerts;
			}
			verts[nVerts++] = new Point3d(x, y, z);
		}
	}

	private void computeAllVerts() {
		int i, a, b, c;
		double x, y, z;
		nVerts = 0;
		verts = new Point3d[planes.length * planes.length];
		for (i = 0; i < planes.length; i++) {
			pDotN[i] = -planes[i].x * planes[i].w * planes[i].x - planes[i].y
					* planes[i].w * planes[i].y - planes[i].z * planes[i].w
					* planes[i].z;
		}
		for (a = 0; a < planes.length - 2; a++) {
			for (b = a + 1; b < planes.length - 1; b++) {
				for (c = b + 1; c < planes.length; c++) {
					computeVertex(a, b, c);
				}
			}
		}
		// XXXX: correctly compute centroid
		x = y = z = 0.0;
		Point3d newVerts[] = new Point3d[nVerts];
		for (i = 0; i < nVerts; i++) {
			x += verts[i].x;
			y += verts[i].y;
			z += verts[i].z;
			// copy the verts into an array of the correct size
			newVerts[i] = verts[i];
		}
		this.verts = newVerts; // copy the verts into an array of the correct
								// size
		centroid.x = x / nVerts;
		centroid.y = y / nVerts;
		centroid.z = z / nVerts;
	}

	private boolean pointInPolytope(double x, double y, double z) {
		for (int i = 0; i < planes.length; i++) {
			if ((x * planes[i].x + y * planes[i].y + z * planes[i].z + planes[i].w) > EPSILON) {
				return false;
			}
		}
		return true;
	}

	private void checkBoundsIsEmpty() {
		boundsIsEmpty = (planes.length < 4);
	}
	
	private void initEmptyPolytope() {
		planes = new Vector4d[6];
		pDotN  = new double[6];
		mag    = new double[6];
		verts  = new Point3d[planes.length*planes.length];
		nVerts = 0;
		 
		planes[0] = new Vector4d( 1.0, 0.0, 0.0, -1.0 );
		planes[1] = new Vector4d(-1.0, 0.0, 0.0, -1.0 );
		planes[2] = new Vector4d( 0.0, 1.0, 0.0, -1.0 );
		planes[3] = new Vector4d( 0.0,-1.0, 0.0, -1.0 );
		planes[4] = new Vector4d( 0.0, 0.0, 1.0, -1.0 );
		planes[5] = new Vector4d( 0.0, 0.0,-1.0, -1.0 );
		mag[0] = 1.0;
		mag[1] = 1.0;
		mag[2] = 1.0;
		mag[3] = 1.0;
		mag[4] = 1.0;
		mag[5] = 1.0;
		checkBoundsIsEmpty();
	}
}

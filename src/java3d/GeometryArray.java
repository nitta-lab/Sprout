package java3d;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

public abstract class GeometryArray extends Geometry {
//	public static final int ALLOW_COLOR_READ = 2;
//	public static final int ALLOW_COLOR_WRITE = 3;
//	public static final int ALLOW_COORDINATE_READ = 0;
//	public static final int ALLOW_COORDINATE_WRITE = 1;
//	public static final int ALLOW_COUNT_READ = 8;
//	public static final int ALLOW_COUNT_WRITE = 20;
//	public static final int ALLOW_FORMAT_READ = 17;
//	public static final int ALLOW_NORMAL_READ = 4;
//	public static final int ALLOW_NORMAL_WRITE = 5;
//	public static final int ALLOW_REF_DATA_READ = 21;
//	public static final int ALLOW_REF_DATA_WRITE = 19;
//	public static final int ALLOW_TEXCOORD_READ = 6;
//	public static final int ALLOW_TEXCOORD_WRITE = 7;
	
	public static final int BY_REFERENCE = 128;
	public static final int COLOR_3 = 4;
	public static final int COLOR_4 = 12;
	public static final int COORDINATES = 1;
	public static final int INTERLEAVED = 256;
	public static final int NORMALS = 2;
	public static final int TEXTURE_COORDINATE_2 = 32;
	public static final int TEXTURE_COORDINATE_3 = 64;
	public static final int TEXTURE_COORDINATE_4 = 1024;
	public static final int USE_COORD_INDEX_ONLY = 512;
	public static final int USE_NIO_BUFFER = 2048;
	
	protected int vertexCount;
	protected int vertexFormat;
	protected FloatBuffer vertexBuffer = null;
	protected FloatBuffer normalBuffer = null;
	protected FloatBuffer uvBuffer = null;

	// コンストラクタ
	public GeometryArray(int vertexCount, int vertexFormat) {
		this.vertexCount = vertexCount;
		this.vertexFormat = vertexFormat;
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertexCount * 4 * 3);
		vbb.order(ByteOrder.nativeOrder());
		vertexBuffer = vbb.asFloatBuffer();
		if ((vertexFormat & NORMALS) != 0) {
			ByteBuffer vbb2 = ByteBuffer.allocateDirect(vertexCount * 4 * 3);
			vbb2.order(ByteOrder.nativeOrder());
			normalBuffer = vbb2.asFloatBuffer();
		}
		if ((vertexFormat & TEXTURE_COORDINATE_2) != 0) {
			ByteBuffer vbb3 = ByteBuffer.allocateDirect(vertexCount * 4 * 2);
			vbb3.order(ByteOrder.nativeOrder());
			uvBuffer = vbb3.asFloatBuffer();
		} else if ((vertexFormat & TEXTURE_COORDINATE_3) != 0) {
			ByteBuffer vbb3 = ByteBuffer.allocateDirect(vertexCount * 4 * 3);
			vbb3.order(ByteOrder.nativeOrder());
			uvBuffer = vbb3.asFloatBuffer();
		} else if ((vertexFormat & TEXTURE_COORDINATE_4) != 0) {
			ByteBuffer vbb3 = ByteBuffer.allocateDirect(vertexCount * 4 * 4);
			vbb3.order(ByteOrder.nativeOrder());
			uvBuffer = vbb3.asFloatBuffer();
		}
//		mVertexBuffer.put(vertices);
//		mVertexBuffer.position(0);
	}
	
//	public GeometryArray(int vertexCount, int vertexFormat, int texCoordSetCount, int[] texCoordSetMap) {
//		this.vertexCount = vertexCount;
//		this.vertexFormat = vertexFormat;
//		this.texCoordSetCount = texCoordSetCount;
//		this.texCoordSetMap = texCoordSetMap;
//	}
	
	/** オブジェクトの頂点数 */
	public int getVertexCount() {
		return vertexCount;
	}
	
	public int getVertexFormat() {
		return vertexFormat;
	}
	
	/** indexの頂点の座標をpに新しく格納する */
	public void getCoordinate(int index, float[] p) {
		vertexBuffer.position(index * 3);
		vertexBuffer.get(p);
		vertexBuffer.position(0);
	}

	/** indexの頂点の座標をpに新しく格納する */
	public void getCoordinate(int index, double[] p) {
		p[0] = vertexBuffer.get(index * 3);
		p[1] = vertexBuffer.get(index * 3 + 1);
		p[2] = vertexBuffer.get(index * 3 + 2);
	}

	/** indexの頂点の座標をpに新しく格納する */
	public void getCoordinate(int index, Point3d p) {
		p.x = vertexBuffer.get(index * 3);
		p.y = vertexBuffer.get(index * 3 + 1);
		p.z = vertexBuffer.get(index * 3 + 2);
	}
	
	public void getCoordinates(int index, double[] coordinates) {
		for (int n = 0; n < coordinates.length / 3 && n + index < vertexBuffer.capacity() / 3; n++) {
			coordinates[n * 3] = vertexBuffer.get((index + n) * 3);
			coordinates[n * 3 + 1] = vertexBuffer.get((index + n) * 3 + 1);
			coordinates[n * 3 + 2] = vertexBuffer.get((index + n) * 3 + 2);
		}
	}

	/** indexの頂点の法線をpに新しく格納する */
	public void getNormal(int index, float[] p) {
		if (normalBuffer == null) return;
		normalBuffer .position(index * 3);
		normalBuffer.get(p);
		normalBuffer .position(0);
	}

	/** indexの頂点の法線をpに新しく格納する */
	public void getNormal(int index, Vector3f p) {
		if (normalBuffer == null) return;
		p.x = normalBuffer.get(index * 3);
		p.y = normalBuffer.get(index * 3 + 1);
		p.z = normalBuffer.get(index * 3 + 2);	
	}
	
	public void setCoordinate(int index, float[] p) {
		vertexBuffer.position(index * 3);
		vertexBuffer.put(p);
		vertexBuffer.position(0);
	}
	
	public void setCoordinate(int index, double[] p) {
		vertexBuffer.position(index * 3);
		vertexBuffer.put((float)p[0]);
		vertexBuffer.put((float)p[1]);
		vertexBuffer.put((float)p[2]);
		vertexBuffer.position(0);
	}
	
	public void setCoordinate(int index, Point3d p) {
		vertexBuffer.position(index * 3);
		vertexBuffer.put((float)p.x);
		vertexBuffer.put((float)p.y);
		vertexBuffer.put((float)p.z);
		vertexBuffer.position(0);
	}
	
	public void setCoordinates(int index, float[] p) {
		vertexBuffer.position(index * 3);
		vertexBuffer.put(p);
		vertexBuffer.position(0);
	}
	
	public void setCoordinates(int index, double[] p) {
		vertexBuffer.position(index * 3);
		for (int i = 0; i < p.length; i++) {
			vertexBuffer.put((float)p[i]);
		}
		vertexBuffer.position(0);
	}
	
	public void setNormal(int index, float[] n) {
		if (normalBuffer == null) return;
		normalBuffer .position(index * 3);
		normalBuffer.put(n);
		normalBuffer.position(0);
	}
	
	public void setNormal(int index, Vector3f n) {
		if (normalBuffer == null) return;
		normalBuffer.position(index * 3);
		normalBuffer.put(n.x);
		normalBuffer.put(n.y);
		normalBuffer.put(n.z);
		normalBuffer.position(0);
	}
	
	public void setTextureCoordinate(int index, float[] texCoord) {
		if ((vertexFormat & TEXTURE_COORDINATE_2) != 0) {
			uvBuffer.position(index * 2);
			uvBuffer.put(texCoord[0]);
			uvBuffer.put(texCoord[1]);
		} else if ((vertexFormat & TEXTURE_COORDINATE_3) != 0) {
			uvBuffer.position(index * 3);
			uvBuffer.put(texCoord[0]);
			uvBuffer.put(texCoord[1]);
			uvBuffer.put(texCoord[2]);
		} else if ((vertexFormat & TEXTURE_COORDINATE_4) != 0) {
			uvBuffer.position(index * 4);
			uvBuffer.put(texCoord[0]);
			uvBuffer.put(texCoord[1]);
			uvBuffer.put(texCoord[2]);
			uvBuffer.put(texCoord[3]);
		}		
		uvBuffer.position(0);
	}
	
	public void setTextureCoordinates(int index, float[] texCoords) {
		if ((vertexFormat & TEXTURE_COORDINATE_2) != 0) {
			ByteBuffer vbb3 = ByteBuffer.allocateDirect((texCoords.length + index) * 4 * 2);
			vbb3.order(ByteOrder.nativeOrder());
			uvBuffer = vbb3.asFloatBuffer();
			uvBuffer.position(index * 2);
		} else if ((vertexFormat & TEXTURE_COORDINATE_3) != 0) {
			ByteBuffer vbb3 = ByteBuffer.allocateDirect((texCoords.length + index) * 4 * 3);
			vbb3.order(ByteOrder.nativeOrder());
			uvBuffer = vbb3.asFloatBuffer();
			uvBuffer.position(index * 3);
		} else if ((vertexFormat & TEXTURE_COORDINATE_4) != 0) {
			ByteBuffer vbb3 = ByteBuffer.allocateDirect((texCoords.length + index) * 4 * 4);
			vbb3.order(ByteOrder.nativeOrder());
			uvBuffer = vbb3.asFloatBuffer();
			uvBuffer.position(index * 4);
		}		
		uvBuffer.put(texCoords);
		uvBuffer.position(0);
	}
	
	public FloatBuffer getVertexBuffer() {
		return vertexBuffer;
	}
	
	public FloatBuffer getNormalBuffer() {
		return normalBuffer;
	}
	
	public FloatBuffer getUVBuffer() {
		return uvBuffer;
	}
}

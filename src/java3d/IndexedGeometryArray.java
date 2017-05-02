package java3d;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;

public abstract class IndexedGeometryArray extends GeometryArray {

	protected int indexCount;
	protected ShortBuffer coordinateIndexBuffer = null;
	protected ShortBuffer texCoordinateIndexBuffer = null;
	protected ShortBuffer normalIndexBuffer = null;

	// コンストラクタ
	public IndexedGeometryArray(int vertexCount, int vertexFormat, int indexCount) {
		super(vertexCount, vertexFormat);
		this.indexCount = indexCount;
		ByteBuffer vbb = ByteBuffer.allocateDirect(indexCount * 2);
		vbb.order(ByteOrder.nativeOrder());
		coordinateIndexBuffer = vbb.asShortBuffer();
		if ((vertexFormat & TEXTURE_COORDINATE_2) != 0 
				|| (vertexFormat & TEXTURE_COORDINATE_3) != 0 
				|| (vertexFormat & TEXTURE_COORDINATE_4) != 0) {
			ByteBuffer vbb2 = ByteBuffer.allocateDirect(indexCount * 2);
			vbb2.order(ByteOrder.nativeOrder());
			texCoordinateIndexBuffer = vbb2.asShortBuffer();
		}
		if ((vertexFormat & NORMALS) != 0) {
			ByteBuffer vbb3 = ByteBuffer.allocateDirect(indexCount * 2);
			vbb3.order(ByteOrder.nativeOrder());
			normalIndexBuffer = vbb3.asShortBuffer();
		}
	}

	/** オブジェクトのインデックス数を取得する */
	public int getIndexCount() {
		return indexCount;
	}

	public int getCoordinateIndex(int index) {
		return coordinateIndexBuffer.get(index);
	}
	
	public void getCoordinateIndices(int index, int[] coordinateIndicies) {
		for (int i = 0; i < coordinateIndicies.length && index + i < coordinateIndexBuffer.limit(); i++) {
			coordinateIndicies[i] = coordinateIndexBuffer.get(index + i);
		}
	}

	public void setCoordinateIndex(int index, int coordinateIndex) {
		coordinateIndexBuffer.put(index, (short)coordinateIndex);
	}

	public void setCoordinateIndices(int index, int[] coordinateIndicies) {
		coordinateIndexBuffer.position(index);
		for (int i = 0; i < coordinateIndicies.length; i++) {
			coordinateIndexBuffer.put((short)coordinateIndicies[i]);
		}
		coordinateIndexBuffer.position(0);
	}

	public int getTextureCoordinateIndex(int index) {
		return texCoordinateIndexBuffer.get(index);
	}

	public void setTextureCoordinateIndex(int index, int texCoordIndex) {
		coordinateIndexBuffer.put(index, (short)texCoordIndex);
	}
	
	public int getNormalIndex(int index) {
		return normalIndexBuffer.get(index);
	}

	public void setNormalIndex(int index, int normalIndex) {
		normalIndexBuffer.put(index, (short)normalIndex);
	}
	
	public ShortBuffer getCoordinateIndexBuffer() {
		return coordinateIndexBuffer;
	}
	
	public ShortBuffer getTextureCoordinateIndexBuffer() {
		return texCoordinateIndexBuffer;
	}
	
	public ShortBuffer getNormalIndexBuffer() {
		return normalIndexBuffer;
	}
}

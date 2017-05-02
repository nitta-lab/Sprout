package java3d;

public class IndexedTriangleStripArray extends IndexedGeometryStripArray {
	public IndexedTriangleStripArray(int vertexCount, int vertexFormat, int indexCount, int[] stripIndexCounts) {
		super(vertexCount, vertexFormat, indexCount, stripIndexCounts);
	}

	@Override
	public NodeComponent cloneNodeComponent() {
		IndexedTriangleStripArray newOne = new IndexedTriangleStripArray(vertexCount, vertexFormat, indexCount, (int [])stripIndexCounts.clone());
		newOne.vertexBuffer = vertexBuffer.duplicate();
		if (normalBuffer != null) newOne.normalBuffer = normalBuffer.duplicate();
		if (uvBuffer != null) newOne.uvBuffer = uvBuffer.duplicate();
		newOne.indexCount = indexCount;
		newOne.coordinateIndexBuffer = coordinateIndexBuffer.duplicate();
		if (texCoordinateIndexBuffer != null) newOne.texCoordinateIndexBuffer = texCoordinateIndexBuffer.duplicate();
		if (normalIndexBuffer != null) newOne.normalIndexBuffer = normalIndexBuffer.duplicate();
		return newOne;
	}
}

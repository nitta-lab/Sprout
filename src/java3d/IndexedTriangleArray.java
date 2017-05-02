package java3d;

public class IndexedTriangleArray extends IndexedGeometryArray {
	public IndexedTriangleArray(int vertexCount, int vertexFormat, int indexCount) {
		super(vertexCount, vertexFormat, indexCount);
	}

	@Override
	public NodeComponent cloneNodeComponent() {
		IndexedTriangleArray newOne = new IndexedTriangleArray(vertexCount, vertexFormat, indexCount);
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

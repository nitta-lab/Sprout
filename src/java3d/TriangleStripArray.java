package java3d;

public class TriangleStripArray extends GeometryStripArray {

	public TriangleStripArray(int vertexCount, int vertexFormat, int[] stripIndexCounts) {
		super(vertexCount, vertexFormat, stripIndexCounts);
	}

	@Override
	public NodeComponent cloneNodeComponent() {
		TriangleStripArray newOne = new TriangleStripArray(vertexCount, vertexFormat, (int [])stripIndexCounts.clone());
		newOne.vertexBuffer = vertexBuffer.duplicate();
		if (normalBuffer != null) newOne.normalBuffer = normalBuffer.duplicate();
		if (uvBuffer != null) newOne.uvBuffer = uvBuffer.duplicate();
		return newOne;
	}

}

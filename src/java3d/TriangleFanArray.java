package java3d;

public class TriangleFanArray extends GeometryStripArray {

	public TriangleFanArray(int vertexCount, int vertexFormat, int[] stripIndexCounts) {
		super(vertexCount, vertexFormat, stripIndexCounts);
	}

	@Override
	public NodeComponent cloneNodeComponent() {
		TriangleFanArray newOne = new TriangleFanArray(vertexCount, vertexFormat, (int [])stripIndexCounts.clone());
		newOne.vertexBuffer = vertexBuffer.duplicate();
		if (normalBuffer != null) newOne.normalBuffer = normalBuffer.duplicate();
		if (uvBuffer != null) newOne.uvBuffer = uvBuffer.duplicate();
		return newOne;
	}

}

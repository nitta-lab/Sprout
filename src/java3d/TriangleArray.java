package java3d;

public class TriangleArray extends GeometryArray {

	public TriangleArray(int vertexCount, int vertexFormat) {
		super(vertexCount, vertexFormat);
	}

	@Override
	public NodeComponent cloneNodeComponent() {
		TriangleArray newOne = new TriangleArray(vertexCount, vertexFormat);
		newOne.vertexBuffer = vertexBuffer.duplicate();
		if (normalBuffer != null) newOne.normalBuffer = normalBuffer.duplicate();
		if (uvBuffer != null) newOne.uvBuffer = uvBuffer.duplicate();
		return newOne;
	}

}

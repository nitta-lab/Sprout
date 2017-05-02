package java3d;

public abstract class GeometryStripArray extends GeometryArray {
	protected int[] stripIndexCounts;

	public GeometryStripArray(int vertexCount, int vertexFormat, int[] stripIndexCounts) {
		super(vertexCount, vertexFormat);
		this.stripIndexCounts = stripIndexCounts;
	}

	public int getNumStrips() {
		return stripIndexCounts.length;
	}
	
	public void setStripIndexCounts(int[] stripIndexCounts) {
		this.stripIndexCounts = stripIndexCounts;
	}
	
	public void getStripVertexCounts(int[] stripIndexCounts) {
		System.arraycopy(this.stripIndexCounts, 0, stripIndexCounts, 0, stripIndexCounts.length);
	}
}

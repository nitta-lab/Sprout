package java3d;

public abstract class IndexedGeometryStripArray extends IndexedGeometryArray {
	protected int[] stripIndexCounts;

	public IndexedGeometryStripArray(int vertexCount, int vertexFormat, int indexCount, int[] stripIndexCounts) {
		super(vertexCount, vertexFormat, indexCount);
		this.stripIndexCounts = stripIndexCounts;
	}

	public int getNumStrips() {
		return stripIndexCounts.length;
	}
	
	public void setStripIndexCounts(int[] stripIndexCounts) {
		this.stripIndexCounts = stripIndexCounts;
	}
	
	public void getStripIndexCounts(int[] stripIndexCounts) {
		System.arraycopy(this.stripIndexCounts, 0, stripIndexCounts, 0, stripIndexCounts.length);
	}
}

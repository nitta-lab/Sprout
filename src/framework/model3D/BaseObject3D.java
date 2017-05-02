package framework.model3D;

import java.util.ArrayList;
import java.util.Enumeration;

import java3d.BoundingPolytope;
import java3d.Box;
import java3d.Cone;
import java3d.Cylinder;
import java3d.IndexedTriangleArray;
import java3d.IndexedTriangleFanArray;
import java3d.IndexedTriangleStripArray;
import java3d.Node;
import java3d.TriangleStripArray;
import java3d.Vector3d;
import java3d.Vector4d;

import java3d.Appearance;
import java3d.Geometry;
import java3d.Shape3D;
import java3d.Sphere;
import java3d.TransformGroup;
import java3d.TriangleArray;
import java3d.TriangleFanArray;

public class BaseObject3D implements Placeable {
	public TransformGroup center;
	
	protected BoundingSurface[] boundingSurfaces = null;
	
	public BaseObject3D() {
		center = new TransformGroup();
	}
	
	public BaseObject3D(Geometry g, Appearance a) {
		center = new TransformGroup();
		Shape3D shape = new Shape3D(g, a);
		center.addChild(shape);
	}

	@Override
	public TransformGroup getTransformGroupToPlace() {
		return getBody().center;
	}
	
	public BaseObject3D getBody() {
		return this;
	}

	public Node getPrimitiveNode() {
		return (Node)center.getChild(0);
	}
	
	public Enumeration<Node> getPrimitiveNodes() {
		return (Enumeration<Node>)center.getAllChildren();
	}
	
	/**
	 * 衝突判定用のボリューム（ポリゴンと粗い判定用の多角柱）を取得する
	 * @return　衝突判定用のボリューム列
	 */
	public BoundingSurface[] getBoundingSurfaces() {
		if (boundingSurfaces == null) {
			Node node = getPrimitiveNode();
			if (node == null) return null;
			
			ArrayList<Vector3d> vertex3DList = null;
			if (node instanceof Box) {
				// Boxの場合
				Box box = ((Box)node);
				// 頂点列を取得する
				vertex3DList = getVertexList(box.getShape(Box.BACK).getGeometry());
				vertex3DList.addAll(getVertexList(box.getShape(Box.BOTTOM).getGeometry()));
				vertex3DList.addAll(getVertexList(box.getShape(Box.FRONT).getGeometry()));
				vertex3DList.addAll(getVertexList(box.getShape(Box.LEFT).getGeometry()));
				vertex3DList.addAll(getVertexList(box.getShape(Box.RIGHT).getGeometry()));
				vertex3DList.addAll(getVertexList(box.getShape(Box.TOP).getGeometry()));
			} else if (node instanceof Cylinder) {
				// Cylinderの場合
				Cylinder cylinder = ((Cylinder)node);
				// 頂点列を取得する
				vertex3DList = getVertexList(cylinder.getShape(Cylinder.BODY).getGeometry());
				vertex3DList.addAll(getVertexList(cylinder.getShape(Cylinder.BOTTOM).getGeometry()));
				vertex3DList.addAll(getVertexList(cylinder.getShape(Cylinder.TOP).getGeometry()));
			} else if (node instanceof Cone) {
				// Coneの場合
				Cone cone = ((Cone)node);
				// 頂点列を取得する
				vertex3DList = getVertexList(cone.getShape(Cone.BODY).getGeometry());
				vertex3DList.addAll(getVertexList(cone.getShape(Cone.CAP).getGeometry()));
			} else if (node instanceof Sphere) {
				// Sphereの場合
				Sphere sphere = ((Sphere)node);
				// 頂点列を取得する
				vertex3DList = getVertexList(sphere.getShape(Sphere.BODY).getGeometry());
			} else if (node instanceof Shape3D) {
				// Shape3Dの場合
				Shape3D shape = (Shape3D)node;				
				// 頂点列を取得する
				vertex3DList = getVertexList(shape.getGeometry());
			}
			if (vertex3DList == null) return null;

			BoundingSurface[] surfaces = new BoundingSurface[vertex3DList.size() / 3];

			for (int i = 0; i < vertex3DList.size(); i += 3) {
				Vector3d v1 = vertex3DList.get(i);
				Vector3d v2 = vertex3DList.get(i + 1);
				Vector3d v3 = vertex3DList.get(i + 2);
				BoundingSurface bSurface = new BoundingSurface();
				bSurface.addVertex((Vector3d)v1.clone());
				bSurface.addVertex((Vector3d)v2.clone());
				bSurface.addVertex((Vector3d)v3.clone());
				bSurface.setBounds(createBoundingPolytope(v1, v2, v3));
				surfaces[i / 3] = bSurface;
			}
			boundingSurfaces = surfaces;
		}
		return boundingSurfaces;
	}

	private ArrayList<Vector3d> getVertexList(Geometry g) {
		ArrayList<Vector3d> vertex3DList = new ArrayList<Vector3d>();
		double coordinate1[] = new double[3];
		if (g instanceof IndexedTriangleArray) {
			// IndexedTriangleArray の場合
			IndexedTriangleArray triArray = (IndexedTriangleArray)g;

			// 全頂点を3D上の頂点をvertex3DListに入れていく。
			for (int i = 0; i < triArray.getIndexCount(); i++) {
				triArray.getCoordinates(triArray.getCoordinateIndex(i), coordinate1);
				vertex3DList.add(new Vector3d(coordinate1));
			}
		} else if (g instanceof TriangleArray) {
			// TriangleArray の場合
			TriangleArray triArray = (TriangleArray)g;

			// 全頂点を3D上の頂点をvertex3DListに入れていく。
			for (int i = 0; i < triArray.getVertexCount(); i++) {
				triArray.getCoordinates(i, coordinate1);
				vertex3DList.add(new Vector3d(coordinate1));
			}
		} else if (g instanceof IndexedTriangleStripArray) {
			// IndexedTriangleStripArray の場合
			IndexedTriangleStripArray triStripAttay = (IndexedTriangleStripArray)g;				
			int stripVertexCounts[] = new int[triStripAttay.getNumStrips()];
			triStripAttay.getStripIndexCounts(stripVertexCounts);
			// 全頂点を3D上の頂点をvertex3DListに入れていく
			int index = 0;
			double coordinate2[] = new double[3];
			double coordinate3[] = new double[3];
			double coordinate4[] = new double[3];
			for (int i = 0; i < triStripAttay.getNumStrips(); i++) {
				for (int j = 0; j < stripVertexCounts[i]; j += 2) {						
					triStripAttay.getCoordinates(triStripAttay.getCoordinateIndex(index), coordinate1);
					triStripAttay.getCoordinates(triStripAttay.getCoordinateIndex(index+1), coordinate2);
					triStripAttay.getCoordinates(triStripAttay.getCoordinateIndex(index+2), coordinate3);
					triStripAttay.getCoordinates(triStripAttay.getCoordinateIndex(index+3), coordinate4);
					vertex3DList.add(new Vector3d(coordinate1));	//1つめの三角形
					vertex3DList.add(new Vector3d(coordinate2));
					vertex3DList.add(new Vector3d(coordinate3));
					vertex3DList.add(new Vector3d(coordinate2));	//2つめの三角形
					vertex3DList.add(new Vector3d(coordinate4));
					vertex3DList.add(new Vector3d(coordinate3));
					index += 2;
				}
			}
		} else if (g instanceof TriangleStripArray) {
			// TriangleStripArray の場合
			TriangleStripArray triStripAttay = (TriangleStripArray)g;				
			int stripVertexCounts[] = new int[triStripAttay.getNumStrips()];
			triStripAttay.getStripVertexCounts(stripVertexCounts);
			// 全頂点を3D上の頂点をvertex3DListに入れていく
			int index = 0;
			double coordinate2[] = new double[3];
			double coordinate3[] = new double[3];
			double coordinate4[] = new double[3];
			for (int i = 0; i < triStripAttay.getNumStrips(); i++) {
				for (int j = 0; j < stripVertexCounts[i]; j += 2) {						
					triStripAttay.getCoordinates(index, coordinate1);
					triStripAttay.getCoordinates(index+1, coordinate2);
					triStripAttay.getCoordinates(index+2, coordinate3);
					triStripAttay.getCoordinates(index+3, coordinate4);
					vertex3DList.add(new Vector3d(coordinate1));	//1つめの三角形
					vertex3DList.add(new Vector3d(coordinate2));
					vertex3DList.add(new Vector3d(coordinate3));
					vertex3DList.add(new Vector3d(coordinate2));	//2つめの三角形
					vertex3DList.add(new Vector3d(coordinate4));
					vertex3DList.add(new Vector3d(coordinate3));
					index += 2;
				}
			}
		} else if (g instanceof IndexedTriangleFanArray) {
			// IndexedTriangleFanArray の場合
			IndexedTriangleFanArray triFanAttay = (IndexedTriangleFanArray)g;				
			int stripVertexCounts[] = new int[triFanAttay.getNumStrips()];
			triFanAttay.getStripIndexCounts(stripVertexCounts);
			// 全頂点を3D上の頂点をvertex3DListに入れていく
			int index = 0;
			double coordinate2[] = new double[3];
			double coordinate3[] = new double[3];
			double coordinate4[] = null;
			for (int i = 0; i < triFanAttay.getNumStrips(); i++) {
				triFanAttay.getCoordinates(triFanAttay.getCoordinateIndex(index), coordinate1);			// 中心点
				triFanAttay.getCoordinates(triFanAttay.getCoordinateIndex(index+1), coordinate2);
				index += 2;
				for (int j = 2; j < stripVertexCounts[i]; j++) {
					triFanAttay.getCoordinates(triFanAttay.getCoordinateIndex(index), coordinate3);
					vertex3DList.add(new Vector3d(coordinate1));
					vertex3DList.add(new Vector3d(coordinate2));
					vertex3DList.add(new Vector3d(coordinate3));
					coordinate4 = coordinate2;
					coordinate2 = coordinate3;
					coordinate3 = coordinate4;
					index++;
				}
			}
		} else if (g instanceof TriangleFanArray) {
			// TriangleFanArray の場合
			TriangleFanArray triFanAttay = (TriangleFanArray)g;				
			int stripVertexCounts[] = new int[triFanAttay.getNumStrips()];
			triFanAttay.getStripVertexCounts(stripVertexCounts);
			// 全頂点を3D上の頂点をvertex3DListに入れていく
			int index = 0;
			double coordinate2[] = new double[3];
			double coordinate3[] = new double[3];
			double coordinate4[] = null;
			for (int i = 0; i < triFanAttay.getNumStrips(); i++) {
				triFanAttay.getCoordinates(index, coordinate1);			// 中心点
				triFanAttay.getCoordinates(index + 1, coordinate2);
				index += 2;
				for (int j = 2; j < stripVertexCounts[i]; j++) {
					triFanAttay.getCoordinates(index, coordinate3);
					vertex3DList.add(new Vector3d(coordinate1));
					vertex3DList.add(new Vector3d(coordinate2));
					vertex3DList.add(new Vector3d(coordinate3));
					coordinate4 = coordinate2;
					coordinate2 = coordinate3;
					coordinate3 = coordinate4;
					index++;
				}
			}
		} else {
			return null;
		}
		return vertex3DList;
	}

	protected BoundingPolytope createBoundingPolytope(Vector3d vertex1,
			Vector3d vertex2, Vector3d vertex3) {
		Vector3d v1 = new Vector3d();
		Vector3d v2 = new Vector3d();
		Vector3d v3 = new Vector3d();
		Vector3d v4 = new Vector3d();
		Vector3d v5 = new Vector3d();
		Vector3d v6 = new Vector3d();
		Vector3d cv1 = new Vector3d();
		Vector3d cv2 = new Vector3d();
		cv1.sub(vertex3, vertex1);
		cv2.sub(vertex2, vertex1);
		Vector3d cv = new Vector3d();
		cv.cross(cv1, cv2);
		cv.normalize();
		cv.scale(0.01);
		v1.set(vertex1);
		v2.set(vertex2);
		v3.set(vertex3);
		v4.set(vertex1);
		v4.add(cv);
		v5.set(vertex2);
		v5.add(cv);
		v6.set(vertex3);
		v6.add(cv);

		Vector3d pv1 = new Vector3d();
		Vector3d pv2 = new Vector3d();
		Vector3d pv3 = new Vector3d();
		Vector3d pn = new Vector3d();
		Vector4d[] plane = new Vector4d[5];

		// 0
		pv1 = v1;
		pv2.sub(v2, v1);
		pv3.sub(v3, v1);

		pn.cross(pv2, pv3);
		pn.normalize();
		plane[0] = new Vector4d(pn.x, pn.y, pn.z, -pn.dot(pv1));

		// 1
		pv1 = v1;
		pv2.sub(v4, v1);
		pv3.sub(v2, v1);

		pn.cross(pv2, pv3);
		pn.normalize();
		plane[1] = new Vector4d(pn.x, pn.y, pn.z, -pn.dot(pv1));

		// 2
		pv1 = v1;
		pv2.sub(v3, v1);
		pv3.sub(v4, v1);

		pn.cross(pv2, pv3);
		pn.normalize();
		plane[2] = new Vector4d(pn.x, pn.y, pn.z, -pn.dot(pv1));

		// 3
		pv1 = v6;
		pv2.sub(v3, v6);
		pv3.sub(v5, v6);

		pn.cross(pv2, pv3);
		pn.normalize();
		plane[3] = new Vector4d(pn.x, pn.y, pn.z, -pn.dot(pv1));

		// 4
		pv1 = v6;
		pv2.sub(v5, v6);
		pv3.sub(v4, v6);

		pn.cross(pv2, pv3);
		pn.normalize();
		plane[4] = new Vector4d(pn.x, pn.y, pn.z, -pn.dot(pv1));

		return new BoundingPolytope(plane);
	}
	
	public boolean hasAppearancePrepared() {
		return true;
	}
}
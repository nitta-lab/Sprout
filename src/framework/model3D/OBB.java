package framework.model3D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import java3d.BoundingPolytope;
import java3d.BoundingSphere;
import java3d.Transform3D;
import java3d.Matrix4d;
import java3d.Point3d;
import java3d.Vector3d;
import java3d.Vector4d;


public class OBB implements Cloneable {
	public ArrayList<Vector3d> vertexList = new ArrayList<Vector3d>();
	public Vector4d[] plane = new Vector4d[6];
	public BoundingPolytope bp = null;
	public static int edges[][] = { { 0, 1 }, { 0, 2 }, { 1, 3 }, { 2, 3 },
			{ 0, 4 }, { 1, 5 }, { 3, 7 }, { 2, 6 }, { 4, 5 }, { 4, 6 },
			{ 5, 7 }, { 6, 7 } };
	static boolean[][] inside = new boolean[8][6];
	static boolean[] inside2 = new boolean[8];

	public OBB() {
	}
	
	public OBB(Vector3d v1, Vector3d v2, Vector3d v3, Vector3d v4, 
			Vector3d v5, Vector3d v6, Vector3d v7, Vector3d v8) {
		vertexList.add(v1);
		vertexList.add(v2);
		vertexList.add(v3);
		vertexList.add(v4);
		vertexList.add(v5);
		vertexList.add(v6);
		vertexList.add(v7);
		vertexList.add(v8);
		createPlanes();
	}

	public void addVertex(Vector3d v) {
		vertexList.add(v);
	}
	
	public Vector3d getVertex(int i) {
		return vertexList.get(i);
	}

	public BoundingSphere getBoundingSphere() {
		double radius = 0.0;
		Point3d p = new Point3d();
		Vector3d cv = new Vector3d();
		for (int i = 0; i < vertexList.size() - 1; i++) {
			for (int j = i + 1; j < vertexList.size(); j++) {
				Vector3d v = new Vector3d();
				v.sub(vertexList.get(i), vertexList.get(j));
				if (radius < v.length()) {
					radius = v.length();
					cv.add(vertexList.get(i), vertexList.get(j));
					cv.scale(0.5);
					p.x = cv.x;
					p.y = cv.y;
					p.z = cv.z;
				}
			}
		}
		BoundingSphere s = new BoundingSphere(p, radius / 2);
		return s;
	}
	
	/**
	 * 頂点情報を元に面および境界多面体を生成する
	 */
	public void createPlanes() {
		// 面の作成
		Vector3d v1 = new Vector3d();
		Vector3d v2 = new Vector3d();
		Vector3d v3 = new Vector3d();
		Vector4d[] plane = new Vector4d[6];
	
		// 0231
		v1 = vertexList.get(0);
		v2.sub(vertexList.get(2), vertexList.get(0));
		v3.sub(vertexList.get(1), vertexList.get(0));
		Vector3d n = new Vector3d();
		n.cross(v2, v3);
		n.normalize();
		plane[0] = new Vector4d();
		plane[0].set(n.x, n.y, n.z, -n.dot(v1));
		
	
		// 0154
		v1 = vertexList.get(0);
		v2.sub(vertexList.get(1), vertexList.get(0));
		v3.sub(vertexList.get(4), vertexList.get(0));
		n = new Vector3d();
		n.cross(v2, v3);
		n.normalize();
		plane[1] = new Vector4d();
		plane[1].set(n.x, n.y, n.z, -n.dot(v1));
	
		// 1375
		v1 = vertexList.get(1);
		v2.sub(vertexList.get(3), vertexList.get(1));
		v3.sub(vertexList.get(5), vertexList.get(1));
		n = new Vector3d();
		n.cross(v2, v3);
		n.normalize();
		plane[2] = new Vector4d();
		plane[2].set(n.x, n.y, n.z, -n.dot(v1));
	
		// 4576
		v1 = vertexList.get(6);
		v2.sub(vertexList.get(4), vertexList.get(6));
		v3.sub(vertexList.get(7), vertexList.get(6));
		n = new Vector3d();
		n.cross(v2, v3);
		n.normalize();
		plane[3] = new Vector4d();
		plane[3].set(n.x, n.y, n.z, -n.dot(v1));
	
		// 0462
		v1 = vertexList.get(6);
		v2.sub(vertexList.get(2), vertexList.get(6));
		v3.sub(vertexList.get(4), vertexList.get(6));
		n = new Vector3d();
		n.cross(v2, v3);
		n.normalize();
		plane[4] = new Vector4d();
		plane[4].set(n.x, n.y, n.z, -n.dot(v1));
	
		// 2673
		v1 = vertexList.get(6);
		v2.sub(vertexList.get(7), vertexList.get(6));
		v3.sub(vertexList.get(2), vertexList.get(6));
		n = new Vector3d();
		n.cross(v2, v3);
		n.normalize();
		plane[5] = new Vector4d();
		plane[5].set(n.x, n.y, n.z, -n.dot(v1));
		
		this.plane = plane;
		
		bp = new BoundingPolytope();
		bp.setPlanes(plane);
	}

	/**
	 * 平面との衝突判定
	 * @param plane 衝突判定の対象となる平面
	 * @return 衝突判定の結果（衝突していない場合はnull）
	 */
	public CollisionResult intersect(Vector4d plane) {
		int i = 0;
		boolean inside = false;
		boolean outside = false;
		int count = 0;
		Vector3d center = new Vector3d(0, 0, 0);
		double l = Math.sqrt(plane.x * plane.x + plane.y * plane.y + plane.z * plane.z);
		double length;
		double deepest = 0.0;
		Vector3d v;
		for (i = 0; i < vertexList.size(); i++) {
			v = vertexList.get(i);
			if (GeometryUtility.inside(v, plane)) {
				inside = true;
				length = -(v.x * plane.x + v.y * plane.y + v.z * plane.z + plane.w) / l;
				if (length > deepest + GeometryUtility.TOLERANCE) {
					center.set(v);
					count = 1;
					deepest = length;
				} else if (length >= deepest - GeometryUtility.TOLERANCE) {
					center.add(v);
					count++;			
				}
			} else {
				outside = true;				
			}
		}

		if (!inside || !outside) {
			// 全頂点が外側か全頂点が内側の場合
			return null;
		}

		center.scale(1.0 / count);

		CollisionResult cr = new CollisionResult();
		cr.length = deepest;
		cr.collisionPoint.setX(center.x);
		cr.collisionPoint.setY(center.y);
		cr.collisionPoint.setZ(center.z);
		cr.normal.setX(plane.x / l);
		cr.normal.setY(plane.y / l);
		cr.normal.setZ(plane.z / l);

		return cr;
	}

	/**
	 * OBBとの衝突判定
	 * @param o 衝突判定の対象となるOBB
	 * @return 衝突判定の結果（衝突していない場合はnull）
	 */
	public CollisionResult intersect(OBB o) {
		// oの各頂点がthisの各面の内側にあるかを調べる
		// i：頂点 j：面
		for (int i = 0; i < o.vertexList.size(); i++) {
			for (int j = 0; j < plane.length; j++) {
				inside[i][j] = GeometryUtility.inside(o.vertexList.get(i), plane[j]);
			}
		}

		boolean collision = false;
		int count = 0;
		Vector3d center = new Vector3d(0, 0, 0);

		// oの各頂点がthisに包含されているか？
		for (int v = 0; v < 8; v++) {
			boolean f1 = false;
			for (int p = 0; p < 6; p++) {
				f1 = inside[v][p];
				if (!f1) {
					break;
				}
			}
			inside2[v] = f1;
			if (f1) {
				collision = true;
				center.add(o.vertexList.get(v));
				count++;
			}
		}

		if (!collision) {
			// 辺が相手の面と交わっているか？
			for (int p = 0; p < 6; p++) {
				Vector3d intersection = null;
				for (int e = 0; e < 12; e++) {
					if (inside[edges[e][0]][p] == !inside[edges[e][1]][p]) {
						intersection = GeometryUtility.intersect(
								plane[p],
								o.vertexList.get(edges[e][0]), 
								o.vertexList.get(edges[e][1]));
						boolean bInside = true;
						for (int p2 = 0; p2 < 6; p2++) {
							if (p != p2
								&& !GeometryUtility.inside(intersection, plane[p2])) {
								bInside = false;
								break;
							}
						}
						if (bInside) {
							collision = true;
							center.add(intersection);
							count++;
						}
					}
				}
			}
		}

		if (!collision)
			return null;

		center.scale(1.0 / count);

		CollisionResult cr = new CollisionResult();

		double lMin = 0;
		int cm = -1;
		Vector3d normal = null;
		for (int m = 0; m < plane.length; m++) {
			Vector3d n = new Vector3d(plane[m].x, plane[m].y, plane[m].z);
			double l = -(center.x * plane[m].x + center.y * plane[m].y + center.z
							* plane[m].z + plane[m].w) / n.length();
			if (lMin > l || cm == -1) {
				lMin = l;
				cm = m;
				normal = n;
			}
		}
		cr.length = lMin;
		cr.collisionPoint.setVector3d(center);
		cr.normal.setX(plane[cm].x / normal.length());
		cr.normal.setY(plane[cm].y / normal.length());
		cr.normal.setZ(plane[cm].z / normal.length());

		return cr;

	}

	public Object clone() {
		OBB obb = new OBB();
		obb.plane = new Vector4d[6];
		for (int i = 0; i < plane.length; i++) {
			obb.plane[i] = (Vector4d) plane[i].clone();
		}
		for (int i = 0; i < vertexList.size(); i++) {
			obb.vertexList.add((Vector3d) vertexList.get(i).clone());
		}
		obb.bp = new BoundingPolytope(obb.plane);

		// System.out.println("veretexListSIZE:"+vertexList.size());
		return obb;
	}

	public void transform(Transform3D t) {
		// TODO Auto-generated method stub
		bp.transform(t);
		bp.getPlanes(plane);
		// for(int i = 0; i<plane.length;i++){
		// System.out.println("plane");
		// System.out.print(plane[i].x + "," + plane[i].y + "," + plane[i].z +
		// "," + plane[i].w);
		// t.transform(plane[i]);
		// System.out.println("-->" + plane[i].x + "," + plane[i].y + "," +
		// plane[i].z + "," + plane[i].w);
		// }
		for (int i = 0; i < vertexList.size(); i++) {
			// System.out.println("vertex");
			// System.out.print(vertexList.get(i).x + "," + vertexList.get(i).y
			// + "," + vertexList.get(i).z);
			Matrix4d mat4d = new Matrix4d();
			t.get(mat4d);
			double x = mat4d.m00 * vertexList.get(i).x + mat4d.m01
					* vertexList.get(i).y + mat4d.m02 * vertexList.get(i).z
					+ mat4d.m03;
			double y = mat4d.m10 * vertexList.get(i).x + mat4d.m11
					* vertexList.get(i).y + mat4d.m12 * vertexList.get(i).z
					+ mat4d.m13;
			double z = mat4d.m20 * vertexList.get(i).x + mat4d.m21
					* vertexList.get(i).y + mat4d.m22 * vertexList.get(i).z
					+ mat4d.m23;
			vertexList.get(i).x = x;
			vertexList.get(i).y = y;
			vertexList.get(i).z = z;
			// t.transform(vertexList.get(i));
			// System.out.println("-->" + vertexList.get(i).x + "," +
			// vertexList.get(i).y + "," + vertexList.get(i).z);
		}
	}
}

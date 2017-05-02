package framework.model3D;

import java.util.ArrayList;
import java.util.Hashtable;

import java3d.IndexedGeometryArray;
import java3d.AxisAngle4d;
import java3d.Matrix3d;
import java3d.Point3d;
import java3d.Vector3d;
import java3d.Vector4d;

public class GeometryUtility {
	public static final double TOLERANCE = 0.0000002;
	public static final Vector3d X_AXIS = new Vector3d(1.0, 0.0, 0.0);
	public static final Vector3d Y_AXIS = new Vector3d(0.0, 1.0, 0.0);
	public static final Vector3d Z_AXIS = new Vector3d(0.0, 0.0, 1.0);

	public static ProjectionResult projection3D(
			ArrayList<Vector3d> vertex3Dlist, Vector3d axis) {
		double k = 0.0;
		int i = 0;

		ProjectionResult pr = new ProjectionResult();

		axis.normalize();

		for (i = 0; i < vertex3Dlist.size(); i++) {
			Vector3d p = vertex3Dlist.get(i);
			Vector3d p1 = new Vector3d();

			k = p.dot(axis);
			p1.scaleAdd(-k, axis, p);

			if (i == 0 || k >= pr.max) {
				pr.max = k;
			} else if (i == 0 || k <= pr.min) {
				pr.min = k;
			}
			pr.vertexList.add(p1);
		}
		return pr;
	}

	public static ProjectionResult projection2D(
			ArrayList<Vector3d> vertex2Dlist, Vector3d axis) {
		int i = 0;
		double k = 0.0;
		// System.out.println("point3�F"+axis);
		if (axis.x != 0 || axis.y != 0 || axis.z != 0) {
			axis.normalize();
		}
		// System.out.println("point3_1�F"+axis);
		ProjectionResult pr = new ProjectionResult();

		for (i = 0; i < vertex2Dlist.size(); i++) {
			Vector3d p = vertex2Dlist.get(i);

			k = p.dot(axis);
			// System.out.println("k�F"+k);
			// System.out.println("point3�F"+axis);
			if (i == 0 || k >= pr.max) {
				pr.max = k;
			} else if (i == 0 || k <= pr.min) {
				pr.min = k;
			}
		}
		return pr;
	}

	// �ȉ��A���_�̕\���̔��胁�\�b�h
	public static boolean inside(Vector3d v, Vector4d plane) {
		// System.out.println("vertex:" + v.x + "," + v.y + "," + v.z);
		// System.out.println("plane:" + plane.x + "," + plane.y + "," + plane.z
		// + "," + plane.w);
		Vector3d pv = new Vector3d(plane.x, plane.y, plane.z);
		// Vector3d nv = (Vector3d)pv.clone();
		// nv.scaleAdd(plane.w / pv.lengthSquared(),v);
		if (pv.dot(v) + plane.w <= TOLERANCE) {
			// System.out.println("��������I�I"+nv.dot(pv));
			pv = null;
			return true;
		}
		// System.out.println("�O������I�I"+nv.dot(pv));
		pv = null;
		return false;
	}

	/**
	 * 3�_��ʂ镽�ʂ��쐬����i�������Av1, v2, v3�̓��e��������������̂Œ��Ӂj
	 * 
	 * @param v1
	 *            --- 1�_�ڂ̍��W
	 * @param v2
	 *            --- 2�_�ڂ̍��W
	 * @param v3
	 *            --- 3�_�ڂ̍��W
	 * @return�@v1, v2, v3��ʂ镽��
	 */
	public static Vector4d createPlane(Vector3d v1, Vector3d v2, Vector3d v3) {
		v2.sub(v1);
		v3.sub(v1);
		v2.cross(v2, v3);
		v2.normalize();
		return new Vector4d(v2.x, v2.y, v2.z, -v2.dot(v1));
	}

	/**
	 * 
	 * ���ʂƒ����̌�_�����߂�
	 * 
	 * @param plane
	 *            ����
	 * @param v1
	 *            ������̓_1
	 * @param v2
	 *            ������̓_2
	 * @return ��_
	 */
	public static Vector3d intersect(Vector4d plane, Vector3d v1, Vector3d v2) {
		Vector3d n = new Vector3d(plane.x, plane.y, plane.z);
		Vector3d v21 = (Vector3d) v2.clone();
		v21.sub(v1);
		v21.scale((-plane.w - n.dot(v1)) / n.dot(v21));
		v21.add(v1);
		return v21;
	}

	/**
	 * 
	 * �w�肳�ꂽ�_���ʃ|���S���̓����ɕ�܂���Ă��邩�H
	 * 
	 * @param vertexList
	 *            �ʃ|���S���̒��_��
	 * @param point
	 *            �w��_
	 * @return true --- ��܂���Ă���, false --- ��܂���Ă��Ȃ�
	 */
	public static boolean inside(ArrayList<Vector3d> vertexList, Vector3d point, Vector3d normal) {
		boolean inside = true;
		for (int i = 0; i < vertexList.size(); i++) {
			// �|���S���̊e�ӂɑ΂��ďՓ˓_���E�����������H
			Vector3d center = (Vector3d) point.clone();
			Vector3d v2 = (Vector3d) (vertexList.get((i + 1)
					% vertexList.size()).clone());
			Vector3d v1 = (Vector3d) vertexList.get(i).clone();
			center.sub(v1);
			v2.sub(v1);
			v1.cross(v2, center);
			if (normal.dot(v1) < -GeometryUtility.TOLERANCE) {
				inside = false;
				break;
			}
		}
		// ���ׂĉE���A�܂��͂��ׂč����������ꍇ�A�ʃ|���S���̓����Ɉʒu�����ƍl����
		return inside;
	}

	// IndexedGeometryArray�̃C���f�b�N�X�̂����A�������W�������C���f�b�N�X��u��������
	public static void compressGeometry(IndexedGeometryArray g) {
		// 1. Hashtable�����Ȃ���A������W���_�Q�̑�\representaion[]�����߂�
		Hashtable<Double, ArrayList<Integer>> h = new Hashtable<Double, ArrayList<Integer>>();
		Point3d p = new Point3d();
		Point3d p2 = new Point3d();
		double hashValue;
		ArrayList<Integer> vertexList;
		int[] representation = new int[g.getVertexCount()];

		for (int i = 0; i < g.getVertexCount(); i++) {
			g.getCoordinate(i, p);

			hashValue = p.getX() + p.getY() + p.getZ();

			vertexList = h.get(hashValue);

			if (vertexList == null) {// hash�ɑΉ�����v�f���Ȃ��ꍇ
				// Hashtable�����
				vertexList = new ArrayList<Integer>();
				vertexList.add(i);
				h.put(hashValue, vertexList);
				// representation[]�����
				representation[i] = i;
			} else {
				boolean bFound = false;
				for (int j = 0; j < vertexList.size(); j++) {
					g.getCoordinate(vertexList.get(j), p2);
					if (p.getX() == p2.getX() 
							&& p.getY() == p2.getY()
							&& p.getZ() == p2.getZ()) {
						representation[i] = vertexList.get(j);
						bFound = true;
						break;
					}
				}
				if (!bFound) {
					vertexList.add(i);
					// representation[]�����
					representation[i] = i;
				}
			}
		}

		// 2. index�̒u������
		for (int i = 0; i < g.getIndexCount(); i++) {
			int newIndex = representation[g.getCoordinateIndex(i)];
			g.setCoordinateIndex(i, newIndex);
		}
	}

	public static Matrix3d calcRotationForView(Vector3d viewLine) {
		Vector3d v1 = (Vector3d)viewLine.clone();
		v1.normalize();
		Vector3d v2 = new Vector3d();
		v2.cross(v1, Y_AXIS);
		double angle2 = Math.atan2(-v2.z, v2.x);
		double angle1 = Math.PI / 2.0 - Math.acos(v1.dot(Y_AXIS));
		Matrix3d rot1 = new Matrix3d();
		rot1.rotX(angle1);
		Matrix3d rot2 = new Matrix3d();
		rot2.rotY(angle2);
		rot2.mul(rot1);
		return rot2;
	}

	public static Quaternion3D calcQuaternionForView(Vector3d viewLine) {
		Vector3d v1 = (Vector3d)viewLine.clone();
		v1.normalize();
		Vector3d v2 = new Vector3d();
		v2.cross(v1, Y_AXIS);
		double angle2 = Math.atan2(-v2.z, v2.x);
		double angle1 = Math.PI / 2.0 - Math.acos(v1.dot(Y_AXIS));
		Quaternion3D quat1 = new Quaternion3D(new AxisAngle4d(X_AXIS, angle1));
		Quaternion3D quat2 = new Quaternion3D(new AxisAngle4d(Y_AXIS, angle2));
		
		return quat1.mul(quat2);
	}
	
	public static Quaternion3D calcQuaternionFromSrcToDst(Vector3d src, Vector3d dst) {
		Vector3d v1 = new Vector3d();
		Vector3d v2 = new Vector3d();
		v1.cross(src, Y_AXIS);
		v2.cross(dst, Y_AXIS);
		Quaternion3D quat1;
		Quaternion3D quat2;
		double angle2 = Math.acos(Y_AXIS.dot(src) / src.length()) - Math.acos(Y_AXIS.dot(dst) / dst.length());
		if (Math.abs(v2.length()) < TOLERANCE) {
			v1.normalize();
			quat1 = new Quaternion3D();
			quat2 = new Quaternion3D(new AxisAngle4d(v1, angle2));			
		} else {
			if (Math.abs(v1.length()) < TOLERANCE) {
				quat1 = new Quaternion3D();
			} else {
				v1.normalize();
				v2.normalize();
				double cos1 = v1.dot(v2);
				v1.cross(v1, v2);
				double sin1 = v1.dot(Y_AXIS);
				double angle1 = Math.atan2(sin1, cos1);
				quat1 = new Quaternion3D(new AxisAngle4d(Y_AXIS, angle1));
			}
			quat2 = new Quaternion3D(new AxisAngle4d(v2, angle2));
		}		
		return quat1.mul(quat2);
	}
}
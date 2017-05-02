package framework.model3D;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;

import java3d.Appearance;
import java3d.AxisAngle4d;
import java3d.BoundingSphere;
import java3d.Box;
import java3d.Cone;
import java3d.Cylinder;
import java3d.Geometry;
import java3d.GeometryStripArray;
import java3d.IndexedGeometryArray;
import java3d.Leaf;
import java3d.Node;
import java3d.Primitive;
import java3d.Quat4d;
import java3d.Shape3D;
import java3d.Sphere;
import java3d.Transform3D;
import java3d.TransformGroup;
import java3d.TriangleArray;
import java3d.Vector3d;


public class Object3D extends BaseObject3D {
	public String name;
	public TransformGroup pos;
	public TransformGroup rot;
	public TransformGroup scale;
	public Object3D[] children = new Object3D[0];
	private Position3D position;
	protected Quaternion3D quaternion;
	
	private OBB obb = new OBB();
//	private boolean bLOD = false;
//	private LOD lodNode = null;
	
	public BoundingSphere bs = null;
	private UndoBuffer undoBuffer = new UndoBuffer();

	public Object3D(String name, Primitive prim) {
		init(name);
		center.addChild(prim);
	}

	public Object3D(String name, Leaf node) {
		init(name);
		center.addChild(node);
	}
	
	public Object3D(String name, Geometry g, Appearance a) {
		this(name, new Shape3D(g, a));
	}
	
	public Object3D(String name, Object3D[] children) {
		init(name);
		this.children = children;
		for (int i = 0; i < children.length; i++) {
			center.addChild(children[i].getTransformGroupToPlace());
		}		
	}
		
	public Object3D(String name, Object3D[] children, Transform3D defaultTransform) {
		this(name, children);
		if (defaultTransform == null) return;
		pos.setTransform(defaultTransform);
	}
	
	// コピーコンストラクタ
	public Object3D(Object3D obj) {
		this.name = new String(obj.name);
		if (obj.position != null) {
			this.position = new Position3D(obj.position);
		} else {
			this.position = new Position3D();
		}
		if (obj.getQuaternion() != null) {
			this.quaternion = new Quaternion3D(obj.getQuaternion());
		} else {
			this.quaternion = new Quaternion3D();
		}
		Transform3D transPos = new Transform3D();
		obj.pos.getTransform(transPos);
		this.pos = new TransformGroup(transPos);
		Transform3D transRot = new Transform3D();
		obj.rot.getTransform(transRot);
		this.rot = new TransformGroup(transRot);
		Transform3D transScale = new Transform3D();
		obj.scale.getTransform(transScale);
		this.scale = new TransformGroup(transScale);
		Transform3D transCenter = new Transform3D();
		obj.center.getTransform(transCenter);
		this.center = new TransformGroup(transCenter);
		this.pos.addChild(this.rot);
		this.rot.addChild(this.scale);
		this.scale.addChild(this.center);
//			this.pos.addChild(this.scale);
////			this.rot.addChild(this.scale);
//			this.scale.addChild(this.center);
		if (obj.hasChildren()) {
			this.children = new Object3D[obj.children.length];
			for (int i = 0; i < obj.children.length; i++) {
				this.children[i] = new Object3D(obj.children[i]);
				this.center.addChild(this.children[i].pos);
			}
		} else {
			this.children = new Object3D[0];
			Enumeration<Node> nodes = obj.getPrimitiveNodes();
			while (nodes.hasMoreElements()) {
				Node node = nodes.nextElement();
				if (node != null && node instanceof Shape3D) {
					Shape3D shape = (Shape3D)node;
					Appearance a = (Appearance)shape.getAppearance().cloneNodeComponent();
					this.center.addChild(new Shape3D((Geometry) shape.getGeometry(), a));
				} else if (node != null && node instanceof Primitive) {
					Primitive primitive = (Primitive)node;
					primitive = (Primitive)primitive.cloneTree();
					this.center.addChild(primitive);
				}
			}				
		}
		if (obj.obb != null) {
			this.obb = obj.obb;
		}
		if (obj.bs != null) {
			this.bs = obj.bs;
		}
		this.undoBuffer = new UndoBuffer(obj.undoBuffer);
	}

	// 自分を複製する（クローンを作る）
	public Object3D duplicate() {
		Object3D obj = new Object3D(this);
		return obj;
	}

	protected void init(String name) {
		this.name = new String(name);
		pos = new TransformGroup();
		rot = new TransformGroup();
		scale = new TransformGroup();
		center = new TransformGroup();
		pos.addChild(rot);
		rot.addChild(scale);
		scale.addChild(center);
	}

	public Node getPrimitiveNode() {
		if (hasChildren()) return null;
//		if (!bLOD) {
			return (Node)center.getChild(0);
			// TODO 未実装
//		} else {
//			// LOD の場合
//			return lodNode.getSwitch(0).getChild(0);			
//		}
	}
	
	public Enumeration<Node> getPrimitiveNodes() {
		if (hasChildren()) return null;
//		if (!bLOD) {
			return (Enumeration<Node>)center.getAllChildren();
			// TODO 未実装
//		} else {
//			// LOD の場合
//			return lodNode.getSwitch(0).getAllChildren();						
//		}
	}

	@Override
	public TransformGroup getTransformGroupToPlace() {
		return pos;
	}
	
	public void setPosition(Position3D p) {
		position = (Position3D) p.clone();
		Vector3d v = new Vector3d(p.getX(), p.getY(), p.getZ());
		Transform3D trans = new Transform3D();
		trans.set(v);
		pos.setTransform(trans);
	}
	
	public Position3D getPosition3D() {
		return (Position3D) position.clone();
	}
	
	public void scale(double s) {
		Transform3D trans = new Transform3D();
		trans.setScale(s);
		scale.setTransform(trans);
	}

	public void scale(double x, double y, double z) {
		Transform3D trans = new Transform3D();
		trans.setScale(new Vector3d(x, y, z));
		scale.setTransform(trans);
	}

	// キャラクターを回転させる
	public void rotate(double vx, double vy, double vz, double a) {
		AxisAngle4d t = new AxisAngle4d(vx, vy, vz, a);
		Transform3D trans = new Transform3D();
		trans.setRotation(t);
		rot.setTransform(trans);
	}
	
	public void apply(Property3D p, boolean enableUndo) {
		if (enableUndo) {
			undoBuffer.push(p);
		}
		p.applyTo(this);
	}
	
//	undoするポイントを設定する。
	public void setUndoMark() {
		undoBuffer.setUndoMark();
	}
		
//	undoする。
	public void undo() {
		Iterator<Property3D> iterator = undoBuffer.undo().iterator();
		while (iterator.hasNext()) {
			Property3D p = iterator.next();
			p.applyTo(this);
		}
	}
	
	// objectに子供がいるかどうかを調べる
	public boolean hasChildren() {
		if (this.children != null && this.children.length > 0)
			return true;
		else
			return false;
	}
		
	/**
	 * 部品オブジェクトを探す
	 * @param partName　部品名
	 * @return　partName を持つ部品オブジェクト
	 */
	public Object3D getPart(String partName) {
		if (partName.equals(this.name)) return this;

		for (int i = 0; i < children.length; i++) {
			if (children[i] != null) {
				Object3D obj = children[i].getPart(partName);
				if (obj != null) return obj;
			}
		}
		return null;
	}

	protected void rotate(Quat4d quat) {
		Transform3D trans = new Transform3D();
		trans.setRotation(quat);
		rot.setTransform(trans);		
	}

	public void accept(ObjectVisitor objectVisitor) {
		int i;
		objectVisitor.preVisit(this);
		if (children != null) {
			for (i = 0; i < children.length; i++)
				children[i].accept(objectVisitor);
		} 
		objectVisitor.postVisit(this);		
	}
	
	// Quaternion3D の applyTo() 以外からは呼ばないこと
		void setQuaternion(Quaternion3D quaternion) {
			this.quaternion = quaternion;
			rotate(quaternion.getQuat());
		}

		public Quaternion3D getQuaternion() {
			return quaternion;
		}

		public OBB getOBB(int pattern) {
			if (obb == null || obb.bp == null) {
				Node node = getPrimitiveNode();
				if (node == null) return null;
				if (node instanceof Box) {
					// Boxの場合
					Box box = ((Box)node);
					double xDim = box.getXdimension();
					double yDim = box.getYdimension();
					double zDim = box.getZdimension();
					obb = new OBB(new Vector3d(-xDim, -yDim, -zDim),
							new Vector3d(-xDim, yDim, -zDim),
							new Vector3d(-xDim, -yDim, zDim),
							new Vector3d(-xDim, yDim, zDim),
							new Vector3d(xDim, -yDim, -zDim),
							new Vector3d(xDim, yDim, -zDim),
							new Vector3d(xDim, -yDim, zDim),
							new Vector3d(xDim, yDim, zDim));
				} else if (node instanceof Cylinder) {
					// Cylinderの場合
					Cylinder cylinder = ((Cylinder)node);
					double xDim = cylinder.getRadius();
					double yDim = cylinder.getHeight() / 2;
					double zDim = cylinder.getRadius();
					obb = new OBB(new Vector3d(-xDim, -yDim, -zDim),
							new Vector3d(-xDim, yDim, -zDim),
							new Vector3d(-xDim, -yDim, zDim),
							new Vector3d(-xDim, yDim, zDim),
							new Vector3d(xDim, -yDim, -zDim),
							new Vector3d(xDim, yDim, -zDim),
							new Vector3d(xDim, -yDim, zDim),
							new Vector3d(xDim, yDim, zDim));
				} else if (node instanceof Cone) {
					// Coneの場合
					Cone cone = ((Cone)node);
					double xDim = cone.getRadius();
					double yDim = cone.getHeight() / 2;
					double zDim = cone.getRadius();
					obb = new OBB(new Vector3d(-xDim, -yDim, -zDim),
							new Vector3d(-xDim, yDim, -zDim),
							new Vector3d(-xDim, -yDim, zDim),
							new Vector3d(-xDim, yDim, zDim),
							new Vector3d(xDim, -yDim, -zDim),
							new Vector3d(xDim, yDim, -zDim),
							new Vector3d(xDim, -yDim, zDim),
							new Vector3d(xDim, yDim, zDim));
				} else if (node instanceof Sphere) {
					// Sphereの場合
					Sphere sphere = ((Sphere)node);
					double xDim = sphere.getRadius();
					double yDim = sphere.getRadius();
					double zDim = sphere.getRadius();
					obb = new OBB(new Vector3d(-xDim, -yDim, -zDim),
							new Vector3d(-xDim, yDim, -zDim),
							new Vector3d(-xDim, -yDim, zDim),
							new Vector3d(-xDim, yDim, zDim),
							new Vector3d(xDim, -yDim, -zDim),
							new Vector3d(xDim, yDim, -zDim),
							new Vector3d(xDim, -yDim, zDim),
							new Vector3d(xDim, yDim, zDim));
				} else {
					if (!(node instanceof Shape3D)) return null;
					// Shape3Dの場合
					Shape3D shape = (Shape3D)node;
					double coordinate[] = new double[3];
		
					// OBBの作成に用いる頂点列の取得
					ArrayList<Vector3d> vertex3DList = new ArrayList<Vector3d>();		
					if (shape.getGeometry() instanceof IndexedGeometryArray) {
						// IndexedGeometryArrayの場合
						IndexedGeometryArray iga = (IndexedGeometryArray) shape.getGeometry();			
						for (int i = 0; i < iga.getIndexCount(); i++) {
							iga.getCoordinates(iga.getCoordinateIndex(i), coordinate);
							Vector3d p = new Vector3d(coordinate[0], coordinate[1], coordinate[2]);
							vertex3DList.add(p);
						}
					} else if (shape.getGeometry() instanceof GeometryStripArray) {
						// GeometryStripArrayの場合
						GeometryStripArray gsa = (GeometryStripArray) shape.getGeometry();
						for (int i = 0; i < gsa.getVertexCount(); i++) {
							gsa.getCoordinates(i, coordinate);
							Vector3d p = new Vector3d(coordinate[0], coordinate[1], coordinate[2]);
							vertex3DList.add(p);
						}						
					} else if (shape.getGeometry() instanceof TriangleArray) {
						// TriangleArrayの場合
						TriangleArray tra = (TriangleArray) shape.getGeometry();
						for (int i = 0; i < tra.getVertexCount(); i++) {
							tra.getCoordinates(i, coordinate);
							Vector3d p = new Vector3d(coordinate[0], coordinate[1], coordinate[2]);
							vertex3DList.add(p);
						}						
					}
		
					if (pattern == 0) {
						// 最大面積法
						Vector3d cv1 = new Vector3d();
						Vector3d cv2 = new Vector3d();
			
						double cvMax = 0.0;
						Vector3d axis1 = new Vector3d(); // 3D頂点から求めた法線ベクトル
						Vector3d axis2 = new Vector3d(); // 2D頂点から求めた法線ベクトル
						Vector3d axis3 = new Vector3d(); // axis1,axis2の外積から求めた法線ベクトル
			
						// 面積を3D頂点リストから求め、法線を求める処理
						for (int i = 0; i < vertex3DList.size(); i += 3) {
							cv1.sub(vertex3DList.get(i + 2), vertex3DList.get(i));
							cv2.sub(vertex3DList.get(i + 1), vertex3DList.get(i));
							Vector3d cv = new Vector3d();
							cv.cross(cv1, cv2);
							if (i == 0 || cv.length() >= cvMax) {
								cvMax = cv.length();
								axis1 = cv;
							}
						}
			
						ProjectionResult pr1 = GeometryUtility.projection3D(vertex3DList, axis1);
						// 辺のを2D頂点リストから求め、法線を求める処理
						for (int i = 0; i < pr1.vertexList.size() - 1; i++) {
							Vector3d cv = new Vector3d();
							cv.sub(vertex3DList.get(i + 1), vertex3DList.get(i));
							if (i == 0 || cv.length() >= cvMax) {
								cvMax = cv.length();
								axis2 = cv;
							}
						}
			
			
						ProjectionResult pr2 = GeometryUtility.projection2D(pr1.vertexList, axis2);
						// axis1,axis2で外積でaxis3を求める。
						axis3.cross(axis1, axis2);
			
						ProjectionResult pr3 = GeometryUtility.projection2D(pr1.vertexList, axis3);
						AxisResult ar = new AxisResult(axis1, axis2, axis3);
			
						// ここから最大面積法で求めた点を取得していく処理
						
						// OBBの生成
						obb = new OBB();
			
						// No.1
						ar.axis1.scale(pr1.min);
						ar.axis2.scale(pr2.min);
						ar.axis3.scale(pr3.min);
						ar.axis1.add(ar.axis2);
						ar.axis3.add(ar.axis1);
						obb.addVertex(ar.axis3);
			
						// No.2
						ar = new AxisResult(axis1, axis2, axis3);
						ar.axis1.scale(pr1.min);
						ar.axis2.scale(pr2.max);
						ar.axis3.scale(pr3.min);
						ar.axis1.add(ar.axis2);
						ar.axis3.add(ar.axis1);
						obb.addVertex(ar.axis3);
			
						// No.3
						ar = new AxisResult(axis1, axis2, axis3);
						ar.axis1.scale(pr1.min);
						ar.axis2.scale(pr2.min);
						ar.axis3.scale(pr3.max);
						ar.axis1.add(ar.axis2);
						ar.axis3.add(ar.axis1);
						obb.addVertex(ar.axis3);
			
						// No.4
						ar = new AxisResult(axis1, axis2, axis3);
						ar.axis1.scale(pr1.min);
						ar.axis2.scale(pr2.max);
						ar.axis3.scale(pr3.max);
						ar.axis1.add(ar.axis2);
						ar.axis3.add(ar.axis1);
						obb.addVertex(ar.axis3);
			
						// No.5
						ar = new AxisResult(axis1, axis2, axis3);
						ar.axis1.scale(pr1.max);
						ar.axis2.scale(pr2.min);
						ar.axis3.scale(pr3.min);
						ar.axis1.add(ar.axis2);
						ar.axis3.add(ar.axis1);
						obb.addVertex(ar.axis3);
			
						// No.6
						ar = new AxisResult(axis1, axis2, axis3);
						ar.axis1.scale(pr1.max);
						ar.axis2.scale(pr2.max);
						ar.axis3.scale(pr3.min);
						ar.axis1.add(ar.axis2);
						ar.axis3.add(ar.axis1);
						obb.addVertex(ar.axis3);
			
						// No.7
						ar = new AxisResult(axis1, axis2, axis3);
						ar.axis1.scale(pr1.max);
						ar.axis2.scale(pr2.min);
						ar.axis3.scale(pr3.max);
						ar.axis1.add(ar.axis2);
						ar.axis3.add(ar.axis1);
						obb.addVertex(ar.axis3);
			
						// No.8
						ar = new AxisResult(axis1, axis2, axis3);
						ar.axis1.scale(pr1.max);
						ar.axis2.scale(pr2.max);
						ar.axis3.scale(pr3.max);
						ar.axis1.add(ar.axis2);
						ar.axis3.add(ar.axis1);
						obb.addVertex(ar.axis3);
						
						// 面および境界多面体の生成
						obb.createPlanes();
					} else {
						// AABB
						double minX, maxX, minY, maxY, minZ, maxZ;
						minX = maxX = vertex3DList.get(0).x;
						minY = maxY = vertex3DList.get(0).y;
						minZ = maxZ = vertex3DList.get(0).z;
						for (int n = 1; n < vertex3DList.size(); n++) {
							Vector3d v = vertex3DList.get(n);
							if (minX > v.x) minX = v.x;
							if (maxX < v.x) maxX = v.x;
							if (minY > v.y) minY = v.y;
							if (maxY < v.y) maxY = v.y;
							if (minZ > v.z) minZ = v.z;
							if (maxZ < v.z) maxZ = v.z;
						}
						obb = new OBB(new Vector3d(minX, minY, minZ),
										new Vector3d(minX, maxY, minZ),
										new Vector3d(minX, minY, maxZ),
										new Vector3d(minX, maxY, maxZ),
										new Vector3d(maxX, minY, minZ),
										new Vector3d(maxX, maxY, minZ),
										new Vector3d(maxX, minY, maxZ),
										new Vector3d(maxX, maxY, maxZ));
					}
				}
			}
			return obb;
		}

		private class AxisResult {
			Vector3d axis1;
			Vector3d axis2;
			Vector3d axis3;

			AxisResult(Vector3d a1, Vector3d a2, Vector3d a3) {
				axis1 = (Vector3d) a1.clone();
				axis2 = (Vector3d) a2.clone();
				axis3 = (Vector3d) a3.clone();
			}
		}		
}

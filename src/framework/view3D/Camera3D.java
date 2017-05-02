package framework.view3D;

import java.util.ArrayList;

import java3d.Transform3D;
import java3d.TransformGroup;
import java3d.Vector3d;
import framework.model3D.GeometryUtility;
import framework.model3D.Object3D;
import framework.model3D.Placeable;
import framework.model3D.Position3D;
import framework.model3D.Universe;

/**
 * 画角調整機能が付いたカメラ<BR>
 * 視点、注視対象、視線のうち2つを指定して使う。
 * @author 新田直也
 *
 */
public class Camera3D {
	private static final double NEAREST = 3.0;
	private static final Vector3d VIEW_FORWARD = new Vector3d(0.0, 0.0, -1.0);
	private static final Vector3d VIEW_DOWN = new Vector3d(0.0, -1.0, 0.0);
	
	private Universe universe = null;

	private double frontClipDistance = 0.5;			// デプスバッファが小さいためあまり小さい値にできない
	private double backClipDistance = 1000.0;		// デプスバッファが小さいためあまり大きい値にできない
	private double fieldOfView = Math.PI / 2.0;
	
	private TransformGroup viewPlatformTransform = null;
	protected ArrayList<Object3D> targetObjList = null;
	protected ArrayList<Position3D> targetList = null;
	protected Position3D viewPoint = null;
	protected Object3D viewPointObj = null;
	private Vector3d viewLine = null;
	private Vector3d cameraBack = null;	
	private Vector3d viewUp = null;
	private boolean fParallel = false;

	// 以下の変数は省メモリ化のため導入
	private Transform3D worldToView = new Transform3D();
	private double matrix[] = new double[]{1.0, 0.0, 0.0, 0.0,
										   0.0, 1.0, 0.0, 0.0,
										   0.0, 0.0, 1.0, 0.0,
										   0.0, 0.0, 0.0, 1.0};
	
	private Transform3D cameraTransform = new Transform3D();
	
	public Camera3D(Universe universe) {
		this.universe = universe;
	}
	
	public Universe getUniverse() {
		return universe;
	}
	
	/**
	 * カメラの注視点を追加する
	 * 
	 * @param target
	 *            注視点
	 */
	public void addTarget(Position3D target) {
		if (targetList == null) targetList = new ArrayList<Position3D>();
		targetList.add(target);
	}
	
	/**
	 * カメラの注視対象を追加する
	 * 
	 * @param target
	 *            注視対象
	 */
	public void addTarget(Object3D target) {
		if (targetObjList == null) targetObjList = new ArrayList<Object3D>();
		targetObjList.add(target);
	}

	/**
	 * カメラの注視対象を追加する
	 * 
	 * @param target
	 *            注視対象
	 */
	public void addTarget(Placeable target) {
		if (targetObjList == null) targetObjList = new ArrayList<Object3D>();
		if (target.getBody() instanceof Object3D) {
			targetObjList.add((Object3D)target.getBody());
		}
	}

	/**
	 * カメラの視点を設定する
	 * 
	 * @param viewPoint
	 *            視点
	 */
	public void setViewPoint(Position3D viewPoint) {
		this.viewPoint = viewPoint;
	}

	/**
	 * カメラの視点を設定する
	 * 
	 * @param viewPoint
	 *            視点となるオブジェクト
	 */
	public void setViewPoint(Object3D viewPointObj) {
		this.viewPointObj = viewPointObj;
	}

	/**
	 * カメラの視点を設定する
	 * 
	 * @param viewPoint
	 *            視点となる登場物
	 */
	public void setViewPoint(Placeable viewPointActor) {
		if (viewPointActor.getBody() instanceof Object3D) {
			viewPointObj = (Object3D)viewPointActor.getBody();
		}
	}

	/**
	 * 手前からの視線に設定する
	 */
	public void setSideView() {
		viewLine = VIEW_FORWARD;
	}

	/**
	 * 上から見下ろした視線に設定する
	 */
	public void setTopView() {
		viewLine = VIEW_DOWN;
	}

	/**
	 * 視線を設定する
	 */
	public void setViewLine(Vector3d viewLine) {
		this.viewLine = viewLine;
	}
	

	/**
	 * 視野角を設定する
	 * @param a 視野角
	 */
	public void setFieldOfView(double a) {
		fieldOfView = a;
	}
	
	/**
	 * 視野角を取得する
	 * @return 視野角
	 */
	public double getFieldOfView() {
		return fieldOfView;
	}
	
	/**
	 * フロントクリップ距離を設定する
	 * @param d
	 */
	public void setFrontClipDistance(double d) {
		frontClipDistance = d;
	}
	
	/**
	 * フロントクリップ距離を取得する
	 * @return フロントクリップ距離
	 */
	public double getFrontClipDistance() {
		return frontClipDistance;
	}

	/**
	 * バッククリップ距離を設定する
	 * @param d
	 */
	public void setBackClipDistance(double d) {
		backClipDistance = d;
	}	

	/**
	 * バッククリップ距離を取得する
	 * @return バッククリップ距離
	 */
	public double getBackClipDistance() {
		return backClipDistance;
	}
	
	public void setViewUp(Vector3d viewUp) {
		this.viewUp = viewUp;
	}
		
	public Position3D getViewPoint() {
		if (viewPoint != null) return viewPoint;
		if (viewPointObj != null) return viewPointObj.getPosition3D();
		// 視点が設定されていない場合
		Vector3d center = getTargetCenter();
		if (center != null) {
			Vector3d vz = new Vector3d();
			if (viewLine != null) {
				// 注視対象と視線が設定されている場合
				vz.negate(viewLine);
			} else {
				// 注視対象のみが設定されている場合
				vz.negate(VIEW_FORWARD);
			}
			Vector3d vx = new Vector3d();
			Vector3d vy = new Vector3d();
			vx.cross(vz, VIEW_DOWN);
			if (vx.length() > GeometryUtility.TOLERANCE) {
				vx.normalize();
			} else {
				vx = new Vector3d(1.0, 0.0, 0.0);
			}
			vy.cross(vz, vx);
			
			// 注視対象から下がる距離
			if (cameraBack != null) {
				vx.scale(cameraBack.x);
				vy.scale(cameraBack.y);
				vz.scale(cameraBack.z);
				center.add(vz);
				center.add(vy);
				center.add(vz);
			} else {
				double z = getStandBackDistance(vx, vy);			
				vz.scale(z);
				center.add(vz);
			}
			return new Position3D(center);
		}
		// 視点も注視点も設定されていない場合
		return new Position3D();
	}
	
	public Vector3d getViewLine() {
		if (viewLine != null) {			
			// 視線が設定されている場合
			return viewLine;
		}
		Vector3d center = getTargetCenter();
		if (center != null) {
			if (viewPoint != null) {
				center.sub(viewPoint.getVector3d());
			} else if (viewPointObj != null) {
				center.sub(viewPointObj.getPosition3D().getVector3d());
			} else {
				center.set(VIEW_FORWARD);
			}
		} else {
			center = new Vector3d(VIEW_FORWARD);
		}
		center.normalize();
		return center;
	}
	
	public Vector3d getViewUp() {
		if (viewUp != null) {
			return viewUp;
		} else {
			Vector3d vy = new Vector3d(0.0, 1.0, 0.0);
			Vector3d vv = (Vector3d) getViewLine().clone();
			vv.cross(vy, vv);
			vv.cross(getViewLine(), vv);
			return vv;
		}
	}
	
	public Vector3d getTargetCenter() {
		Vector3d center = new Vector3d();
		if (targetObjList != null && targetObjList.size() != 0) {
			for (int i = 0; i < targetObjList.size(); i++) {
				Position3D position = targetObjList.get(i).getPosition3D();
				center.add(position.getVector3d());
			}
			center.scale(1.0 / targetObjList.size());
		} else if (targetList != null && targetList.size() != 0) {
			for (int i = 0; i < targetList.size(); i++) {
				Position3D position = targetList.get(i);
				center.add(position.getVector3d());
			}
			center.scale(1.0 / targetList.size());
		} else {
			return null;
		}
		return center;
	}
	
	/**
	 * 注視対象や視点の移動、視線の変化に伴う画角調整
	 */
	public void adjust(long interval) {
		// カメラ座標系（vx, vy, vz）を計算する
		Vector3d vx = new Vector3d(), vy = new Vector3d(), vz = new Vector3d();
		if (viewLine == null) {
			// 視線が設定されていない場合
			if ((viewPoint == null && viewPointObj == null) 
					|| ((targetObjList == null || targetObjList.size() == 0) 
							&& (targetList == null || targetList.size() == 0))) {
				// 視点または注視対象が設定されていない場合、手前からの視線にする
				vz.negate(VIEW_FORWARD);
			} else {
				// 注視対象の重心を注視点とする
				Vector3d center = getTargetCenter();
				if (center == null) center = new Vector3d();
				if (viewPoint != null) {
					center.sub(viewPoint.getVector3d());
				} else {
					center.sub(viewPointObj.getPosition3D().getVector3d());
				}
				center.normalize();
				vz.negate(center);
			}
		} else {
			// 視線が設定されている場合 vz を視線方向 と逆向きに設定する
			viewLine.normalize();
			vz.negate(viewLine);
		}
		vx.cross(vz, VIEW_DOWN);
		if (vx.length() > GeometryUtility.TOLERANCE) {
			vx.normalize();
		} else {
			vx = new Vector3d(1.0, 0.0, 0.0);
		}
		vy.cross(vz, vx);
		
		// 世界座標からカメラ座標への変換を計算する
		if (viewPoint != null || viewPointObj != null) {
			// 視点が設定されている場合
			Vector3d vp;
			if (viewPoint != null) {
				vp = viewPoint.getVector3d();
			} else {
				vp = viewPointObj.getPosition3D().getVector3d();
			}
			matrix[0]  = vx.x; matrix[1]  = vx.y; matrix[2]  = vx.z; matrix[3]  = 0.0;
			matrix[4]  = vy.x; matrix[5]  = vy.y; matrix[6]  = vy.z; matrix[7]  = 0.0;
			matrix[8]  = vz.x; matrix[9]  = vz.y; matrix[10] = vz.z; matrix[11] = 0.0;
			matrix[12] = 0.0;  matrix[13] = 0.0;  matrix[14] = 0.0;  matrix[15] = 1.0;
			worldToView.set(matrix);
			worldToView.invert();
			worldToView.setTranslation(vp);
		} else {
			// 視点が設定されていない場合、注視対象と視線から（カメラ座標系上での）視点を逆計算する
			if ((targetObjList == null || targetObjList.size() == 0)
					&& (targetList == null || targetList.size() == 0)) return;	// 視点も注視対象も設定されていない
	
			// 注視対象の中心
			Vector3d center = getTargetCenter();
			
			// カメラ座標上での注視点の座標
			Vector3d eye = new Vector3d(center.dot(vx), center.dot(vy), center.dot(vz));
			if (cameraBack != null) {
				// カメラの引き
				eye.add(cameraBack);
			} else {
				// 注視対象が入るようにカメラを引く
				double z = getStandBackDistance(vx, vy);
				eye.z += z;
			}
			
			matrix[0]  = vx.x; matrix[1]  = vx.y; matrix[2]  = vx.z; matrix[3]  = -eye.x;
			matrix[4]  = vy.x; matrix[5]  = vy.y; matrix[6]  = vy.z; matrix[7]  = -eye.y;
			matrix[8]  = vz.x; matrix[9]  = vz.y; matrix[10] = vz.z; matrix[11] = -eye.z;
			matrix[12] = 0.0;  matrix[13] = 0.0;  matrix[14] = 0.0;  matrix[15] = 1.0;
			worldToView.set(matrix);
			worldToView.invert();
		}
		if (viewPlatformTransform != null) viewPlatformTransform.setTransform(worldToView);
	}

	public double getStandBackDistance(Vector3d vx, Vector3d vy) {
		double xmax = 0;
		double xmin = 0;
		double ymax = 0;
		double ymin = 0;
		if (targetObjList != null && targetObjList.size() != 0) {
			for (int i = 0; i < targetObjList.size(); i++) {
				Position3D position = targetObjList.get(i).getPosition3D();
				double px = position.getVector3d().dot(vx);
				double py = position.getVector3d().dot(vy);
				if (i == 0) {
					xmax = xmin = px;
					ymax = ymin = py;
				} else {
					if (xmax < px)
						xmax = px;
					if (xmin > px)
						xmin = px;
					if (ymax < py)
						ymax = py;
					if (ymin > py)
						ymin = py;
				}
			}
		} else if (targetList != null && targetList.size() != 0) {
			for (int i = 0; i < targetList.size(); i++) {
				Position3D position = targetList.get(i);
				double px = position.getVector3d().dot(vx);
				double py = position.getVector3d().dot(vy);
				if (i == 0) {
					xmax = xmin = px;
					ymax = ymin = py;
				} else {
					if (xmax < px)
						xmax = px;
					if (xmin > px)
						xmin = px;
					if (ymax < py)
						ymax = py;
					if (ymin > py)
						ymin = py;
				}
			}
		}

		double x = (xmax + xmin) / 2;
		double y = (ymax + ymin) / 2;
		double x_diff = Math.abs(xmax - x);
		double y_diff = Math.abs(ymax - y);
		if (x_diff < NEAREST)
			x_diff = NEAREST;
		if (y_diff < NEAREST)
			y_diff = NEAREST;
		double z;
		if (x_diff < y_diff) {
			z = y_diff / Math.tan(Math.PI / 18.0);
		} else {
			z = x_diff / Math.tan(Math.PI / 18.0);
		}
		return z;
	}
	
	public void setWorldToView(Position3D vp, Vector3d vl) {
		setViewPoint(vp);
		setViewLine(vl);
		//上方向のベクトル計算
		Vector3d vy = new Vector3d(0, 1, 0);
		Vector3d vv = (Vector3d) vl.clone();
		vv.cross(vy, vv);
		vv.cross(vv, vl);
//		activity.setCamera(vp, vl, vv);
	}
	
	public void setParallel(){
		this.fParallel = true;
	}
	
	public boolean isParallel(){
		return this.fParallel;
	}
	
}

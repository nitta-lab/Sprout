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
 * ��p�����@�\���t�����J����<BR>
 * ���_�A�����ΏہA�����̂���2���w�肵�Ďg���B
 * @author �V�c����
 *
 */
public class Camera3D {
	private static final double NEAREST = 3.0;
	private static final Vector3d VIEW_FORWARD = new Vector3d(0.0, 0.0, -1.0);
	private static final Vector3d VIEW_DOWN = new Vector3d(0.0, -1.0, 0.0);
	
	private Universe universe = null;

	private double frontClipDistance = 0.5;			// �f�v�X�o�b�t�@�����������߂��܂菬�����l�ɂł��Ȃ�
	private double backClipDistance = 1000.0;		// �f�v�X�o�b�t�@�����������߂��܂�傫���l�ɂł��Ȃ�
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

	// �ȉ��̕ϐ��͏ȃ��������̂��ߓ���
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
	 * �J�����̒����_��ǉ�����
	 * 
	 * @param target
	 *            �����_
	 */
	public void addTarget(Position3D target) {
		if (targetList == null) targetList = new ArrayList<Position3D>();
		targetList.add(target);
	}
	
	/**
	 * �J�����̒����Ώۂ�ǉ�����
	 * 
	 * @param target
	 *            �����Ώ�
	 */
	public void addTarget(Object3D target) {
		if (targetObjList == null) targetObjList = new ArrayList<Object3D>();
		targetObjList.add(target);
	}

	/**
	 * �J�����̒����Ώۂ�ǉ�����
	 * 
	 * @param target
	 *            �����Ώ�
	 */
	public void addTarget(Placeable target) {
		if (targetObjList == null) targetObjList = new ArrayList<Object3D>();
		if (target.getBody() instanceof Object3D) {
			targetObjList.add((Object3D)target.getBody());
		}
	}

	/**
	 * �J�����̎��_��ݒ肷��
	 * 
	 * @param viewPoint
	 *            ���_
	 */
	public void setViewPoint(Position3D viewPoint) {
		this.viewPoint = viewPoint;
	}

	/**
	 * �J�����̎��_��ݒ肷��
	 * 
	 * @param viewPoint
	 *            ���_�ƂȂ�I�u�W�F�N�g
	 */
	public void setViewPoint(Object3D viewPointObj) {
		this.viewPointObj = viewPointObj;
	}

	/**
	 * �J�����̎��_��ݒ肷��
	 * 
	 * @param viewPoint
	 *            ���_�ƂȂ�o�ꕨ
	 */
	public void setViewPoint(Placeable viewPointActor) {
		if (viewPointActor.getBody() instanceof Object3D) {
			viewPointObj = (Object3D)viewPointActor.getBody();
		}
	}

	/**
	 * ��O����̎����ɐݒ肷��
	 */
	public void setSideView() {
		viewLine = VIEW_FORWARD;
	}

	/**
	 * �ォ�猩���낵�������ɐݒ肷��
	 */
	public void setTopView() {
		viewLine = VIEW_DOWN;
	}

	/**
	 * ������ݒ肷��
	 */
	public void setViewLine(Vector3d viewLine) {
		this.viewLine = viewLine;
	}
	

	/**
	 * ����p��ݒ肷��
	 * @param a ����p
	 */
	public void setFieldOfView(double a) {
		fieldOfView = a;
	}
	
	/**
	 * ����p���擾����
	 * @return ����p
	 */
	public double getFieldOfView() {
		return fieldOfView;
	}
	
	/**
	 * �t�����g�N���b�v������ݒ肷��
	 * @param d
	 */
	public void setFrontClipDistance(double d) {
		frontClipDistance = d;
	}
	
	/**
	 * �t�����g�N���b�v�������擾����
	 * @return �t�����g�N���b�v����
	 */
	public double getFrontClipDistance() {
		return frontClipDistance;
	}

	/**
	 * �o�b�N�N���b�v������ݒ肷��
	 * @param d
	 */
	public void setBackClipDistance(double d) {
		backClipDistance = d;
	}	

	/**
	 * �o�b�N�N���b�v�������擾����
	 * @return �o�b�N�N���b�v����
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
		// ���_���ݒ肳��Ă��Ȃ��ꍇ
		Vector3d center = getTargetCenter();
		if (center != null) {
			Vector3d vz = new Vector3d();
			if (viewLine != null) {
				// �����ΏۂƎ������ݒ肳��Ă���ꍇ
				vz.negate(viewLine);
			} else {
				// �����Ώۂ݂̂��ݒ肳��Ă���ꍇ
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
			
			// �����Ώۂ��牺���鋗��
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
		// ���_�������_���ݒ肳��Ă��Ȃ��ꍇ
		return new Position3D();
	}
	
	public Vector3d getViewLine() {
		if (viewLine != null) {			
			// �������ݒ肳��Ă���ꍇ
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
	 * �����Ώۂ⎋�_�̈ړ��A�����̕ω��ɔ�����p����
	 */
	public void adjust(long interval) {
		// �J�������W�n�ivx, vy, vz�j���v�Z����
		Vector3d vx = new Vector3d(), vy = new Vector3d(), vz = new Vector3d();
		if (viewLine == null) {
			// �������ݒ肳��Ă��Ȃ��ꍇ
			if ((viewPoint == null && viewPointObj == null) 
					|| ((targetObjList == null || targetObjList.size() == 0) 
							&& (targetList == null || targetList.size() == 0))) {
				// ���_�܂��͒����Ώۂ��ݒ肳��Ă��Ȃ��ꍇ�A��O����̎����ɂ���
				vz.negate(VIEW_FORWARD);
			} else {
				// �����Ώۂ̏d�S�𒍎��_�Ƃ���
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
			// �������ݒ肳��Ă���ꍇ vz ���������� �Ƌt�����ɐݒ肷��
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
		
		// ���E���W����J�������W�ւ̕ϊ����v�Z����
		if (viewPoint != null || viewPointObj != null) {
			// ���_���ݒ肳��Ă���ꍇ
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
			// ���_���ݒ肳��Ă��Ȃ��ꍇ�A�����ΏۂƎ�������i�J�������W�n��ł́j���_���t�v�Z����
			if ((targetObjList == null || targetObjList.size() == 0)
					&& (targetList == null || targetList.size() == 0)) return;	// ���_�������Ώۂ��ݒ肳��Ă��Ȃ�
	
			// �����Ώۂ̒��S
			Vector3d center = getTargetCenter();
			
			// �J�������W��ł̒����_�̍��W
			Vector3d eye = new Vector3d(center.dot(vx), center.dot(vy), center.dot(vz));
			if (cameraBack != null) {
				// �J�����̈���
				eye.add(cameraBack);
			} else {
				// �����Ώۂ�����悤�ɃJ����������
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
		//������̃x�N�g���v�Z
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

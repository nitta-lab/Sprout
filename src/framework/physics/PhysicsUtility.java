package framework.physics;

import java.util.ArrayList;

import java3d.BoundingSphere;
import java3d.Transform3D;
import java3d.Vector3d;

import framework.model3D.BoundingSurface;
import framework.model3D.CollisionResult;
import framework.model3D.Object3D;
import framework.model3D.Position3D;


/**
 * �������Z�̃R�A
 * @author �V�c����
 *
 */
public class PhysicsUtility {
	public static final double GRAVITY = 9.8;	// �d�͂̉����x
	public static final Vector3d horizon = new Vector3d(1.0, 0.0, 0.0);
	public static final Vector3d vertical = new Vector3d(0.0, 1.0, 0.0);

	public static Vector3d gravityDirection = new Vector3d(0.0, 1.0, 0.0);

	/**
	 * ���̂ɉ����d�͂����߂�
	 * @param body �Ώە��� 
	 * @return body�ɉ����d��
	 */
	public static Force3D getGravity(Solid3D body) {
		return new Force3D(gravityDirection.x * -body.mass * GRAVITY, gravityDirection.y * -body.mass * GRAVITY, gravityDirection.z * -body.mass * GRAVITY);
	}

	/**
	 * @param v
	 *            �F�P�ʃx�N�g���A���邢�̓[���x�N�g���������œn������
	 */
	public static void setGravityDirection(Vector3d v) {
		gravityDirection = new Vector3d(v.x, v.y, v.z);
	}

	// ���[�����g�̌v�Z
	static Vector3d calcMoment(Force3D f, Position3D gravityCenter,
			Position3D applicationPoint) {
		Vector3d v1 = applicationPoint.getVector3d();
		Vector3d v2 = gravityCenter.getVector3d();
		v1.sub(v2);

		Vector3d cv = new Vector3d();
		Vector3d fv = f.getVector3d();
		cv.cross(v1, fv);
		return cv;

	}

	/**
	 * ���̂̔����W������Փˎ��ɉ����͂����߂�
	 * @param interval �Փ˂��Ă��鎞��
	 * @param nor ���̂��Ԃ������ʂ̕��x�N�g��
	 * @param solid ����
	 * @return �Փˎ��ɉ�����
	 */
	public static Force3D calcForce(long interval, Vector3d nor, Solid3D solid) {
		double f1 = 0.0;
		Vector3d vf = new Vector3d(solid.getVelocity().getX(), solid
				.getVelocity().getY(), solid.getVelocity().getZ());
		f1 = solid.mass * (vf.length() + solid.e * vf.length())
				/ ((double) interval / 1000.0);
		nor.scale(f1);
		Force3D f = new Force3D(nor.x, nor.y, nor.z);
		return f;
	}

	/**
	 * ���̂ƒn�ʂƂ̏Փ˔���
	 * @param obj ����
	 * @param ground �n��
	 * @return �Փˏ��inull�̂Ƃ��Փ˂Ȃ��j
	 */
	public static CollisionResult doesIntersect(Solid3D obj, Ground ground) {
		if (ground == null) return null;
		CollisionResult cr = null;
		BoundingSurface boundingSurface = ground.getBoundingSurface();

		// BoundingSphere���g���đ�G�c�ɏՓ˔�����s��
		ArrayList<BoundingSurface> boundingSurfaceList = null;
		if (obj.bs != null) {
			BoundingSphere bs = (BoundingSphere) (obj.bs.clone());
			Transform3D t3d = new Transform3D();
			obj.center.getTransform(t3d);
			bs.transform(t3d);
			obj.scale.getTransform(t3d);
			bs.transform(t3d);
			obj.rot.getTransform(t3d);
			bs.transform(t3d);
			obj.pos.getTransform(t3d);
			bs.transform(t3d);
			// �e���Փ˔�����s���i�ŏ�ʂ�BoundingSurface��BoundingSphere�̊ԂŁj
			boundingSurfaceList = boundingSurface.intersect(bs);
			bs = null;
			t3d = null;
		}

		if (obj.bs == null) {
			// BoundingSphere ���܂�����Ă��ȂȂ������ꍇ�A
			// �ڍׂȏՓ˔���̂��߂ɁA�ŏ�ʂ̑S BoundingSurface ���擾����
			if (boundingSurfaceList == null) boundingSurfaceList = new ArrayList<BoundingSurface>();
			boundingSurfaceList.add(boundingSurface);
		}
		
		if (boundingSurfaceList.size() > 0) {
			// �e���Փ˔���ŏՓ˂��Ă����ꍇ�AOBB�̏W����p���Ă��ڂ����Փ˔�����s��
			// �iBoundingSphere ���܂�����Ă��Ȃ��ꍇ�AOBB �̍쐬�Ɠ����� BoundingSphere ���쐬�����j
			BoundingBoxVisitor obbVisitor = new BoundingBoxVisitor();
			obj.accept(obbVisitor);
			for (int i = 0; i < obbVisitor.getObbList().size(); i++) {
				// OBB�ƏՓ˔��������ꍇ�́A�n�ʂ𑽊p�`�̂܂܈���
				for (int j = 0; j < boundingSurfaceList.size(); j++) {
					cr = boundingSurfaceList.get(j).intersect(obbVisitor.getObbList().get(i));
					if (cr != null) {
						return cr;
					}
				}
			}
			obbVisitor = null;			
		}
		return null;
	}

	/**
	 * ���̓��m�̏Փ˔���
	 * @param obj1 ����1
	 * @param part1 ���肷�镨��1�̕����̖���
	 * @param obj2 ����2
	 * @param part2 ���肷�镨��2�̕����̖���
	 * @return�@�Փˏ��inull�̂Ƃ��Փ˂Ȃ��j
	 */
	public static CollisionResult checkCollision(Object3D obj1, String part1,
			Object3D obj2, String part2) {
		CollisionResult cr = null;

		// BoundingSphere���g���đ�G�c�ɏՓ˔�����s��
		boolean f = false;
		if (obj1.bs != null && obj2.bs != null) {
			// sol1 �� BoundingSphere�@���v�Z
			BoundingSphere bs1 = (BoundingSphere) (obj1.bs.clone());
			Transform3D t3d = new Transform3D();
			obj1.center.getTransform(t3d);
			bs1.transform(t3d);
			obj1.scale.getTransform(t3d);
			bs1.transform(t3d);
			obj1.rot.getTransform(t3d);
			bs1.transform(t3d);
			obj1.pos.getTransform(t3d);
			bs1.transform(t3d);

			// sol2 �� BoundingSphere�@���v�Z
			BoundingSphere bs2 = (BoundingSphere) (obj2.bs.clone());
			obj2.center.getTransform(t3d);
			bs2.transform(t3d);
			obj2.scale.getTransform(t3d);
			bs2.transform(t3d);
			obj2.rot.getTransform(t3d);
			bs2.transform(t3d);
			obj2.pos.getTransform(t3d);
			bs2.transform(t3d);
			
			// BoundingSphere ���m�̏Փ˔���
			if (bs1.intersect(bs2)) {
				f = true;
			}
			t3d = null;
			bs1 = null;
			bs2 = null;
		}
		if (f || obj1.bs == null || obj2.bs == null) {
			BoundingBoxVisitor visitor1 = new BoundingBoxVisitor(part1);
			BoundingBoxVisitor visitor2 = new BoundingBoxVisitor(part2);
			obj1.accept(visitor1);
			obj2.accept(visitor2);
			// OBB o1 = obj1.getOBB(0);
			// OBB o2 = obj2.getOBB(0);
			// cr = o1.intersect(o2);
			int i, j;
			for (i = 0; i < visitor1.getObbList().size(); i++) {
				for (j = 0; j < visitor2.getObbList().size(); j++) {
					cr = visitor2.getObbList().get(j).intersect(visitor1.getObbList().get(i));
					// System.out.println("flg:"+flg);
					if (cr != null) {
						// Vector3d v = new Vector3d(0,0,0);
						// System.out.println("checkColision1
						// Yes!!"+cr.collisionPoint.getX());
						// System.out.println("checkColision1
						// Yes!!"+cr.collisionPoint.getY());
						// System.out.println("checkColision1
						// Yes!!"+cr.collisionPoint.getZ());
						// return v;
						return cr;
					} else {
						cr = visitor1.getObbList().get(i).intersect(
								visitor2.getObbList().get(j));
						if (cr != null) {
							cr.normal.scale(-1.0);
							// Vector3d v = new Vector3d(0,0,0);
							// System.out.println("checkColision2
							// Yes!!"+cr.collisionPoint.getX());
							// System.out.println("checkColision2
							// Yes!!"+cr.collisionPoint.getY());
							// System.out.println("checkColision2
							// Yes!!"+cr.collisionPoint.getZ());
							// return v;
							return cr;
						}
					}
				}
			}
		}
		// System.out.println("Yes!!");
		return null;
	}
}

package framework.physics;

import java.util.ArrayList;

import java3d.AxisAngle4d;
import java3d.Vector3d;

import framework.model3D.Object3D;
import framework.model3D.Position3D;
import framework.model3D.Quaternion3D;

/**
 * �����I�ȐU�镑�������镨�́i���́j��\��
 * @author �V�c����
 *
 */
public class Solid3D extends Object3D {
	private Velocity3D velocity;
	private AngularVelocity3D angularvelocity;
	private Position3D gravityCenter = getPosition3D();
	public double e = 1.0;
	public double mass = 10;
	private Inertia3D inertia = null;

	// �R�s�[�R���X�g���N�^
	public Solid3D(Object3D obj) {
		super(obj);
		velocity = new Velocity3D();
		angularvelocity = new AngularVelocity3D();
		inertia = new Inertia3D(this);
	}

	public Solid3D(Object3D obj, double mass) {
		super(obj);
		velocity = new Velocity3D();
		angularvelocity = new AngularVelocity3D();
		this.mass = mass;
		inertia = new Inertia3D(this);
	}

	public Solid3D(Solid3D solid) {
		super(solid);
		if (solid.velocity != null) {
			velocity = new Velocity3D(solid.velocity);
		} else {
			velocity = new Velocity3D();
		}
		if (solid.angularvelocity != null) {
			angularvelocity = new AngularVelocity3D(solid.angularvelocity);
		} else {
			angularvelocity = new AngularVelocity3D();
		}
		mass = solid.mass;
		inertia = new Inertia3D(this);
	}

	/**
	 * �͊w�^���̌v�Z�i�����͂�1�̏ꍇ�j
	 * @param interval �P�ʎ���
	 * @param f ��
	 * @param applicationPoint �͂̍�p�_
	 */
	public void move(long interval, Force3D f, Position3D applicationPoint) {
		// ���[�����g�̌v�Z
		Vector3d moment = PhysicsUtility.calcMoment(f, getGravityCenter(),
				applicationPoint);
		moveSub(interval, f, moment);
	}
	
	/**
	 * �͊w�^���̌v�Z�i�����ɕ����̗͂������ꍇ�j
	 * @param interval �P�ʎ���
	 * @param forces �́i�����j
	 * @param appPoints ���ꂼ��̗͂̍�p�_
	 */
	public void move(long interval, ArrayList<Force3D> forces,
			ArrayList<Position3D> appPoints) {
		// �d�S�ɉ����͂̍��v�����߂�
		Force3D f = new Force3D(0.0, 0.0, 0.0);
		for (int n = 0; n < forces.size(); n++) {
			f.add(forces.get(n));
		}
		
		// ���[�����g�̍��v���v�Z����
		Position3D gc = getGravityCenter();
		Vector3d moment = new Vector3d(0.0, 0.0, 0.0);
		for (int n2 = 0; n2 < forces.size(); n2++) {
			moment.add(PhysicsUtility.calcMoment(forces.get(n2), gc, appPoints.get(n2)));
		}
		moveSub(interval, f, moment);		
	}
	
	private void moveSub(long interval, Force3D f, Vector3d moment) {
		// 1.�d�S�̉^���������i�j���[�g���������j
		// �����x�A���x�v�Z
		Vector3d deltaV = f.getVector3d(); // �̓x�N�g���̎擾
		deltaV.scale(1.0 / mass * ((double) interval / 1000.0)); // �����x���瑬�x�̍������v�Z
		Velocity3D v = getVelocity().add(deltaV); // ���x�ɍ��������Z
		apply(v, false);
		
		// �d�S�ʒu�v�Z
		Vector3d deltaP = velocity.getVector3d(); // ���x�x�N�g���̎擾
		deltaP.scale(((double) interval / 1000.0));
		Position3D p = getPosition3D().add(deltaP); // �ʒu�ɍ��������Z
		apply(p, false);

		// 2.�I�C���[�̊p�^��������

		// �p�����x�A�p���x�v�Z
		AngularVelocity3D w = getAngularVelocity();
		Vector3d deltaAngularV = new Vector3d(
				(moment.x + (inertia.iyy - inertia.izz) * w.getY() * w.getZ()) / inertia.ixx,
				(moment.y + (inertia.izz - inertia.ixx) * w.getZ() * w.getX()) / inertia.iyy, 
				(moment.z + (inertia.ixx - inertia.iyy) * w.getX() * w.getY()) / inertia.izz);
		deltaAngularV.scale((double) interval / 1000.0);
		w.add(deltaAngularV);
		apply(w, false);

		// �p���x�ɂ���]�v�Z
		AxisAngle4d axisAngle = w.getAxisAngle4d();
		axisAngle.angle *= ((double) interval / 1000.0);
		Quaternion3D q = getQuaternion().add(axisAngle);
		apply(q, false);		
	}

	// ���������
	public Object3D duplicate() {
		Object3D copy = new Solid3D(this);
		return copy;
	}
	
	public void scale(double s) {
		super.scale(s);
		inertia = new Inertia3D(this);
	}
	
	public void scale(double sx, double sy, double sz) {
		super.scale(sx, sy, sz);
		inertia = new Inertia3D(this);
	}

	public Velocity3D getVelocity() {
		return (Velocity3D) velocity.clone();
	}

	public AngularVelocity3D getAngularVelocity() {
		return (AngularVelocity3D) angularvelocity.clone();
	}

	// Velocity3D �� applyTo �ȊO����͌Ă΂Ȃ�����
	void setVelocity(Velocity3D v) {
		velocity = (Velocity3D) v.clone();
	}

	// AngularVelocity3D �� applyTo �ȊO����͌Ă΂Ȃ�����
	void setAngularVelocity(AngularVelocity3D w) {
		angularvelocity = (AngularVelocity3D) w.clone();
	}

	public void setGravityCenter(Position3D gravityCenter) {
		this.gravityCenter = gravityCenter;
	}

	public Position3D getGravityCenter() {
		return getPosition3D().add(gravityCenter);
	}

	public void setMass(double mass) {
		this.mass = mass;
		inertia = new Inertia3D(this);
	}

	public double getMass() {
		return mass;
	}
	
}

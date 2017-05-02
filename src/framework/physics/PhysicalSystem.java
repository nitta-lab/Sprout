package framework.physics;

import java.util.ArrayList;
import java3d.Vector3d;

import framework.model3D.CollisionResult;
import framework.model3D.Position3D;


public class PhysicalSystem {

	public ArrayList<Solid3D> objects = new ArrayList<Solid3D>();

	// ���̂̑}��
	public  int add(Solid3D s) {
		objects.add(s);
		return objects.size() - 1;
	}

	// ���̂̉^��
	public void motion(int id, long interval, Force3D f, Position3D appPoint, Ground ground) {
		ArrayList<Force3D> forces[] = new ArrayList[objects.size()];
		ArrayList<Position3D> appPoints[] = new ArrayList[objects.size()];
		for (int i = 0; i < objects.size(); i++) {
			forces[i] = new ArrayList<Force3D>();
			appPoints[i] = new ArrayList<Position3D>();
		}
		
		// id�Ԗڂ̊O�͂̌v�Z
		forces[id].add(f);
		appPoints[id].add(appPoint);
//		objects.get(id).move(interval, f, appPoint); 

		double l; // �΂˂̐L��

		Force3D penalty = new Force3D(0.0, 0.0, 0.0); // �y�i���e�B�@�ɂ��y�i���e�B�̍�p�̗�
		Force3D inversepenalty; //
		// //�y�i���e�B�@�ɂ��y�i���e�B�̔���p�̗�
		CollisionResult cr;
		Solid3D s;
		for (int n = 0; n < objects.size(); n++) {
			// �d�͂̌v�Z
			s = objects.get(n);
			forces[n].add(PhysicsUtility.getGravity(s));
			appPoints[n].add(s.getGravityCenter());
//			objects.get(n).move(interval,
//					PhysicsFacade.getGravity(objects.get(n)),
//					objects.get(n).getGravityCenter()); // �d�͂̌v�Z
			// �n�ʂƂ̓����蔻��
			cr = PhysicsUtility.doesIntersect(s, ground);
			// �n�ʂɕ��̂��߂荞��ł���ꍇ
			if (cr != null) {
				double gk = 5000.0; // �n�ʂł̂΂ˌW��
				double e = 1.0; // �n�ʂł̒��˕Ԃ莞�̒�R�W��
				double b = 300.0;
				l = cr.length;
				// <��p�̗͂̌v�Z>
				// �y�i���e�B�̕ϐ�
//				Vector3d v = cr.normal;
//				v.scale(gk * l);
				// ��p�_�x�N�g���̍쐬
				Vector3d r = cr.collisionPoint.getVector3d();
				// (��p�_-�d�S)�x�N�g��
				r.sub(s.getGravityCenter().getVector3d());
				// �p���x�x�N�g���̍쐬
				Vector3d angVel = s.getAngularVelocity().getVector3d();
				// �p���x�x�N�g����(��p�_-�d�S)�x�N�g���̊O�όv�Z
				angVel.cross(angVel, r);
				// ���x�x�N�g��+�p���x�x�N�g����(��p�_-�d�S)�x�N�g���̊O�όv�Z
				Vector3d relV = s.getVelocity().getVector3d();
				// ���Α��x�x�N�g���̍쐬
				relV.add(angVel);
				Vector3d v = cr.normal;
//System.out.println(r + "," + (gk * l) + "," + (- relV.dot(v) * b));
				// �y�i���e�B�̑傫������
				v.scale(gk * l - relV.dot(v) * b);
				penalty = new Force3D(v);

				// ��p�̗͂ɂ��^��
				forces[n].add(penalty);
				appPoints[n].add(cr.collisionPoint);
//				objects.get(n).move(interval, penalty, cr.collisionPoint);
			}
			// �n�ʂɕ��̂��߂荞��ł��Ȃ��ꍇ
			else {
			}
			for (int m = 0; m < n; m++) {
				Solid3D s1 = objects.get(n);
				Solid3D s2 = objects.get(m);
				cr = PhysicsUtility.checkCollision(s1, null, s2, null);
				// ���̂��߂荞��ł���ꍇ
				if (cr != null) {
					double sk = 5000; // ���̂ł̂΂ˌW��
					double e = 0.2; // ���̂ł̒��˕Ԃ莞�̒�R�W��
					double b = 300.0;
					l = cr.length;
					// <��p�̗͂̌v�Z>
					// ��p�_�x�N�g���̍쐬
					// s1�Ɋւ���v�Z
					// s1�̊p���x�x�N�g���̍쐬
					Vector3d r = cr.collisionPoint.getVector3d();
					r.sub(s1.getGravityCenter().getVector3d());
					Vector3d s1AngVel = s1.getAngularVelocity().getVector3d();
					s1AngVel.cross(s1AngVel, r);
					// s1�̑��x�x�N�g���̍쐬
					Vector3d s1RelV = s1.getVelocity().getVector3d();
					// s1�̑��x�x�N�g��+s1�̊p���x�x�N�g����(��p�_-s1�̏d�S)�x�N�g���̊O�όv�Z
					s1RelV.add(s1AngVel);
					// s2�Ɋւ���v�Z
					// s2�̊p���x�x�N�g���̍쐬
					r = cr.collisionPoint.getVector3d();
					r.sub(s2.getGravityCenter().getVector3d());
					Vector3d s2AngVel = s2.getAngularVelocity().getVector3d();
					s2AngVel.cross(s2AngVel, r);
					// s2�̑��x�x�N�g���̍쐬
					Vector3d s2RelV = s2.getVelocity().getVector3d();
					// s2�̑��x�x�N�g��+s2�̊p���x�x�N�g����(��p�_-s2�̏d�S)�x�N�g���̊O�όv�Z
					s2RelV.add(s2AngVel);
					// ���Α��x�x�N�g���̍쐬
					s1RelV.sub(s2RelV);
					// �y�i���e�B�̑傫������
					Vector3d v = (Vector3d)cr.normal.clone();
//System.out.println(r + "," + (sk * l) + "," + (- relV.dot(v) * b));
					v.scale(sk * l - s1RelV.dot(v) * b);
					penalty = new Force3D(v);

					// ����p�̗͂̌v�Z
					v.scale(-1);
					inversepenalty = new Force3D(v);

					// ��p�̗͂ɂ�镨�̂̈ړ�
					forces[n].add(penalty);
					appPoints[n].add(cr.collisionPoint);
//					s1.move(interval, penalty, cr.collisionPoint);

					// ����p�̗͂ɂ�镨�̂̈ړ�
					forces[m].add(inversepenalty);
					appPoints[m].add(cr.collisionPoint);
//					s2.move(interval, inversepenalty, cr.collisionPoint);
				}
//				// ���̂��߂荞��ł��Ȃ��ꍇ
//				else {
//					s2.move(interval, f, s2.getGravityCenter());
//					s1.move(interval, f, s1.getGravityCenter());
//				}
			}
		}
		for (int n2 = 0; n2 < objects.size(); n2++) {
			objects.get(n2).move(interval, forces[n2], appPoints[n2]);				
		}
	}
}

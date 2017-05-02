package framework.physics;

import java.util.ArrayList;
import java3d.Vector3d;

import framework.model3D.CollisionResult;
import framework.model3D.Position3D;


public class PhysicalSystem {

	public ArrayList<Solid3D> objects = new ArrayList<Solid3D>();

	// 物体の挿入
	public  int add(Solid3D s) {
		objects.add(s);
		return objects.size() - 1;
	}

	// 物体の運動
	public void motion(int id, long interval, Force3D f, Position3D appPoint, Ground ground) {
		ArrayList<Force3D> forces[] = new ArrayList[objects.size()];
		ArrayList<Position3D> appPoints[] = new ArrayList[objects.size()];
		for (int i = 0; i < objects.size(); i++) {
			forces[i] = new ArrayList<Force3D>();
			appPoints[i] = new ArrayList<Position3D>();
		}
		
		// id番目の外力の計算
		forces[id].add(f);
		appPoints[id].add(appPoint);
//		objects.get(id).move(interval, f, appPoint); 

		double l; // ばねの伸び

		Force3D penalty = new Force3D(0.0, 0.0, 0.0); // ペナルティ法によるペナルティの作用の力
		Force3D inversepenalty; //
		// //ペナルティ法によるペナルティの反作用の力
		CollisionResult cr;
		Solid3D s;
		for (int n = 0; n < objects.size(); n++) {
			// 重力の計算
			s = objects.get(n);
			forces[n].add(PhysicsUtility.getGravity(s));
			appPoints[n].add(s.getGravityCenter());
//			objects.get(n).move(interval,
//					PhysicsFacade.getGravity(objects.get(n)),
//					objects.get(n).getGravityCenter()); // 重力の計算
			// 地面との当たり判定
			cr = PhysicsUtility.doesIntersect(s, ground);
			// 地面に物体がめり込んでいる場合
			if (cr != null) {
				double gk = 5000.0; // 地面でのばね係数
				double e = 1.0; // 地面での跳ね返り時の抵抗係数
				double b = 300.0;
				l = cr.length;
				// <作用の力の計算>
				// ペナルティの変数
//				Vector3d v = cr.normal;
//				v.scale(gk * l);
				// 作用点ベクトルの作成
				Vector3d r = cr.collisionPoint.getVector3d();
				// (作用点-重心)ベクトル
				r.sub(s.getGravityCenter().getVector3d());
				// 角速度ベクトルの作成
				Vector3d angVel = s.getAngularVelocity().getVector3d();
				// 角速度ベクトルと(作用点-重心)ベクトルの外積計算
				angVel.cross(angVel, r);
				// 速度ベクトル+角速度ベクトルと(作用点-重心)ベクトルの外積計算
				Vector3d relV = s.getVelocity().getVector3d();
				// 相対速度ベクトルの作成
				relV.add(angVel);
				Vector3d v = cr.normal;
//System.out.println(r + "," + (gk * l) + "," + (- relV.dot(v) * b));
				// ペナルティの大きさ決定
				v.scale(gk * l - relV.dot(v) * b);
				penalty = new Force3D(v);

				// 作用の力による運動
				forces[n].add(penalty);
				appPoints[n].add(cr.collisionPoint);
//				objects.get(n).move(interval, penalty, cr.collisionPoint);
			}
			// 地面に物体がめり込んでいない場合
			else {
			}
			for (int m = 0; m < n; m++) {
				Solid3D s1 = objects.get(n);
				Solid3D s2 = objects.get(m);
				cr = PhysicsUtility.checkCollision(s1, null, s2, null);
				// 物体がめり込んでいる場合
				if (cr != null) {
					double sk = 5000; // 物体でのばね係数
					double e = 0.2; // 物体での跳ね返り時の抵抗係数
					double b = 300.0;
					l = cr.length;
					// <作用の力の計算>
					// 作用点ベクトルの作成
					// s1に関する計算
					// s1の角速度ベクトルの作成
					Vector3d r = cr.collisionPoint.getVector3d();
					r.sub(s1.getGravityCenter().getVector3d());
					Vector3d s1AngVel = s1.getAngularVelocity().getVector3d();
					s1AngVel.cross(s1AngVel, r);
					// s1の速度ベクトルの作成
					Vector3d s1RelV = s1.getVelocity().getVector3d();
					// s1の速度ベクトル+s1の角速度ベクトルと(作用点-s1の重心)ベクトルの外積計算
					s1RelV.add(s1AngVel);
					// s2に関する計算
					// s2の角速度ベクトルの作成
					r = cr.collisionPoint.getVector3d();
					r.sub(s2.getGravityCenter().getVector3d());
					Vector3d s2AngVel = s2.getAngularVelocity().getVector3d();
					s2AngVel.cross(s2AngVel, r);
					// s2の速度ベクトルの作成
					Vector3d s2RelV = s2.getVelocity().getVector3d();
					// s2の速度ベクトル+s2の角速度ベクトルと(作用点-s2の重心)ベクトルの外積計算
					s2RelV.add(s2AngVel);
					// 相対速度ベクトルの作成
					s1RelV.sub(s2RelV);
					// ペナルティの大きさ決定
					Vector3d v = (Vector3d)cr.normal.clone();
//System.out.println(r + "," + (sk * l) + "," + (- relV.dot(v) * b));
					v.scale(sk * l - s1RelV.dot(v) * b);
					penalty = new Force3D(v);

					// 反作用の力の計算
					v.scale(-1);
					inversepenalty = new Force3D(v);

					// 作用の力による物体の移動
					forces[n].add(penalty);
					appPoints[n].add(cr.collisionPoint);
//					s1.move(interval, penalty, cr.collisionPoint);

					// 反作用の力による物体の移動
					forces[m].add(inversepenalty);
					appPoints[m].add(cr.collisionPoint);
//					s2.move(interval, inversepenalty, cr.collisionPoint);
				}
//				// 物体がめり込んでいない場合
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

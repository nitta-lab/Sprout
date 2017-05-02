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
 * 物理演算のコア
 * @author 新田直也
 *
 */
public class PhysicsUtility {
	public static final double GRAVITY = 9.8;	// 重力の加速度
	public static final Vector3d horizon = new Vector3d(1.0, 0.0, 0.0);
	public static final Vector3d vertical = new Vector3d(0.0, 1.0, 0.0);

	public static Vector3d gravityDirection = new Vector3d(0.0, 1.0, 0.0);

	/**
	 * 物体に加わる重力を求める
	 * @param body 対象物体 
	 * @return bodyに加わる重力
	 */
	public static Force3D getGravity(Solid3D body) {
		return new Force3D(gravityDirection.x * -body.mass * GRAVITY, gravityDirection.y * -body.mass * GRAVITY, gravityDirection.z * -body.mass * GRAVITY);
	}

	/**
	 * @param v
	 *            ：単位ベクトル、あるいはゼロベクトルを引数で渡すこと
	 */
	public static void setGravityDirection(Vector3d v) {
		gravityDirection = new Vector3d(v.x, v.y, v.z);
	}

	// モーメントの計算
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
	 * 物体の反発係数から衝突時に加わる力を求める
	 * @param interval 衝突している時間
	 * @param nor 物体がぶつかった面の宝仙ベクトル
	 * @param solid 物体
	 * @return 衝突時に加わる力
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
	 * 物体と地面との衝突判定
	 * @param obj 物体
	 * @param ground 地面
	 * @return 衝突情報（nullのとき衝突なし）
	 */
	public static CollisionResult doesIntersect(Solid3D obj, Ground ground) {
		if (ground == null) return null;
		CollisionResult cr = null;
		BoundingSurface boundingSurface = ground.getBoundingSurface();

		// BoundingSphereを使って大雑把に衝突判定を行う
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
			// 粗い衝突判定を行う（最上位のBoundingSurfaceとBoundingSphereの間で）
			boundingSurfaceList = boundingSurface.intersect(bs);
			bs = null;
			t3d = null;
		}

		if (obj.bs == null) {
			// BoundingSphere がまだ作られていななかった場合、
			// 詳細な衝突判定のために、最上位の全 BoundingSurface を取得する
			if (boundingSurfaceList == null) boundingSurfaceList = new ArrayList<BoundingSurface>();
			boundingSurfaceList.add(boundingSurface);
		}
		
		if (boundingSurfaceList.size() > 0) {
			// 粗い衝突判定で衝突していた場合、OBBの集合を用いてより詳しい衝突判定を行う
			// （BoundingSphere がまだ作られていない場合、OBB の作成と同時に BoundingSphere を作成される）
			BoundingBoxVisitor obbVisitor = new BoundingBoxVisitor();
			obj.accept(obbVisitor);
			for (int i = 0; i < obbVisitor.getObbList().size(); i++) {
				// OBBと衝突判定をする場合は、地面を多角形のまま扱う
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
	 * 物体同士の衝突判定
	 * @param obj1 物体1
	 * @param part1 判定する物体1の部分の名称
	 * @param obj2 物体2
	 * @param part2 判定する物体2の部分の名称
	 * @return　衝突情報（nullのとき衝突なし）
	 */
	public static CollisionResult checkCollision(Object3D obj1, String part1,
			Object3D obj2, String part2) {
		CollisionResult cr = null;

		// BoundingSphereを使って大雑把に衝突判定を行う
		boolean f = false;
		if (obj1.bs != null && obj2.bs != null) {
			// sol1 の BoundingSphere　を計算
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

			// sol2 の BoundingSphere　を計算
			BoundingSphere bs2 = (BoundingSphere) (obj2.bs.clone());
			obj2.center.getTransform(t3d);
			bs2.transform(t3d);
			obj2.scale.getTransform(t3d);
			bs2.transform(t3d);
			obj2.rot.getTransform(t3d);
			bs2.transform(t3d);
			obj2.pos.getTransform(t3d);
			bs2.transform(t3d);
			
			// BoundingSphere 同士の衝突判定
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

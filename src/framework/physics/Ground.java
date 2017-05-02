package framework.physics;
import java3d.BranchGroup;
import java3d.TransformGroup;

import framework.model3D.BaseObject3D;
import framework.model3D.BoundingSurface;
import framework.model3D.Object3D;
import framework.model3D.Placeable;


/**
 * 地面などの（基本的に動かない）構造物を表すオブジェクト
 * @author 新田直也
 *
 */
public class Ground implements Placeable {
	private BaseObject3D groundObj = null;
	private BoundingSurface boundingSurface = null;		// 衝突判定用ボリュームのキャッシュ
	
	public Ground(BaseObject3D obj) {
		groundObj = obj;
	}
	
	public BaseObject3D getBody() {
		return groundObj;
	}
	
	public void updateBody(BaseObject3D obj) {
		groundObj = obj;
		boundingSurface = null;
	}

	@Override
	public TransformGroup getTransformGroupToPlace() {
		return groundObj.getTransformGroupToPlace();
	}

	/**
	 * 衝突判定用のボリュームを取得する
	 * @return 衝突判定用ボリューム（階層化されている場合がある）
	 */
	BoundingSurface getBoundingSurface() {
		if (boundingSurface == null) {
			// キャッシュに何も積まれていない場合のみ計算する
			BoundingSurfaceVisitor surfaceVisitor = new BoundingSurfaceVisitor();
			if (groundObj instanceof Object3D) {
				// Object3Dの場合階層構造をたどる
				((Object3D)groundObj).accept(surfaceVisitor);
			} else {
				// BaseObject3dの場合階層構造がない
				surfaceVisitor.baseVisit(groundObj);
			}
			boundingSurface = surfaceVisitor.getBoundingSurface();
		}
		return boundingSurface;
	}
}

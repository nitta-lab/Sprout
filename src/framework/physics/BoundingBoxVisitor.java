package framework.physics;
import java.util.ArrayList;

import java3d.BoundingSphere;

import framework.model3D.OBB;
import framework.model3D.Object3D;
import framework.model3D.ObjectVisitor;


public class BoundingBoxVisitor extends ObjectVisitor {
	private ArrayList<OBB> obbList = new ArrayList<OBB>();	// 全構成要素のOBBのリスト
	private ArrayList<BoundingSphere> bsStack = new ArrayList<BoundingSphere>();	// オブジェクトの階層毎のBoundingSphereのスタック
	private String partName = null;	// 部品を指定する場合に使う
	private boolean inPart = false;
	
	public BoundingBoxVisitor() {
		partName = null;
	}
	
	public BoundingBoxVisitor(String partName) {
		this.partName = partName;
	}
	
	public void preVisit(Object3D obj) {
		pushTransform(obj);
		if (partName != null && obj.name.equals(partName)) {
			inPart = true;
		}
		if (obj.hasChildren() && obj.bs == null) {
			// 子供がいる場合、下の階層用にnullをpushする
			bsStack.add(null);
		}
	}

	public void postVisit(Object3D obj) {
		int pattern = 2;
		if (!obj.hasChildren()) {
			// 葉の場合
			OBB obb = obj.getOBB(pattern);
			if (obb != null) {
				if (obj.bs == null) {
					obj.bs = obb.getBoundingSphere();
				}
	
				obb = (OBB)obb.clone();
				BoundingSphere bs = (BoundingSphere)obj.bs.clone();
				for (int i = stackList.size() - 1; i >= 0; i--) {
					obb.transform(stackList.get(i));
					bs.transform(stackList.get(i));
				}
				if (partName == null || partName.length() == 0 || inPart) {
					obbList.add(obb); // Transform3Dを適応させたBoundsをboundsListに追加
					int stackTop = bsStack.size() - 1;
					if (bs != null && stackTop >= 0) {
						if (bsStack.get(stackTop) == null) {
							// その階層の最初のオブジェクトの場合、nullを置き換え
							bsStack.set(stackTop, bs);
						} else {
							// その階層の2番目以降のオブジェクトの場合、結合
							bsStack.get(stackTop).combine(bs);
						}
					}
				}
			}
		} else {
			// 子供がいる場合
			int stackTop = bsStack.size() - 1;
			if (obj.bs == null) {
				// 下の階層の結合結果をpopして利用する
				obj.bs = bsStack.remove(stackTop);
				stackTop--;
			}
			if (obj.bs != null && stackTop >= 0) {
				if (bsStack.get(stackTop) == null) {
					// その階層の最初のオブジェクトの場合、nullを置き換え
					bsStack.set(stackTop, obj.bs);
				} else {
					// その階層の2番目以降のオブジェクトの場合、結合
					bsStack.get(stackTop).combine(obj.bs);
				}
			}
		}
		popTransform();
		if (partName != null && obj.name.equals(partName)) {
			inPart = false;
		}
	}

	public ArrayList<OBB> getObbList() {
		return obbList;
	}
}

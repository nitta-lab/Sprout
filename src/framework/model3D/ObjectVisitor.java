package framework.model3D;

import java.util.ArrayList;

import java3d.Transform3D;

/**
 * オブジェクトの階層構造をトラバースするビジター
 * @author 新田直也
 *
 */
public abstract class ObjectVisitor {
	/**
	 * 根から訪問節点までのパス上に存在する変換行列
	 */
	protected ArrayList<Transform3D> stackList = new ArrayList<Transform3D>();
	
	/*
	 * 節点（オブジェクト）を訪問する直前に呼ばれるメソッド
	 * @param obj 訪問節点
	 */
	public abstract void preVisit(Object3D obj);
	/**
	 * 節点（オブジェクトを訪問した直後に呼ばれるメソッド）
	 * @param obj 訪問節点
	 */
	public abstract void postVisit(Object3D obj);
	
	/**
	 * 1階層潜ったときに変換行列を増やす
	 * @param obj　新しく訪問した節点
	 */
	protected void pushTransform(Object3D obj) {
		Transform3D transPos = new Transform3D();
		obj.pos.getTransform(transPos);
		stackList.add(transPos);
		Transform3D transRot = new Transform3D();
		obj.rot.getTransform(transRot);
		stackList.add(transRot);
		Transform3D transScale = new Transform3D();
		obj.scale.getTransform(transScale);
		stackList.add(transScale);
		Transform3D transCenter = new Transform3D();
		obj.center.getTransform(transCenter);
		stackList.add(transCenter);
	}
	
	/**
	 *　1階層復帰したときに変換行列を 減らす
	 */
	protected void popTransform() {
		for (int i = 0; i < 4; i++) {
			stackList.remove(stackList.size() - 1);
		}
	}

}

package framework.model3D;

import java.util.ArrayList;

import java3d.Transform3D;

/**
 * �I�u�W�F�N�g�̊K�w�\�����g���o�[�X����r�W�^�[
 * @author �V�c����
 *
 */
public abstract class ObjectVisitor {
	/**
	 * ������K��ߓ_�܂ł̃p�X��ɑ��݂���ϊ��s��
	 */
	protected ArrayList<Transform3D> stackList = new ArrayList<Transform3D>();
	
	/*
	 * �ߓ_�i�I�u�W�F�N�g�j��K�₷�钼�O�ɌĂ΂�郁�\�b�h
	 * @param obj �K��ߓ_
	 */
	public abstract void preVisit(Object3D obj);
	/**
	 * �ߓ_�i�I�u�W�F�N�g��K�₵������ɌĂ΂�郁�\�b�h�j
	 * @param obj �K��ߓ_
	 */
	public abstract void postVisit(Object3D obj);
	
	/**
	 * 1�K�w�������Ƃ��ɕϊ��s��𑝂₷
	 * @param obj�@�V�����K�₵���ߓ_
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
	 *�@1�K�w���A�����Ƃ��ɕϊ��s��� ���炷
	 */
	protected void popTransform() {
		for (int i = 0; i < 4; i++) {
			stackList.remove(stackList.size() - 1);
		}
	}

}

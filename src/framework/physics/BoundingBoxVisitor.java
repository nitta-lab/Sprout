package framework.physics;
import java.util.ArrayList;

import java3d.BoundingSphere;

import framework.model3D.OBB;
import framework.model3D.Object3D;
import framework.model3D.ObjectVisitor;


public class BoundingBoxVisitor extends ObjectVisitor {
	private ArrayList<OBB> obbList = new ArrayList<OBB>();	// �S�\���v�f��OBB�̃��X�g
	private ArrayList<BoundingSphere> bsStack = new ArrayList<BoundingSphere>();	// �I�u�W�F�N�g�̊K�w����BoundingSphere�̃X�^�b�N
	private String partName = null;	// ���i���w�肷��ꍇ�Ɏg��
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
			// �q��������ꍇ�A���̊K�w�p��null��push����
			bsStack.add(null);
		}
	}

	public void postVisit(Object3D obj) {
		int pattern = 2;
		if (!obj.hasChildren()) {
			// �t�̏ꍇ
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
					obbList.add(obb); // Transform3D��K��������Bounds��boundsList�ɒǉ�
					int stackTop = bsStack.size() - 1;
					if (bs != null && stackTop >= 0) {
						if (bsStack.get(stackTop) == null) {
							// ���̊K�w�̍ŏ��̃I�u�W�F�N�g�̏ꍇ�Anull��u������
							bsStack.set(stackTop, bs);
						} else {
							// ���̊K�w��2�Ԗڈȍ~�̃I�u�W�F�N�g�̏ꍇ�A����
							bsStack.get(stackTop).combine(bs);
						}
					}
				}
			}
		} else {
			// �q��������ꍇ
			int stackTop = bsStack.size() - 1;
			if (obj.bs == null) {
				// ���̊K�w�̌������ʂ�pop���ė��p����
				obj.bs = bsStack.remove(stackTop);
				stackTop--;
			}
			if (obj.bs != null && stackTop >= 0) {
				if (bsStack.get(stackTop) == null) {
					// ���̊K�w�̍ŏ��̃I�u�W�F�N�g�̏ꍇ�Anull��u������
					bsStack.set(stackTop, obj.bs);
				} else {
					// ���̊K�w��2�Ԗڈȍ~�̃I�u�W�F�N�g�̏ꍇ�A����
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

package java3d;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

public class Group extends Node {

	private List<Node> children = new ArrayList<Node>();

	public Node getChild(int num) {
		return children.get(num);
	}

	public int numChildren() {
		return children.size();
	}

	public void addChild(Node node) {
		children.add(node);
	}

	public void removeChild(Node node) {
		children.remove(node);
	}

	public Enumeration<Node> getAllChildren() {
		return Collections.enumeration(children);
	}
	
	public Node cloneTree() {
		Group newInstance = new Group();
		newInstance.children = new ArrayList<Node>();
		for (int n = 0; n < children.size(); n++) {
			newInstance.children.add(children.get(n).cloneTree());
		}
		return newInstance;
	}
}

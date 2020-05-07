
public class AVLTree<T extends Comparable<T>>{
	
	public class Node<T> {
		int bf; //balance factor
		T value;
		int height; //height of the node in this tree
		Node left; //left and right children of this node
		Node right;
		public Node(T value) {
			this.value = value;
		}
		public Node getLeft() {
			return left;
		}
		public Node getRight() {
			return right;
		}
		public String getValue() {
			return String.valueOf(value);
		}
	}
	
	Node root; //root node of this tree
	private int totalNodes = 0; //tracks the total number of notes in this tree
	
	/*
	 * the height of a rooted tree is the number of edges between the root and
	 * the tree's farthest leaf. So a tree containing a single node must have a height of 0.
	 */
	public int height() {
		if (root == null) return 0;
		return root.height;
	}
	
	public int size() {
		return totalNodes;
	}
	
	public boolean isEmpty() {
		return totalNodes == 0;
	}
	

}

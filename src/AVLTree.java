public class AVLTree<T extends Comparable<T>>{
	
	public class BSTNode<T extends Comparable<T>> implements Comparable<BSTNode<T>> {
		private T data;
		private BSTNode<T> left;
		private BSTNode<T> right;
		
		protected int height;
		
		public BSTNode (T data){
			this.data = data;
			height = 0;
		}
		public BSTNode (T data, BSTNode<T> left, BSTNode<T> right) {
			this.data = data;
			this.left = left;
			this.right = right;
			height = 0;
		}
		//desired setter and getter methods
		@Override
		public int compareTo(BSTNode<T> o) {
			// TODO Auto-generated method stub
			return this.data.compareTo(o.data);
		}
		@Override
		public String toString() {
			return data.toString();
		}
	}
	BSTNode<T> root;
	public void add(T newData){
		root = recAdd(root, newData);
	}
	private BSTNode<T> recAdd(BSTNode<T> node, T newData) {
		//base case
		if (node == null) {
			BSTNode<T> newNode = new BSTNode<T>(newData);
			return newNode;
		}
		if(newData.compareTo(node.data) < 0) {
			recAdd(node.left, newData);
		} else if (newData.compareTo(node.data) > 0) {
			recAdd(node.right, newData);
		}
		return node;
	}
	/*
	 * Method to find the rightmost node in the left subtree of a given node.
	 * @param BSTNode n, node whose predecessor is to replace it.
	 */
	private T getPredecessor(BSTNode<T> n) {
		if(n.left == null) {
			//this can't happen?? 
			throw new NullPointerException("no left child? ");
		} else {
			BSTNode<T> current = n.left;
			while(current.right != null) {
				current = current.right;
			}
			return current.data;
		}
	}
	/*
	 * Implementation of the remove operation involves several smaller methods. 
	 * public Remove() method.
	 */
	public void remove(T item) {
		root = recRemove(root, item);
	}
	/* 
	 * This method locates the node that is to be removed
	 * @param node and item. Node is current starting point to compared to Item. 
	 */
	private BSTNode<T> recRemove(BSTNode<T> node, T item){
		if (root == null) {
			// do nothing, the item is not in the tree
			System.out.println("Item is not in the tree");
		} else if (item.compareTo(node.data) < 0) {
			node.left = recRemove(node.left, item);
		} else if (item.compareTo(node.data) > 0) {
			node.right = recRemove(node.right, item);
		} else {
			//you found the node!
			node = remove(node);
		}
		return node;
	}
	/*
	 * Actual removal of the node once the node is located. 
	 */
	private BSTNode<T> remove(BSTNode<T> node){
		if (node.left == null) {
			return node.right;
		}
		if (node.right == null) {
			return node.left;
		}
		// node has two children and we must find the predecessor
		T data = getPredecessor(node);
		node.data = data;
		//now both this node and the predecessor have the same value so we must remove the predecessor
		node.left = recRemove(node.left, data);
		return node;
	}
	/*
	 * What makes this an AVL tree is its self-balancing factor
	 * balance factor method is the difference between the height of the left and right subtrees.
	 * Balance Factor = in the set of {-1, 0, 1}
	 */
	private int balanceFactor(BSTNode<T> n) {
		if (n.right == null) {
			return n.height;
		}
		if (n.left == null) {
			return -n.height;
		}
		return n.left.height - n.right.height;
	}
	/*
	 * In order to compute the balance factor and determine a rotation (if any) is needed at that node,
	 * we need to know the height associated with every node. When we update the height of any given node
	 * we assume that its children's heights are correct. 
	 */
	private void updateHeight(BSTNode<T> node) {
		//if node is a leaf
		if(node.right == null && node.left == null) {
			node.height = 0; // this is sometimes set to 1, idk why, google it
		} 
		//node only has right child
		else if (node.left == null) {
			node.height = node.right.height + 1;
		}
		//node only has left child
		else if (node.right == null) {
			node.height = node.left.height + 1;
		} else {
			//node has two children
			node.height = Math.max(node.left.height, node.right.height) + 1;
		}
	}
	/*
	 * When we add or remove nodes, we need to go back to the root and check one by one if each node
	 * needs re-balancing
	 * Balance factor must always be in the range of {-1, 0, 1}
	 * So if it's 2 or -2 we must perform one of the four possible rotations. 
	 * balanceFactor() method should never return smaller than -1 or greater than 1
	 * 
	 * Note: in all the rotations below we change the structure of the tree (re-link the nodes) and
	 * the data is never copied from one node to another.
	 * 
	 * LL imbalance: left subtree as >2 more levels than right subtree. in left subtree of A, either both subtrees of B
	 * have same height or left subtree of B has height 1 larger than right subtree.
	 * balanceLL(): rtate the subtree to the right. The right subtree of B becomes left subtree of A
	 * 
	 * RR imbalance: vice versa of LL imbalance
	 * balanceRR(): rotate the subtree to the left. The left subtree of B becomes the right subtree of A.
	 * 
	 * LR imbalance: A's left subtree has >2 more lebels than right subtree. In left subtree of A, the right subtrees of B
	 * has height one larger than left subtree of B.
	 * balanceLR(): rotate to the left at nodeB, then rotate to the right at node A.
	 * 
	 * RL imbalance: vice versa of LR imbalance
	 * balanceRL(): rotate the right at nodeB, then rotate to the left at node A.
	 * 
	 */
	
	private BSTNode<T> balanceLL(BSTNode<T> A){
		BSTNode<T> B = A.left;
		A.left = B.right;
		B.right = A;
		updateHeight(A);
		updateHeight(B);
		return B;
	}
	private BSTNode<T> balanceRR(BSTNode<T> A){
		BSTNode<T> B = A.right;
		A.right = B.left;
		B.left = A;
		return A;
	}
	
	private BSTNode<T> balanceLR(BSTNode<T> A){
		BSTNode<T> B = A.left;
		BSTNode<T> C = B.right;
		
		A.left = C.right;
		B.right = C.left;
		C.left = B;
		C.right = A;
		
		updateHeight(A);
		updateHeight(B);
		updateHeight(C);
		
		return C;
	}
	private BSTNode<T> balanceRL(BSTNode<T> A){
		BSTNode<T> B = A.right;
		BSTNode<T> C = B.left;
		
		A.right = C.left;
		B.left = C.right;
		C.left = A;
		C.right = B;
		
		updateHeight(A);
		updateHeight(B);
		updateHeight(C);
		
		return C;
	}
	
}

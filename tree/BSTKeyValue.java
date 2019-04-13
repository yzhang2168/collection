package tree;

public class BSTKeyValue<K extends Comparable<K>, V> {

	private class Node {
		private K key;
		private V value;
		private Node left;
		private Node right;

		public Node(K key, V value) {
			this.key = key;
			this.value = value;
			left = null;
			right = null;
		}

		@Override
		public String toString() {
			return key.toString() + ": " + value.toString();
		}
	}

	Node root;

	public BSTKeyValue() {
		root = null;
	}

	public Node root() {
		return root;
	}

	public boolean isEmpty() {
		return root == null;
	}

	public V search(K key) {
		if (root == null) {
			return null;
		}
		
		Node curr = root;
		while (curr != null) {
			if (curr.key.equals(key)) {
				return curr.value;
			} else if (key.compareTo(curr.key) < 0) {
				curr = curr.left;
			} else {
				curr = curr.right;
			}
		}
		// curr == null
		return null;
	}

	public V searchRecursive(Node node, K key) {
		if (node == null) {
			return null;
		} else if (node.key.equals(key)) {
			return node.value;
		} else if (key.compareTo(node.key) < 0) {
			return searchRecursive(node.left, key);
		} else {
			return searchRecursive(node.right, key);
		}
	}

	public void insert(K key, V value) {
		if (key == null) {
			throw new IllegalArgumentException("Key cannot be null");
		}

		Node newNode = new Node(key, value);
		if (root == null) {
			root = newNode;
			return;
		}

		Node curr = root;
		while (curr != null) {
			if (key.equals(curr.key)) {
				curr.value = value;
				return;
			} else if (key.compareTo(curr.key) < 0) {
				if (curr.left == null) {
					curr.left = newNode;
					return;
				} else {
					curr = curr.left;
				}
			} else {
				if (curr.right == null) {
					curr.right = newNode;
					return;
				} else {
					curr = curr.right;
				}
			}
		}
	}

	public Node insertRecursive(Node node, K key, V value) {
		if (node == null) {
			return new Node(key, value);
		}

		if (key.equals(node.key)) {
			node.value = value;
		} else if (key.compareTo(node.key) < 0) {
			node.left = insertRecursive(node.left, key, value);
		} else {
			node.right = insertRecursive(node.right, key, value);
		}

		return node;
	}

	/*
	 * 3 3 / \ → / \ 2 8 2 9 / \ / \ 6 12 6 12 / \ / \ 11 14 11 14 / / 9 10 \ / \ 10
	 * / \
	 */
	public void delete(K key) {
		if (key == null) {
			throw new IllegalArgumentException("key cannot be null");
		}
		root = delete(root, key);
	}

	public Node delete(Node root, K key) {
		if (root == null) {
			return null;
		}

		// find target key
		if (key.compareTo(root.key) < 0) {
			root.left = delete(root.left, key);
			return root;
		} else if (key.compareTo(root.key) > 0) {
			root.right = delete(root.right, key);
			return root;
		}

		// now root is target
		// 1. no children
		// 2-3. only left or right child
		if (root.left == null) {
			return root.right;
		} else if (root.right == null) {
			return root.left;
		}

		// root has left and right children
		// 4.1 right child has no left child
		if (root.right.left == null) {
			root.right.left = root.left;
			return root.right;
		}

		// 4.2 right child has left child
		Node smallest = deleteSmallest(root.right);
		smallest.left = root.left;
		smallest.right = root.right;
		return smallest;
	}

	private Node deleteSmallest(Node node) {
		Node prev = node;
		Node curr = node.left;
		while (curr.left != null) {
			prev = curr;
			curr = curr.left;
		}

		// curr.left == null, curr is the left most node
		prev.left = prev.left.right;
		return curr;
	}

	public static void main(String[] args) {
		BSTKeyValue<Integer, Character> tree = new BSTKeyValue<>();
		tree.insert(1, 'a');
		System.out.println(tree.root());

		tree.insert(1, 'b');
		System.out.println(tree.root());
		System.out.println(tree.root().left);
		System.out.println(tree.root().right);

		tree.insert(0, 'A');
		System.out.println(tree.root());
		System.out.println(tree.root().left);
		System.out.println(tree.root().right);

		tree.insert(2, 'B');
		System.out.println(tree.root());
		System.out.println(tree.root().left);
		System.out.println(tree.root().right);

		tree.insert(3, 'Z');
		System.out.println(tree.root());
		System.out.println(tree.root().left);
		System.out.println(tree.root().right);
		System.out.println(tree.root().right.right);
		System.out.println(tree.searchRecursive(tree.root(), 1));
		System.out.println(tree.searchRecursive(tree.root(), 0));
		System.out.println(tree.searchRecursive(tree.root(), 3));
	}
}

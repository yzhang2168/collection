package tree;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;


// E here is different from BST<E>
class TreeNode<Ei extends Comparable<Ei>> {
    protected Ei e;
    protected TreeNode<Ei> left;
    protected TreeNode<Ei> right;

    protected TreeNode(Ei e) {
        this.e = e;
        left = null;
        right = null;
    }

    @Override
    public String toString() {
        return e.toString();
    }
}

public class BST<E extends Comparable<E>> implements Iterable<E> {

    private TreeNode<E> root;
    private int size;

    public BST() {
        root = null;
        size = 0;
    }

    public BST(E[] objects) {
        for (E e : objects) {
            insert(e);
        }
    }

    public TreeNode<E> getRoot() {
        return root;
    }

    public boolean search(E e) {
        TreeNode<E> curr = root;
        while (curr != null && !curr.e.equals(e)) {
            if (curr.e.compareTo(e) < 0) {
                curr = curr.right;
            } else if (curr.e.compareTo(e) > 0) {
                curr = curr.left;
            }
        }
        
        // curr == null || curr.e.equals(e)
        return curr != null;
    }

    public boolean insert(E e) {
        TreeNode<E> newNode = new TreeNode<E>(e);
        if (root == null) {
            root = newNode;
        } else {
            TreeNode<E> curr = root;
            while (curr != null) {
                if (e.equals(curr.e)) {
                    return false;
                } else if (e.compareTo(curr.e) < 0) {
                    if (curr.left == null) {
                    	curr.left = newNode;
                    	break;
                    } else {
                    	curr = curr.left;
                    }
                } else {
                	if (curr.right == null) {
                		curr.right = newNode;
                		break;
                	} else {
                		curr = curr.right;
                	}
                }
            }
        }

        size++;
        return true;
    }

    public boolean delete(E e) {
        // locate the element to be deleted
        TreeNode<E> parent = null;
        TreeNode<E> curr = root;
        while (curr != null) {
            if (e.compareTo(curr.e) < 0) {
                parent = curr;
                curr = curr.left;
            } else if (e.compareTo(curr.e) > 0) {
                parent = curr;
                curr = curr.right;
            } else { // equals, curr is target
                break;
            }
        }

        // not found
        if (curr == null) {
            return false;
        }

        // curr is target
        // case 1: curr has no left child, connect parent with curr's right
        // child
        if (curr.left == null) {
            if (parent == null) {
                root = curr.right;
            } else {
                if (e.compareTo(parent.e) < 0) {
                    parent.left = curr.right;
                } else {
                    parent.right = curr.right;
                }
            }
        } else {
            // case 2: curr has a left child. Locate the rightmost node in
            // the left subtree and its parent.
            TreeNode<E> parentOfRightmost = curr;
            TreeNode<E> rightmost = curr.left;

            // keep going to the right
            while (rightmost.right != null) {
                parentOfRightmost = rightmost;
                rightmost = rightmost.right;
            }

            // replace the element in curr by the element in rightmost
            curr.e = rightmost.e;

            // eliminate rightmost
            // rightmost is the largest in left subtree and cannot have a
            // right child
            // rightmost may or may not have a left child
            if (parentOfRightmost.right == rightmost) {
                parentOfRightmost.right = rightmost.left;
            } else {
                parentOfRightmost.left = rightmost.left;
            }
        }

        size--;
        return true;
    }

    public TreeNode<E> delete(TreeNode<E> root, E e) {
		if (root == null) {
			return null; 
		}
		
		if (e.compareTo(root.e) < 0) {
			root.left = delete(root.left, e);
			return root;
		} else if (e.compareTo(root.e) > 0) {
			root.right = delete(root.right, e);
			return root;
		} 
		
		// root is target
		if (root.left == null) {
			return root.right;
		} else if (root.right == null) {
			return root.left;
		} else if (root.right.left == null) {
			root.right.left = root.left;
			return root.right;
		} else {
			TreeNode<E> smallest = deleteSmallest(root.right);
			smallest.left = root.left;
			smallest.right = root.right;
			return smallest;
		}
	}
	
	private TreeNode<E> deleteSmallest(TreeNode<E> node) {
		TreeNode<E> prev = node;
		TreeNode<E> curr = node.left;
		while (curr.left != null) {
			prev = curr;
			curr = curr.left;
		}
		// curr.left == null
		prev.left = curr.right;
		return curr;
		
	}
	
    public List<E> inorderRecursive() {
        List<E> result = new ArrayList<E>();
        inorderRecursion(root, result);
        return result;
    }

    private void inorderRecursion(TreeNode<E> curr, List<E> result) {
        if (curr == null) {
            return;
        }
        inorderRecursion(curr.left, result);
        result.add(curr.e);
        inorderRecursion(curr.right, result);
    }

    public List<E> inorder1() {
        List<E> result = new ArrayList<E>();
        Deque<TreeNode<E>> stack = new ArrayDeque<TreeNode<E>>();
        TreeNode<E> curr = null;

        while (curr != null || !stack.isEmpty()) {
            // try to go left before curr node is printed
        	if (curr != null) {
            	stack.offerFirst(curr);
            	curr = curr.left;
            } else {
            	// when left is null, left subtree has been traversed, the next to traverse is the top node in stack
            	curr = stack.pollFirst();
            	result.add(curr.e);
            	curr = curr.right;
            }
        }

        return result;
    }
    
    public List<E> inorder() {
        List<E> result = new ArrayList<E>();
        Deque<TreeNode<E>> stack = new ArrayDeque<TreeNode<E>>();
        pushLeft(root, stack);

        while (!stack.isEmpty()) {
            TreeNode<E> curr = stack.pop();
            result.add(curr.e);
            curr = curr.right;
            pushLeft(curr, stack);
        }
        return result;
    }

    private void pushLeft(TreeNode<E> node, Deque<TreeNode<E>> stack) {
        while (node != null) {
            stack.push(node);
            node = node.left;
        }
    }

    public List<E> preorder() {
        List<E> result = new ArrayList<E>();
        //preorderRecursion(root, result);
        if (root == null) {
        	return result;
        }
        
        Deque<TreeNode<E>> stack = new ArrayDeque<>();
        stack.offer(root);
        while (!stack.isEmpty()) {
        	TreeNode<E> curr = stack.pollFirst();
        	result.add(curr.e);
        	if (curr.right != null) {
        		stack.offerFirst(curr.right);
        	}
        	if (curr.left != null) {
        		stack.offerFirst(curr.left);
        	}
        }
        return result;
    }

    private void preorderRecursion(TreeNode<E> curr, List<E> result) {
        if (curr == null) {
            return;
        }
        result.add(curr.e);
        preorderRecursion(curr.left, result);
        preorderRecursion(curr.right, result);
    }

    /** mimics the call stack
     * can be used a template for all traversals */
    public List<E> postorder() {
        List<E> result = new ArrayList<E>();
        //postorderRecursion(root, result);
        if (root == null) {
        	return result;
        }
        
        Deque<TreeNode<E>> stack = new ArrayDeque<TreeNode<E>>();
        TreeNode<E> prev = null;
        stack.offerFirst(root);
        
        while (!stack.isEmpty()) {
        	TreeNode<E> curr = stack.peekFirst();
        	
        	// top to down
        	if (prev == null || curr == prev.left || curr == prev.right) {
        		// try to go left first
        		if (curr.left != null) {
        			stack.offerFirst(curr.left);
        		} else if (curr.right != null) {
        			stack.offerFirst(curr.right);
        		} else {
        			// if we cannot go either left or right, curr is leaf
        			result.add(curr.e);
        			stack.pollFirst();
        		}
        	} else if (prev == curr.left) { // back from left subtree 
        		if (curr.right != null) {
        			stack.offerFirst(curr.right);
        		} else {
        			result.add(curr.e);
        			stack.pollFirst();
        		}
        	} else { // back from right subtree
        		result.add(curr.e);
        		stack.pollFirst();
        	}
        	
        	prev = curr;
        }
        
        return result;
    }

    private void postorderRecursion(TreeNode<E> curr, List<E> result) {
        if (curr == null) {
            return;
        }
        postorderRecursion(curr.left, result);
        postorderRecursion(curr.right, result);
        result.add(curr.e);
    }

    public List<List<E>> BFS() {
        List<List<E>> result = new ArrayList<List<E>>();

        Queue<TreeNode<E>> queue = new ArrayDeque<TreeNode<E>>();
        if (root == null) {
            return result;
        }

        queue.offer(root);
        while (!queue.isEmpty()) {
            int size = queue.size();
            List<E> level = new ArrayList<E>();
            for (int i = 0; i < size; i++) {
                TreeNode<E> curr = queue.poll();
                level.add(curr.e);
                if (curr.left != null) {
                    queue.offer(curr.left);
                }
                if (curr.right != null) {
                    queue.offer(curr.right);
                }
            }
            result.add(level);
        }

        return result;
    }
    
    public List<E> getRange(E lower, E upper) {
    	List<E> result = new ArrayList<E>();
    	getRange(root, lower, upper, result);
    	return result;
    }
    
    private void getRange(TreeNode<E> root, E lower, E upper, List<E> result) {
    	if (root == null) {
    		return;
    	}
    	
    	if (lower.compareTo(root.e) < 0) {
    		getRange(root.left, lower, upper, result);
    	}
    	
    	if (lower.compareTo(root.e) <= 0 && upper.compareTo(root.e) >= 0) {
    		result.add(root.e);
    	}
    	
    	if (upper.compareTo(root.e) > 0) {
    		getRange(root.right, lower, upper, result);
    	}
    }

    public List<E> pathFromRoot(E e) {
        List<E> result = new ArrayList<E>();
        TreeNode<E> curr = root;

        while (curr != null) {
            result.add(curr.e);
            if (e.compareTo(curr.e) < 0) {
                curr = curr.left;
            } else if (e.compareTo(curr.e) > 0) {
                curr = curr.right;
            } else { // equals
                break;
            }
        }

        if (curr == null) {
            return null;
        } else {
            return result;
        }
    }

    public int height() {
        return getHeight(root);
    }

    private int getHeight(TreeNode<E> root) {
        if (root == null) {
            return 0;
        }

        return Math.max(getHeight(root.left), getHeight(root.right)) + 1;
    }

    public boolean isCompleteBST() {
    	if (root == null) {
            return true;
        }
        
    	Queue<TreeNode<E>> queue = new ArrayDeque<TreeNode<E>>();
    	boolean seenNull = false;
        queue.offer(root);        
        while (!queue.isEmpty()) {
        	TreeNode<E> curr = queue.poll();
        	if (curr.left == null) {
        		if (seenNull) {
        			return false;
        		} else {
        			seenNull = true;
        		}
        	} else {
        		queue.offer(curr.left);
        	}

			if (curr.right == null) {
        		if (seenNull) {
        			return false;
        		} else {
        			seenNull = true;
        		}
        	} else {
        		queue.offer(curr.right);
        	}
        }
        return true;
     }

    public boolean isCompleteBST0() {
        return size == Math.pow(2, height()) - 1;
    }

    public int getNumberOfLeaves() {
        int leaves = 0;
        Deque<TreeNode<E>> stack = new ArrayDeque<TreeNode<E>>();

        pushLeft(root, stack);
        while (!stack.isEmpty()) {
            TreeNode<E> curr = stack.pop();
            if (curr.left == null && curr.right == null) {
                leaves++;
            }
            curr = curr.right;
            pushLeft(curr, stack);
        }
        return leaves;
    }

    public int getNumberOfLeaves(TreeNode<E> node) {
        if (node == null) {
            return 0;
        }
        if (node.left == null && node.right == null) {
            return 1;
        } else {
            return getNumberOfLeaves(node.left) + getNumberOfLeaves(node.right);
        }
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof BST)) {
            return false;
        }

        BST<E> other = (BST<E>) o;
        if (this.size != other.size) {
            return false;
        }

        return equals(this.root, other.root);
    }

    private boolean equals(TreeNode<E> one, TreeNode<E> two) {
        if (one == two) {
            return true;
        } else if (one == null || two == null) {
            return false;
        } else {
            return one.e.equals(two.e) && equals(one.left, two.left) && equals(one.right, two.right);
        }
    }

    @Override
    public BST<E> clone() throws CloneNotSupportedException {
    	BST<E> clone = new BST<E>();
        clone(clone, root);
        return clone;
    }

    // deep copy
    private void clone(BST<E> clone, TreeNode<E> root) {
        if (root == null) {
            return;
        }
        
        clone.insert(root.e);
        clone(clone, root.left);
        clone(clone, root.right);
    }
    
    @Override
    public String toString() {
        return inorder().toString();
    }

    @Override
    public Iterator<E> iterator() {
        return new InorderIterator();
    }

    private class InorderIterator implements Iterator<E> {
        private List<E> list;
        private int curr;

        public InorderIterator() {
            list = inorder();
            curr = 0;
        }

        @Override
        public boolean hasNext() {
            return curr < list.size();
        }

        @Override
        public E next() {
            return list.get(curr++);
        }

        @Override
        public void remove() {
            delete(list.get(curr)); // tree
            list = inorder(); // rebuild the list, not efficient
            // throw new UnsupportedOperationException ("Removing an element
            // from the iterator is not supported");
        }
        
        public Iterator<E> preorderIterator() {
            return new PreorderIterator();
        }
        
        private class PreorderIterator implements Iterator<E> {
            
            private List<E> list;
            private int curr;
            
            public PreorderIterator() {
                list = preorder();
                curr = 0;
            }
            
            public boolean hasNext() {
                return curr < list.size();
            }
            
            public E next() {
                return list.get(curr++);
            }
            
            public void remove() {
                delete(list.get(curr));
                list = preorder();
            }
        }
    }

    public static void main(String[] args) {
    	BST<Integer> test = new BST<Integer>();
        test.insert(5);
        System.out.println("size:    " + test.size());
        System.out.println("inorder: " + test.inorder());

        test.insert(15);
        test.insert(0);
        test.insert(7);
        test.insert(2);
        System.out.println("\nsize     : " + test.size());
        System.out.println("inorder  : " + test.inorder());
        System.out.println("preorder : " + test.preorder());
        System.out.println("postorder: " + test.postorder());
        System.out.println("BFS      : " + test.BFS());
        System.out.println("toString():" + test);
        System.out.println("height   : " + test.height());
        System.out.println("complete : " + test.isCompleteBST());
        System.out.println("leaves   : " + test.getNumberOfLeaves());
        System.out.println("leaves   : " + test.getNumberOfLeaves(test.getRoot()));
        System.out.println("path to 2: " + test.pathFromRoot(2));
        System.out.println("path to 7: " + test.pathFromRoot(7));
        System.out.println("path to 10:" + test.pathFromRoot(10));

        test.delete(5);
        System.out.println("\ndeleted 5: " + test);
        System.out.println("BFS      : " + test.BFS());
        System.out.println("height   : " + test.height());
        System.out.println("complete : " + test.isCompleteBST());
        System.out.println("leaves   : " + test.getNumberOfLeaves());
        System.out.println("leaves   : " + test.getNumberOfLeaves(test.getRoot()));

        test.delete(7);
        System.out.println("\ndeleted 7: " + test);
        System.out.println("BFS      : " + test.BFS());
        System.out.println("height   : " + test.height());
        System.out.println("complete : " + test.isCompleteBST());
        System.out.println("leaves   : " + test.getNumberOfLeaves());
        System.out.println("leaves   : " + test.getNumberOfLeaves(test.getRoot()));

        System.out.print("iterator: ");
        for (Integer i : test) {
            System.out.print(i + " ");
        }
    }
}

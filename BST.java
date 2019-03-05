import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;

class TreeNode<E extends Comparable<E>> {
    protected E e;
    protected TreeNode<E> left;
    protected TreeNode<E> right;

    protected TreeNode(E e) {
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
        while (curr != null) {
            if (curr.e.equals(e)) {
                return true;
            } else if (curr.e.compareTo(e) < 0) {
                curr = curr.right;
            } else {
                curr = curr.left;
            }
        }
        return false;
    }

    public boolean insert(E e) {
        TreeNode<E> newNode = new TreeNode<E>(e);
        if (root == null) {
            root = newNode;
            size++;
            return true;
        }

        TreeNode<E> parent = null;
        TreeNode<E> curr = root;
        while (curr != null) {
            if (e.equals(curr.e)) {
                return false;
            } else if (e.compareTo(curr.e) < 0) {
                parent = curr;
                curr = curr.left;
            } else {
                parent = curr;
                curr = curr.right;
            }
        }

        // curr == null
        if (e.compareTo(parent.e) < 0) {
            parent.left = newNode;
        } else {
            parent.right = newNode;
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

    public java.util.List<E> preorder() {
        List<E> result = new ArrayList<E>();
        preorderRecursion(root, result);
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

    public java.util.List<E> postorder() {
        List<E> result = new ArrayList<E>();
        postorderRecursion(root, result);
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

    public java.util.List<E> pathFromRoot(E e) {
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
        System.out.println("size     : " + test.size());
        System.out.println("inorder  : " + test.inorder());
        System.out.println("preorder : " + test.preorder());
        System.out.println("postorder: " + test.postorder());
        System.out.println("toString():" + test);
        
        System.out.println("path to 2: " + test.pathFromRoot(2));
        System.out.println("path to 7: " + test.pathFromRoot(7));
        System.out.println("path to 10:" + test.pathFromRoot(10));
        
        test.delete(5);
        System.out.println("deleted 5: " + test);
        
        System.out.print("iterator: ");
        for (Integer i : test) {
            System.out.print(i + " ");
        }
    }
}

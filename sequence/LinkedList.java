package sequence;

public class LinkedList<E> {

	private class Node {
		private E value;
		private Node next;

		public Node(E value) {
			this.value = value;
			this.next = null;
		}

		@Override
		public String toString() {
			return String.valueOf(value);
		}
	}

	private Node head;
	int size;

	public LinkedList() {
		head = null;
		size = 0;
	}

	public int size() {
		return size;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public E getHead() {
		if (head == null) {
			System.err.println("Warning: The list is empty!");
			return null;
		} else {
			return head.value;
		}
	}

	public E getTail() {
		Node curr = head;
		while (curr != null && curr.next != null) {
			curr = curr.next;
		}

		if (curr == null) {
			System.err.println("Warning: The list is empty!");
			return null;
		} else {
			return curr.value;
		}
	}

	/**
	 * index == 0, new head index == size, append to tail index in the middle
	 */
	public Node add(int index, E value) {
		if (index < 0 || index > size) {
			throw new IndexOutOfBoundsException("Invalid index");
		}

		Node newNode = new Node(value);
		// head == null || index == 0
		if (index == 0) {
			newNode.next = head;
			head = newNode;
		} else {
			// head != null
			Node prev = head;
			while (prev != null && index > 1) {
				prev = prev.next;
				index--;
			}			
			newNode.next = prev.next;
			prev.next = newNode;
		}
		size++;
		return head;
	}

	public Node add(E value) {
		return add(size, value);
	}

	public Node addHead(E value) {
		return add(0, value);
	}

	/* pass by value, head is a copy */
	public Node addHead(Node head, E value) {
		Node newHead = new Node(value);
		newHead.next = head;
		head = newHead;
		size++;
		return head;
	}

	public Node addTail(E value) {
		return add(value);
	}

	/* return first occurrence */
	public Node remove(E value) {
		if (head == null) {
			return null;
		} else if (head.value == value) {
			head = head.next;
			return head;
		} else {
			Node curr = head;
			while (curr != null && curr.next != null && curr.next.value != value) {
				curr = curr.next;
			}
			// curr.next == null || curr.next.value == value
			if (curr.next != null) {
				curr.next = curr.next.next;
			}
			return head;
		}
	}

	public E get(int index) {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException("invalid index");
		}

		Node curr = head;
		while (index > 0 && curr != null) {
			curr = curr.next;
			index--;
		}

		// index == 0 || curr == null
		if (curr == null) {
			return null;
		} else {
			return curr.value;
		}
	}

	public String toString() {
		StringBuilder sb = new StringBuilder("[");
		Node curr = head;
		while (curr != null) {
			sb.append(curr.toString()).append(',');
			curr = curr.next;
		}

		if (sb.length() > 1) {
			sb.deleteCharAt(sb.length() - 1);
		}
		sb.append(']');
		return sb.toString();
	}

	public static void main(String[] args) {
		LinkedList<Integer> l = new LinkedList<>();
		System.out.println(l);
		
		System.out.println("\nadd(0,1): " + l.add(0, 1));
		System.out.println(l);
		
		System.out.println("\nadd(0,2): " + l.add(0, 2));
		System.out.println(l);

		System.out.println("\nadd(2,3): " + l.add(2, 3));
		System.out.println(l);
		System.out.println(l.get(0));
		System.out.println(l.get(1));
		System.out.println(l.get(2));
		//System.out.println(l.get(-2));

		System.out.print("\nremoving non-existing value: ");
		System.out.println(l.remove(10));
		System.out.println(l);

		System.out.print("\nremoving head: ");
		System.out.println(l.remove(2));
		System.out.println(l);

		System.out.print("\nremoving head: ");
		System.out.println(l.remove(1));
		System.out.println(l);

		System.out.print("\nremoving head: ");
		System.out.println(l.remove(3));
		System.out.println(l);

		System.out.print("\nremoving from a list with head == null: ");
		System.out.println(l.remove(1));
		System.out.println(l);

	}
}

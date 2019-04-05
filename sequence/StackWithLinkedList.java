package sequence;

/**
 * implements stack using a linked list
 */
public class StackWithLinkedList<E> {

	private class Node {
		private E value;
		private Node next;

		public Node(E value) {
			this.value = value;
			this.next = null;
		}
	}

	private Node head;
	private int size;

	public StackWithLinkedList() {
		head = null;
		size = 0;
	}

	public void push(E value) {
		Node newHead = new Node(value);
		newHead.next = head;
		head = newHead;
		size++;
	}

	/*
	 * The old head can be garbage collected once you have no more references
	 * pointing to it. It does not matter if the old head still contains references
	 * to other objects (thus you do not need to do `oldHead.setNext(null)`). On the
	 * other hand, as long as reference "oldHead" still point to the old head, the
	 * old head cannot be garbage-collected, you would need to do `oldHead = null`
	 * or let the reference "oldHead" go out of scope.
	 */
	public E pop() {
		if (head == null) {
			return null;
		} else {
			E result = head.value;
			head = head.next;
			size--;
			return result;
		}
	}

	public E peek() {
		if (head == null) {
			return null;
		} else {
			return head.value;
		}
	}

	public int size() {
		return size;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("[");
		Node curr = head;
		while (curr != null) {
			sb.append(curr.value).append(',');
			curr = curr.next;
		}
		if (sb.length() > 1) {
			sb.deleteCharAt(sb.length() - 1);
		}
		sb.append(']');
		return sb.toString();
	}

	public static void main(String[] args) {
		StackWithLinkedList<Integer> s = new StackWithLinkedList<>();
		System.out.println("\npeek(): " + s.peek());
		System.out.println(s);

		s.push(1);
		System.out.println("\npeek(): " + s.peek());
		System.out.println(s);

		s.push(2);
		System.out.println("\npeek(): " + s.peek());
		System.out.println(s);

		s.push(3);
		System.out.println("\npeek(): " + s.peek());
		System.out.println(s);

		System.out.print("\npop(): ");
		System.out.println(s.pop());
		System.out.println("peek(): " + s.peek());
		System.out.println(s);

		System.out.print("\npop(): ");
		System.out.println(s.pop());
		System.out.println("peek(): " + s.peek());
		System.out.println(s);

		System.out.print("\npop(): ");
		System.out.println(s.pop());
		System.out.println("peek(): " + s.peek());
		System.out.println(s);

		System.out.print("\npop(): ");
		System.out.println(s.pop());
		System.out.println("peek(): " + s.peek());
		System.out.println(s);
	}
}
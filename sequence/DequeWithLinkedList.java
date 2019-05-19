package sequence;


public class DequeWithLinkedList<E> {

	private class Node {
		private E value;
		private Node next;
		private Node prev;

		public Node(E value) {
			this.value = value;
			this.next = null;
			this.prev = null;
		}
	}
	
	private Node head;
	private Node tail;
	private int size;
	
	public int size() {
		return size;
	}
	
	public boolean isEmpty() {
		return size() == 0;
	}
	
	public void offerFirst(E e) {
		Node node = new Node(e);
		if (head == null) {
			head = node;
			tail = node;
		} else {
			node.next = head;
			head.prev = node;
			head = node;
		}
		size++;
	}

	public void offerLast(E e) {
		Node node = new Node(e);
		if (tail == null) {
			tail = node;
			head = node;
		} else {
			tail.next = node;
			node.prev = tail;
			tail = node;
		}
		size++;
	}

	public E peekFirst() {
		if (isEmpty()) {
			return null;
		} else {
			return head.value;
		}
	}
	
	public E peekLast() {
		if (isEmpty()) {
			return null;
		} else {
			return tail.value;
		}
	}
	
	public E pollFirst() {
		if (isEmpty()) {
			return null;
		} else {
			Node removed = head;
			head = head.next;
			if (head == null) {
				tail = null;
			} else {
				removed.next = null;
				head.prev = null;
			}
			size--;
			return removed.value;
		}
	}

	public E pollLast() {
		if (isEmpty()) {
			return null;
		} else {
			Node removed = tail;
			tail = tail.prev;
			if (tail == null) {
				head = null;
			} else {
				removed.prev = null;
				tail.next = null;
			}
			size--;
			return removed.value;
		}
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		Node curr = head;
		while (curr != null) {
			sb.append(curr.value);
			if (curr.next != null) {
				sb.append(',');
			}
			curr = curr.next;
		}
		return sb.toString();
	}
	
	public static void main(String[] args) {
		DequeWithLinkedList<Integer> deque = new DequeWithLinkedList<>();
		deque.offerFirst(1);
		deque.offerLast(2);
		deque.offerFirst(3);
		deque.offerLast(4);
		deque.offerLast(5);
		System.out.println(deque);
		System.out.println(deque.pollFirst());
		System.out.println(deque.pollFirst());
		System.out.println(deque);
		System.out.println(deque.pollLast());
		System.out.println(deque);
		System.out.println(deque.pollLast());
		System.out.println(deque.pollLast());
		System.out.println(deque);
		deque.offerFirst(100);
		System.out.println(deque);
		System.out.println(deque.size());
	}
}

package sequence;

/**
 * implements queue using a linked list
 */
public class QueueWithLinkedList<E> {

	private class Node {
		private E value;
		private Node next;

		public Node(E value) {
			this.value = value;
			this.next = null;
		}
	}

	private Node head;
	private Node tail;
	private int size;

	public QueueWithLinkedList() {
		head = null;
		tail = null;
		size = 0;
	}

	/**
	 * add at tail, remove at head: remove() is easier - head = head.next;
	 * */
	public void offer(E value) {
		Node node = new Node(value);
		if (head == null) {
			head = node;
			tail = node;
		} else {
			tail.next = node;
			tail = node;
		}
		size++;
	}

	public E poll() {
		if (head == null) {
			return null;
		} else {
			E result = head.value;
			if (head == tail) {
				tail = null;
			} 
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
			sb.append(curr.value.toString()).append(',');
			curr = curr.next;
		}
		if (sb.length() > 1) {
			sb.deleteCharAt(sb.length() - 1);
		}
		sb.append(']');
		return sb.toString();
	}

	public static void main(String[] args) {
		QueueWithLinkedList<Integer> q = new QueueWithLinkedList<>();
		System.out.println("\npeek(): " + q.peek());
		System.out.println(q);

		q.offer(1);
		System.out.println("\npeek(): " + q.peek());
		System.out.println(q);

		q.offer(2);
		System.out.println("\npeek(): " + q.peek());
		System.out.println(q);

		q.offer(3);
		System.out.println("\npeek(): " + q.peek());
		System.out.println(q);

		System.out.println("\npoll(): " + q.poll());
		System.out.println("peek(): " + q.peek());
		System.out.println(q);

		System.out.println("\npoll(): " + q.poll());
		System.out.println("peek(): " + q.peek());
		System.out.println(q);

		System.out.println("\npoll(): " + q.poll());
		System.out.println("peek(): " + q.peek());
		System.out.println(q);

		System.out.println("\npoll(): " + q.poll());
		System.out.println("peek(): " + q.peek());
		System.out.println(q);
	}
}

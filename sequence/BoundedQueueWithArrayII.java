package sequence;

/**
 * implements queue using a circular array
 * size field is not allowed
 * (head, tail): active data
 * head + 1 == tail or head == length - 1 && tail == 0: empty
 * head == tail: full
 */
public class BoundedQueueWithArrayII<E> {
	private E[] array;
	private int head;
	private int tail;
	
	public BoundedQueueWithArrayII(int cap) {
		if (cap <= 0) {
			throw new IllegalArgumentException("Invalid capacity");
		}
		array = (E[]) new Object[cap];
		head = 0;
		tail = 1;
	}
	
	public boolean isEmpty() {
		int start = head == array.length - 1 ? 0 : head + 1;
		return start == tail;
	}
	
	public boolean isFull() {
		return head == tail;
	}
	
	public boolean offer(E value) {
		if (isFull()) {
			return false;
		} else {
			array[tail] = value;
			tail = tail == array.length - 1 ? 0 : tail + 1;
			return true;
		}
	}
	
	public E poll() {
		if (isEmpty()) {
			return null;
		} else {
			head = head == array.length - 1 ? 0 : head + 1;
			return array[head];
		}
	}
	
	public E peek() {
		if (isEmpty()) {
			return null;
		} else {
			int start = head == array.length - 1 ? 0: head + 1;
			return array[start];
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("[");
		// covers empty
		if (head < tail) {
			for (int i = head + 1; i < tail; i++) {
				sb.append(array[i]).append(',');
			}
		} else {
			for (int i = head + 1; i < array.length; i++) {
				sb.append(array[i]).append(',');
			}
			for (int i = 0; i < tail; i++) {
				sb.append(array[i]).append(',');
			}
		}
		
		if (sb.length() > 1) {
			sb.deleteCharAt(sb.length() - 1);
		}
		sb.append(']');
		
		return sb.toString();
	}
	
	public static void main(String[] args) {
		BoundedQueueWithArrayII<Integer> q = new BoundedQueueWithArrayII<>(4);
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

		q.offer(4);
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

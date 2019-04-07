package sequence;

/**
 * implements queue using a circular array
 * head: read
 * tail: write
 * [head, tail): active data
 */
public class BoundedQueueWithArrayI<E> {
	private E[] array;
	private int head;
	private int tail;
	private int size;
	
	public BoundedQueueWithArrayI(int cap) {
		array = (E[]) new Object[cap];
		head = 0;
		tail = 0;
		size = 0;
	}
	
	public boolean offer(E value) {
		if (isFull()) {
			return false;
		} else {
			array[tail] = value;
			tail = tail + 1 == array.length ? 0 : tail + 1;
			size++;
			return true;
		}
	}
	
	public E peek() {
		if (isEmpty()) {
			return null;
		} else {
			return array[head];
		}
	}
	
	public E poll() {
		if (isEmpty()) {
			return null;
		} else {
			E result = array[head];
			head = head + 1 == array.length ? 0 : head + 1;
			size--;
			return result;
		}
	}
	
	public int size() {
		return size;
	}
	
	public boolean isEmpty() {
		return size == 0;
	}
	
	public boolean isFull() {
		return size == array.length;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder("[");
		if (head < tail) {
			for (int i = head; i < tail; i++) {
				sb.append(array[i]).append(',');
			}
		} else if (!isEmpty()) {
		// head > tail
	    // head == tail: is empty or full
			for (int i = head; i < array.length; i++) {
				sb.append(array[i]).append(',');
			}
			for (int i = 0; i < tail; i++) {
				sb.append(array[i]).append(',');
			}
		}
		
		sb.append(']');
		return sb.toString();		
	}
	
	public static void main(String[] args) {
		BoundedQueueWithArrayI<Integer> q = new BoundedQueueWithArrayI<>(4);
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

		q.offer(0);
		System.out.println("\npeek(): " + q.peek());
		System.out.println(q);

		System.out.println("\npoll(): " + q.poll());
		System.out.println("peek(): " + q.peek());
		System.out.println(q);

		System.out.println("\npoll(): " + q.poll());
		System.out.println("peek(): " + q.peek());
		System.out.println(q);
	}

}

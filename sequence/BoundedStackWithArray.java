package sequence;

/**
 * implements stack using an array
 * [0, head]: active data
 * head: read
 * head + 1: write
 * head == -1: empty
 * head == length - 1: full
 */
public class BoundedStackWithArray<E> {
	private E[] array;
	private int head;

	public BoundedStackWithArray(int cap) {
		if (cap <= 0) {
			throw new IllegalArgumentException("Invalid capacity");
		}
		array = (E[]) new Object[cap];
		head = -1;
	}

	public int size() {
		return head + 1;
	}

	public boolean isEmpty() {
		return head == -1;
	}

	public boolean isFull() {
		return head == array.length - 1;
	}

	public boolean push(E value) {
		if (isFull()) {
			return false;
		} else {
			array[++head] = value;
			return true;
		}
	}

	public E pop() {
		if (isEmpty()) {
			return null;
		} else {
			return array[head--];
		}
	}

	public E top() {
		return isEmpty() ? null : array[head];
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("[");

		for (int i = 0; i <= head; i++) {
			sb.append(array[i].toString()).append(",");
		}

		if (sb.length() > 1) {
			sb.deleteCharAt(sb.length() - 1);
		}

		sb.append("]");
		return sb.toString();
	}

	public static void main(String[] args) {
		BoundedStackWithArray<Integer> s = new BoundedStackWithArray<>(4);
		System.out.println(s);

		System.out.println("\npush 1: " + s.push(1));
		System.out.println(s);

		System.out.println("\npush 2: " + s.push(2));
		System.out.println(s);

		System.out.println("\npush 3: " + s.push(3));
		System.out.println(s);

		System.out.println("\npush 4: " + s.push(4));
		System.out.println(s);

		System.out.println("\npush 5: " + s.push(5));
		System.out.println(s);

		System.out.println("\npop: " + s.pop());
		System.out.println(s);

		System.out.println("\npop: " + s.pop());
		System.out.println(s);

		System.out.println("\npop: " + s.pop());
		System.out.println(s);

		System.out.println("\npop: " + s.pop());
		System.out.println(s);

		System.out.println("\npop: " + s.pop());
		System.out.println(s);
	}
}

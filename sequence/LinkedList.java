package sequence;

import java.util.Iterator;

public class LinkedList<E> implements Iterable<E> {

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
	private int size;

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

	public E get(int index) {
		if (isEmpty()) {
			return null;
		}

		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException("Invalid index");
		}

		Node curr = head;
		while (curr != null && index > 0) {
			curr = curr.next;
			index--;
		}
		// index == 0
		return curr.value;
	}

	public E getFirst() {
		return get(0);
	}

	public E getLast() {
		return get(size - 1);
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
			// index == 1
			newNode.next = prev.next;
			prev.next = newNode;
		}
		size++;
		return head;
	}

	public Node add(E value) {
		return add(size, value);
	}

	public Node addFirst(E value) {
		return add(0, value);
	}

	public void addLast(E value) {
		add(value);
	}

	public boolean contains(E value) {
		if (isEmpty()) {
			return false;
		}

		Node curr = head;
		while (curr != null && !curr.value.equals(value)) {
			curr = curr.next;
		}
		return curr != null;
	}

	public E remove(int index) {
		// covers isEmpty()
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException("Invalid index");
		}

		E result = null;
		if (index == 0) {
			result = head.value;
			head = head.next;
		} else {
			Node prev = head;
			while (prev != null && prev.next != null && index > 1) {
				prev = prev.next;
				index--;
			}
			// index == 1, prev.next is target
			result = prev.next.value;
			prev.next = prev.next.next;
		}
		size--;
		return result;
	}

	/** removes first occurrence */
	public boolean remove(E value) {
		if (isEmpty()) {
			return false;
		} else if (head.value.equals(value)) {
			head = head.next;
			size--;
			return true;
		} else {
			Node prev = head;
			while (prev != null && prev.next != null && !prev.next.value.equals(value)) {
				prev = prev.next;
			}
			// prev.next == null || prev.next.value.equals(value)
			if (prev.next != null) {
				prev.next = prev.next.next;
				size--;
				return true;
			} else {
				return false;
			}
		}
	}

	public void clear() {
		head = null;
		size = 0;
	}

	public Object[] toArray() {
		Object[] result = new Object[size];
		Node curr = head;
		for (int i = 0; i < size; i++) {
			result[i] = curr.value;
			curr = curr.next;
		}
		return result;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder("[");
		Node curr = head;
		while (curr != null) {
			sb.append(curr.toString());
			if (curr.next != null) {
				sb.append(',');
			}
			curr = curr.next;			
		}

		sb.append(']');
		return sb.toString();
	}

	@Override
	public Iterator<E> iterator() {
		return new MyIterator();
	}

	private class MyIterator implements Iterator<E> {
		private int curr;

		@Override
		public boolean hasNext() {
			return curr < size;
		}

		@Override
		public E next() {
			return get(curr++);
		}

		@Override
		public void remove() {
			LinkedList.this.remove(curr);
		}
	}

	public static void main(String[] args) {		
		LinkedList<Integer> l = new LinkedList<>();
		System.out.println(l);
		Object[] array = l.toArray();	
		for (Object o : array) {
			System.out.println(o);
		}
		System.out.println(array);
		
		System.out.println("\nadd(0,1): " + l.add(0, 1));
		System.out.println(l);

		System.out.println("\nadd(0,2): " + l.add(0, 2));
		System.out.println(l);

		System.out.println("\nadd(2,3): " + l.add(2, 3));
		System.out.println(l);
		System.out.println(l.get(0));
		System.out.println(l.get(1));
		System.out.println(l.get(2));
		// System.out.println(l.get(-2));

		System.out.println("\nusing Iterator<E>");
		for (Integer i : l) {
			System.out.println(i);
		}
		
		System.out.println("\nusing toArray()");
		array = l.toArray();		
		for (Object i : array) {
			System.out.println(i);
		}
		
		System.out.print("\nremoving non-existing value: ");
		//System.out.println(l.remove(10));

		System.out.print("\nremoving head: ");
		System.out.println(l.remove(2));
		System.out.println(l);

		System.out.print("\nremoving head: ");
		System.out.println(l.remove(1));
		System.out.println(l);

		System.out.print("\nremoving head: ");
		System.out.println(l.remove(new Integer(3)));
		System.out.println(l);

		System.out.print("\nremoving from a list with head == null: ");
		//System.out.println(l.remove(1));
		System.out.println(l);

	}

}

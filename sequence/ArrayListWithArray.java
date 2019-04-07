package sequence;

import java.util.Arrays;
import java.util.Iterator;

public class ArrayListWithArray<E> implements Iterable<E> {

	private E[] array;
	private static final int INIT_CAPACITY = 10;
	private static final double EXPANDING_FACTOR = 1.5;
	private int size;
	
	public ArrayListWithArray() {
		array = (E[]) new Object[INIT_CAPACITY];
		size = 0;
	}
	
	public ArrayListWithArray(int capacity) {
		if (capacity <= 0) {
			throw new IllegalArgumentException("Invalid capacity");
		}
		array = (E[]) new Object[capacity];
		size = 0;
	}
	
	public void add(int index, E value) {
		if (index < 0 || index > size) {
			throw new IndexOutOfBoundsException("Invalid index");
		}
		
		// shift right
		for (int i = size; i > index; i--) {
			array[i] = array[i - 1];
		}
		array[index] = value;
		size++;
		
		if (needsExpansion()) {
			expand();
		}
	}
	
	private boolean needsExpansion() {
		return size == array.length;
	}
	
	public void add(E value) {
		add(size, value);
	}
	
	public E get(int index) {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException("Invalid index");
		}
		return array[index];
	}
	
	public E set(int index, E value) {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException("Invalid index");
		}
		
		E result = array[index];
		array[index] = value;
		return result;
	}
	
	public E remove(int index) {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException("Invalid index");
		}
		
		E result = array[index];
		for (int i = index; i < size - 1; i++) {
			array[i] = array[i + 1];
		}
		size--;
		return result;
	}
	
	public boolean remove(E value) {
		for (int i = 0; i < size; i++) {
			if (array[i].equals(value)) {
				for (int j = i; j < size - 1; j++) {
					array[j] = array[j + 1];
				}
				size--;
				return true;
			}
		}
		return false;
	}
	
	public boolean contains(E value) {
		for (int i = 0; i < size; i++) {
			if (array[i].equals(value)) {				
				return true;
			}
		}
		return false;
	}
	
	public int indexOf(E value) {
		for (int i = 0; i < size; i++) {
			if (array[i].equals(value)) {				
				return i;
			}
		}
		return -1;
	}
	
	public int size() {
		return size;
	}
	
	public boolean isEmpty() {
		return size == 0;
	}
	
	public void clear() {
		Arrays.fill(array, null);
		size = 0;
	}
	
	private void expand() {
		E[] old = array;
		E[] array = (E[]) new Object[(int) (size * EXPANDING_FACTOR)]; 
		for (int i = 0; i < size; i++) {
			array[i] = old[i];
		}
		// old = null; // not needed because old is a local variable
	}
	
	@Override
	public Iterator<E> iterator() {
		return new ArrayListIterator();
	}	
	
	private class ArrayListIterator implements Iterator<E> {
		
		private int curr = 0;
		
		public ArrayListIterator() {			
		}

		@Override
		public boolean hasNext() {
			return curr < size;
		}

		@Override
		public E next() {
			return array[curr++];
		}
		
		@Override
	    public void remove() {
			ArrayListWithArray.this.remove(curr);
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("[");
		for (int i = 0; i < size; i++) {
			sb.append(array[i].toString());
			if (i < size - 1) {
				sb.append(',');
			}
		}
		sb.append("]");
		return sb.toString();
	}
	
	public static void main(String[] args) {
		ArrayListWithArray<Integer> l = new ArrayListWithArray<>();
		System.out.println(l);
		l.add(1);
		l.add(2);
		l.add(3);
		
		
		for (Integer i : l) {
			System.out.println(i);
		}
		
		l.remove(1);
		System.out.println(l);
	}
	
}

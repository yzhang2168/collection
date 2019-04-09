package tree;

import java.util.Collection;
import java.util.NoSuchElementException;

public class MinHeap <E extends Comparable<E>> {
	private E[] array;
	private int size;
	private static final double SCALING_FACTOR = 1.5;

	public MinHeap(int cap) {
		if (cap <= 0) {
			throw new IllegalArgumentException("Capacity cannot be 0 or less");
		}
		
		array = (E[]) new Object[cap];
		size = 0;
	}	
	
	public MinHeap(Collection<? extends E> c) {
		if (c == null || c.size() == 0) {
			throw new IllegalArgumentException("Input collection cannot be null or empty");
		}
		array = (E[]) c.toArray();
		size = array.length;
		heapify(); // the only place heapify() is called
	}

	/** heapify the heapArray */
	private void heapify() {
		for (int i = size / 2 - 1; i >= 0; i--) {
			percolateDown(i);
		}
	}

	private void percolateUp(int index) {
		while (index > 0) {
			int parentIndex = parentIndex(index);
			if (array[parentIndex].compareTo(array[index]) <= 0) {
				break;
			} else {
				swap(array, parentIndex, index);
			}
			index = parentIndex;
		}
	}

	private void percolateDown(int index) {
		// has left child
		while (index <= size / 2 - 1) {
			int leftChildIndex = leftChildIndex(index);
			int rightChildIndex = rightChildIndex(index);
			
			// the smaller between left and right
			int swapCandidate = leftChildIndex;
			
			// update swapCandiate if right child exists (in range) and < left child
			if (rightChildIndex <= size - 1 && array[leftChildIndex].compareTo(array[rightChildIndex]) >= 0) {
				swapCandidate = rightChildIndex;
			}
			
			// if curr is smaller, swap
			if (array[index].compareTo(array[swapCandidate]) > 0) {
				swap(array, index, swapCandidate);
			} else {
				break;
			}
			
			index = swapCandidate;
		}
		
	}
	
	private void swap(E[] array, int i, int j) {
		E temp = array[i];
		array[i] = array[j];
		array[j] = temp;
	}
	
	public E peek() {
		if (isEmpty()) {
			throw new NoSuchElementException("Heap is empty");
		} else {
			return array[0]; 
		}
	}
	
	public E poll() {
		if (isEmpty()) {
			throw new NoSuchElementException("Heap is empty");
		} else {
			E result = array[0];
			if (size == 1) {
				array[0] = null;
			} else {
				array[0] = array[size - 1];
				percolateDown(0);
			}
			size--;
			return result;
		}
	}
	
	public void offer(E value) {
		if (isFull()) {
			expand();
		}
		array[size] = value;
		percolateUp(size++);
	}
	
	private void expand() {
		E[] old = array;
		array = (E[]) new Object[(int) (size * SCALING_FACTOR)];
		for (int i = 0; i < size; i++) {
			array[i] = old[i];
		}
	}
	
	public E update(int index, E value) {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException();
		}
		
		E result = array[index];
		array[index] = value;
		if (result.compareTo(value) < 1) {
			percolateDown(index);
		} else {
			percolateUp(index);
		}
		return result;
	}
	
	private int parentIndex(int index) {
		return (index - 1) / 2;
	}
	
	private int leftChildIndex(int index) {
		return index * 2 + 1;
	}
	
	private int rightChildIndex(int index) {
		return index * 2 + 2;
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
}

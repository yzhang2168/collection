package tree;

import java.util.Collection;
import java.util.Comparator;
import java.util.NoSuchElementException;

public class MinHeap<E extends Comparable<E>> {
	private Object[] array;
	private int size;
	private static final int INIT_CAPACITY = 10;
	private static final double SCALING_FACTOR = 1.5;
	private Comparator<E> comparator;

	public MinHeap() {
		array = new Object[INIT_CAPACITY];
		size = 0;
		this.comparator = null;
	}

	public MinHeap(int cap) {
		if (cap <= 0) {
			throw new IllegalArgumentException("Capacity cannot be 0 or less");
		}

		array = new Object[cap];
		size = 0;
		this.comparator = null;
	}

	public MinHeap(Comparator<E> comparator) {
		array = new Object[INIT_CAPACITY];
		size = 0;
		this.comparator = comparator;
	}

	public MinHeap(Collection<? extends E> c) {
		if (c == null || c.size() == 0) {
			throw new IllegalArgumentException("Input collection cannot be null or empty");
		}
		array = c.toArray();
		size = array.length;
		this.comparator = null;
		heapify(); // the only place heapify() is called
	}

	/** heapify the heapArray */
	private void heapify() {
		int parentLastNode = parentIndex(size - 1);
		for (int i = parentLastNode; i >= 0; i--) {
			percolateDown(i);
		}
	}

	private void percolateUp(int index) {
		while (index > 0) {
			int parentIndex = parentIndex(index);
			if (comparator != null && comparator.compare((E) array[parentIndex], (E) array[index]) <= 0
					|| comparator == null && ((E) array[parentIndex]).compareTo((E) array[index]) <= 0) {
				break;
			} else {
				swap(array, parentIndex, index);
				index = parentIndex;
			}
		}
	}

	private void percolateDown(int index) {
		// has left child
		while (index <= parentIndex(size - 1)) { // size / 2 - 1) {
			int leftChildIndex = leftChildIndex(index);
			int rightChildIndex = rightChildIndex(index);
			// comparison among 3: converted to pairwise comparisons
			// the smaller between left and right
			int swapCandidate = leftChildIndex;

			// update swapCandiate if right child exists (in range) and < left child
			if (rightChildIndex <= size - 1) {
				if (comparator == null && ((E) array[leftChildIndex]).compareTo((E) array[rightChildIndex]) >= 0 ||
						comparator != null && comparator.compare((E) array[leftChildIndex], (E) array[rightChildIndex]) >= 0) {
				    swapCandidate = rightChildIndex;
				}
			}

			// if curr is larger, swap
			if (comparator == null && ((E) array[index]).compareTo((E) array[swapCandidate]) > 0 ||
					comparator != null && comparator.compare((E) array[index], (E) array[swapCandidate]) > 0) {
				swap(array, index, swapCandidate);
				index = swapCandidate;
			} else {
				break;
			}
		}
	}

	private void swap(Object[] array, int i, int j) {
		Object temp = array[i];
		array[i] = array[j];
		array[j] = temp;
	}

	public E peek() {
		if (isEmpty()) {
			throw new NoSuchElementException("Heap is empty");
		} else {
			return (E) array[0];
		}
	}

	public E poll() {
		if (isEmpty()) {
			throw new NoSuchElementException("Heap is empty");
		} else {
			E result = (E) array[0];
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
		Object[] old = array;
		array = new Object[(int) (size * SCALING_FACTOR)];
		for (int i = 0; i < size; i++) {
			array[i] = old[i];
		}
	}

	public E update(int index, E value) {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException();
		}

		E result = (E) array[index];
		array[index] = value;
		if (result.compareTo(value) < 0) {
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

	public static void main(String[] args) {
		MinHeap<Integer> heap1 = new MinHeap<Integer>();
		for (int i = 5; i >= 1; i--) {
			heap1.offer(i);
		}
		System.out.println("minheap size: " + heap1.size());

		for (int i = 0; i < 5; i++) {
			System.out.println(heap1.poll());
		}
		System.out.println("size: " + heap1.size());

		MinHeap<Integer> heap2 = new MinHeap<Integer>(new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				if (o1 == o2 || o1.equals(o2)) {
					return 0;
				} else if (o1 < o2) {
					return 1;
				} else {
					return -1;
				}
			}

		});

		for (int i = 1; i <= 5; i++) {
			heap2.offer(i);
		}
		System.out.println("\nmaxheap size: " + heap2.size());

		for (int i = 0; i < 5; i++) {
			System.out.println(heap2.poll());
		}
		System.out.println("size: " + heap2.size());
	}
}

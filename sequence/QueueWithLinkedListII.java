package sequence;

/** implements queue using Java LinkedList and composition
 * */
public class QueueWithLinkedListII<E> {

	private java.util.LinkedList<E> list;
	
	public QueueWithLinkedListII() {
		list = new java.util.LinkedList<>();
	}
	
	public void offer(E value) {
		list.add(value);
	}
	
	public E poll() {
		return list.size() == 0 ? null : list.remove(0);
	}
	
	public E peek() {
		return list.size() == 0 ? null : list.get(0);
	}
	
	public int size() {
		return list.size();
	}
	
	public boolean isEmpty() {
		return size() == 0;
	}
	
	@Override
	public String toString() {
		return list.toString();
	}
	
	public static void main(String[] args) {
		QueueWithLinkedListII<Integer> l = new QueueWithLinkedListII<>();
		System.out.println(l);
		System.out.println(l.poll());
		System.out.println(l.peek());
		l.offer(1);
		l.offer(2);
		l.offer(3);
		System.out.println(l);
		System.out.println(l.poll());
		System.out.println(l.peek());
		System.out.println(l);		
	}
}

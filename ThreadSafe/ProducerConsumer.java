package ThreadSafe;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ProducerConsumer {

	public static void main(String[] args) {
		Q q = new Q(20);
		List<Thread> threads = new ArrayList<Thread>();
		
		// referencing the same q
		for (int i = 0; i < 100; i++) {
			threads.add(new Thread(new Producer(q)));
		}
		
		for (int i = 0; i < 100; i++) {
			threads.add(new Thread(new Consumer(q)));			
		}
	
		for (Thread t : threads) {
			t.start();
		}
	}
}

class Producer implements Runnable {
	final Q q;
	
	public Producer(final Q q) {
		super();
		this.q = q;
	}
	
	@Override
	public void run() {
		q.put(0);
	}
}

class Consumer implements Runnable {
	final Q q;
	
	public Consumer(final Q q) {
		super();
		this.q = q;
	}
	
	@Override
	public void run() {
		System.out.println(q.take());
	}
}

/** a blocking queue */
class Q {
	private Queue<Integer> q;
	private final int limit;
	
	public Q(int limit) {
		this.q = new LinkedList<Integer>();
		this.limit = limit;
	}
	
	/** synchronized(this) */
	public synchronized void put(Integer e) {
		while (q.size() == limit) {
			try {
				wait();// if q is not full, room #2 to room #3, no lock anymore
				// consumer notify(): from room #3 to room #1. 
				// put(): when we get the lock, from room #1 to room #2, q could be full
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		}
		
		// q.size() != limit
		if (q.size() == 0) {
			notifyAll();
		}
		
		q.offer(e);
	}
	
	public synchronized Integer take() {
		while (q.size() == 0) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		if (q.size() == limit) {
			notifyAll();
		}
		
		return q.poll();
	}
}

package hashing;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;

public class HashSetImpl<E> implements Iterable<E> {

    private static final int INIT_CAPACITY = 10;
    private static final int MAX_CAPACITY = 1 << 30; // 2 ^ 30
    private static final int SCALING_FACTOR = 2;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private LinkedList<E>[] table;
    private float loadFactor;
    private int size;
    
    public HashSetImpl() {
        this(INIT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }
    
    public HashSetImpl(int capacity) {
        this(capacity, DEFAULT_LOAD_FACTOR);
    }
    
    public HashSetImpl(int capacity, float loadFactor) {
        capacity = capacity > MAX_CAPACITY ? MAX_CAPACITY : trimToPowerOf2(capacity);
        this.loadFactor = loadFactor; 
        table = (LinkedList<E>[]) new LinkedList[capacity];
        size = 0;
    }
    
    /** change capacity to min power of 2 > capacity such that i % N => i & (N - 1), much faster */
    private int trimToPowerOf2(int capacity) {
        int cap = 1;
        while (cap < capacity) {
            cap <<= 1;
        }
        return cap;
    }
    
    public void clear() {
        Arrays.fill(table, null);
        size = 0;
    }
    
    public boolean contains(E e) {
        int index = getIndex(e);
        LinkedList<E> list = table[index];
        if (list != null) {
            for (E curr : list) {
                if (curr.equals(e)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private int getIndex(E e) {       
        if (e == null) {
            return 0;
        }
        
        // post processing: hashValue -> non-negative number
        int hashValue = e.hashCode() & 0x7fffffff;
        return hashValue & (table.length - 1);        
    }
    
    public boolean add(E e) {
        if (contains(e)) {
            return false;
        }

        int index = getIndex(e);
        LinkedList<E> list = table[index];
        if (list == null) {
            table[index] = new LinkedList<E>();
        }
        list.add(e);
        size++;
        
        if (needsRehashing()) {
            rehash();
        }
        
        return true;
    }
    
    private boolean needsRehashing() {
        return size >= table.length * loadFactor;
    }
    
    private void rehash() {
        LinkedList<E>[] old = table;
        table = (LinkedList<E>[]) new LinkedList[size * SCALING_FACTOR];
        size = 0;
        
        for (LinkedList<E> list : old) {
            if (list != null) {
                for (E curr : list) {                    
                    add(curr);
                }
            }
        }
    }
    
    public boolean remove(E e) {
        int index = getIndex(e);
        LinkedList<E> list = table[index];
        boolean found = false;
        if (list != null) {
            found = list.remove(e);
        }
        
        if (found) {
            size--;
            return true;
        } else {
            return false;
        }
    }
    
    public boolean isEmpty() {
        return size == 0;
    }
    
    public int size() {
        return size;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        
        for (LinkedList<E> list : table) {
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    sb.append(list.get(i));
                    if (i != list.size() - 1) {
                        sb.append(", ");
                    }
                }
            }
        }
        
        sb.append(']');
        return sb.toString();
    }
    
    private ArrayList<E> setToList() {
        ArrayList<E> result = new ArrayList<E> ();
        for (LinkedList<E> list : table) {
            result.addAll(list);
        }
        return result;
    }
    
    @Override
    public Iterator<E> iterator() {
        return new HashSetImplIterator(this);
    }
    
    private class HashSetImplIterator implements Iterator<E> {
        private ArrayList<E> list;
        private int curr = 0;
        private HashSetImpl<E> set;
        
        public HashSetImplIterator(HashSetImpl<E> set) {
            this.set = set;
            list = setToList();
        }
        
        @Override
        public boolean hasNext() {
            return curr < list.size();
        }
        
        @Override
        public E next() {
            return list.get(curr++);
        }
        
        @Override
        public void remove() {
            set.remove(list.get(curr));
            list.remove(curr);
        }
    }
    
}

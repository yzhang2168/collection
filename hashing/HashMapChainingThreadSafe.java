package hashing;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * data race: visiting the same memory, write, concurrent
 * */
public class HashMapChainingThreadSafe<K, V> {

    private static final int INIT_CAPACITY = 16;
    private static final int MAX_CAPACITY = 1 << 30; // 2 ^ 30. 1 << 1 => *= 2
    private static final int SCALE_FACTOR = 2;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private final float loadFactor;

    private ArrayList<Entry<K, V>>[] hashTable;
    private int size = 0;

    public HashMapChainingThreadSafe() {
        this(INIT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public HashMapChainingThreadSafe(int capacity) {
        this(capacity, DEFAULT_LOAD_FACTOR);
    }

    public HashMapChainingThreadSafe(int capacity, float loadFactor) {
        capacity = capacity <= MAX_CAPACITY ? trimToPowerOf2(capacity) : MAX_CAPACITY;
        this.loadFactor = loadFactor;
        hashTable = (ArrayList<Entry<K, V>>[]) new ArrayList[capacity]; // [null,
                                                                        // null,...]
    }

    /**
     * change capacity to min power of 2 > capacity such that i % N => i & (N -
     * 1), much faster
     */
    private int trimToPowerOf2(int capacity) {
        int cap = 1;
        while (cap < capacity) {
            cap <<= 1; // same as *= 2
        }
        return cap;
    }

    public synchronized void clear() {
        Arrays.fill(hashTable, null);
        size = 0;
    }

    private int getIndex(K key) {
        if (key == null) {
            return 0;
        }
        
        // non-negative
        int hash = key.hashCode() & 0x7fffffff;
        // when N is a power of 2, % N => & (N - 1)
        return hash & (hashTable.length - 1); 
    }

    /** Ensure the hashing is evenly distributed */
    private static int supplementalHash(int h) {
        h ^= (h >>> 20) ^ (h >>> 12);
        return h ^ (h >>> 7) ^ (h >>> 4);
    }

    public synchronized V put(K key, V value) {
        int index = getIndex(key);
        ArrayList<Entry<K, V>> list = hashTable[index];

        if (list != null) {
            for (Entry<K, V> entry : list) {
                if (entry.getKey().equals(key)) {
                    V old = entry.getValue();
                    entry.set(value);
                    return old;
                }
            }
        }

        // key not found
        if (list == null) {
            list = new ArrayList<Entry<K, V>>();
        }

        list.add(new Entry<K, V>(key, value));
        size++;

        // resize
        if (needsRehashing()) {
            rehash();
        }

        return value;
    }

    private boolean needsRehashing() {
        return size >= hashTable.length * loadFactor;
    }

    private void rehash() {
        if (hashTable.length >= MAX_CAPACITY) {
            throw new RuntimeException("Exceeding maximum capacity");
        }

        ArrayList<Entry<K, V>>[] old = hashTable;
        int capacity = hashTable.length * SCALE_FACTOR; 
        hashTable = (ArrayList<Entry<K, V>>[]) new ArrayList[capacity]; // [null, null,...]
        size = 0;        

        for (ArrayList<Entry<K, V>> list : old) {
            if (list != null) {
                for (Entry<K, V> entry : list) {
                    put(entry.getKey(), entry.getValue());
                }
            }
        }
    }

    public synchronized boolean containsKey(K key) {
        return get(key) != null;
    }

    public synchronized boolean containsValue(V value) {
        for (ArrayList<Entry<K, V>> list : hashTable) {
            if (list != null) {
                for (Entry<K, V> entry : list) {
                    if (entry.getValue().equals(value)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public synchronized V get(K key) {
        int index = getIndex(key);
        ArrayList<Entry<K, V>> list = hashTable[index];

        if (list != null) {
            for (Entry<K, V> entry : list) {
                if (entry.getKey().equals(key)) {
                    return entry.getValue();
                }
            }
        }

        return null;
    }

    public synchronized void remove(K key) {
        int index = getIndex(key);
        ArrayList<Entry<K, V>> list = hashTable[index];
        if (list != null) {
            for (Entry<K, V> entry : list) {
                if (entry.getKey().equals(key)) {
                    list.remove(entry);
                    size--;
                    return;
                }
            }
        }
    }

    public synchronized Set<Entry<K, V>> entrySet() {
        Set<Entry<K, V>> result = new HashSet<Entry<K, V>>();

        for (ArrayList<Entry<K, V>> list : hashTable) {
            if (list != null) {
                for (Entry<K, V> entry : list) {
                    result.add(entry);
                }
            }
        }

        return result;
    }

    public synchronized Set<K> keySet() {
        Set<K> result = new HashSet<K>();

        for (ArrayList<Entry<K, V>> list : hashTable) {
            if (list != null) {
                for (Entry<K, V> entry : list) {
                    result.add(entry.getKey());
                }
            }
        }

        return result;
    }

    public synchronized Set<V> valueSet() {
        Set<V> result = new HashSet<V>();

        for (ArrayList<Entry<K, V>> list : hashTable) {
            if (list != null) {
                for (Entry<K, V> entry : list) {
                    result.add(entry.getValue());
                }
            }
        }

        return result;
    }

    public synchronized boolean isEmpty() {
        return size == 0;
    }

    public synchronized int size() {
        return size;
    }

    @Override
    public synchronized String toString() {
        StringBuilder sb = new StringBuilder("[");

        for (ArrayList<Entry<K, V>> list : hashTable) {
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    sb.append(list.get(i)).append(", ");
                }
            }
        }

        if (sb.length() > 1) {
            sb.delete(sb.length() - 2, sb.length());
        }

        sb.append("]");
        return sb.toString();
    }

    /**
     * public: makes the Entry class accessible for the users; without public,
     * only within package nested: such that the class name does not pollute
     * outside static: such that an Entry object can be created without a
     * HashMap object if non-static, an Entry object depends on a HashMap object
     * by making the nested class `static` it loses access to all members of the
     * outer class.
     */
    public static class Entry<Ki, Vi> {
        private final Ki key;
        private Vi value;

        public Entry(Ki key, Vi value) {
            this.key = key;
            this.value = value;
        }

        public Vi getValue() {
            return value;
        }

        public Ki getKey() {
            return key;
        }

        public Vi set(Vi value) {
            Vi old = value;
            this.value = value;
            return old;
        }

        @Override
        public String toString() {
            return key.toString() + "=" + value.toString();
        }
    }
}

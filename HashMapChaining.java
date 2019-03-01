import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class HashMapChaining<K, V> {

    private static final int INIT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int MAX_CAPACITY = 1 << 30; // 2 ^ 30
    private int capacity;
    private final float loadFactor;
    
    private ArrayList<Entry<K, V>>[] hashTable;
    private int size = 0;
    
    public HashMapChaining() {
        this(INIT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }
    
    public HashMapChaining(int capacity) {
        this(capacity, DEFAULT_LOAD_FACTOR);
    }

    public HashMapChaining(int capacity, float loadFactor) {
        this.capacity = capacity <= MAX_CAPACITY? trimToPowerOf2(capacity) : MAX_CAPACITY;
        this.loadFactor = loadFactor;
        hashTable = (ArrayList<Entry<K, V>>[]) new ArrayList[this.capacity];    
    }
    
    private int trimToPowerOf2(int capacity) {
        int cap = 1;
        while (cap < capacity) {
            cap <<= 1; // same as *= 2
        }
        return cap;
    }
    
    public void clear() {
        hashTable = (ArrayList<Entry<K, V>>[]) new ArrayList[INIT_CAPACITY];
        size = 0;
    }
    
    private int hashCode(K key) {
        return key.hashCode();
    }
    
    private int hash(int hashCode) {
        return supplementalHash(hashCode) & (capacity - 1); // same as % capacity
    }
    
    /** Ensure the hashing is evenly distributed */
    private static int supplementalHash(int h) {
        h ^= (h >>> 20) ^ (h >>> 12);
        return h ^ (h >>> 7) ^ (h >>> 4);
    }
    
    public V put(K key, V value) {
        int index = hash(key.hashCode());
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
        if (size >= capacity * loadFactor) {
            if (capacity >= MAX_CAPACITY) {
                throw new RuntimeException("Exceeding maximum capacity");
            } else {
                rehash();
            }
        }
        
        return value;
    }
    
    private void rehash() {
        capacity <<= 1; // same as capacity * 2
        ArrayList<Entry<K, V>>[] old = hashTable;
        hashTable = (ArrayList<Entry<K, V>>[]) new ArrayList[capacity];
        size = 0;
        
        for (ArrayList<Entry<K, V>> list : old) {
            if (list != null) {
                for (Entry<K, V> entry : list) {
                    put(entry.getKey(), entry.getValue());
                }
            }
        }
    }

    public boolean containsKey(K key) {
        return get(key) != null;
    }
    
    public boolean containsValue(V value) {
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
    
    public V get(K key) {
        int index = hash(key.hashCode());
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
    
    public void remove(K key) {
        int index = hash(key.hashCode());
        ArrayList<Entry<K, V>> list = hashTable[index];
        if (list != null) {
            for (Entry<K, V> entry : list) {
                if (entry.getKey().equals(key)) {
                    list.remove(entry);
                    size--;
                    break;
                }
            }
        }
    }
    
    public Set<Entry<K, V>> entrySet() {
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
    
    public Set<K> keys() {
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
    
    public Set<V> values() {
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
    
    public boolean isEmpty() {
        return size == 0;
    }
    
    public int size() {
        return size;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        
        for (ArrayList<Entry<K, V>> list : hashTable) {
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
    
    public static class Entry<Ki, Vi> { // --------> static
        private final Ki key;
        private Vi value;
        
        public Entry (Ki key, Vi value) {
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


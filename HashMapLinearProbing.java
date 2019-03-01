import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class HashMapLinearProbing<K, V> {

    private static final int INIT_CAPACITY = 4;
    private static final int MAX_CAPACITY = 1 << 30; // 2 ^ 30
    private static final int SCALE_FACTOR = 2;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private float loadFactor;

    private ArrayList<Entry<K, V>> table;
    private int size;

    public HashMapLinearProbing() {
        this(INIT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public HashMapLinearProbing(int capacity) {
        this(capacity, DEFAULT_LOAD_FACTOR);
    }

    public HashMapLinearProbing(int capacity, float loadFactor) {
        capacity = capacity > MAX_CAPACITY ? MAX_CAPACITY : trimToPowerOf2(capacity);
        this.loadFactor = loadFactor;

        table = (ArrayList<Entry<K, V>>) new ArrayList();
        for (int i = 0; i < capacity; i++) {
            table.add(null);
        }
        
        size = 0;
    }

    public void clear() {
        table.clear();
        size = 0;
    }

    /* not dealing with deleted entry situation */
    public V put(K key, V value) {
        int index = getIndex(key);
        Entry<K, V> entry = table.get(index);
        while (entry != null) {
            if (entry.getKey().equals(key)) {
                V old = entry.getValue();
                entry.setValue(value);
                return old;
            }
            index++;
            index = index % table.size();
        }
        
        // entry == null
        table.set(index, new Entry<K, V>(key, value));
        size++;
        
        if (needsRehashing()) {
            rehash();
        }
        
        return value;
    }

    private boolean needsRehashing() {
        if (table.size() == MAX_CAPACITY) {
            throw new RuntimeException("exceeding maximum capacity");
        }
        return size >= table.size() * loadFactor;
    }
    
    private void rehash() {
        // resize
        ArrayList<Entry<K, V>> old = table;
        table = (ArrayList<Entry<K, V>>) new ArrayList();
        size = 0;
        for (int i = 0; i < old.size() * SCALE_FACTOR; i++) {
            table.add(null);
        }
        
        // reuse code
        for (Entry<K, V> entry : old) {
            if (entry != null) {
                put(entry.getKey(), entry.getValue());
            }
        }
    }
    
    private int trimToPowerOf2(int initCapacity) {
        int capacity = 1;
        while (capacity < initCapacity) {
            capacity <<= 1;
        }
        return capacity;
    }
    
    public V get(K key) {
        int index = getIndex(key);
        while (table.get(index) != null && !table.get(index).deleted) {
            if (table.get(index).getKey().equals(key)) {
                return table.get(index).getValue();
            }
            index++;
            index = index % table.size();
        }
        return null;
    }

    /** 
     * ==== allows duplicate keys ====
     *  public V put(K key, V value) {
            int index = getIndex(key);
            Entry<K, V> entry = table.get(index);
            while (entry != null) {
                index++;
                index = index % table.size();
            }

            table.set(index, new Entry<K, V>(key, value));
            size++;
        
            if (needsRehashing()) {
                rehash();
            }
        
            return value;
        }
        
        public Set<V> getAll(K key) {
            Set<V> result = new HashSet<V>();
            int index = getIndex(key);
            
            while (table.get(index) != null) {
                if (table.get(index).getKey().equals(key)) {
                    result.add(table.get(index).getValue());
                }
            }
            return result;
        }    
         * */
    
    private int getIndex(K key) {
        if (key == null) {
            return 0;
        }
        int hash = key.hashCode() & (table.size() - 1); // % table.size()
        return hash & 0x7fffffff; // non-negative
    }

    public void remove(K key) {
        int index = getIndex(key);
        Entry<K, V> entry = table.get(index);
        while (entry != null) {
            if (entry.getKey().equals(key)) {
                entry.deleted = true; // cannot shift everything up, affects put(), get(), rehash()
                size--;
                return;
            }
            index++;
            index = index % table.size();
        }
    }

    public boolean containsKey(K key) {
        return get(key) != null;
    }

    public boolean containsValue(V value) {
        for (Entry<K, V> entry : table) {
            if (entry != null && entry.getValue().equals(value)) {
                    return true;
            }
        }
        return false;
    }

    public Set<Entry<K, V>> entrySet() {
        Set<Entry<K, V>> result = new HashSet<Entry<K, V>>();
        for (Entry<K, V> entry : table) {
            if (entry != null) {
                result.add(entry);
            }
        }
        return result;
    }

    public Set<K> keySet() {
        Set<K> result = new HashSet<K>();
        for (Entry<K, V> entry : table) {
            if (entry != null) {
                result.add(entry.getKey());
            }
        }
        return result;
    }

    public Set<V> valueSet() {
        Set<V> result = new HashSet<V>();
        for (Entry<K, V> entry : table) {
            if (entry != null) {
                result.add(entry.getValue());
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
        for (int i = 0; i < table.size(); i++) {
            if (table.get(i) != null) {
                sb.append(table.get(i));
                    sb.append(", ");
            }
        }
        if (sb.length() > 1) {
            sb.delete(sb.length() - 2, sb.length());
        }
        sb.append(']');
        return sb.toString();
    }

    public static class Entry<Ki, Vi> {

        private Ki key;
        private Vi value;
        private boolean deleted;

        public Entry(Ki key, Vi value) {
            this.key = key;
            this.value = value;
            this.deleted = false;
        }

        public Ki getKey() {
            return key;
        }

        public Vi getValue() {
            return value;
        }
        
        public Vi setValue(Vi value) {
            Vi old = this.value;
            this.value = value;
            return old;
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }

            if (o instanceof Entry) {
                Entry<Ki, Vi> other = (Entry<Ki, Vi>) o;
                return this.key.equals(other.key) && this.value.equals(other.value);
            }

            return false;
        }

        @Override
        public int hashCode() {
            return key.hashCode() * 101 + value.hashCode();
        }

        @Override
        public String toString() {
            return key + "=" + value;
        }
    }
    
    public static void main(String[] args) {
        HashMapLinearProbing<String, Integer> map = new HashMapLinearProbing<>();
        System.out.println(map);
        System.out.println("size = " + map.size());
        System.out.println("table size = " + map.table.size());
        System.out.println();
        
        map.put("A", 1);
        System.out.println(map);
        System.out.println("size = " + map.size());
        System.out.println("table size = " + map.table.size());
        System.out.println();

        map.put("B", 2);
        System.out.println(map);
        System.out.println("size = " + map.size());
        System.out.println("table size = " + map.table.size());
        System.out.println();

        map.put("C", 3);
        System.out.println(map);
        System.out.println("size = " + map.size());
        System.out.println("table size = " + map.table.size());
        System.out.println();
        
        map.put("D", 4);
        System.out.println(map);
        System.out.println("size = " + map.size());
        System.out.println("table size = " + map.table.size());
        System.out.println();
        
        map.put("E", 5);
        System.out.println(map);
        System.out.println("size = " + map.size());
        System.out.println("table size = " + map.table.size());
        System.out.println();

        map.put("F", 6);
        System.out.println(map);
        System.out.println("size = " + map.size());
        System.out.println("table size = " + map.table.size());
        System.out.println();

        map.put("G", 7);
        System.out.println(map);
        System.out.println("size = " + map.size());
        System.out.println("table size = " + map.table.size());
        System.out.println();

        map.put("A", 8);
        System.out.println(map);
        System.out.println("size = " + map.size());
        System.out.println("table size = " + map.table.size());
        System.out.println(map.entrySet());
        System.out.println(map.keySet());
        System.out.println(map.valueSet());
        System.out.println();

        map.remove("A");
        System.out.println(map);
        System.out.println("size = " + map.size());
        System.out.println("table size = " + map.table.size());
        System.out.println(map.containsKey("A"));
        System.out.println(map.containsKey("B"));
        
        for (Entry<String, Integer> entry : map.entrySet()) { // a set
            System.out.println(entry);
        }
    }
}

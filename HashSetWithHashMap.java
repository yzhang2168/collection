import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


public class HashSetWithHashMap<E> implements Iterable<E> {

    private HashMap<E, E> map;
    
    public HashSetWithHashMap() {
        map = new HashMap<E, E>();
    }
    
    public void clear() {
        map.clear();
    }
    
    public boolean contains(E e) {
        return map.containsKey(e);
    }
    
    public boolean add(E e) {
        E element = map.put(e, e);
        return element != null;
    }
    
    public boolean remove(E e) {
        E value = map.remove(e);
        return value != null;
    }
    
    public int size() {
        return map.size();
    }
    
    public boolean isEmpty() {
        return map.isEmpty();
    }
    
    public List<E> setToList() {
        List<E> list = new ArrayList<E>();
        for (E e : map.keySet()) {
            list.add(e);
        }
        return list;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        
        List<E> list = setToList();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i));
            if ( i != list.size() - 1) {
                sb.append(", ");
            }
        }
        
        sb.append("]");
        return sb.toString();
    }
    
    @Override
    public Iterator<E> iterator() {
        return new HashSetIterator(this);
    }
    
    /** inner class for iterator 
     *  no <E> because <E> already exists and this class is already generic
     * */
    private class HashSetIterator implements Iterator<E> { 
        private List<E> list;
        private int curr;
        
        public HashSetIterator(HashSetWithHashMap<E> set) {
            list = set.setToList();
        }
        
        @Override
        public boolean hasNext() {
            return curr < list.size();
        }

        @Override
        public E next() {
            return list.get(curr++);
        }
    }
    
    public static void main(String[] args) {
        // Create a MyHashSet
        HashSetWithHashMap<String> set = new HashSetWithHashMap<>();
        set.add("Smith");
        set.add("Anderson");
        set.add("Lewis");
        set.add("Cook");
        set.add("Smith");

        System.out.println("Elements in set: " + set);
        System.out.println("Number of elements in set: " + set.size());
        System.out.println("Is Smith in set? " + set.contains("Smith"));

        System.out.println("Remove Smith in set " + set.remove("Smith"));
        System.out.println("Names in set in uppercase are ");
        
        for (String s: set) {
            System.out.print(s.toUpperCase() + " ");
        }
                    
        set.clear();
        System.out.println("\nElements in set: " + set);
    }
}

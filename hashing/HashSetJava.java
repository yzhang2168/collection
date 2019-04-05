package hashing;

import java.util.HashMap;

public class HashSetJava<K> {

	private HashMap<K, Object> map;
	
	// a special object for all existing keys
	private static final Object PRESENT = new Object();
	
	public HashSetJava() {
		map = new HashMap<K, Object>();
	}
	
	public boolean contains(K key) {
		return map.containsKey(key);
	}
	
	public boolean add(K key) {
		return map.put(key, PRESENT) == null;
	}
}

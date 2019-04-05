package hashing;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


public class HashMapChainingArray<K, V> {
		/**
		 * public: makes the EntryNode class accessible from outside; without public, only within package
		 * 
		 * nested class: so that the class name does not pollute outside
		 * static class: so that an EntryNode object can be created without a HashMap object
		 * 			if non-static, an EntryNode object depends on a HashMap object
		 * 			by making the nested class `static` it loses access to all members of the outer class.
		 * 
		 * make the class public because we want to make EntryNode available to users
		 * no public: accessible from within package
		 * */
		
		//TODO: thread safety; distributed HashMap		
		public static class Entry<Ki, Vi> {
			// private fields need getter and setter
			private final Ki key;
			private Vi value;
			private Entry<Ki, Vi> next;

			// no need to declare K, V because they are declared at class name
			public Entry(Ki key, Vi value) {
				this.key = key;
				this.value = value;
				this.next = null;
			}

			// private fields need getter and setter
			// key is final, so no setter for key
			public Ki getKey() {
				return key;
			}

			public Vi getValue() {
				return value;
			}

			public Vi setValue(Vi value) {
				Vi old = getValue();
				this.value = value;	
				return old;
			}
			
			/*
			@Override
			public boolean equals(Object obj) {
				if (this == obj) {
					return true;
				}
				if (!(obj instanceof Entry)) {
					return false;
				}
				Entry<Ki, Vi> other = (Entry<Ki, Vi>) obj;
				return this.key == other.key && this.value == other.value;
			}
			
			@Override
			public int hashCode() {
				if (key == null) {
					return 0;
				} else {
					return key.hashCode() * 101 + value.hashCode();		
				}
			}
			*/
			
			@Override
			public String toString() {
				return key + "=" + value;
			}
		}

		// use a constant instead of a hard-coded number, easier to find and understand
		private static final int INIT_CAPACITY = 10;
		private static final int MAX_CAPACITY = 1 << 30; // 2^30. 1 << 1 => *= 2
		private static final double LOAD_FACTOR = 0.75;
		private static final int SCALING_FACTOR = 2;

		private Entry<K, V>[] array;
		private int size; // be sure to update size as we add/remove entry

		public HashMapChainingArray() {
			array = (Entry<K, V>[]) new Entry[INIT_CAPACITY];
			// Java array does not allow generic type because JVM needs to know the size of each slot
			// array = new EntryNode<K, V>[INIT_CAP]; - compiling error
			// need to cast it to EntryNode<K, V> type
			size = 0; 
		}

		public int size() {
			return size;
		}

		public boolean isEmpty() {
			return size == 0;
		}

		public void clear() {
			Arrays.fill(array, null);
			size = 0;
		}

		public V get(K key) {
			// traverse the list, target cell could be null
			Entry<K, V> curr = array[getIndex(key)];
			while (curr != null) {
				// key could be null, cannot use curr.getKey().equals(key) directly
				if (keysEqual(curr.getKey(), key)) {
					return curr.getValue();
				}
				curr = curr.next;
			}
			// if return value is null, then either not found, or key was found and value is null
			return null;
		}

		public boolean containsKey(K key) {
			// traverse the list, target cell could be null
			Entry<K, V> curr = array[getIndex(key)];
			while (curr != null) {
				// if (curr.getKey().equals(key)) { // NPE: curr.getKey() could be null
				if (keysEqual(curr.getKey(), key)) {
					return true;
				}
				curr = curr.next;
			}
			return false;
		}

		public V put(K key, V value) {
			// calculate hash value & value % size
			int index = getIndex(key);

			// traverse the list to find key, target cell could be null
			Entry<K, V> curr = array[getIndex(key)];
			while (curr != null) {
				if (keysEqual(curr.getKey(), key)) {
					V oldValue = curr.getValue();
					curr.setValue(value);
					return oldValue;
				}
				curr = curr.next;
			}
			
			// if key was not found, return null
			Entry<K, V> newNode = new Entry<K, V>(key, value);
			// insert as head of list
			newNode.next = array[index];
			array[index] = newNode;
			size++;
			
			// size increased, may need to rehash!
			if (needsRehashing()) {
				rehash();
			}
			
			return null;
		}

		public V remove(K key) {
			// calculate hash value & value % size
			int index = getIndex(key);
			
			// traverse the list
			Entry<K, V> prev = null;
			Entry<K, V> curr = array[index];
			// if curr == null, not into the while()
			while (curr != null) {
				if (keysEqual(curr.getKey(), key)) {
					if (prev == null) {
						array[index] = curr.next;
					} else {
						prev.next = curr.next;
					}
					size--; // do not forget!!
					return curr.getValue();
				}
				prev = curr;
				curr = curr.next;
			}
			return null;
		}

		/** cover corner cases not handled by hashCode() */
		private int getHash(K key) {
			// always put null into array[0]
			if (key == null) {
				return 0;
			}
			// post processing: hashValue -> non-negative number
			// bitwise & any & 0 = 0 sign bit
			return key.hashCode() & 0x7fffffff;
		}

		private int getIndex(K key) {
			return getHash(key) % array.length;
		}

		/** cover key == null that is not handled by equals() */
		private boolean keysEqual(K key1, K key2) {
			/*
			if (key1 == key2) { // null & same object
				return true;
			} else if (key1 == null || key2 == null) {
				return false;
			} else {
				return key1.equals(key2);
			}
			// can be simplified to:
			*/
			return key1 == key2 || key1 != null && key1.equals(key2); 
		}
		
		private boolean needsRehashing() {
			return size > LOAD_FACTOR * array.length;
		}

		private void rehash() {
			if (array.length >= MAX_CAPACITY) {
	            throw new RuntimeException("Exceeding maximum capacity");
	        }
			
			Entry<K, V>[] old = array;
			array = (Entry<K, V>[]) new Entry[old.length * SCALING_FACTOR];

			for (Entry<K, V> curr : old) {
				// traverse the list
				while (curr != null) {
					// save node.next b/c node.next will be set to a different node
					Entry<K, V> next = curr.next;

					// new hash will re-separate nodes b/c array.length is doubled
					// find new cell in new array and insert into head of list
					int newIndex = getIndex(curr.getKey());
					curr.next = array[newIndex];
					array[newIndex] = curr;
					
					curr = next;
				}
			}
		}
		
		public Set<K> keySet() {
			Set<K> result = new HashSet<K>();
			for (Entry<K, V> curr : array) {
				while (curr != null) {
					result.add(curr.getKey());
					curr = curr.next;
				}
			}
			return result;
		}

		public Set<V> valueSet() {
			Set<V> result = new HashSet<V>();
			for (Entry<K, V> curr : array) {
				while (curr != null) {
					result.add(curr.getValue());
					curr = curr.next;
				}
			}
			return result;
		}
		
		public Set<Entry<K, V>> entrySet() {
	        Set<Entry<K, V>> result = new HashSet<Entry<K, V>>();
	        for (Entry<K, V> curr : array) {
	            while (curr != null) {
	                result.add(curr);
	                curr = curr.next;
	            }
	        }
	        return result;
	    }

		@Override
		public String toString() {
			/*
			StringBuilder sb = new StringBuilder("[");
			
			for (Entry<K, V> entry : array) {
				// each cell is either EntryNode or null
				while (entry != null) {
					sb.append(entry.toString()).append(", ");
					entry = entry.next;
				}
			}
			
			if (sb.length() > 1) {
                sb.delete(sb.length() - 2, sb.length());
            }

			sb.append("]");
			return sb.toString();
			*/
			return entrySet().toString();
		}
		
		
		public static void main(String[] args) {
			char c0 = (char) Math.pow(2, 32);
			char c1 = 65;
			System.out.println(c0);
			System.out.println(c1);
			
			Entry<String, Integer> node = new Entry<>("F", 100);
			System.out.println(node.getValue());
			System.out.println(node.setValue(-100));
			System.out.println(node.getValue());

			HashMapChainingArray<String, Integer> map0 = null;
			System.out.println(map0);
			//System.out.println(map0.containsKey("G")); // NPE
			
			HashMapChainingArray<String, Integer> map = new HashMapChainingArray<>();
			map.put(null, -1);
			map.put(null, -2);
			map.put("A", 1);
			map.put("B", 2);
			System.out.println(map.get("B"));
			System.out.println("contains G   :" + map.containsKey("G"));
			System.out.println("contains null:" + map.containsKey(null)); // key could be null
			System.out.println("toString():   " + map);
			System.out.println("keySet():     " + map.keySet());
			System.out.println("valueSet():   " + map.valueSet());
			System.out.println("entrySet():   " + map.entrySet());
		}
}

package frontsnapk1ck.utility.cache;

import java.util.Set;

public interface Cache<K , H> {

    /**
     * Associates the specified value with the specified key in this map. 
     * If the map previously contained a mapping for
     * the key, the old value is replaced by the specified value.
     * 
     * @param key key with which the specified value is to be associated
     * @param value value to be associated with the specified key
     */
    void put(K key, H value);
 
   /**
     * Returns the value to which the specified key is mapped,
     * or {@code null} if this map contains no mapping for the key.
     *
     * @param key the key whose associated value is to be returned
     * @return the value to which the specified key is mapped, or
     *          {@code null} if this map contains no mapping for the key     */
    H get(K key);

    /**
     * Returns a {@link Set} view of the keys contained in this map.
     * 
     * @return a set view of the keys contained in this map
     */
    Set<K> keySet();
    
    /**
     * Removes the mapping for a key from this map if it is present
     * 
     * @param key key whose mapping is to be removed from the map
     * @return the previous value associated with {@code key}, or
     *         {@code null} if there was no mapping for {@code key}.
     */
    H remove(K key);
 
    /**
     * Returns the number of key-value mappings in this map.
     *
     * @return the number of key-value mappings in this map
     */
    int size();

    /**
     * Returns {@code true} if this map contains a mapping for the specified
     * key where {@code k} satisfies {@code Objects.equals(key, k)}.
     * @param key key whose presence in this map is to be tested
     * @return {@code true} if this map contains a mapping for the specified
     *         key
     */
    boolean has(K key);

    
}

package by.pintusov.myCache.api;

/**
 * 
 * My cache interface
 * @author pintusov
 */
public interface ICache<K, V> {
    /**
     * Puts values to cache
     * @param key
     * @param value
     */
    void put (K key, V value);

    /**
     * Extract object from cache by key
     * @param key - element to find
     * @throws ObjectNotFoundException if key is not found
     */
    V extract (K key) throws ObjectNotFoundException;

    /**
     * Removes element from cache
     * @param key element to remove
     */
    void remove(K key);

    /**
     * Deletes all elements from cache
     */
    void clear();

    /**
     * Returns the amount of elements cached
     */
    int size();
}
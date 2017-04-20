package by.pintusov.myCache.caching.memory;

import by.pintusov.myCache.api.ICache;
import by.pintusov.myCache.api.ObjectNotFoundException;

import java.util.HashMap;
import java.util.Map;

public class MemoryCache<K, V> implements ICache<K, V> {
    private final Map<K, V> memoryMap = new HashMap<K, V>();

    public void put(K key, V value) {
        memoryMap.put(key, value);
    }

    public V extract(K key) throws ObjectNotFoundException {
        if (!memoryMap.containsKey(key)){
            throw new ObjectNotFoundException();
        }
        return memoryMap.get(key);
    }

    public void clear() {
        memoryMap.clear();
    }

    public void remove(K key) {
        memoryMap.remove(key);
    }

    public int size() {
        return memoryMap.size();
    }
}
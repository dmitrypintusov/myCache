package by.pintusov.myCache.integrator;

import by.pintusov.myCache.api.Cache;
import by.pintusov.myCache.api.ObjectNotFoundException;

import java.util.List;

public class Integrator<K, V> implements Cache<K, V> {
    private List<Cache<K, V>> caches;

    /*
    * Put parameters to first level cache
    * */
    public void put(K key, V value) {
        caches.get(0).put(key, value);
    }

    public V extract(K key) throws ObjectNotFoundException {
        V value = null;
        for (Cache <K, V> cache : caches) {
            try {
                value = cache.extract(key);
                return value;
            } catch (ObjectNotFoundException e) {
                System.out.println(e);
            }
        }
        return value;
    }

    public void remove(K key) {
        for (Cache<K, V> cache : caches) {
            cache.remove(key);
        }
    }

    public void clear() {
        for (Cache <K, V> cache : caches) {
            cache.clear();
        }
    }

    public int size() {
        int size = 0;
        for (Cache <K, V> cache : caches) {
            size = size + cache.size();
        }
        return size;
    }


    public void setCaches(List<Cache<K, V>> caches) {
        this.caches = caches;
    }

    public List<Cache<K, V>> getCaches() {
        return caches;
    }
}

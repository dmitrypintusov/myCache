package by.pintusov.myCache.integrator;

import by.pintusov.myCache.api.ICache;
import by.pintusov.myCache.api.ObjectNotFoundException;

import java.util.List;

public class Integrator<K, V> implements ICache<K, V> {
    private List<ICache<K, V>> caches;

    /*
    * Put parameters to first level cache
    * */
    public void put(K key, V value) {
        caches.get(0).put(key, value);
    }

    public V extract(K key) throws ObjectNotFoundException {
        V value = null;
        for (ICache<K, V> ICache : caches) {
            try {
                value = ICache.extract(key);
                return value;
            } catch (ObjectNotFoundException e) {
                System.out.println(e);
            }
        }
        return value;
    }

    public void remove(K key) {
        for (ICache<K, V> ICache : caches) {
            ICache.remove(key);
        }
    }

    public void clear() {
        for (ICache<K, V> ICache : caches) {
            ICache.clear();
        }
    }

    public int size() {
        int size = 0;
        for (ICache<K, V> ICache : caches) {
            size = size + ICache.size();
        }
        return size;
    }

    public void setCaches(List<ICache<K, V>> caches) {
        this.caches = caches;
    }

    public List<ICache<K, V>> getCaches() {
        return caches;
    }

    @Override
    public String toString() {
        return "Integrator{" +
                "caches=" + caches +
                '}';
    }
}

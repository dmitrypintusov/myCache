package by.pintusov.myCache.builder;

import by.pintusov.myCache.api.Cache;

/**
 * Cache with only one level of caching
 * @author pintusov
 */
public class OneLevelCache<K, V> implements LeveledCache<K, V> {
    private final Cache<K, V> cache;

    public OneLevelCache(Cache<K, V> cache) {
        this.cache = cache;
    }

    public Cache<K, V> asCache() {
        return cache;
    }

    public void recache() {
        //no needs to implement
        throw new UnsupportedOperationException();
    }
}

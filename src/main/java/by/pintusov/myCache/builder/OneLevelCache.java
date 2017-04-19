package by.pintusov.myCache.builder;

import by.pintusov.myCache.api.ICache;

/**
 * ICache with only one level of caching
 * @author pintusov
 */
public class OneLevelCache<K, V> implements ILeveledCache<K, V> {
    private final ICache<K, V> ICache;

    public OneLevelCache(ICache<K, V> ICache) {
        this.ICache = ICache;
    }

    public ICache<K, V> allCache() {
        return ICache;
    }

    public void recache() {
        //no needs to implement
    }
}

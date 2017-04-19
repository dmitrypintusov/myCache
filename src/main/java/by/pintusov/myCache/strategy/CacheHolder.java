package by.pintusov.myCache.strategy;

import by.pintusov.myCache.api.ICache;

/**
 * Strategy for creating cache
 * @author pintusov
 */
public interface CacheHolder<K, V> {

    /**
     * @return ICache to use
     */
    ICache<K, V> getCache();

    /**
     * If the cache is leveled, redistributes values between levels
     */
    void recache();

}

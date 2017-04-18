package by.pintusov.myCache.strategy;

import cache.api.Cache;

/**
 * Strategy for creating cache
 * 
 * @author pintusov
 */
public interface CacheHolder<K, V> {

    /**
     * @return Cache to use
     */
    Cache<K, V> getCache();

    /**
     * If the cache is leveled, redistributes values between levels
     */
    void recache();

}

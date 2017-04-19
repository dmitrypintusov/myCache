package by.pintusov.myCache.builder;

import by.pintusov.myCache.api.ICache;

/**
 * Leveled cache - objects which can have multiple levels of caching
 * @author pintusov
 */
public interface ILeveledCache<K, V> {

    /**
     * @return Returns all levels of cache wrapped in one {@link ICache} instance.
     */
    ICache<K, V> allCache();

    /**
     * Redistributes cached objects within levels. Strategy of redistributing depends on implementation.
     */
    void recache();

}
package by.pintusov.myCache.builder;

import by.pintusov.myCache.api.Cache;

/**
 * Leveled cache - objects which can have multiple levels of caching
 * @author pintusov
 */
public interface LeveledCache<K, V> {

    /**
     * @return Returns all levels of cache wrapped in one {@link Cache} instance.
     */
    Cache<K, V> asCache();

    /**
     * Redistributes cached objects within levels. Strategy of redistributing depends on implementation.
     */
    void recache();

}
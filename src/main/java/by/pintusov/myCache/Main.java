package by.pintusov.myCache;

import cache.api.Cache;
import cache.api.DoesNotExistException;
import cache.strategy.CacheHolder;
import cache.strategy.CacheStrategies;

public class Main {

    /**
     * @param args
     * @throws DoesNotExistException
     */
    public static void main(String[] args) throws DoesNotExistException {

        CacheStrategies<String, String> strategies = new CacheStrategies<String, String>();
        CacheHolder<String, String> cacheHolder = strategies.TWO_LEVELS_MEMORY_HARDDISK;

        Cache<String, String> cache = cacheHolder.getCache();

        cache.cache("key1", "value1");
        cache.cache("key2", "value2");
        cache.cache("key3", "value3");
        cache.cache("key4", "value4");

        cache.retrieve("key1");
        cache.retrieve("key4");

        // now values by key1 and key4 becomes the most used elements

        cacheHolder.recache();

        // now only the most used values are stored in memory,
        // the rest are stored on the hard disk

        // see tests for details

    }

}

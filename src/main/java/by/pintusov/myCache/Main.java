package by.pintusov.myCache;

import by.pintusov.myCache.api.ICache;
import by.pintusov.myCache.api.ObjectNotFoundException;
import by.pintusov.myCache.strategy.CacheHolder;
import by.pintusov.myCache.strategy.CacheStrategies;

public class Main {

    /**
     * @param args
     * @throws ObjectNotFoundException
     */
    public static void main(String[] args) throws ObjectNotFoundException {

        CacheStrategies<String, String> strategies = new CacheStrategies<String, String>();
        CacheHolder<String, String> cacheHolder = strategies.TWO_LEVELS_MEMORY_HARDDISK;

        ICache<String, String> cache = cacheHolder.getCache();
        System.out.println(cache);
        cache.put("key1", "value1");
        cache.put("key2", "value2");
        cache.put("key3", "value3");
        cache.put("key4", "value4");
        cache.put("key5", "value5");
        cache.put("key6", "value6");
        cache.put("key7", "value7");
        System.out.println(cache.size());

        System.out.println(cache);
        cache.extract("key1");
        cache.extract("key4");
        cache.extract("key4");
        cache.extract("key4");
        cache.extract("key4");
        System.out.println(cache.size());
        System.out.println(cache);
        // now values by key1 and key4 becomes the most used elements
        cacheHolder.recache();
        System.out.println(cache.size());
        System.out.println(cache);
        // now only the most used values are stored in memory,
        // the rest are stored on the hard disk

        // see tests for details

    }

}

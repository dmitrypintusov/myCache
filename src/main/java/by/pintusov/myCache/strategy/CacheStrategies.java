package by.pintusov.myCache.strategy;

import java.io.Serializable;

import cache.api.Cache;
import cache.builder.MultipleLevelCache;
import cache.builder.OneLevelCache;
import cache.caching.disk.FileSerializationCache;
import cache.caching.disk.strategy.StandartMarshallerUnmarshaller;
import cache.caching.memory.MemoryCache;

/**
 * Implementations of CacheHolder interfaces
 * 
 * @author pintusov
 */
public class CacheStrategies<K extends Serializable, V extends Serializable> {
    /*
     * May be moved in several classes
     */
    public final CacheHolder<K, V> ONE_LEVEL_MEMORY = new CacheHolder<K, V>() {
        private OneLevelCache<K, V> cacheHolder;
        {
            this.cacheHolder = new OneLevelCache<K, V>(new MemoryCache<K, V>());
        }

        @Override
        public Cache<K, V> getCache() {
            return cacheHolder.asCache();
        }

        @Override
        public void recache() {
            this.cacheHolder.recache();
        }
    };

    public final CacheHolder<K, V> ONE_LEVEL_HARDDISK = new CacheHolder<K, V>() {
        private OneLevelCache<K, V> cacheHolder;

        {
            FileSerializationCache<K, V> cache = new FileSerializationCache<K, V>();
            cache.setMarshallerUnmarshaller(new StandartMarshallerUnmarshaller<K, V>());
            this.cacheHolder = new OneLevelCache<K, V>(cache);
        }

        @Override
        public Cache<K, V> getCache() {
            return cacheHolder.asCache();
        }

        @Override
        public void recache() {
            this.cacheHolder.recache();
        }
    };

    public final CacheHolder<K, V> TWO_LEVELS_MEMORY_HARDDISK = new CacheHolder<K, V>() {
        private MultipleLevelCache<K, V> cacheHolder;

        {
            // using default constructor of MultipleLevelCache which provides
            // needed two levels caching
            this.cacheHolder = new MultipleLevelCache<K, V>();
        }

        @Override
        public Cache<K, V> getCache() {
            return cacheHolder.asCache();
        }

        @Override
        public void recache() {
            this.cacheHolder.recache();
        }
    };

}

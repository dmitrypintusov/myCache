package by.pintusov.myCache.strategy;

import by.pintusov.myCache.api.ICache;
import by.pintusov.myCache.builder.MultipleLevelCache;
import by.pintusov.myCache.builder.OneLevelCache;
import by.pintusov.myCache.caching.disk.FileSerializationCache;
import by.pintusov.myCache.caching.disk.strategy.StandartMarshallerUnmarshaller;
import by.pintusov.myCache.caching.memory.MemoryCache;

import java.io.Serializable;

/**
 * Implementations of CacheHolder interfaces
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

        public ICache<K, V> getCache() {
            return cacheHolder.allCache();
        }

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
        public ICache<K, V> getCache() {
            return cacheHolder.allCache();
        }

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

        public ICache<K, V> getCache() {
            return cacheHolder.allCache();
        }

        public void recache() {
            this.cacheHolder.recache();
        }
    };

}

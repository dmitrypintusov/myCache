package by.pintusov.myCache.builder;

import by.pintusov.myCache.api.ICache;
import by.pintusov.myCache.api.ObjectNotFoundException;
import by.pintusov.myCache.caching.CacheCounterInterceptor;
import by.pintusov.myCache.caching.disk.FileSerializationCache;
import by.pintusov.myCache.caching.disk.strategy.StandartMarshallerUnmarshaller;
import by.pintusov.myCache.caching.memory.MemoryCache;
import by.pintusov.myCache.common.CallFrequencyCounter;
import by.pintusov.myCache.integrator.Integrator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * ICache which supports a multiple levels of caching
 * @author pintusov
 */
public class MultipleLevelCache<K extends Serializable, V extends Serializable> implements ILeveledCache<K, V> {
    /**
     * Decorator for integrator which provides countering the requests to the cache.
     */
    private CacheCounterInterceptor<K, V> cacheCounterInterceptor;
    private Integrator<K, V> integrator;
    private ICache<K, V>[] caches;

    /**
     * Creates two-level caching, the first is for caching in memory, the second is for caching on the hard disk by
     * using default serialization mechanism
     */
    /*
     * Warning caused by init() method, MemoryCache and cacheLevel2 are ICache and K and V provided "typesafe" to java
     * compiler, so the warning can be suppressed.
     */
    public MultipleLevelCache() {
        // creating caches of two levels
        MemoryCache<K, V> cacheLevel1 = new MemoryCache<K, V>();
        FileSerializationCache<K, V> cacheLevel2 = new FileSerializationCache<K, V>(
                new StandartMarshallerUnmarshaller<K, V>());
        init(cacheLevel1, cacheLevel2);
    }

    /**
     * Allows to define caches to use. Caches should be added in order of their priority. The first has the highest
     * priority.
     * 
     * @param caches
     */
    public MultipleLevelCache(ICache<K, V>... caches) {
        init(caches);
    }
    /**
     * Initializes the object.
     * @param caches
     */
    private void init(ICache<K, V>... caches) {
        this.caches = caches;
        integrator = new Integrator<K, V>();
        integrator.setCaches(Arrays.asList(caches));
        cacheCounterInterceptor = new CacheCounterInterceptor<K, V>(integrator);
    }

    public ICache<K, V> allCache() {
        return cacheCounterInterceptor;
    }

    public void recache() {
        int cachesAmount = caches.length;
        CallFrequencyCounter<K> freq = cacheCounterInterceptor.getCallFrequencyCounter();
        List<List<K>> distributedKeys = keysToDistibute(freq);
        /*
         * Redistributing all values in caches. For that using distributedKeys with keys sorted by how frequently they
         * were called and the most frequent will go to the first cache, the second to the next, etc.
         */
        for (int cacheNumber = 0; cacheNumber < cachesAmount; ++cacheNumber) {
            List<K> keysToMove = distributedKeys.get(cacheNumber);
            for (K key : keysToMove) {
                // calling integrator for not affecting the counter
                try {
                    V value = integrator.extract(key);
                    integrator.remove(key);
                    // moving the pair to the {cacheNumber} cache
                    caches[cacheNumber].put(key, value);
                } catch (ObjectNotFoundException e) {
                    throw new IllegalStateException("this should not have been happened");
                }
            }
        }
    }

    /**
     * Returns a list of keys to distribute between all caches.
     * Keys are distributed based on information obtained from {@link CallFrequencyCounter} and its method
     * {@link CallFrequencyCounter#getMostFrequent()}.
     * @param freq {@link CallFrequencyCounter} instance
     * @return a list of keys to distribute between all caches.
     */
    private List<List<K>> keysToDistibute(CallFrequencyCounter<K> freq) {
        List<K> freqs = freq.getMostFrequent();
        /*
         * Distribute key between caches based on how frequently they were retrieved
         */
        int cachesAmount = caches.length;
        int keysAmount = cacheCounterInterceptor.size();
        int size = keysAmount / cachesAmount;
        int start = 0;
        List<List<K>> distributedKeys = new ArrayList<List<K>>();
        for (int i = 0; i < cachesAmount - 1; ++i) {
            distributedKeys.add(freqs.subList(start, start + size));
            start = start + size;
        }
        distributedKeys.add(freqs.subList(start, keysAmount));
        // for debugging
        if (distributedKeys.size() != cachesAmount) {
            throw new RuntimeException("Error in the algorithm! keysToDistribute() method, CacheBuilder.");
        }
        return distributedKeys;
    }
}

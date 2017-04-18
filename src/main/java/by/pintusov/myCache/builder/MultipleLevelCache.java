package by.pintusov.myCache.builder;

import by.pintusov.myCache.api.Cache;
import by.pintusov.myCache.integrator.Integrator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Cache which suppors a multiple levels of caching
 * 
 * @author pintusov
 * 
 * @param <K>
 * @param <V>
 */
public class MultipleLevelCache<K extends Serializable, V extends Serializable> implements LeveledCache<K, V> {
    /**
     * Decorator for integrator which provides countering the reqiests to the cache.
     */
    private CacheCounterInterceptor<K, V> cacheCounterInterceptor;
    private Integrator<K, V> integrator;
    private Cache<K, V>[] caches;

    /**
     * Creates two-level caching, the first is for caching in memory, the second is for caching on the hard disk by
     * using default serialization mechanism
     */
    /*
     * Warning caused by init() method, MemoryCache and cacheLevel2 are Cache and K and V provided "typesafe" to java
     * compiler, so the warning can be suppressed.
     */
    @SuppressWarnings("unchecked")
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
    public MultipleLevelCache(Cache<K, V>... caches) {
        init(caches);
    }

    /**
     * Initializes the object.
     * 
     * @param caches
     */
    private void init(Cache<K, V>... caches) {
        this.caches = caches;

        integrator = new Integrator<K, V>();
        integrator.setCaches(Arrays.asList(caches));

        cacheCounterInterceptor = new CacheCounterInterceptor<K, V>(integrator);
    }

    @Override
    public Cache<K, V> asCache() {
        return cacheCounterInterceptor;
    }

    @Override
    public void recache() {

        int cachesAmount = caches.length;

        CallFrequencyCounter<K> freq = cacheCounterInterceptor.getCallFrequencyCounter();

        List<List<K>> distibutedKeys = keysToDistibute(freq);

        /*
         * Redistibuting all values in caches. For that using distibutedKeys with keys sorted by how frequently they
         * were called and the most frequent will go to the first cache, the second to the next, etc.
         */
        for (int cacheNumber = 0; cacheNumber < cachesAmount; ++cacheNumber) {
            List<K> keysToMove = distibutedKeys.get(cacheNumber);

            for (K key : keysToMove) {
                // calling integrator for not affecting the counter
                try {
                    V value = integrator.retrieve(key);
                    integrator.remove(key);

                    // moving the pair to the {cacheNumber} cache
                    caches[cacheNumber].cache(key, value);

                } catch (DoesNotExistException e) {
                    throw new IllegalStateException("this should not have been happened");
                }
            }

        }

    }

    /**
     * Returns a list of keys to distribute between all caches.
     * 
     * Keys are distibuted based on information obtained from {@link CallFrequencyCounter} and its method
     * {@link CallFrequencyCounter#getMostFrequent()}.
     * 
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

        List<List<K>> distibutedKeys = new ArrayList<List<K>>();

        for (int i = 0; i < cachesAmount - 1; ++i) {
            distibutedKeys.add(freqs.subList(start, start + size));
            start = start + size;
        }

        distibutedKeys.add(freqs.subList(start, keysAmount));

        // for debugging
        if (distibutedKeys.size() != cachesAmount) {
            throw new RuntimeException("Error in the algorithm! keysToDistibute() method, CacheBuilder.");
        }

        return distibutedKeys;
    }
}

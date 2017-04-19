package by.pintusov.myCache.caching;


import by.pintusov.myCache.api.ICache;
import by.pintusov.myCache.api.ObjectNotFoundException;
import by.pintusov.myCache.common.CallFrequencyCounter;
import by.pintusov.myCache.common.CallFrequencyCounterImpl;

/**
 * Decorator for {@link ICache} which provides countering the requests to the cache.
 * @author pintusov
 */
public class CacheCounterInterceptor<K, V> implements ICache<K, V> {
    final private ICache<K, V> innerICache;
    private CallFrequencyCounter<K> callFrequencyCounter = new CallFrequencyCounterImpl<K>();

    public CacheCounterInterceptor(ICache<K, V> innerICache) {
        this.innerICache = innerICache;
    }

    public void put(K key, V value) {
        getCallFrequencyCounter().register(key);
        innerICache.put(key, value);
    }

    public V extract(K key) throws ObjectNotFoundException {
        getCallFrequencyCounter().call(key);
        return innerICache.extract(key);
    }

    public void clear() {
        innerICache.clear();
        getCallFrequencyCounter().reset();
    }

    public void remove(K key) {
        innerICache.remove(key);
    }

    public int size() {
        return innerICache.size();
    }

    public void setCallFrequencyCounter(CallFrequencyCounter<K> callFrequencyCounter) {
        this.callFrequencyCounter = callFrequencyCounter;
    }

    public CallFrequencyCounter<K> getCallFrequencyCounter() {
        return callFrequencyCounter;
    }

    @Override
    public String toString() {
        return "CacheCounterInterceptor{" +
                "innerICache=" + innerICache +
                ", callFrequencyCounter=" + callFrequencyCounter +
                '}';
    }
}

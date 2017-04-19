package by.pintusov.myCache.common;

import by.pintusov.myCache.api.ObjectNotFoundException;

import java.util.List;

/**
 * Counter that hold the frequency of its called elements.
 * @author pintusov
 */
public interface CallFrequencyCounter<K> {
    /**
     * Registers element in the counter. Sets the counter to zero.
     * Note: calling this for already registered element will reset the counter.
     * @param key
     */
    void register(K key);

    /**
     * Logs the call of the key. Then the number of calls can be retrieved by invoking
     * {@link CallFrequencyCounter#getFrequency(Object)}. Each key should be registered with
     * {@link CallFrequencyCounter#register(Object)} before this method can be used, otherwise
     * {@link ObjectNotFoundException} will be thrown.
     * @param key to call
     */
    void call(K key) throws ObjectNotFoundException;

    /**
     * Returns frequency of the key. Each key should be registered with {@link CallFrequencyCounter#register(Object)}
     * before this method can be used, otherwise {@link ObjectNotFoundException} will be thrown.
     * @param key
     * @return the number of times the key was called
     * @throws ObjectNotFoundException is thrown when frequency of not registered element is asked
     */
    int getFrequency(K key) throws ObjectNotFoundException;

    /**
     * Returns most used values.
     * 
     * @param amount to retrieve
     * @return the set of most used keys
     */
    List<K> getMostFrequent(int amount);

    /**
     * @return the whole list of all keys sorted by frequency of calls
     */
    List<K> getMostFrequent();

    /**
     * Resets all counters
     */
    void reset();

    /**
     * Removes element and associated counter
     * @param key to remove
     */
    void removeElement(K key);
}
package by.pintusov.myCache.common;

import by.pintusov.myCache.api.ObjectNotFoundException;

import java.util.*;
import java.util.Map.Entry;

/**
 * Counter that hold the frequency of its called elements. Implementation of {@link CallFrequencyCounter}
 * @author pintusov
 */
public class CallFrequencyCounterImpl<K> implements CallFrequencyCounter<K> {
    private Map<K, Integer> frequency = new HashMap<K, Integer>();

    public void register(K key) {
        frequency.put(key, Integer.valueOf(0));
    }

    public void call(K key) throws ObjectNotFoundException {
        if (frequency.containsKey(key)) {
            Integer frequency = this.frequency.get(key);
            frequency++;
            this.frequency.put(key, frequency);
        } else {
            throw new ObjectNotFoundException();
        }
    }

    public int getFrequency(K key) throws ObjectNotFoundException {
        if (frequency.containsKey(key)) {
            return frequency.get(key);
        } else {
            throw new ObjectNotFoundException();
        }
    }

    /*
	* Creating a list of all entries in the map. Then sort it by the integer value;
	* return only {amount} of them
	*/
    public List<K> getMostFrequent(int amount) {

        List<Entry<K, Integer>> frequencies = new ArrayList<Entry<K, Integer>>(frequency.entrySet());
        Collections.sort(frequencies, new Comparator<Entry<K, Integer>>() {
            public int compare(Entry<K, Integer> o1, Entry<K, Integer> o2) {
                return -o1.getValue().compareTo(o2.getValue());
            }
        });

        List<K> result = new ArrayList<K>();
        for (int i = 0; i < amount; ++i) {
            result.add(frequencies.get(i).getKey());
        }
        return result;
    }

    public List<K> getMostFrequent() {
        return getMostFrequent(frequency.size());
    }

    public void reset() {
        frequency.clear();
    }

    public void removeElement(K key) {
        frequency.remove(key);
    }

    @Override
    public String toString() {
        return "CallFrequencyCounterImpl{" +
                "frequency=" + frequency +
                '}';
    }
}

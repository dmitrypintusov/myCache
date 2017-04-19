package by.pintusov.myCache.caching.disk.strategy;

import java.io.File;
import java.io.Serializable;
import java.util.Map;

/**
 * Strategy for encoding-decoding serializable classes
 * @author pintusov
 */
public interface MarshallerUnmarshaller<K extends Serializable, V extends Serializable> {

    Map.Entry<K, V> convertFromXml(File file);

    void convertIntoXml(K key, V value, File file);

}
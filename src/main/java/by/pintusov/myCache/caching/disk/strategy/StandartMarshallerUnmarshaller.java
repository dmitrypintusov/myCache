package by.pintusov.myCache.caching.disk.strategy;

import java.io.*;
import java.util.Map.Entry;

/**
 * Marchaller/Unmarchaller for standard serialization
 * @author pintusov
 */
public class StandartMarshallerUnmarshaller<K extends Serializable, V extends Serializable> implements
        MarshallerUnmarshaller<K, V> {

    public void convertIntoXml(K key, V value, File file) {
        OutputStream os = null;
        ObjectOutputStream oos = null;
        try {
            os = new BufferedOutputStream(new FileOutputStream(file));
            oos = new ObjectOutputStream(os);

            oos.writeObject(key);
            oos.writeObject(value);
            oos.flush();
        } catch (IOException e) {
            throw new RuntimeException("IO exception during unmarchalling", e);
        } finally {
            close(oos);
            close(os);
        }
    }

    private static void close(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException e) {
           e.printStackTrace();
        }
    }

    public Entry<K, V> convertFromXml(File file) {
        Entry<K, V> result = null;
        InputStream is = null;
        ObjectInputStream ois = null;

        try {
            is = new BufferedInputStream(new FileInputStream(file));
            ois = new ObjectInputStream(is);

            K key = (K) ois.readObject();
            V value = (V) ois.readObject();
            result = new SerializableEntry<K, V>(key, value);
        } catch (IOException e) {
            throw new RuntimeException("IO exception during unmarchalling", e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Class not found during unmarchalling", e);
        } finally {
            close(ois);
            close(is);
        }

        return result;
    }

    /**
     * 
     * Inner class to hold entries (pair key-value)
     * @author pintusov
     */
    private static class SerializableEntry<K, V> implements Entry<K, V> {
        private final K key;
        private final V value;

        private SerializableEntry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public V setValue(V value) {
            throw new RuntimeException("Operation is not supported");
        }
    }

    @Override
    public String toString() {
        return "StandartMarshallerUnmarshaller{}";
    }
}
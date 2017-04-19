package by.pintusov.myCache.caching.disk;

import by.pintusov.myCache.api.ICache;
import by.pintusov.myCache.api.ObjectNotFoundException;
import by.pintusov.myCache.caching.disk.strategy.MarshallerUnmarshaller;

import java.io.File;
import java.io.Serializable;
import java.util.Map.Entry;
import java.util.UUID;

public class FileSerializationCache<K extends Serializable, V extends Serializable> implements ICache<K, V> {
    private String DIR = "cache/";
    private int elements;
    private MarshallerUnmarshaller<K, V> marshallerUnmarshaller;

    public FileSerializationCache(MarshallerUnmarshaller<K, V> marshallerUnmarshaller) {
        this.marshallerUnmarshaller = marshallerUnmarshaller;
    }

    public FileSerializationCache() {

    }

    /**
     * Looks up the value in the given directory
     * @param dir
     * @param key
     * @return File with value or null if none found
     */
    private File lookThroughDir(File dir, K key) {
        File[] files = dir.listFiles();
        for (File f : files) {
            Entry<K, V> entry = marshallerUnmarshaller.convertFromXml(f);
            if (entry.getKey().equals(key)) {
                return f;
            }
        }
        return null;
    }

    public void put(K key, V value) {
        String keyHashCode = String.valueOf(key.hashCode());
        String dirName = getDIR() + keyHashCode + "/";
        File dir = new File (dirName);
        File inUse = null;
        if (!dir.exists()) {
            dir.mkdir();
            inUse = new File (dirName + randomString());
            elements++;
        } else {
            inUse = lookThroughDir(dir, key);
            if (inUse == null) {
                inUse = new File(dirName + randomString());
                elements++;
            }
        }
        marshallerUnmarshaller.convertIntoXml(key, value, inUse);
    }

    public V extract(K key) throws ObjectNotFoundException {
        String keyHashCode = String.valueOf(key.hashCode());
        String dirName = getDIR() + keyHashCode + "/";
        File dir = new File (dirName);
        if (!dir.exists()) {
            throw new ObjectNotFoundException();
        }
        File inUse = lookThroughDir(dir, key);
        if (inUse == null) {
            throw new ObjectNotFoundException();
        }
        V result = marshallerUnmarshaller.convertFromXml(inUse).getValue();
        return result;
    }

    public void remove(K key) {
        String keyHashCode = String.valueOf(key.hashCode());
        String dirName = getDIR() + keyHashCode + "/";
        File dir = new File(dirName);
        if (dir.exists()) {
            File toUse = lookThroughDir(dir, key);
            if (toUse != null) {
                toUse.delete();
                elements--;
            }
        }
    }

    public void clear() {
        recurseDelete(new File(getDIR()));
    }

    public int size() {
        return elements;
    }

    public void setMarshallerUnmarshaller(MarshallerUnmarshaller<K, V> marshallerUnmarshaller) {
        this.marshallerUnmarshaller = marshallerUnmarshaller;
    }

    public MarshallerUnmarshaller<K, V> getMarshallerUnmarshaller() {
        return marshallerUnmarshaller;
    }

    public void setDIR(String dIR) {
        DIR = dIR;
    }

    public String getDIR() {
        return DIR;
    }

    private String randomString() {
        return UUID.randomUUID().toString();
    }

    /**
     * Recursive deletes all the files
     * @param file
     */
    private void recurseDelete(File file) {
        for (File inner : file.listFiles()) {
            if (inner.isDirectory()) {
                recurseDelete(inner);
            } else {
                boolean deleted = inner.delete();
                assert deleted;
            }
        }
    }

    @Override
    public String toString() {
        return "FileSerializationCache{" +
                "DIR='" + DIR + '\'' +
                ", elements=" + elements +
                ", marshallerUnmarshaller=" + marshallerUnmarshaller +
                '}';
    }
}
package ro.trenulmeu.mobile.helpers;

import java.util.HashMap;
import java.util.Map;

/**
 * Cache of Arbitrary Data, used to not call the API multiple times.
 */
public class DataCache {

    private Map<String, Object> cache = new HashMap<>();

    public <T> T get(String key, Class<T> clazz) {
        Object o = cache.get(key);
        try {
            return clazz.cast(o);
        } catch (ClassCastException e) {
            return null;
        }
    }

    public <T> T get(String key, Class<T> clazz, T defaultValue) {
        if (defaultValue == null) {
            return get(key, clazz);
        } else {
            T obj = get(key, clazz);
            return obj == null ? defaultValue : obj;
        }
    }

    public <T> T getAndDelete(String key, Class<T> clazz) {
        T o = get(key, clazz);
        delete(key);
        return o;
    }

    public <T> T getAndDelete(String key, Class<T> clazz, T defaultValue) {
        if (defaultValue == null) {
            return getAndDelete(key, clazz);
        } else {
            T obj = get(key, clazz);
            delete(key);
            return obj == null ? defaultValue : obj;
        }
    }

    public void set(String key, Object value) {
        cache.put(key, value);
    }

    public void delete(String key) {
        cache.remove(key);
    }

    public boolean hasKey(String key) {
        return cache.containsKey(key);
    }

}

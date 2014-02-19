package net.cokkee.comker.model;

import java.util.HashMap;

/**
 *
 * @author drupalex
 */
public class ComkerHashMap<K, V> extends HashMap<K, V> {
    public ComkerHashMap(Object ... params) {
        if (params.length % 2 != 0) {
            throw new IllegalArgumentException("number of arguments should be even.");
        }
        boolean single = true;
        K key = null;
        V value = null;
        for(Object argument: params) {
            if (single) {
                key = (K) argument;
            } else {
                value = (V) argument;
                this.put(key, value);
            }
            single = !single;
        }
    }
}

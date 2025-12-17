package com.cec.examine.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MapCounter<T> {
    private Map<T,Long> counter = new HashMap<T, Long>();

    public void incr(T key, Long delta) {
        Long value = counter.get(key);
        if(value == null) {
            counter.put(key, delta);
        } else {
            counter.put(key, delta + value);
        }
    }

    public void incr(T key) {
        incr(key,1L);
    }

    public Long get(T key) {
        Long n = counter.get(key);
        return n == null ? 0: n;
    }

    public Long put(T key, Long value) {
        return counter.put(key,value);
    }

    public Set<T> keySet() {
        return counter.keySet();
    }

    public T getMaxKey() {
        Long max = Long.MIN_VALUE;
        T maxKey = null;
        for(T key: counter.keySet()) {
            Long value = counter.get(key);
            if(max < value) {
                max = value;
                maxKey = key;
            }
        }
        return maxKey;
    }

    public T getMinKey() {
        Long min = Long.MAX_VALUE;
        T minKey = null;
        for(T key: counter.keySet()) {
            Long value = counter.get(key);
            if(min > value) {
                min = value;
                minKey = key;
            }
        }
        return minKey;
    }

    public String toString() {
        return counter.toString();
    }
}

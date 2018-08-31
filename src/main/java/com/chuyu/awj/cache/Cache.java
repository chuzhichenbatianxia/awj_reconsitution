package com.chuyu.awj.cache;

/**
 * 缓存
 */
public interface Cache {
    void put(Object key, Object value);
    <T> T get(Object key);
    void remove(Object key);
    void clear();
}

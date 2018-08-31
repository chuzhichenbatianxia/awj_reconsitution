package com.chuyu.awj.cache;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

/**
 * @author chuyu
 * @date 2018.8.31
 */
public class CacheImpl implements Cache{

    private Ehcache ehcache;

    public CacheImpl(Ehcache ehcache) {
        this.ehcache = ehcache;
    }

    @Override
    public void put(Object key, Object value) {
        ehcache.put(new Element(key,value));
    }

    @Override
    public <T> T get(Object key) {
        Element element = ehcache.get(key);
        if (element!=null){
            return (T)element.getObjectValue();
        }
        return null;
    }

    @Override
    public void remove(Object key) {
        if (ehcache.isKeyInCache(key)){
            ehcache.remove(key);
        }
    }

    @Override
    public void clear() {
        ehcache.removeAll(true);
    }
}

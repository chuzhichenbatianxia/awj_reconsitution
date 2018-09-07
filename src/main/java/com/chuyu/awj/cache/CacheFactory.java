package com.chuyu.awj.cache;

import net.sf.ehcache.Ehcache;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ContextLoader;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chuyu
 * @date 2018.8.31
 */
@Component
public class CacheFactory {

    private Map<String,Cache> map;

    public CacheFactory() {
        map = new HashMap<String, Cache>();
    }

    public Cache getCache(String cacheId){
        synchronized (this){
            if (!map.containsKey(cacheId)){
                Ehcache ehcache = ContextLoader.getCurrentWebApplicationContext().getBean(cacheId,Ehcache.class);
                if (ehcache==null){
                    return null;
                }
                map.put(cacheId,new CacheImpl(ehcache));
            }
        }
        return map.get(cacheId);
    }

}

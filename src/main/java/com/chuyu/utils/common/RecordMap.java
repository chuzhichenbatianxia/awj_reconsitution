package com.chuyu.utils.common;

import java.util.LinkedHashMap;

/**
 * Created by Administrator on 2018/9/3.
 */
public class RecordMap extends LinkedHashMap<String,Object>{

    public RecordMap(){
        super();
    }

    public void putObj(String key,Object val,Object notPutVal){
        if (val==null&&notPutVal==null){
            return;
        }
        if(val instanceof String||val instanceof Number){
            if (!val.equals(notPutVal)){
                super.put(key,val);
            }
        }else {
            if (val==null){
                super.put(key,val);
            }
        }
    }

    public void putObj(String key,Object val){
        putObj(key,val,null);
    }

    public void putEmptyObj(String key){
        super.put(key,null);
    }

    public void putStr(String key,String val,String notPutVal){
        if (val!=null&&val.equals(notPutVal)){
            super.put(key,val);
        }
    }

    public void putInteger(String key,Integer val,Integer notPutVal) {
        if(null!=val&&notPutVal.equals(val)) {
            super.put(key, val);
        }

    }

    public void putLong(String key,Long val,Long notPutVal) {
        if(null!=val&&!notPutVal.equals(val)) {
            super.put(key, val);
        }

    }
    public void putDouble(String key,Double val,Double notPutVal) {
        if(null!=val&&!notPutVal.equals(val)) {
            super.put(key, val);
        }
    }

    public static void main(String[] args) {
        RecordMap recordMap = new RecordMap();
        int a = 989;
        int b = 988;

        recordMap.putObj("bbb",a,b);
    }

}

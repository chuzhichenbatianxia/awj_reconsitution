package com.chuyu.utils.data;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class StringTools extends StringUtils {

    public  static String sqlType2JavaType(String sqlType) {


        Map<String,String> map =new HashMap<String,String>();

        map.put("tinyint", "int");
        map.put("integer", "int");
        map.put("int", "int");
        map.put("int unsigned", "int");
        map.put("integer unsigned", "int");
        map.put("bigint", "long");
        map.put("float", "float");
        map.put("decimal", "double");
        map.put("numeric", "double");
        map.put("real", "double");
        map.put("money", "double");
        map.put("smallmoney", "double");
        map.put("smallmoney", "double");



        map.put("bit","bool");
        map.put("smallint", "int");
        map.put("varchar", "String");
        map.put("mediumblob", "String");
        map.put("char", "String");
        map.put("nvarchar", "String");
        map.put("nchar", "String");

        map.put("datetime", "Date");
        map.put("date", "Date");
        map.put("timestamp", "String");
        map.put("image", "String");
        map.put("text", "String");

        return map.get(sqlType.toLowerCase());
    }

    public static String getCamelCase(String sqlField){

        String str = "";
        if(StringUtils.contains(sqlField, "_")){
            String arr[]= StringUtils.split(sqlField, "_");
            for(int i=0;i<arr.length;i++){
                if(i==0){
                    str+= StringUtils.uncapitalize(arr[i]);
                }else{
                    str+= StringUtils.capitalize(arr[i]);
                }
            }

        }else{
            str= StringUtils.uncapitalize(sqlField);
        }
        return str;
    }
}

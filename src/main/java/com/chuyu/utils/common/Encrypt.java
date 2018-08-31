package com.chuyu.utils.common;

import java.security.MessageDigest;

/**
 * Created by Administrator on 2017-6-5.
 */
public class Encrypt
{
    /**
     * md5加密
     *
     * @param data 需要加密的字符串
     * @return
     */
    public static String MD5(String data)
    {
        return baseEncrypt(data, "MD5");
    }

    /**
     * sha256加密
     *
     * @param data 需要加密的字符串
     * @return
     */
    public static String SHA256(String data)
    {
        return baseEncrypt(data, "SHA-256");
    }

    /**
     * sha1加密
     *
     * @param data 需要加密的字符串
     * @return
     */
    public static String SHA1(String data)
    {
        return baseEncrypt(data, "SHA-1");
    }

    private static String baseEncrypt(String data, String encryptName)
    {
        if(data == null){
            return null;
        }
        String strDes = null;
        if(encryptName == null && encryptName.trim().length() == 0){
            encryptName = "MD5";
        }
        try{
            byte[] bt = data.getBytes();
            MessageDigest sha256 = MessageDigest.getInstance(encryptName);
            byte[] barr = sha256.digest(data.getBytes());  //將 byte 陣列加密
            StringBuffer sb = new StringBuffer();  //將 byte 陣列轉成 16 進制
            for(int i = 0; i < barr.length; i++){
                sb.append(byte2Hex(barr[i]));
            }
            strDes = sb.toString();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return strDes;
    }


    public static String byte2Hex(byte b)
    {
        String[] h = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};
        int i = b;
        if(i < 0){
            i += 256;
        }
        return h[i / 16] + h[i % 16];
    }
}

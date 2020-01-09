package shr.cache.utils;


import java.io.UnsupportedEncodingException;

/**
 * @Author : 佘浩然
 * 描述
 * --------数据转换工具类
 * @Package : shr.cache.utils
 * @Create : 2017/9/29  18:16
 */
public class DataConvert {

    public static String byteConvertString(byte[] data){
        String d = "";
        try {
            d = new String(data,"UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();

        }
        return d;

    }

}

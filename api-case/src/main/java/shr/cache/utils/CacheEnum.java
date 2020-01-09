package shr.cache.utils;

/**
 * @Author : 佘浩然
 * 描述
 * --------缓存的常量枚举
 * @Package : shr.cache.utils
 * @Create : 2017/9/25  11:50
 */
public enum  CacheEnum {
    MONGODB("MONGODB"),  //momgodb的标识
    REDIS("REDIS");      //redis的标识

    String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    private CacheEnum(String value){
        this.value = value;
    }
}

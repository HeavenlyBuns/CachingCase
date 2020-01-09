package shr.cache.utils;

import com.sun.org.apache.regexp.internal.RE;
import shr.cache.mongo.pojo.MongoProperties;
import shr.cache.mongo.service.MongoDbService;
import shr.cache.mongo.service.MongoDbServiceManager;
import shr.cache.redis.common.RedisStorageService;
import shr.cache.redis.common.RedisStorageServiceImpl;

import java.util.*;

/**
 * @Author : 佘浩然
 * 描述
 * -------- 缓存的管理器
 * @Package : shr.cache.utils
 * @Create : 2017/9/25  11:50
 */
public class CacheManager {
    /* 缓存管理器存储 */
    private static Map<CacheEnum, MongoDbService> cacheMongo = new HashMap<>();

    private static Map<CacheEnum, RedisStorageService> cacheRedis = new HashMap<>();

    private static List<MongoProperties> list = new ArrayList<>();

    public static void initMongoDb(MongoProperties mongoProperties) {
        synchronized (mongoProperties) {
            if (null != mongoProperties) {
                Iterator<MongoProperties> iterator = list.iterator();
                MongoDbServiceManager mongoDbServiceManager;
                do {

                    if(!iterator.hasNext()){
                        break;
                    }
                    MongoProperties next = iterator.next();
                    if(next.getDbBase().equals(mongoProperties.getDbBase())){
                        list.remove(next);

                    }else {
                        list.add(next);

                    }
                } while (iterator.hasNext());

                if(null != cacheMongo.get(CacheEnum.MONGODB)){
                    return;
                }

                mongoDbServiceManager = MongoDbServiceManager.getNewIntance();
                mongoDbServiceManager.startp(mongoProperties);
                cacheMongo.put(CacheEnum.MONGODB, mongoDbServiceManager);

            }
        }

    }

    public static RedisStorageService initRedisCluster(String listUrl, String pwd){
        synchronized (cacheMongo){
            RedisStorageService redis = cacheRedis.get(CacheEnum.REDIS);
            if(null == redis){
                RedisStorageService storageService = new RedisStorageServiceImpl();
                storageService.getRedisClusterManager().startup(listUrl,pwd);
                cacheRedis.put(CacheEnum.REDIS,storageService);

                return storageService;
            }
            return redis;

        }

    }


    /**
     *  获取redis配置
     * @param cacheEnum
     * @return
     */
    public static RedisStorageService getRedisService(CacheEnum cacheEnum){

        RedisStorageService storageService = cacheRedis.get(CacheEnum.REDIS);
        return storageService;

    }

    /**
     *  获取mongo的service
     * @param mongoEnum
     * @return
     */
    public static MongoDbService getMongoDb(CacheEnum mongoEnum){
        synchronized (cacheMongo){
            MongoDbService service = cacheMongo.get(mongoEnum);
            if(null != service){
                return service;

            }
        }
        return null;

    }

}

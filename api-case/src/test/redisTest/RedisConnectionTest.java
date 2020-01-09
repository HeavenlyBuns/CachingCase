package redisTest;

import org.junit.Test;
import redis.clients.jedis.*;
import shr.cache.redis.common.RedisStorageService;
import shr.cache.redis.common.RedisStorageServiceImpl;
import shr.cache.redis.manager.RedisClusterManager;
import shr.cache.utils.CacheEnum;
import shr.cache.utils.CacheManager;
import shr.cache.utils.DataConvert;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * @Author : 佘浩然
 * 描述
 * --------redis测试
 * @Package : redis
 * @Create : 2017/9/29  14:36
 */
public class RedisConnectionTest {

    @Test
    public void testJedis(){

        Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
//Jedis Cluster will attempt to discover cluster nodes automatically
        jedisClusterNodes.add(new HostAndPort("192.168.19.3", 7001));
        JedisCluster jc = new JedisCluster(jedisClusterNodes);
       // jc.set("foo", "bar");
        String value = jc.get("foo");
        System.out.println(value);
    }

    @Test
    public void testBinaryJedis() throws UnsupportedEncodingException {
        Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
        jedisClusterNodes.add(new HostAndPort("192.168.19.3", 7000));
        jedisClusterNodes.add(new HostAndPort("192.168.19.3", 7001));
        jedisClusterNodes.add(new HostAndPort("192.168.19.3", 7002));
        jedisClusterNodes.add(new HostAndPort("192.168.19.3", 7003));
        jedisClusterNodes.add(new HostAndPort("192.168.19.3", 7004));
        jedisClusterNodes.add(new HostAndPort("192.168.19.3", 7005));
        BinaryJedisCluster jedisCluster = new BinaryJedisCluster(jedisClusterNodes);
        byte[] foos = jedisCluster.get("foo".getBytes());
        String s = new String(foos,new String("UTF-8"));

        System.out.println(""+s);

    }

    @Test
    public void testSharedJedisPool(){
        JedisPoolConfig  poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(1024);
        poolConfig.setMaxIdle(200);
        poolConfig.setMaxWaitMillis(20000);
        //poolConfig.setTestOnBorrow(false);
        //poolConfig.setTestWhileIdle(false);
        List<JedisShardInfo> jedisClusterNodes = new ArrayList<JedisShardInfo>();
        JedisShardInfo jedisShardInfo = new JedisShardInfo("192.168.19.3", 6379);
        jedisShardInfo.setPassword("root");
        jedisClusterNodes.add(jedisShardInfo);
        /*jedisClusterNodes.add(new JedisShardInfo("192.168.19.3", 7001));
        jedisClusterNodes.add(new JedisShardInfo("192.168.19.3", 7002));
        jedisClusterNodes.add(new JedisShardInfo("192.168.19.3", 7003));
        jedisClusterNodes.add(new JedisShardInfo("192.168.19.3", 7004));
        jedisClusterNodes.add(new JedisShardInfo("192.168.19.3", 7005));*/

        ShardedJedisPool pool = new ShardedJedisPool(poolConfig,jedisClusterNodes);
        ShardedJedis resource = pool.getResource();
        String foo = resource.get("foo");

        System.out.println(foo);
    }
    @Test
    public void testBinaryJedisTest(){
        Properties properties= new Properties();
        try {
            properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("configure.properties"));
            String url = properties.getProperty("redis.cluster.url");
            RedisClusterManager.getNewIntance().startup(url,"root");
            BinaryJedisCluster testJedis = RedisClusterManager.getNewIntance().getTestJedis();
            RedisClusterManager.getNewIntance().test(testJedis,"foo");
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    @Test
    public void testByte(){
        Properties properties= new Properties();
        try {
            RedisStorageServiceImpl storage = new RedisStorageServiceImpl();
            properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("configure.properties"));
            String url = properties.getProperty("redis.cluster.url");
            storage.getRedisClusterManager().startup(url,"root");
            BinaryJedisCluster testJedis =storage.getRedisClusterManager().getTestJedis();
            //storage.set(testJedis,"test","value");
            byte[] tests = storage.get(testJedis, "test");
            String s = DataConvert.byteConvertString(tests);
            System.out.println(s);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    @Test
    public void testRedisManager(){
        Properties properties= new Properties();
        try {
            properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("configure.properties"));
            String url = properties.getProperty("redis.cluster.url");

            CacheManager.initRedisCluster(url,"root");
            RedisStorageService redisService = CacheManager.getRedisService(CacheEnum.REDIS);
            BinaryJedisCluster testJedis = redisService.getRedisClusterManager().getTestJedis();

            byte[] tests = redisService.get(testJedis, "test");
            String s = DataConvert.byteConvertString(tests);
            System.out.println(s);


        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Test
    public void testItera(){
        List list = new ArrayList();
        list.add(1);
        list.add(2);
        Iterator iterator = list.iterator();
        while(iterator.hasNext()){
            Object next = iterator.next();
            System.out.println(next);

        }


    }

    public static void change(String str){
        str = "1234";
        //System.out.println(str);

    }
    @Test
    public void test1(){
        String string = new String("kk");
        change(string);
        System.out.println(string);

    }


    public void test2(){
        Object obj = null;

    }

}

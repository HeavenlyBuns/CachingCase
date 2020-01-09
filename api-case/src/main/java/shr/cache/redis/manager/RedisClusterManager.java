package shr.cache.redis.manager;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.BinaryJedisCluster;
import redis.clients.jedis.HostAndPort;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author : 佘浩然
 * 描述
 * --------redis操作管理
 * @Package : shr.cache.redis.manager
 * @Create : 2017/9/29  14:42
 */
public class RedisClusterManager {

    /* 最大总数 */
    private static final int MAX_TOAL = 1024;

    /* 最大空闲 */
    private static final int MAX_IDLE = 200;

    /* 最大等待时间 */
    private static final int MAX_WAIT = 10000;

    /* 最大连接时间 */
    private static final int TIME_OUT = 5000;

    /* 最大连接数(重定向) */
    private static final int MAX_REDIRECTIONS = 8;

    /* 端口和ip地址set */
    private Set<HostAndPort> hostAndPortSet;

    /* redis的测试连接 */
    private BinaryJedisCluster testJedis;

    /* 单例 */
    private static RedisClusterManager instance = new RedisClusterManager();

    public static RedisClusterManager getNewIntance() {
        return instance;

    }

    private RedisClusterManager() {
    }

    /**
     *  redis集群配置初始化
     * @param listHostAndPort host port参数列表
     * @param pwd             密码
     * @return
     */
    public boolean startup(String listHostAndPort, String pwd) {
        if ("".equals(listHostAndPort) || null == listHostAndPort) {
            return false;

        }
        String[] hostList = listHostAndPort.split(",");
        hostAndPortSet = new HashSet<>();
        for (String host : hostList) {
            String[] hostPort = host.split(":");
            hostAndPortSet.add(new HostAndPort(hostPort[0], Integer.parseInt(hostPort[1])));

        }
        /**
         * 配置连接池信息
         */
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxTotal(MAX_TOAL);
        poolConfig.setMaxIdle(MAX_IDLE);
        poolConfig.setMaxWaitMillis(MAX_WAIT);
        poolConfig.setBlockWhenExhausted(false);
        poolConfig.setTestOnBorrow(false);
        if (!"".equals(pwd) && null != pwd) {
            /* 初始化 */
            testJedis = new BinaryJedisCluster(hostAndPortSet, TIME_OUT, TIME_OUT, MAX_REDIRECTIONS, pwd, poolConfig);

        } else {
            testJedis = new BinaryJedisCluster(hostAndPortSet, TIME_OUT, TIME_OUT, MAX_REDIRECTIONS, poolConfig);

        }

        return true;

    }




    public void test(BinaryJedisCluster jedis, String key) throws UnsupportedEncodingException {
        byte[] bytes = jedis.get(key.getBytes());
        String s = null != bytes ? new String(bytes,new String("UTF-8")) : "";
        System.out.println(s);

    }


    public BinaryJedisCluster getTestJedis() {
        return testJedis;

    }

    public void stop(){
        if (null != testJedis){
            try {
                testJedis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
}

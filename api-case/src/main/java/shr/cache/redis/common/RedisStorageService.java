package shr.cache.redis.common;

import redis.clients.jedis.BinaryJedisCluster;
import shr.cache.redis.manager.RedisClusterManager;

import java.util.List;
import java.util.Map;

public interface RedisStorageService {
    public RedisClusterManager getRedisClusterManager();

    void set(BinaryJedisCluster jedisCluster, String key, String value);

    byte[] get(BinaryJedisCluster jedisCluster, String key);

    Long incr(BinaryJedisCluster jedisCluster, String key);

    Long decr(BinaryJedisCluster jedisCluster, String key);

    Long incrby(BinaryJedisCluster jedisCluster, String key, Long increment);

    Long decrby(BinaryJedisCluster jedisCluster, String key, Long decrement);

    Long append(BinaryJedisCluster jedisCluster, String key, String value);

    void lpush(BinaryJedisCluster jedisCluster, String key, String value, List<? extends Object> data);

    void rpush(BinaryJedisCluster jedisCluster, String key, String value, List<? extends Object> listData);

    List<String> lrange(BinaryJedisCluster jedisCluster, String key, Long start, Long end);

    void lpushx(BinaryJedisCluster jedisCluster, String key, String value);

    String lpop(BinaryJedisCluster jedisCluster, String key);

    String rpop(BinaryJedisCluster jedisCluster, String key);

    void rpoplpush(BinaryJedisCluster jedisCluster, String resource, String destination);

    Long llen(BinaryJedisCluster jedisCluster, String key);

    void lset(BinaryJedisCluster jedisCluster, String key, Long index, String value);

    void lrem(BinaryJedisCluster jedisCluster, String key, Long count, String value);

    void linsert(BinaryJedisCluster jedisCluster, String key, byte status, String specify, String value);

    void sadd(BinaryJedisCluster jedisCluster, String key, String value);

    List<String> smembers(BinaryJedisCluster jedisCluster, String key);

    Long scard(BinaryJedisCluster jedisCluster, String key);

    Boolean sismember(BinaryJedisCluster jedisCluster, String key, String specify);

    void srem(BinaryJedisCluster jedisCluster, String key, String specify);

    String srandmember(BinaryJedisCluster jedisCluster, String key);

    List<String> sdiff(BinaryJedisCluster jedisCluster, String key1, String key2);

    Long sdiffstore(BinaryJedisCluster jedisCluster, String destination, String key1, String key2);

    List<String> sinter(BinaryJedisCluster jedisCluster, String key1, String key2);

    Long sinterstore(BinaryJedisCluster jedisCluster, String destination, String key1, String key2);

    List<String> sunion(BinaryJedisCluster jedisCluster, String key1, String key2);

    long sunionstore(BinaryJedisCluster jedisCluster, String destination, String key1, String key2);

    void zadd(BinaryJedisCluster jedisCluster, String key, Double score, String member);

    Long zcard(BinaryJedisCluster jedisCluster, String key);

    Long zcount(BinaryJedisCluster jedisCluster, String key, Double min, Double max);

    void zincrby(BinaryJedisCluster jedisCluster, String key, Double score, String member);

    List<String> zrange(BinaryJedisCluster jedisCluster, String key, Long start, Long end);

    List<String> zrangebyscore(BinaryJedisCluster jedisCluster, String key, Double min, Double max, Integer offset, Integer count);

    Long zrank(BinaryJedisCluster jedisCluster, String key, String member);

    void zrem(BinaryJedisCluster jedisCluster, String key, String member);

    Double zscore(BinaryJedisCluster jedisCluster, String key, String member);

    Long hset(BinaryJedisCluster jedisCluster, String key, String field, String value);

    Map<String, String> hgetall(BinaryJedisCluster jedisCluster, String key);

    String hget(BinaryJedisCluster jedisCluster, String key, String field);

    void hmset(BinaryJedisCluster jedisCluster, String key, Map map);

    List<String> hmget(BinaryJedisCluster jedisCluster, String key, String field);

    Boolean hexists(BinaryJedisCluster jedisCluster, String key, String field);

    Long hlen(BinaryJedisCluster jedisCluster, String key);

    Long hincrby(BinaryJedisCluster jedisCluster, String key, String field, Long increment);
}

package shr.cache.redis.common;

import redis.clients.jedis.BinaryClient;
import redis.clients.jedis.BinaryJedisCluster;
import shr.cache.redis.manager.RedisClusterManager;
import shr.cache.utils.DataConvert;

import java.nio.charset.Charset;
import java.util.*;

/**
 * @Author : 佘浩然
 * 描述
 * --------redis通用接口
 * @Package : shr.cache.redis.common
 * @Create : 2017/9/29  16:51
 */
public class RedisStorageServiceImpl implements RedisStorageService {

    private RedisClusterManager redisClusterManager;

    public RedisStorageServiceImpl() {
        redisClusterManager = RedisClusterManager.getNewIntance();

    }

    public RedisClusterManager getRedisClusterManager() {
        return redisClusterManager;
    }

    public void setRedisClusterManager(RedisClusterManager redisClusterManager) {
        this.redisClusterManager = redisClusterManager;
    }

    /*********************************************  存储字符串string ***************************************************/
    /**
     * set操作
     *
     * @param jedisCluster
     * @param key          键
     * @param value        值
     */
    @Override
    public void set(BinaryJedisCluster jedisCluster, String key, String value) {
        if (verifyString(key) && verifyString(value)) {
            byte[] keyBytes = key.getBytes(Charset.forName("UTF-8"));
            byte[] valueBytes = value.getBytes(Charset.forName("UTF-8"));
            jedisCluster.set(keyBytes, valueBytes);

        }

    }

    /**
     * 根据key获取
     *
     * @param jedisCluster
     * @param key
     * @return
     */
    @Override
    public byte[] get(BinaryJedisCluster jedisCluster, String key) {
        if (verifyString(key)) {
            byte[] keyBytes = key.getBytes(Charset.forName("UTF-8"));
            byte[] bytes = jedisCluster.get(keyBytes);
            return bytes;

        }
        return null;
    }

    /**
     * 将指定的key的value原子性的递增1.如果该key不存在，其初始值	为0，在incr之后其值为1
     *
     * @param jedisCluster
     * @param key
     */
    @Override
    public Long incr(BinaryJedisCluster jedisCluster, String key) {
        if (verifyString(key)) {
            byte[] keyBytes = key.getBytes(Charset.forName("UTF-8"));
            Long incr = jedisCluster.incr(keyBytes);
            return incr;

        }
        return 0L;
    }

    /**
     * 将指定的key的value原子性的递减1.如果该key不存在，其初始值	为0，在incr之后其值为-1
     *
     * @param jedisCluster
     * @param key
     * @return
     */
    @Override
    public Long decr(BinaryJedisCluster jedisCluster, String key) {
        if (verifyString(key)) {
            byte[] keyBytes = key.getBytes(Charset.forName("UTF-8"));
            Long incr = jedisCluster.decr(keyBytes);
            return incr;

        }
        return 0L;

    }

    /**
     * 将指定的key的value原子性增加increment，如果该	key不存在，初始值为0，在incrby之后，该值为increment
     *
     * @param jedisCluster
     * @param key
     * @param increment    增量
     * @return
     */
    @Override
    public Long incrby(BinaryJedisCluster jedisCluster, String key, Long increment) {
        if (verifyString(key)) {
            byte[] keyBytes = key.getBytes(Charset.forName("UTF-8"));
            Long incr = jedisCluster.incrBy(keyBytes, increment);
            return incr;

        }
        return 0L;

    }

    /**
     * 将指定的key的value原子性减少decrement，如果	该key不存在，初始值为0，在decrby之后，该值为decrement
     *
     * @param jedisCluster
     * @param key
     * @param decrement    减量
     * @return
     */
    @Override
    public Long decrby(BinaryJedisCluster jedisCluster, String key, Long decrement) {
        if (verifyString(key)) {
            byte[] keyBytes = key.getBytes(Charset.forName("UTF-8"));
            Long incr = jedisCluster.decrBy(keyBytes, decrement);
            return incr;

        }
        return 0L;

    }

    @Override
    public Long append(BinaryJedisCluster jedisCluster, String key, String value) {
        if (verifyString(key) && verifyString(value)) {
            byte[] keyBytes = key.getBytes(Charset.forName("UTF-8"));
            byte[] valueBytes = value.getBytes(Charset.forName("UTF-8"));
            Long append = jedisCluster.append(keyBytes, valueBytes);
            return append;

        }
        return -1L;
    }

    /*********************************************  存储 lists 类型****************************************************/

    /**
     * 在指定的key所关联的list的头部插入所有的	values，如果该key不存在，该命令在插入的之前创建一个与该key关联的空链	表
     * ，之后再向该链表的头部插入数据。插入成功，返回元素的个数。
     *
     * @param jedisCluster
     * @param key          传value，就不传list
     * @param data         传list，就不传value
     */
    @Override
    public void lpush(BinaryJedisCluster jedisCluster, String key, String value, List<? extends Object> data) {
        if (verifyString(key, data)) {
            byte[] keyByte = convert(key);
            data.stream().forEach(var -> {
                byte[] convert = convert((String) var);
                jedisCluster.lpush(keyByte, convert);

            });

        }
        if (verifyString(key) && verifyString(value)) {
            byte[] keyByte = convert(key);
            byte[] valueByte = convert(value);
            Long lpush = jedisCluster.lpush(keyByte, valueByte);

        }

    }

    /**
     * 在该list的尾部添加元素
     *
     * @param jedisCluster
     * @param key
     * @param value
     * @param listData     传list，就不传value
     */
    @Override
    public void rpush(BinaryJedisCluster jedisCluster, String key, String value, List<? extends Object> listData) {
        if (verifyString(key, listData)) {
            byte[] keyByte = convert(key);
            listData.stream().forEach(var -> {
                byte[] convert = convert((String) var);
                jedisCluster.rpush(keyByte, convert);

            });

        }
        if (verifyString(key) && verifyString(value)) {
            byte[] keyByte = convert(key);
            byte[] valueByte = convert(value);
            Long lpush = jedisCluster.rpush(keyByte, valueByte);

        }


    }

    /**
     * @param jedisCluster
     * @param key
     * @param start        开始处 ，为正数这是从左到右，例如   1,2,3   位1 就是从2 开始往右截取
     * @param end          结束，为-1，从尾部开始，递增  例  -2，即使从尾部第二位开始  从右往左截取
     *                     正数为头部截取索引，负数为尾部截取索引
     */
    @Override
    public List<String> lrange(BinaryJedisCluster jedisCluster, String key, Long start, Long end) {
        if (verifyString(key)) {
            byte[] bytes = convert(key);
            List<byte[]> lrange = jedisCluster.lrange(bytes, start, end);
            List<String> listData = new ArrayList<>();
            if (lrange.size() != 0 && null != lrange) {
                lrange.stream().forEach(d -> {
                    String str = DataConvert.byteConvertString(d);
                    listData.add(str);

                });
            }
            return listData;

        }
        return null;
    }

    /**
     * 仅当参数中指定的key存在时（如果与key管理的list中没	有值时，则该key是不存在的）在指定的key所关联的list的头部插入value
     *
     * @param jedisCluster
     * @param key
     * @param value
     */
    @Override
    public void lpushx(BinaryJedisCluster jedisCluster, String key, String value) {
        if (verifyString(key) && verifyString(value)) {
            byte[] keyBytes = convert(key);
            byte[] valueByte = convert(value);
            Long lpushx = jedisCluster.lpushx(keyBytes, valueByte);

        }

    }

    /**
     * 在该list的尾部添加元素
     *
     * @param jedisCluster
     * @param key
     * @param value
     */
    private void rpushx(BinaryJedisCluster jedisCluster, String key, String value) {
        if (verifyString(key) && verifyString(value)) {
            byte[] keyBytes = convert(key);
            byte[] valueByte = convert(value);
            Long lpushx = jedisCluster.rpushx(keyBytes, valueByte);

        }

    }

    /**
     * 返回并弹出指定的key关联的链表中的第一个元素，即头部元素
     * 弹出后，redis中将没有那个值了
     *
     * @param jedisCluster
     * @param key
     */
    @Override
    public String lpop(BinaryJedisCluster jedisCluster, String key) {
        if (verifyString(key)) {
            byte[] keybytes = convert(key);
            byte[] lpop = jedisCluster.lpop(keybytes);
            String d = DataConvert.byteConvertString(lpop);
            return d;

        }

        return null;
    }

    /**
     * 从尾部弹出元素。
     *
     * @param jedisCluster
     * @param key
     */
    @Override
    public String rpop(BinaryJedisCluster jedisCluster, String key) {
        if (verifyString(key)) {
            byte[] keys = convert(key);
            byte[] rpop = jedisCluster.rpop(keys);
            String d = DataConvert.byteConvertString(rpop);
            return d;

        }

        return null;
    }

    /**
     * 将链表中的尾部元素弹出并添加到头部
     *
     * @param jedisCluster
     * @param resource     来自与，源头
     * @param destination  目的地，也就是添加到哪个链表的目的地
     */
    @Override
    public void rpoplpush(BinaryJedisCluster jedisCluster, String resource, String destination) {
        if (verifyString(resource) && verifyString(destination)) {
            byte[] res = convert(resource);
            byte[] desti = convert(destination);
            byte[] rpoplpush = jedisCluster.rpoplpush(res, desti);

        }

    }

    /**
     * 返回指定的key关联的链表中的元素的数量
     *
     * @param jedisCluster
     * @param key
     */
    @Override
    public Long llen(BinaryJedisCluster jedisCluster, String key) {
        if (verifyString(key)) {
            byte[] keyBytes = convert(key);
            Long llen = jedisCluster.llen(keyBytes);
            return llen;

        }

        return -1L;
    }

    /**
     * 设置元素  按照索引，设置索引所在的元素的值，相当于  arr[1] = 2;
     *
     * @param jedisCluster
     * @param key
     * @param index        索引，角标
     * @param value        改变的值
     */
    @Override
    public void lset(BinaryJedisCluster jedisCluster, String key, Long index, String value) {
        if (verifyString(key) && verifyString(value) && null != index) {
            byte[] keyBytes = convert(key);
            byte[] vb = convert(value);
            jedisCluster.lset(keyBytes, index, vb);

        }


    }


    /**
     * 删除count个值为value的元素，如果count大于0，从头向尾遍历并删除count个值为value的元素，
     * 如果count小于0，则从尾向头遍历并删除。如果count等于0，则删除链表中所有等于value的元素
     *
     * @param jedisCluster
     * @param key
     * @param count        个数
     * @param value        值
     */
    @Override
    public void lrem(BinaryJedisCluster jedisCluster, String key, Long count, String value) {
        if (verifyString(key) && verifyString(value) && null != count) {
            byte[] kb = convert(key);
            byte[] vb = convert(value);
            Long lrem = jedisCluster.lrem(kb, count, vb);

        }

    }

    /**
     * 在指定的元素前后插入
     *
     * @param jedisCluster
     * @param key
     * @param status       为byte类型，只能为 0 ，1  ，为0 就从LIST_POSITION.BEFORE在前面插入，为1就从后面插入
     * @param specify      指定元素
     * @param value        插入元素
     */
    @Override
    public void linsert(BinaryJedisCluster jedisCluster, String key, byte status, String specify, String value) {
        if (verifyString(key) && verifyString(specify) && verifyString(value)) {
            byte[] kb = convert(key);
            byte[] spb = convert(specify);
            byte[] vb = convert(value);
            BinaryClient.LIST_POSITION s;
            if (status == 0) {
                jedisCluster.linsert(kb, BinaryClient.LIST_POSITION.BEFORE, spb, vb);

            } else {
                jedisCluster.linsert(kb, BinaryClient.LIST_POSITION.AFTER, spb, vb);

            }
        }

    }


    /*********************************************  存储 sets 类型*****************************************************/

    /**
     * 向set中添加数据，如果该key的值已有则不会重复添加
     *
     * @param jedisCluster
     * @param key
     * @param value
     */
    @Override
    public void sadd(BinaryJedisCluster jedisCluster, String key, String value) {
        if (verifyString(key) && verifyString(value)) {
            byte[] kb = convert(key);
            byte[] vb = convert(value);
            jedisCluster.sadd(kb, vb);

        }

    }

    /**
     * 获取set中所有的成员
     *
     * @param jedisCluster
     * @param key
     * @return
     */
    @Override
    public List<String> smembers(BinaryJedisCluster jedisCluster, String key) {
        if (verifyString(key)) {
            byte[] kb = convert(key);
            List<String> listResult = new ArrayList<>();
            Set<byte[]> smembers = jedisCluster.smembers(kb);
            Iterator<byte[]> iterator = smembers.iterator();
            while (iterator.hasNext()) {
                byte[] next = iterator.next();
                String d = DataConvert.byteConvertString(next);
                listResult.add(d);
            }
            return listResult;
        }

        return null;
    }

    /**
     * 获取set中成员的数量
     *
     * @param jedisCluster
     * @param key
     * @return
     */
    @Override
    public Long scard(BinaryJedisCluster jedisCluster, String key) {
        if (verifyString(key)) {
            byte[] kb = convert(key);
            Long scard = jedisCluster.scard(kb);
            return scard;
        }

        return -1L;
    }

    /**
     * 判断指定元素在set只能是否存在,存在就返回1，否则返回 0
     *
     * @param jedisCluster
     * @param key
     * @param specify      指定元素
     */
    @Override
    public Boolean sismember(BinaryJedisCluster jedisCluster, String key, String specify) {
        Boolean sismember = false;
        if (verifyString(key) && verifyString(specify)) {
            byte[] kb = convert(key);
            byte[] sk = convert(specify);
            sismember = jedisCluster.sismember(kb, sk);

        }
        return sismember;

    }

    /**
     * 删除在set中指定的元素
     *
     * @param jedisCluster
     * @param key
     * @param specify
     */
    @Override
    public void srem(BinaryJedisCluster jedisCluster, String key, String specify) {
        if (verifyString(key) && verifyString(specify)) {
            byte[] kb = convert(key);
            byte[] sk = convert(specify);
            jedisCluster.srem(kb, sk);

        }
    }

    /**
     * 获取set中随机一个元素
     *
     * @param jedisCluster
     * @param key
     */
    @Override
    public String srandmember(BinaryJedisCluster jedisCluster, String key) {
        if (verifyString(key)) {
            byte[] kb = convert(key);
            byte[] srandmember = jedisCluster.srandmember(kb);
            String d = DataConvert.byteConvertString(srandmember);
            return d;

        }

        return "";
    }

    /**
     * 返回key1与key2中相差的成员，而且与key的顺序有关。即返回差集。
     *
     * @param jedisCluster
     * @param key1
     * @param key2
     */
    @Override
    public List<String> sdiff(BinaryJedisCluster jedisCluster, String key1, String key2) {
        List<String> resultList = new ArrayList<>();
        if (verifyString(key1) && verifyString(key2)) {
            byte[] k1 = convert(key1);
            byte[] k2 = convert(key2);
            Set<byte[]> sdiffSet = jedisCluster.sdiff(k1, k2);
            Iterator<byte[]> iterator = sdiffSet.iterator();
            while (iterator.hasNext()) {
                byte[] next = iterator.next();
                String d = DataConvert.byteConvertString(next);
                resultList.add(d);

            }
        }
        return resultList;

    }

    /**
     * 将key1、key2相差的成员存储在	 destination上
     *
     * @param jedisCluster
     * @param destination  目标set
     * @param key1
     * @param key2
     */
    @Override
    public Long sdiffstore(BinaryJedisCluster jedisCluster, String destination, String key1, String key2) {
        if (verifyString(destination) && verifyString(key1) && verifyString(key2)) {
            byte[] dest = convert(destination);
            byte[] k1 = convert(key1);
            byte[] k2 = convert(key2);
            Long sdiffstore = jedisCluster.sdiffstore(dest, k1, k2);
            return sdiffstore;
        }
        return -1L;

    }

    /**
     * 返回key1和key2的交集
     *
     * @param jedisCluster
     * @param key1
     * @param key2
     * @return
     */
    @Override
    public List<String> sinter(BinaryJedisCluster jedisCluster, String key1, String key2) {
        List<String> listResult = new ArrayList<>();
        if (verifyString(key1) && verifyString(key2)) {
            byte[] k1 = convert(key1);
            byte[] k2 = convert(key2);
            Set<byte[]> sinter = jedisCluster.sinter(k1, k2);
            Iterator<byte[]> iterator = sinter.iterator();
            while (iterator.hasNext()) {
                byte[] next = iterator.next();
                String s = DataConvert.byteConvertString(next);
                listResult.add(s);

            }

        }
        return listResult;

    }


    /**
     * 将key1和key2返回的交集存储在destination上
     *
     * @param jedisCluster
     * @param key1
     * @param key2
     * @param destination  存储的目标地址
     * @return
     */
    @Override
    public Long sinterstore(BinaryJedisCluster jedisCluster, String destination, String key1, String key2) {
        if (verifyString(key1) && verifyString(key2) && verifyString(destination)) {
            byte[] k1 = convert(key1);
            byte[] k2 = convert(key2);
            byte[] desk = convert(destination);
            Long sinterstore = jedisCluster.sinterstore(desk, k1, k2);
            return sinterstore;

        }
        return -1L;
    }

    /**
     * 返回key1和key2的并集
     *
     * @param jedisCluster
     * @param key1
     * @param key2
     */
    @Override
    public List<String> sunion(BinaryJedisCluster jedisCluster, String key1, String key2) {
        List<String> listResult = new ArrayList<>();
        if (verifyString(key1) && verifyString(key2)) {
            byte[] k1 = convert(key1);
            byte[] k2 = convert(key2);
            Set<byte[]> sunion = jedisCluster.sunion(k1, k2);
            Iterator<byte[]> iterator = sunion.iterator();
            while (iterator.hasNext()) {
                byte[] next = iterator.next();
                String s = DataConvert.byteConvertString(next);
                listResult.add(s);

            }

        }
        return listResult;

    }

    /**
     * key1和key2的并集存储在目标destination里面
     *
     * @param jedisCluster
     * @param destination  存储的set
     * @param key1
     * @param key2
     * @return
     */
    @Override
    public long sunionstore(BinaryJedisCluster jedisCluster, String destination, String key1, String key2) {
        if (verifyString(key1) && verifyString(key2) && verifyString(destination)) {
            byte[] k1 = convert(key1);
            byte[] k2 = convert(key2);
            byte[] desk = convert(destination);
            Long sinterstore = jedisCluster.sunionstore(desk, k1, k2);
            return sinterstore;

        }
        return -1L;

    }

    /*********************************************  存储 sortedset 类型************************************************/

    /**
     * 将所有成员以及该成员的分数存放到sorted-set中
     *
     * @param jedisCluster
     * @param key
     * @param score        分数
     * @param member       成员，值
     */
    @Override
    public void zadd(BinaryJedisCluster jedisCluster, String key, Double score, String member) {
        if (verifyString(key) && verifyString(member) && null != score) {
            byte[] k1 = convert(key);
            byte[] m = convert(member);
            jedisCluster.zadd(k1, score, m);

        }
    }

    /**
     * 获取集合中的成员数量
     *
     * @param jedisCluster
     * @param key
     * @return
     */
    @Override
    public Long zcard(BinaryJedisCluster jedisCluster, String key) {
        if (verifyString(key)) {
            byte[] kb = convert(key);
            Long zcard = jedisCluster.zcard(kb);
            return zcard;

        }
        return -1L;

    }

    /**
     * 获取分数在[min,max]之间的成员
     *
     * @param jedisCluster
     * @param key
     * @param min          最小
     * @param max
     */
    @Override
    public Long zcount(BinaryJedisCluster jedisCluster, String key, Double min, Double max) {
        if (verifyString(key) && null != min && null != max) {
            byte[] kb = convert(key);
            Long zcount = jedisCluster.zcount(kb, min, max);
            return zcount;

        }
        return -1L;
    }

    /**
     * 设置指定成员的增加的分数
     *
     * @param jedisCluster
     * @param key
     * @param score
     * @param member
     */
    @Override
    public void zincrby(BinaryJedisCluster jedisCluster, String key, Double score, String member) {
        if (verifyString(key) && verifyString(member) && null != score) {
            byte[] kb = convert(key);
            byte[] m = convert(member);
            jedisCluster.zincrby(kb, score, m);

        }

    }

    /**
     * 获取集合中脚标为start-end的成员，[withscores]参数表明返回的成员包含其分数。
     *
     * @param jedisCluster
     * @param key
     * @param start
     * @param end
     * @return
     */
    @Override
    public List<String> zrange(BinaryJedisCluster jedisCluster, String key, Long start, Long end) {
        if (null != start && null != end && verifyString(key)) {
            byte[] kb = convert(key);
            Set<byte[]> zrange = jedisCluster.zrange(kb, start, end);
            Iterator<byte[]> iterator = zrange.iterator();
            List<String> list = new ArrayList<>();
            while (iterator.hasNext()) {
                byte[] next = iterator.next();
                String d = DataConvert.byteConvertString(next);
                list.add(d);

            }
            return list;

        }
        return null;

    }


    /**
     * @param jedisCluster
     * @param key
     * @param min          返回分数在[min,max]的成员并按照分数从低到高排序
     * @param max
     * @param offset       显示分数；[limit offset count]：offset，表明从脚标为offset的元素开始并返回count个成员。
     * @param count
     * @return
     */
    @Override
    public List<String> zrangebyscore(BinaryJedisCluster jedisCluster, String key, Double min, Double max, Integer offset, Integer count) {
        if (verifyString(key) && null != min && null != max) {
            byte[] kb = convert(key);
            Set<byte[]> set = jedisCluster.zrangeByScore(kb, min, max, offset, count);
            Iterator<byte[]> iterator = set.iterator();
            List<String> list = new ArrayList<>();
            while (iterator.hasNext()) {
                byte[] next = iterator.next();
                String d = DataConvert.byteConvertString(next);
                list.add(d);

            }
            return list;

        }
        return null;
    }

    /**
     * 返回指定成员所在的位置
     *
     * @param jedisCluster
     * @param key
     * @param member       指定成员
     * @return
     */
    @Override
    public Long zrank(BinaryJedisCluster jedisCluster, String key, String member) {
        if (verifyString(key) && verifyString(member)) {
            byte[] kb = convert(key);
            byte[] mb = convert(member);
            Long zrank = jedisCluster.zrank(kb, mb);
            return zrank;

        }

        return null;
    }

    /**
     * 删除指定成员
     *
     * @param jedisCluster
     * @param key
     * @param member
     */
    @Override
    public void zrem(BinaryJedisCluster jedisCluster, String key, String member) {
        if (verifyString(key) && verifyString(member)) {
            byte[] kb = convert(key);
            byte[] mb = convert(member);
            jedisCluster.zrem(kb, mb);

        }
    }

    /**
     * 返回指定成员所在的分数
     *
     * @param jedisCluster
     * @param key
     * @param member       指定成员
     * @return
     */
    @Override
    public Double zscore(BinaryJedisCluster jedisCluster, String key, String member) {
        if (verifyString(key) && verifyString(member)) {
            byte[] kb = convert(key);
            byte[] mb = convert(member);
            Double zscore = jedisCluster.zscore(kb, mb);
            return zscore;

        }
        return null;

    }


    /*********************************************  存储 hset 类型*****************************************************/

    /**
     * 为指定的key设定field/value对（键值对）。
     *
     * @param jedisCluster
     * @param key
     * @param field
     * @param value
     * @return
     */
    @Override
    public Long hset(BinaryJedisCluster jedisCluster, String key, String field, String value) {
        if (verifyString(key) && verifyString(field) && verifyString(value)) {
            byte[] kb = convert(key);
            byte[] fb = convert(field);
            byte[] vb = convert(value);
            Long hset = jedisCluster.hset(kb, fb, vb);
            return hset;

        }
        return null;

    }

    /**
     * 获取key中的所有filed-vaule
     *
     * @param jedisCluster
     * @param key
     * @return
     */
    @Override
    public Map<String, String> hgetall(BinaryJedisCluster jedisCluster, String key) {
        if (verifyString(key)) {
            Map<String, String> resultMap = new HashMap<>();
            byte[] kb = convert(key);
            Map<byte[], byte[]> map = jedisCluster.hgetAll(kb);
            Set<Map.Entry<byte[], byte[]>> entries = map.entrySet();
            Iterator<Map.Entry<byte[], byte[]>> iterator = entries.iterator();
            while (iterator.hasNext()) {
                Map.Entry<byte[], byte[]> next = iterator.next();
                byte[] ke = next.getKey();
                byte[] value = next.getValue();
                String d = DataConvert.byteConvertString(ke);
                String v = DataConvert.byteConvertString(value);
                resultMap.put(d, v);

            }
            return resultMap;

        }
        return null;

    }

    /**
     * 返回指定的key中的field的值
     *
     * @param jedisCluster
     * @param key
     * @param field
     * @return
     */
    @Override
    public String hget(BinaryJedisCluster jedisCluster, String key, String field) {
        if (verifyString(key) && verifyString(field)) {
            byte[] kb = convert(key);
            byte[] fb = convert(field);
            byte[] hget = jedisCluster.hget(kb, fb);
            String d = DataConvert.byteConvertString(hget);
            return d;

        }
        return key;

    }

    /**
     * 设置key中的多个filed/value
     *
     * @param jedisCluster
     * @param key
     * @param map
     */
    @Override
    public void hmset(BinaryJedisCluster jedisCluster, String key, Map map) {
        if (verifyString(key)) {
            byte[] kb = convert(key);
            jedisCluster.hmset(kb, map);

        }

    }

    /**
     * 获取key中的多个filed的值
     *
     * @param jedisCluster
     * @param key
     * @param field
     * @return
     */
    @Override
    public List<String> hmget(BinaryJedisCluster jedisCluster, String key, String field) {
        if (verifyString(key)) {
            byte[] kb = convert(key);
            byte[] fb = convert(field);
            List<byte[]> hmget = jedisCluster.hmget(kb, fb);
            Iterator<byte[]> iterator = hmget.iterator();
            List<String> resultList = new ArrayList<>();
            while (iterator.hasNext()) {
                byte[] next = iterator.next();
                String d = DataConvert.byteConvertString(next);
                resultList.add(d);

            }
            return resultList;

        }
        return null;

    }

    /**
     * 判断指定的key中的filed是否存在
     *
     * @param jedisCluster
     * @param key
     * @param field
     * @return
     */
    @Override
    public Boolean hexists(BinaryJedisCluster jedisCluster, String key, String field) {
        Boolean hexists;
        if (verifyString(key) && verifyString(field)) {
            byte[] kb = convert(key);
            byte[] fd = convert(field);
            hexists = jedisCluster.hexists(kb, fd);
            return hexists;

        }
        return null;
    }

    /**
     * 获取key中field的数量
     *
     * @param jedisCluster
     * @param key
     * @return
     */
    @Override
    public Long hlen(BinaryJedisCluster jedisCluster, String key) {
        if (verifyString(key)) {
            byte[] kb = convert(key);
            Long hlen = jedisCluster.hlen(kb);
            return hlen;

        }
        return -1L;

    }

    /**
     *  设置key中filed的值增加increment，如：age		增加20
     * @param jedisCluster
     * @param key
     * @param field
     * @param increment
     * @return
     */
    @Override
    public Long hincrby(BinaryJedisCluster jedisCluster, String key, String field, Long increment) {
        if (verifyString(key) && verifyString(field) && null != increment) {
            byte[] kb = convert(key);
            byte[] fd = convert(field);
            Long aLong = jedisCluster.hincrBy(kb, fd, increment);
            return aLong;

        }

        return -1L;
    }


    /*********************************************  辅助方法      *****************************************************/

    /**
     * 验证数据格式
     *
     * @param data
     * @return
     */
    static boolean verifyString(String data) {
        if ("".equals(data) && null == data) {
            return false;

        }
        return true;

    }

    static boolean verifyString(String data, List list) {
        if ("".equals(data) || null == null || list.size() == 0 || null == list) {
            return false;

        }
        return true;

    }

    static byte[] convert(String data) {
        byte[] bytes = data.getBytes(Charset.forName("UTF-8"));
        return bytes;
    }
}

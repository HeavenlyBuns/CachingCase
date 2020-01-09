package eacache.index;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @Author : 佘浩然
 * 描述
 * --------缓存管理类
 * @Package : eacache.index
 * @Create : 2017/10/11  10:33
 */

@Service
public class EhcacheManager {

    private static int index = 0;

    private static int find = 0;

    @CachePut(value = "test1", key = "#id+':'+#name")
    public Policy saveHead(String id, String name) {

        System.out.println("**********************test1 第" + index + "  次添加，进入到缓存中 *****************************");
        Policy policy = new Policy(id, name);

        index++;
        return policy;

    }

    @Cacheable(value = "test1", key = "#id+':'+#name",sync = true)
    public Policy findHead(String id, String name) {
        System.out.println("====================test1 第 " + find + " 次查询 进入到缓存  ==============================");
        Policy policy = new Policy("", "");
        find++;
        return policy;
    }


    @CachePut(value = "test2", key = "#id+':'+#name")
    public Policy saveHead2(String id, String name) {

        System.out.println("**********************test2 第" + index + "  次添加，进入到缓存中 *****************************");
        Policy policy = new Policy(id, name);

        index++;
        return policy;

    }

    @Cacheable(value = "test2", key = "#id+':'+#name",sync = true)
    public Policy findHead2(String id, String name) {
        System.out.println("====================test2 第 " + find + " 次查询 进入到缓存  ==============================");
        Policy policy = new Policy("", "");
        find++;
        return policy;
    }
}

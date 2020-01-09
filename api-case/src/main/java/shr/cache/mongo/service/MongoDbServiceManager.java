package shr.cache.mongo.service;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import shr.cache.mongo.manager.MongoManager;
import shr.cache.mongo.pojo.MongoProperties;

import java.util.List;

/**
 * @Author : 佘浩然
 * 描述
 * --------mongo的Service管理
 * @Package : shr.cache.mongo.service
 * @Create :  2017/9/22 10:47
 */
public class MongoDbServiceManager implements MongoDbService {


    /* 集合名字 */
    public final static String COLLECTION_MESSAGE = "message";

    /*message集合的连接*/
    private MongoCollection<Document> messageCollection;

    private MongoProperties dbProperties;
    private MongoManager manager = MongoManager.getNewIntance();

    private static MongoDbServiceManager instance = new MongoDbServiceManager();

    public static MongoDbServiceManager getNewIntance() {
        return instance;

    }

    /**
     * 启动mongo的实例
     *
     * @param mongoProperties mongo配置文件
     */
    public void startp(MongoProperties mongoProperties) {
        if (mongoProperties != null) {
            this.dbProperties = mongoProperties;
        }
        manager.starup(dbProperties);
        //this.messageCollection = manager.getCollection(dbProperties.getDbBase(), COLLECTION_MESSAGE);

    }

    @Override
    public MongoCollection<Document> getCollection(String dbName, String collectionName) {
        MongoCollection<Document> collection = this.manager.getCollection(dbName, collectionName);
        return collection;

    }

    /**
     * 在message集合里面 添加数据
     *
     * @param data
     */
    public void insert(MongoCollection collection,JSONObject data) {
        this.manager.insert(collection, data);

    }

    @Override
    public void updateById(MongoCollection collection,String id, JSONObject data) {
        this.manager.updateById(collection, id, data);

    }

    @Override
    public void updatePushById(MongoCollection collection,String id, JSONObject data) {
        this.manager.updatePushById(collection, id, data);

    }

    @Override
    public void remove(MongoCollection collection,String id) {
        this.manager.removeById(collection, id);

    }

    @Override
    public void insertBatch(MongoCollection collection,List<JSONObject> data) {
        this.manager.insertBatch(collection, data);

    }

    @Override
    public Integer updateBatch(MongoCollection collection,List<JSONObject> data) {
        Integer updateLine = this.manager.updateBatch(collection, data);
        return updateLine;

    }

    @Override
    public Object findByIdAndSn(MongoCollection collection,String id, List sn) {
        Document byIdAndSn = this.manager.findByIdAndSn(collection, id, sn);
        return byIdAndSn;
    }

}

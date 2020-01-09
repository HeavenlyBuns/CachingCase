package shr.cache.mongo.service;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.List;

/**
 *  MongoDb操作的通用接口
 */
public interface MongoDbService {

    MongoCollection<Document> getCollection(String dbName, String collectionName);

    void insert(MongoCollection collection, JSONObject data);

    void updateById(MongoCollection collection, String id, JSONObject data);

    void updatePushById(MongoCollection collection, String id, JSONObject data);

    void remove(MongoCollection collection, String id);

    void insertBatch(MongoCollection collection, List<JSONObject> data);

    Integer updateBatch(MongoCollection collection, List<JSONObject> data);

    Object findByIdAndSn(MongoCollection collection, String id, List sn);



}

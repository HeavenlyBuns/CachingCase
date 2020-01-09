package shr.cache.mongo.manager;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.*;
import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOneModel;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.WriteModel;
import com.mongodb.client.result.DeleteResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import shr.cache.mongo.pojo.MongoProperties;

import java.util.*;

/**
 * @Author : 佘浩然
 * 描述
 * --------mongo初始化
 * @Package : shr.cache.mongo.init
 * @Create : 17:23 2017/9/21
 */
public class MongoManager {
    /* 连接端口的list */
    private static List<ServerAddress> list_address = new ArrayList<ServerAddress>();

    /* mongo的用户名字配置list */
    private static List<MongoCredential> list_MongoCred = new ArrayList<MongoCredential>();

    /* mongo配置文件 */
    private static MongoProperties mongoProperties = null;

    private MongoClient mongoClient = null;

    private static MongoManager instance = new MongoManager();

    /**
     * @return 单例设计
     */
    public static MongoManager getNewIntance() {
        return instance;

    }

    private MongoManager() {


    }

    /**
     * 开启mongo，初始化配置文件
     *
     * @param mongoProperties mongo配置文件映射
     */
    public void starup(MongoProperties mongoProperties) {
        if (null != mongoProperties && Objects.isNull(mongoProperties)) {
            this.mongoProperties = mongoProperties;

        }
        if (mongoProperties.getHostAndPort() != null && !"".equals(mongoProperties.getHostAndPort())) {
            String hostAndPort = mongoProperties.getHostAndPort();
            String[] host_Port = hostAndPort.split(":");
            ServerAddress address = new ServerAddress(host_Port[0], Integer.parseInt(host_Port[1]));
            list_address.add(address);

        }

        MongoClientOptions options = this.getConfOptions();
        /* 如果开启验证，设置用户名密码 */
        if (mongoProperties.isAuth()) {
            if (mongoProperties.getUrl() != null && !"".equals(mongoProperties.getUrl())) {
                this.mongoClient = new MongoClient(new MongoClientURI(mongoProperties.getUrl()));

            } else {
                /* 配置mongo  验证的用户名字等 */

                /* createMongoCRCredential() 不推荐使用在mongo3，官方文档不推荐使用mongo-cr认真，因为在mongo3会无法认证 */
                //MongoCredential mongoCredential = MongoCredential.createMongoCRCredential(mongoProperties.getUserName(),
                MongoCredential mongoCredential = MongoCredential.createCredential(mongoProperties.getUserName(),
                        mongoProperties.getDbName(), mongoProperties.getPwd().toCharArray());
                list_MongoCred.add(mongoCredential);
                this.mongoClient = new MongoClient(list_address, list_MongoCred, options);

            }

        } else {
            if (mongoProperties.getUrl() != null && !"".equals(mongoProperties.getUrl())) {
                this.mongoClient = new MongoClient(new MongoClientURI(mongoProperties.getUrl()));

            } else {
                /* 没有开启验证，则是默认无需密码连接，直接配置adress 即可 */
                this.mongoClient = new MongoClient(list_address, options);

            }
        }
        System.out.printf("connection success !");

    }

    private MongoClientOptions getConfOptions() {
        return (new MongoClientOptions.Builder()).socketKeepAlive(true).connectTimeout(5000).socketTimeout(10000).readPreference(ReadPreference.primary()).connectionsPerHost(200).maxWaitTime(10000).threadsAllowedToBlockForConnectionMultiplier(5000).build();
    }

    /**
     * 根据mongo数据库的名字获取Database
     *
     * @param databaseName momgo的数据库名字
     * @return
     */
    public MongoDatabase getDatabase(String databaseName) {
        if (null != databaseName && !"".equals(databaseName)) {
            MongoDatabase database = this.mongoClient.getDatabase(databaseName);
            return database;

        }
        return null;
    }

    /**
     * 根据库名和集合名字获取一个集合
     *
     * @param dbName         库名
     * @param collectionName 集合名
     */
    public MongoCollection<Document> getCollection(String dbName, String collectionName) {
        if (!"".equals(dbName) && null != dbName && !"".equals(collectionName) && null != collectionName) {
            MongoCollection<Document> collection = this.mongoClient.getDatabase(dbName).getCollection(collectionName);
            return collection;

        }
        return null;
    }

    /**
     * 向Mongo中插入数据
     *
     * @param coll 集合
     * @param json 要插入的数据
     */
    public void insert(MongoCollection<Document> coll, JSONObject json) {
        if (null != coll && null != json) {
            Document document = Document.parse(json.toJSONString());
            coll.insertOne(document);
        }
    }

    /**
     * 根据Id，修改
     *
     * @param collection
     * @param id         id主键
     * @param data       修改的值
     */
    public void updateById(MongoCollection<Document> collection, String id, JSONObject data) {
        ObjectId _id = null;
        if (id != null) {
            try {
                _id = new ObjectId(id);

            } catch (Exception e) {
                e.printStackTrace();

            }
        }
        Bson filter = Filters.eq("_id", _id);
        Document parse = Document.parse(data.toJSONString());
        Document update = new Document("$set", parse);
        collection.updateOne(filter, update);

    }

    /**
     * $push
     * 针对数组类型，对数组里面的元素进行追加等操作
     *
     * @param collection
     * @param id
     * @param data
     */
    public void updatePushById(MongoCollection<Document> collection, String id, JSONObject data) {
        ObjectId _id = null;
        if (id != null) {
            try {
                _id = new ObjectId(id);

            } catch (Exception e) {
                e.printStackTrace();

            }
        }
        Bson filter = Filters.eq("_id", _id);
        Document parse = Document.parse(data.toJSONString());
        Document update = new Document("$push", parse);
        Document document = new Document();
        collection.updateOne(filter, update);

    }

    /**
     * 根据id和数组中的sn查找
     * 注意：
     * 数组中的元素，要传入list，用$in来查询
     *
     * @param collection
     * @param id
     * @param sn
     */
    public Document findByIdAndSn(MongoCollection<Document> collection, String id, List sn) {
        ObjectId _id = null;
        BasicDBList dbList = new BasicDBList();
        if (id != null) {
            try {
                _id = new ObjectId(id);

            } catch (Exception e) {
                e.printStackTrace();

            }
        }
        dbList.add(sn);
        BasicDBObject _idF = new BasicDBObject("_id", _id);
        BasicDBObject snF = new BasicDBObject("sn", new BasicDBObject("$in", dbList));
        //BasicDBObject snF = new BasicDBObject("sn",sn);
        Bson filter = new BasicDBObject("$and", Arrays.asList(new BasicDBObject[]{_idF, snF}));
        //Document first = collection.find(filter).first();
        FindIterable<Document> documents = collection.find(filter);
        MongoCursor<Document> iterator = documents.iterator();
        if (iterator.hasNext()) {
            Document next = iterator.next();
            System.out.printf(" " + next.toJson());
            return next;

        }
        return null;

    }

    /**
     * 批量增加
     *
     * @param collection
     * @param listData   批量数据
     */
    public void insertBatch(MongoCollection<Document> collection, List<JSONObject> listData) {
        List<Document> documents = new ArrayList<>();
        Iterator<JSONObject> iterator = listData.iterator();
        if (iterator.hasNext()) {
            JSONObject next = iterator.next();
            Document parse = Document.parse(next.toJSONString());
            documents.add(parse);

        }
        collection.insertMany(documents);

    }

    /**
     * 批量修改
     *
     * @param collection
     * @param listData
     * @return
     */
    public Integer updateBatch(MongoCollection<Document> collection, List<JSONObject> listData) {
        List<WriteModel<Document>> documents = new ArrayList<>();
        Iterator<JSONObject> iterator = listData.iterator();
        if (iterator.hasNext()) {
            JSONObject next = iterator.next();
            Document id = new Document("_id", next.getString("id"));
            next.remove("id");
            Document data = Document.parse(next.toJSONString());
            Document update = new Document("$set", data);
            UpdateOneModel<Document> oneModel = new UpdateOneModel<Document>(id, update, (new UpdateOptions()).upsert(true));
            documents.add(oneModel);

        }
        BulkWriteResult bulkWriteResult = collection.bulkWrite(documents);
        if (null != bulkWriteResult) {
            return bulkWriteResult.getModifiedCount();
        } else {
            return 0;
        }

    }


    /**
     * 根据id值，删除
     *
     * @param collection
     * @param id         要删除的id
     */
    public void removeById(MongoCollection<Document> collection, String id) {
        ObjectId _id = null;
        if (id != null) {
            try {
                _id = new ObjectId(id);

            } catch (Exception e) {
                e.printStackTrace();

            }
        }
        Bson filter = Filters.eq("_id", _id);
        DeleteResult deleteResult = collection.deleteOne(filter);
        if (null != deleteResult) {
            System.out.printf("[Mongo执行删除：] " + deleteResult);

        }
    }

    /**
     * 按照时间戳查找元素，加入asc排序方式等
     *
     * @param coll
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param page      页数
     * @param pageSize  每页显示的数量
     */
    public void findASCByTimesmtpAndPage(MongoCollection<Document> coll, long startTime, long endTime, int page, int pageSize) {
        BasicDBObject start = new BasicDBObject("time.timestamp", new BasicDBObject("$gte", Long.valueOf(startTime)));
        BasicDBObject end = new BasicDBObject("time.timestamp", new BasicDBObject("$lt", Long.valueOf(endTime)));
        Bson filter = new BasicDBObject("$and", Arrays.asList(new BasicDBObject[]{start, end}));
        /* ASC过滤条件 */
        BasicDBObject asc = new BasicDBObject("ASC", Integer.valueOf(1));

        MongoCursor<Document> iterator = coll.find(filter).sort(asc).skip((page - 1) * pageSize).limit(page).iterator();
        if (iterator.hasNext()) {
            Document next = iterator.next();
            if (null != next) {
                System.out.printf(next.toJson());

            }
        }

    }


    /**
     * 关闭mongo的连接
     */
    public void stop() {
        if (null != this.mongoClient) {
            this.mongoClient.close();

        }
    }

}

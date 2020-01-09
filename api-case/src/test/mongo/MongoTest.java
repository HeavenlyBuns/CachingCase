package mongo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.junit.Test;
import shr.cache.mongo.manager.MongoManager;
import shr.cache.mongo.pojo.MongoProperties;
import shr.cache.mongo.service.MongoDbService;
import shr.cache.mongo.service.MongoDbServiceManager;
import shr.cache.utils.CacheEnum;
import shr.cache.utils.CacheManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @Author : 佘浩然
 * 描述
 * --------mongo连接测试
 * @Package : mongo
 * @Create : 16:53 2017/9/21
 */
public class MongoTest {

    @Test
    public void testConnection(){
        //连接到mongo服务
        ServerAddress address = new ServerAddress("192.168.19.3",27017);
        List<ServerAddress> list_address = new ArrayList<ServerAddress>();
        list_address.add(address);

        MongoCredential credential = MongoCredential.createCredential("shr","admin","root".toCharArray());
        List<MongoCredential> list_mongoCred = new ArrayList<MongoCredential>();
        list_mongoCred.add(credential);

        MongoClient client = new MongoClient(list_address,list_mongoCred);
        MongoDatabase cc = client.getDatabase("cc");
        MongoCollection<Document> runoob = cc.getCollection("runoob");
        FindIterable<Document> documents = runoob.find();
        long count = runoob.count();
        System.out.printf("connection succes !" + count);

    }


    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    /**
     *  测试MongoInit类
     */
    @Test
    public void testConec(){
        MongoProperties mongoProperties = new MongoProperties("admin","shr","root");
        mongoProperties.addHostAndPort("192.168.19.3","27017");
        mongoProperties.setUrl("mongodb://shr:root@192.168.19.3:27017/admin");
        mongoProperties.setDbBase("cc");
        MongoManager init = MongoManager.getNewIntance();

        init.starup(mongoProperties);
        JSONObject data = new JSONObject();
        //data.put("1","111");
//        JSONArray array = new JSONArray(Arrays.asList(new String("s")));
//        data.put("sn",array);
        List list = new ArrayList();
        list.add("g");
        data.put("sn",list);
        data.put("time.timestamp",System.currentTimeMillis());
        //MongoDbServiceManager.getNewIntance().startp(mongoProperties);
        MongoCollection<Document> coll = init.getCollection("cc", "message");
        //init.insert(coll,data);

        //init.updatePushById(coll,"59c397b557e11e0fa0251187",data);
        //init.updateById(coll,"59c397b557e11e0fa0251187",data);
        init.findASCByTimesmtpAndPage(coll,0,System.currentTimeMillis(),1,4);
        //init.removeById(coll,"59c3a0bf1b896819786edde2");
        //MongoDbServiceManager.getNewIntance().findByIdAndSn("59c397b557e11e0fa0251187",list);
        //init.findByIdAndSn(coll,"59c397b557e11e0fa0251187",list);

    }

    /**
     * 测试CacheManager
     */
    @Test
    public void cacheManager(){
        Properties properties = new Properties();
        try {
            properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("configure.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String db = properties.getProperty("mongo.dbName");
        String userName = properties.getProperty("mongo.userName");
        String pwd = properties.getProperty("mongo.pwd");
        String url = properties.getProperty("mongo.url");
//        MongoProperties mongoProperties = new MongoProperties("admin","shr","root");
        MongoProperties mongoProperties = new MongoProperties(db,userName,pwd);
        mongoProperties.addHostAndPort("192.168.19.3","27017");
        mongoProperties.setUrl(url);
        mongoProperties.setDbBase("cc");
        CacheManager.initMongoDb(mongoProperties);
        MongoDbService mongoDb = CacheManager.getMongoDb(CacheEnum.MONGODB);
        MongoCollection<Document> cc = mongoDb.getCollection("cc", MongoDbServiceManager.COLLECTION_MESSAGE);
        List list = new ArrayList();
        list.add("22");
        JSONArray array = new JSONArray();
        array.add("22");

        mongoDb.findByIdAndSn(cc,"59cb21921b89681b78421f96",list);
        JSONObject data = new JSONObject();
        data.put("list",list);
        data.put("sn",array);
        //mongoDb.insert(cc,data);
    }

}

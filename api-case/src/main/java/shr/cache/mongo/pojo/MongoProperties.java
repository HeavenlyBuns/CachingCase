package shr.cache.mongo.pojo;

/**
 * @Author : 佘浩然
 * 描述
 * --------Mongo的配置文件映射
 * @Package : shr.cache.mongo.pojo
 * @Create : 11:28 2017/9/21
 */
public class MongoProperties {
    /* mongo的url连接 */
    private String url;

    /* mongo的ip 端口等 */
    private String hostAndPort;

    /* Mongo的dataBase名字，一般是admin */
    private String dbName;

    /* 要连接的mongo数据库名，即集合所存在的数据库名  */
    private String dbBase;

    /* Mongo的用户名*/
    private String userName;

    /* Mongo的密码 */
    private String pwd;

    /* mongo是否开启验证  ,默认为false，不开启*/
    private boolean auth = false;


    public MongoProperties(String dbName, String userName, String pwd) {
        /* 如果用户和密码都有值，那么说明是需要进行密码验证的，所以 auth=true */
        if(null != userName && !"".equals(userName) && null != pwd && !"".equals(pwd)){
            this.auth = true;

        }else {
            dbName = "";
            userName = "";
            pwd = "";

        }
        this.dbName = dbName;
        this.userName = userName;
        this.pwd = pwd;

    }

    public void addHostAndPort(String host,String port){
        if(!"".equals(host) && null != host && !"".equals(port) && null != port){
            String hostPort = host + ":" +port;
            this.hostAndPort = hostPort;

        }
    }

    public void setDbBase(String dbBase) {
        this.dbBase = dbBase;
    }

    public String getDbBase() {
        return dbBase;
    }

    public String getHostAndPort() {
        return hostAndPort;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isAuth() {
        return auth;
    }

    public void setAuth(boolean auth) {
        this.auth = auth;
    }
}

package eacache.index;

import java.io.Serializable;

/**
 * @Author : 佘浩然
 * 描述
 * --------实体对象
 * @Package : eacache.index
 * @Create : 2017/10/11  10:34
 */
public class Policy implements Serializable{
    public Policy(String id, String name) {
        this.id = id;
        this.name = name;
    }

    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Policy{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}

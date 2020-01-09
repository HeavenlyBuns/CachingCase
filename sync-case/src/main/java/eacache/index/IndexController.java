package eacache.index;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author : 佘浩然
 * 描述
 * --------测试类
 * @Package : eacache.index
 * @Create : 2017/10/11  9:55
 */
@RestController
public class IndexController {

    @Autowired
    private EhcacheManager ehcacheManager;

    @RequestMapping("/index/{id}&{name}")
    public String index(@PathVariable(value = "id")String id,@PathVariable(value = "name")String name){
        System.out.println(id + "  " + name);
        ehcacheManager.saveHead(id,name);
        ehcacheManager.saveHead2(id,name);
        return "200";

    }

    @RequestMapping("/find/{id}&{name}")
    public String find(@PathVariable(value = "id")String id,@PathVariable(value = "name")String name){
        System.out.println(id + "  " + name);
        ehcacheManager.findHead(id,name);
        ehcacheManager.findHead2(id,name);
        return "200";

    }

    @Value("${server.port}")
    private String port;
    @RequestMapping("/index")
    public String index(){

        return " 端口为:   " + port;

    }

}

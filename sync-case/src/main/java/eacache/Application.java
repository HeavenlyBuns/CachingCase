package eacache;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * @Author : 佘浩然
 * 描述
 * --------spring boot启动
 * @Package : eacache.index
 * @Create : 2017/10/11  10:53
 */
@SpringBootApplication
@EnableCaching
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

    }


}

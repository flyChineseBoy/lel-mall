package com.lele.mall;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@SpringBootApplication
public class MallApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext =SpringApplication.run(MallApplication.class, args);
        String a = applicationContext.getEnvironment().getProperty("spring.cloud.nacos.config.server-addr");
        String userName = applicationContext.getEnvironment().getProperty("server.port");
        System.out.println(a);
        System.out.println(userName);
    }

    @RestController
    public class Hello {
        @GetMapping(value = "/hello/{string}")
        public String hello(@PathVariable String string) {
            return "Hello! 我是Order，感谢你(" + string+")来调用我的hello方法";
        }
    }
}

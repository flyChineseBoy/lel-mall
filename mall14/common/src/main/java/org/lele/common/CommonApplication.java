package org.lele.common;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Demo class
 *
 * @author lele
 */
@SpringBootApplication
public class CommonApplication {

    public static void main(String[] args) {
        System.setProperty("es.set.netty.runtime.available.processors", "false");
        ConfigurableApplicationContext applicationContext =SpringApplication.run(CommonApplication.class, args);
    }

}

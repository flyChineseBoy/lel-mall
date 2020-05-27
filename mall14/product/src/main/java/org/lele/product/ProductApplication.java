package org.lele.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * org.lele.product
 *
 * @author: lele
 * @date: 2020-05-24
 */
@SpringBootApplication
public class ProductApplication {

    public static void main(String[] args) {
        System.setProperty("es.set.netty.runtime.available.processors", "false");
        SpringApplication.run( ProductApplication.class );
    }
}

package org.lele.authorizationcenter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class AuthorizationCenterApplication {

    public static void main(String[] args)
    {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(AuthorizationCenterApplication.class, args);
        System.out.println(applicationContext.getEnvironment().getProperty("server.port"));

       //System.out.println(new BCryptPasswordEncoder().encode("common"));
    }

}

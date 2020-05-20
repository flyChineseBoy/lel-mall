package org.lele.common.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

/*
 * org.lele.common.config
 * @author: lele
 * @date: 2020-05-03
 */
@Configuration
public class RedisConfig  {

    @Value("${redis.host}")
    private String redisServer;
    @Value("${redis.port}")
    private Integer redisPort;
    @Value("${redis.password}")
    private String redisPassword;

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(redisServer, redisPort);
        redisStandaloneConfiguration.setPassword(redisPassword);

        return new LettuceConnectionFactory(redisStandaloneConfiguration);
    }
}




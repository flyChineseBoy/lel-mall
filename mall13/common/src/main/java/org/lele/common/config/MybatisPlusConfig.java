package org.lele.common.config;
/*
 * com.lele.common.config
 * @author: lele
 * @date: 2020-05-01
 *  Mybatis、MybatisPlus配置文件
 */

import org.lele.common.entity.MUser;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@MapperScan(basePackages = {"org.lele.common.dao"})
@Configuration
public class MybatisPlusConfig {
    @Autowired
    private ConfigurableApplicationContext applicationContext;

    @Bean
    MUser user(){
        System.out.println(applicationContext.getBeanFactory());
        return new MUser();
    }

}

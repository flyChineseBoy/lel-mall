package org.lele.common.config;
/*
 * com.lele.common.config
 * @author: lele
 * @date: 2020-05-01
 *  Mybatis、MybatisPlus配置文件
 */

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@MapperScan(basePackages = {"org.lele.common.dao"})
@Configuration
public class MybatisPlusConfig {
}

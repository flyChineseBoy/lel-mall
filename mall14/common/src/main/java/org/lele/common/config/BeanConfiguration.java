package org.lele.common.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * org.lele.user
 *
 * @author: lele
 * @date: 2020-05-08
 */
@ComponentScan({"org.lele.common.authorization","org.lele.common.security"})
@Configuration
public class BeanConfiguration {
}

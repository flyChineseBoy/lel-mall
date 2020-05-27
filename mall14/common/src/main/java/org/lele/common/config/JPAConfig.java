package org.lele.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;


/**
 * org.lele.common.config
 *
 * @author: lele
 * @date: 2020-05-18
 */
@Configuration
@EnableElasticsearchRepositories(basePackages = "org.lele.*.repository")
public class JPAConfig {
}

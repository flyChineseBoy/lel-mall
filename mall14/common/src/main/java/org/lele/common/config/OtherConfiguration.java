package org.lele.common.config;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

/**
 * org.lele.common.config
 *
 * @author: lele
 * @date: 2020-05-25
 */
@Configuration
@EnableFeignClients(basePackages = "org.lele.common.authorization")
@EnableDiscoveryClient
public class OtherConfiguration {
}

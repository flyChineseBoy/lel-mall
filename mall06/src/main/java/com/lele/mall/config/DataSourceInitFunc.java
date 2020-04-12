package com.lele.mall.config;
 
import java.util.List;

import com.alibaba.cloud.sentinel.SentinelProperties;
import com.alibaba.cloud.sentinel.datasource.config.NacosDataSourceProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
 
import com.alibaba.csp.sentinel.datasource.ReadableDataSource;
import com.alibaba.csp.sentinel.datasource.nacos.NacosDataSource;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
 
 
@Configuration
public class DataSourceInitFunc {
 
	@Autowired
	private SentinelProperties sentinelProperties;
 
	@Bean
	public DataSourceInitFunc init() throws Exception {
 
		sentinelProperties.getDatasource().entrySet().stream().filter(map -> {
			return map.getValue().getNacos() != null;
		}).forEach(map -> {
			NacosDataSourceProperties nacos = map.getValue().getNacos();
			ReadableDataSource<String, List<FlowRule>> flowRuleDataSource = new NacosDataSource<>(
					nacos.getServerAddr(), nacos.getGroupId(), nacos.getDataId(),
					source -> JSON.parseObject(source, new TypeReference<List<FlowRule>>() {
					}));
			FlowRuleManager.register2Property(flowRuleDataSource.getProperty());
		});
		return new DataSourceInitFunc();
	}
}


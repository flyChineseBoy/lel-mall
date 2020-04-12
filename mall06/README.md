## 零、系列
  欢迎来嫖**从零开始SpringCloud Alibaba电商系列**:
1. [从零开始SpringCloud Alibaba电商系统(一)——Alibaba与Nacos服务注册与发现](https://blog.csdn.net/qq_35946969/article/details/105188015)  
2. [从零开始SpringCloud Alibaba电商系统(二)——Nacos配置中心](https://blog.csdn.net/qq_35946969/article/details/105279770)  
3. [从零开始SpringCloud Alibaba电商系统(三)——Sentinel流量防卫兵介绍、流量控制demo](https://blog.csdn.net/qq_35946969/article/details/105351082)
4. [从零开始SpringCloud Alibaba电商系统(四)——Sentinel的fallback和blockHandler](https://blog.csdn.net/qq_35946969/article/details/105375003)
5.  [从零开始SpringCloud Alibaba电商系统(五)——Feign Demo，Sentinel+Feign实现多节点间熔断/服务降级](https://blog.csdn.net/qq_35946969/article/details/105397473)

## 一、Sentinel为什么需要规则持久化？
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200412204435153.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM1OTQ2OTY5,size_16,color_FFFFFF,t_70)
  `规则`：限流规则、降级规则等。
  `Sentinel Client`：存在于每一个微服务系统的client，用于保存本系统的规则。
  `Nacos配置中心`：一个远程的，可存储数据的地方。
  

  理解了上述概念后，我们再来看Sentinel规则的创建方式，有如下三种:
  1.  通过API的FlowRuleManager.loadRules方法。
这种创建方式，是在某一系统启动的过程中，调用一个init方法，在改方法中我们硬编码了规则，故规则直接被加载在本地，也就是本地的Sentinel Client。
   **显然，该系统每一次重启都会重新加载规则。**
  
  2. Dashboard。  
在[之前](https://blog.csdn.net/qq_35946969/article/details/105351082)的示范中，我们知道可以直接在Dashboard页面去配置规则，这种方式显然是在Dashboard端修改规则后推送到了对应系统的本地Sentinel Client。
即： 
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200412210152275.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM1OTQ2OTY5,size_16,color_FFFFFF,t_70)
**显然，dashboard没有保存规则，一旦Order系统或Product系统重启，之前配置的规则就会丢失。**
3.  通过DataSource适配不同的数据源去修改，即规则持久化，这才是保证规则可动态更新且不丢失的解决方案，这里就贴一个官方的图吧，比我画的好看。。![在这里插入图片描述](https://img-blog.csdnimg.cn/20200412214202528.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM1OTQ2OTY5,size_16,color_FFFFFF,t_70)
 
##  二、Nacos配合Sentinel
  简述步骤：Nacos保存json格式的Sentinel规则，配置本地服务拉取Nacos中的规则。
  *正常来说应该是用代码生产规则的json，然后上传至Sentinel，不过我们知道json规则的话，自然也是可以直接去写文件的。*
  1. 配置pom.xml。

```yaml
 <!--1.7.2为当前最新版-->
        <dependency>
            <groupId>com.alibaba.csp</groupId>
            <artifactId>sentinel-datasource-nacos</artifactId>
            <version>1.7.2</version>
        </dependency>
```

  2. 配置[Order项目](https://github.com/flyChineseBoy/lel-mall/tree/master/mall05/mall)。
  
```bash
# sentinel nacos 相关配置
spring.cloud.sentinel.datasource.ds.nacos.server-addr=localhost:8848
spring.cloud.sentinel.datasource.ds.nacos.dataId=${spring.application.name}-sentinel
spring.cloud.sentinel.datasource.ds.nacos.groupId=mall:order
spring.cloud.sentinel.datasource.ds.nacos.rule-type=flow
```

  3. 在Nacos控制台增加配置规则，我这里对前文配置的/test方法进行限流，QPS方式限流为2,即1秒内只能访问2次。
  

```java
[
    {
        "resource": "/test",
        "limitApp": "default",
        "grade": 1,
        "count": 2,
        "strategy": 0,
        "controlBehavior": 0,
        "clusterMode": false
    }
]
```
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200412221224719.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM1OTQ2OTY5,size_16,color_FFFFFF,t_70)

	属性简单介绍：
  	resource：资源名，即限流规则的作用对象
	limitApp：流控针对的调用来源，若为 default 则不区分调用来源
	grade：限流阈值类型（QPS 或并发线程数）；0代表根据并发数量来限流，1代表根据QPS来进行流量控制
	count：限流阈值
	strategy：调用关系限流策略
	controlBehavior：流量控制效果（直接拒绝、Warm Up、匀速排队）
	clusterMode：是否为集群模式
	
  4. Order项目配置一个数据源init类，用于项目启动时加载Nacos配置中心存储的规则。
```java
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


```

5. 测试。在页面快速访问test接口：[http://localhost:8081/test?name=123](http://localhost:8081/test?name=123)，发现两次之后就会被堵塞降级。
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200412231157410.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM1OTQ2OTY5,size_16,color_FFFFFF,t_70)
再到dashboard查看一下/test资源的规则。
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200412231246470.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM1OTQ2OTY5,size_16,color_FFFFFF,t_70)
**规则已经从nacos配置中心显示到了sentinel dashboard控制台，持久化配置成功！**

## 三、Demo地址
[https://github.com/flyChineseBoy/lel-mall/tree/master/mall06](https://github.com/flyChineseBoy/lel-mall/tree/master/mall06)
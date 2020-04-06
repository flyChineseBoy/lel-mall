@[TOC]

## 一、Sentinel是什么？
  Sentinel是**分布式系统的流量防卫兵**！  
 
 什么意思呢？ 在分布式系统中，各个节点之间往往会存在调用关系。比如电商系统中，**订单节点A**需要调用**库存节点B**的减库存接口，但是在**库存节点B**挂掉了，或者**库存节点B**负载极高，短时间内无法response，就可能导致订单节点也跟着异常或停滞。
 
 为了防止这种，节点B挂掉（或停滞）导致节点A挂掉（或停滞），又进一步导致其他节点一同发生连锁反应，**Hystrix闪亮登场**。

  嗯……说好的Sentinel，Hystrix是个什么鬼？
   
   在SpringCloud的家族里面，Netflix公司开源的Hystrix才是老牌选手，具有“熔断、服务降级、近实时服务监控”的能力。Sentinel则是随着新生代（SpringCloud Alibaba）一同登场的“QPS可控、支撑近十年阿里巴巴双11”的强劲新人（[对两者区别有兴趣可以看看这里](https://www.cnblogs.com/zhyg/p/11474406.html)）。
   
   为了更好的理解Sentinel，我们先来看一下Sentinel最基本的功能——限流。

## 二、Sentinel Dashboard
  Sentinel可视化控制台，我们可以在上面看到已连接服务的一些监控信息。
  1. 下载Sentinel dashboard包。
  [https://github.com/alibaba/Sentinel/releases](https://github.com/alibaba/Sentinel/releases) 可见所有版本的jar包。
  2. 启动dashboard。
  java -Dserver.port=8080 -Dcsp.sentinel.dashboard.server=localhost:8080 -Dproject.name=sentinel-dashboard -jar sentinel-dashboard-1.7.1.jar
  ![在这里插入图片描述](https://img-blog.csdnimg.cn/20200406213054797.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM1OTQ2OTY5,size_16,color_FFFFFF,t_70)
  

3. 访问localhost:8080，用户名密码全部默认sentinel。
  ![在这里插入图片描述](https://img-blog.csdnimg.cn/20200406212951646.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM1OTQ2OTY5,size_16,color_FFFFFF,t_70)
  4. 进入dashboard，可以看到这里有某个服务的QPS数据（图中是Sentinel Dashboard自身的服务接口），左侧菜单栏里面可以配置流量控制规则、服务降级规则、热点数据规则。
  ![在这里插入图片描述](https://img-blog.csdnimg.cn/20200406214140565.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM1OTQ2OTY5,size_16,color_FFFFFF,t_70)
  
## 三、Sentinel 接入端
  dashboard是一个便捷的页面管理工具，真正的Sentinel功能还是依赖在业务项目之中，我们继续在之前的[demo](https://github.com/flyChineseBoy/lel-mall/tree/master/mall01/mall)基础上进行扩充。
  1.  添加依赖。
```yaml
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-sentinel</artifactId>
</dependency>
```
  2. 添加配置。（[上一章](https://blog.csdn.net/qq_35946969/article/details/105279770)我们介绍了可以在Nacos配置中心添加yml或properties配置，但是这里为了代码完整保存，仍然写在了bootstrap.properties中）。
  
```bash
spring.cloud.sentinel.transport.port=8121
spring.cloud.sentinel.transport.dashboard=localhost:8080
```

  3.  添加接口并配置Sentinel资源名称：test。
   

```java
package com.lele.mall.controller;/*
 * com.lele.mall.controller
 * @author: lele
 */

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @SentinelResource("test")
    @GetMapping(value = "/test")
    public String test(){
        return "test";
    }
}
```

  4. 启动application，页面访问测试接口（*注意：必须先访问过该接口，才能在dashboard上看到该接口的信息*）。
  ![在这里插入图片描述](https://img-blog.csdnimg.cn/20200406223342583.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM1OTQ2OTY5,size_16,color_FFFFFF,t_70)
  5.   查看dashboard，可以看到order项目已经被sentinel dashboard监控到了。我们可以直接在页面上配置流量限制（*注意：页面配置不是持久化的，之后章节我们会介绍sentinel规则持久化的方法*）。
  ![在这里插入图片描述](https://img-blog.csdnimg.cn/20200406223541753.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM1OTQ2OTY5,size_16,color_FFFFFF,t_70)
  6. 测试 流量限制，设置test接口的QPS 单机阈值为1.
  ![在这里插入图片描述](https://img-blog.csdnimg.cn/20200406223748172.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM1OTQ2OTY5,size_16,color_FFFFFF,t_70)
  7.  测试 流量限制，连续访问test接口两次，发现第二次接口不可达，测试成功。
  ![在这里插入图片描述](https://img-blog.csdnimg.cn/2020040622391747.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM1OTQ2OTY5,size_16,color_FFFFFF,t_70)
  ![在这里插入图片描述](https://img-blog.csdnimg.cn/20200406223932808.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM1OTQ2OTY5,size_16,color_FFFFFF,t_70)
## 四、热点规则/服务降级规则
  除了流量控制，sentinel还支持热点流量控制、服务降级规则配置等。
  
### 热点规则
   热点规则是对接口规则的更细化的控制，可以通过对参数进行流量控制，比如将test方法改造为带参数的方法（如下代码），然后配置热点规则的参数索引、单机阈值、统计窗口长度（如下图），则可以按照参数限制。
   *限制规则：按照索引下标选择限制的参数，如test接口限制第一个参数name，若该参数为某值的方法连续被调用n次（如name="asd"在窗口长度时间内调用超过单机阈值次数），则接下来的name="asd"的访问请求将无法访问。*
   

```java
@SentinelResource("test")
    @GetMapping(value = "/test")
    public String test(@RequestParam String name){
        return name;
    }
```
   ![在这里插入图片描述](https://img-blog.csdnimg.cn/20200406231240159.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM1OTQ2OTY5,size_16,color_FFFFFF,t_70)
### 降级规则
  降级规则则是从 服务响应时间、接口异常次数、接口异常频率来评估接口，若响应过慢，或异常次数过多，则对接口进行降级。
 
 如：test接口被限制为只要在3秒内异常2次就进行服务降级，测试代码及dashboard配置如下。
 

```java
@SentinelResource("test")
    @GetMapping(value = "/test")
    public String test(@RequestParam String name){
        throw new RuntimeException("asdasd");// 服务降级前页面会看到异常抛出asdasd，服务降级后不会有该信息
        //return name;
```

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200406231727561.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM1OTQ2OTY5,size_16,color_FFFFFF,t_70)
## 其他
**Sentinel的功能很强大，不只是可以对接口进行限制，还可以让超出限制的请求去调用本地的‘失败专用接口’。**

  对接口的限制是在保护节点不会被流量或是压力打死，而后续的‘失败专用接口’则是关乎用户体验的命脉，这一部分以及规则持久化我们下一章再来介绍。

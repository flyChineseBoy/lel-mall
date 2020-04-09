
## 零、系列
  欢迎来嫖**从零开始SpringCloud Alibaba电商系列**:
1. [从零开始SpringCloud Alibaba电商系统(一)——Alibaba与Nacos服务注册与发现](https://blog.csdn.net/qq_35946969/article/details/105188015)  
2. [从零开始SpringCloud Alibaba电商系统(二)——Nacos配置中心](https://blog.csdn.net/qq_35946969/article/details/105279770)  
3. [从零开始SpringCloud Alibaba电商系统(三)——Sentinel流量防卫兵介绍、流量控制demo](https://blog.csdn.net/qq_35946969/article/details/105351082)
4. [从零开始SpringCloud Alibaba电商系统(四)——Sentinel的fallback和blockHandler](https://blog.csdn.net/qq_35946969/article/details/105375003)
## 一、Feign
  [前文](https://blog.csdn.net/qq_35946969/article/details/105375003)我们介绍了Sentinel如何实现单节点上，某服务异常后调用‘替代方法’。那么在微服务场景下，将Feign和Ribbon代入后，Sentinel又是怎么玩的呢？

先来给不知道Feign和Ribbon的同学们扫个盲：

	  Feign：可以像在本地调用接口一样去调用其他节点的接口。
	    （如在订单服务调用库存服务的‘减库存方法’，只需要引入一个Serivce调用方法而不需要自己编写一坨HTTP请求的代码。）
	  
	  Ribbon：Ribbon是Feign的底层支持，主要负责负载均衡和HTTP请求。
	    这里负载均衡指的是：库存服务做了多个节点的负载，订单服务调用方法只需要调用其中一个库存服务节点即可完成逻辑，如何来选择哪个库存节点就是Ribbon的职责。

干说不易理解，来个例子，逻辑如上所述：*库存服务提供`减库存接口`，订单服务消费该接口。*
  
  1. 在服务提供端（即库存服务），增加`ProductController`和reduceInventory方法（代码基于[之前的product模块](https://github.com/flyChineseBoy/lel-mall/tree/master/mall01)改造）。

```java
package com.lele.mall.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/product")
@RestController
public class ProductController {

        @GetMapping("reduce_inventory")
        public String reduceInventory(){
            return "库存减一";
        }

}
```

  2. 消费端（即订单服务）则需要另外增加关于Feign的依赖，pom.xml添加如下：
  

```yaml
<dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-openfeign</artifactId>
            <version>2.1.2.RELEASE</version>
</dependency>
```
  3. 消费端application增加@EnableFeignClients注解
  

```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

```

  4. 消费端增加对应库存服务ProductController的FiegnClient接口，如下：
  

```java
package com.lele.mall.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "product")
public interface ProductService {

    @GetMapping("/product/reduce_inventory")
    public String reduceInventory();
}
```

  5. 最后，在消费端新增OrderController用来消费`减库存服务`，就是如此丝滑清爽的使用方式。
  

```java
package com.lele.mall.controller;/*
 * com.lele.mall.controller
 * @author: lele
 * @date: 2020-04-08
 */

import com.lele.mall.feignclient.ProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RequestMapping("/order")
@RestController
public class OrderController {

    // 像注入本地服务一样注入远端服务
    @Resource
    private ProductService productService;

    @GetMapping("apply")
    public String applyOrder(){
        return productService.reduceInventory();
    }

}
```

  5. 分别启动库存服务和订单服务，启动成功后，页面访问订单服务的 order/apply接口。
  **发现在order的apply服务中，成功调用到了库存服务的reduceInventory方法**。
  ![在这里插入图片描述](https://img-blog.csdnimg.cn/20200408232002136.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM1OTQ2OTY5,size_16,color_FFFFFF,t_70)
  
## 二、Sentinel集成Feign实现多节点间的熔断，保护节点雪崩式故障
  通过上面Feign的Demo我们提出两个问题：
	
		1. 订单服务调用库存服务某个接口的时候，有库存服务宕机了/网络故障了/库存服务很忙处理不过来等问题时，
		   订单服务该怎么办？一直等肯定是不合适的。
		2. 订单服务调用库存服务时，库存服务总是返回非业务异常，订单服务只能一直跟着异常吗？

   Sentinel可以很好的解决上述两个问题，网络故障是吧？那我就网络故障的时候直接服务降级，降级为本节点的某个方法总不会有问题。
   库存服务异常超过一定次数或频率（又或是库存服务响应时间过长）时，我们也可以先进行服务降级，然后再观察库存服务节点状态是否好转。
  
   **基于以上两点，我们给出两个案例：库存服务宕机时，订单服务进行降级；库存服务异常次数过多时，订单服务进行降级。**
   
1. 消费端配置feign启用sentinel，配置文件添加(可以在nacos配置中心添加，我这里为了demo易调试放在了bootstrap.peoperties)：

```yaml
	feign.sentinel.enabled=true
```

2.  消费端的FiegnClient接口配置FallBack函数，以帮助我们在调用库存服务失败时能回调本地服务。

```java
package com.lele.mall.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;


// name与服务名保持一致
@FeignClient(name = "product",fallback = ProductServiceFallback.class,configuration = ProductServiceFallback.class)
public interface ProductService {

    @GetMapping("/product/reduce_inventory")
    public String reduceInventory();
}

// product的降级服务类
class ProductServiceFallback implements ProductService{

    public String reduceInventory(){
        return "订单模块的库存减一";
    };
}
```

3. 停掉库存服务系统，模拟宕机。

4. 访问订单模块: [http://localhost:8081/order/apply](http://localhost:8081/order/apply) 。
 	**发现页面输出如下，熔断成功！**
 	![在这里插入图片描述](https://img-blog.csdnimg.cn/2020040922040115.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM1OTQ2OTY5,size_16,color_FFFFFF,t_70)

**然后来看第二个案例：**
 
 5.  启动库存模块。

6. 启动sentinel dashboard，不清楚dashboard是什么的同学可以到[前前文](https://blog.csdn.net/qq_35946969/article/details/105351082)lou一眼。

7. 为库存模块配置sentinel相关pom、配置文件、@SentinelResource。（详细介绍可见[前前文](https://blog.csdn.net/qq_35946969/article/details/105351082)）

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200409221612694.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM1OTQ2OTY5,size_16,color_FFFFFF,t_70)

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200409221945878.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM1OTQ2OTY5,size_16,color_FFFFFF,t_70)
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200409221917894.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM1OTQ2OTY5,size_16,color_FFFFFF,t_70)
8.  在dashboard找到库存服务模块，找到库存服务模块的reduceInventory接口并对其设置流量限制。
*tips：需要先访问一次reduceInventory接口才可以在dashboard上面看到该模块。*

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200409222245220.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM1OTQ2OTY5,size_16,color_FFFFFF,t_70)
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200409222307371.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM1OTQ2OTY5,size_16,color_FFFFFF,t_70)

9. 连续多次访问订单模块： [http://localhost:8081/order/apply](http://localhost:8081/order/apply)
 
 **前两次显示为`库存减一`，1s内之后的其他访问就变成了`订单模块的库存减一`，测试成功！**![在这里插入图片描述](https://img-blog.csdnimg.cn/20200409222445948.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM1OTQ2OTY5,size_16,color_FFFFFF,t_70)
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200409222456734.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM1OTQ2OTY5,size_16,color_FFFFFF,t_70)

## 三、Demo地址
  ## 六、demo 地址
 *注意：如果不使用nacos请去除pom中nacos相关依赖、将bootstrap.properties变更为application.properties并添加server.port属性。*  

 [https://github.com/flyChineseBoy/lel-mall/tree/master/mall05](https://github.com/flyChineseBoy/lel-mall/tree/master/mall03)

  这里正在完善一个从零开始的基于SpringCloud Alibaba的电商系统，有兴趣就来点个star吧！

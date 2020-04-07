
## 零、系列
  欢迎来嫖**从零开始SpringCloud Alibaba电商系列**:
1. [从零开始SpringCloud Alibaba电商系统(一)——Alibaba与Nacos服务注册与发现](https://blog.csdn.net/qq_35946969/article/details/105188015)  
2. [从零开始SpringCloud Alibaba电商系统(二)——Nacos配置中心](https://blog.csdn.net/qq_35946969/article/details/105279770)  
3. [从零开始SpringCloud Alibaba电商系统(三)——Sentinel流量防卫兵介绍、流量控制demo](https://blog.csdn.net/qq_35946969/article/details/105351082)

## 一、什么是fallback和blockHandler？
  前文（[从零开始SpringCloud Alibaba电商系统(三)——Sentinel流量防卫兵介绍、流量控制demo](https://blog.csdn.net/qq_35946969/article/details/105351082)）我们介绍了Sentinel的限流、服务降级功能，但是只是限制肯定是不够了，我们还要保证调用这些**被限制服务**的调用者，让他们拿到一个合理的结果，而不是扔回去一个异常就完事了。
 
 Sentinel提供了这样的功能，让我们可以另外定义一个方法来代替**被限制或异常服务**返回数据，这就是fallback和blockHandler。

	fallback：失败调用，若本接口出现未知异常，则调用fallback指定的接口。

	blockHandler：sentinel定义的失败调用或限制调用，若本次访问被限流或服务降级，则调用blockHandler指定的接口。
	
  干说不易理解，让我们用demo来说话。

## 二、fallback
  1. 接着我们之前的[demo](https://github.com/flyChineseBoy/lel-mall/tree/master/mall03)来加代码，编写TestController，为test方法的@SentinelResource注解增加fallback属性。
 *这里直接让test接口抛出异常，测试f是否会用fallback指定的接口来替代。*
```java
package com.lele.mall.controller;/*
 * com.lele.mall.controller
 * @author: lele
 */

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class TestController {
	// fallbackClass为异常是调用的方法所属的类，fallback为fallbackClass类中的方法。
    @SentinelResource(value="test",fallback = "testFallback",fallbackClass = TestController.class)
    @GetMapping(value = "/test")
    public String test(@RequestParam String name){
        throw new RuntimeException("asdasd");
        //return name;
    }

    @GetMapping(value = "/testFallback")
    public String testFallback(@RequestParam String name){
        return "testFallback"+name;
    }
}
```
2. 访问test接口：[localhost:8081/test?name=123](localhost:8081/test?name=123)，发现返回结果不是500而是testFallback函数的返回值，测试成功！
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200407222242353.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM1OTQ2OTY5,size_16,color_FFFFFF,t_70)

## 三、blockHandler
  *测试超出流量限制的部分是否会进入到blockHandler的方法。*
  
1.  为test方法的@SentinelResource注解增加blockHandler属性，并增加blockHandler指向的方法。

```java
package com.lele.mall.controller;/*
 * com.lele.mall.controller
 * @author: lele
 */

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class TestController {

    @SentinelResource(value="test",fallback = "testFallback",fallbackClass = TestController.class,blockHandler = "testBlockHandler",blockHandlerClass = TestController.class)
    @GetMapping(value = "/test")
    public String test(@RequestParam String name){
        throw new RuntimeException("asdasd");
        //return name;
    }

    @GetMapping(value = "/testFallback")
    public String testFallback(@RequestParam String name){
        return "testFallback"+name;
    }

    @GetMapping(value = "/testBlockHandler")
    public String testBlockHandler(@RequestParam String name,BlockException ex){
        return "testBlockHandler"+name;
    }
}
```


2. 如[前文](https://blog.csdn.net/qq_35946969/article/details/105351082)一样，我们在sentinel dashboard设置流量控制，QPS阈值为1。
发现快速访问两次，第一次调用到了fallback（因为我们的test方法是直接抛出异常，所以第一次会用fallback替代），第二次调用到了blockHandler。
*测试成功！证明流量超限后会调用到blockHandler。*

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200407223733605.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM1OTQ2OTY5,size_16,color_FFFFFF,t_70)![在这里插入图片描述](https://img-blog.csdnimg.cn/20200407223720869.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM1OTQ2OTY5,size_16,color_FFFFFF,t_70)


## 四、服务降级（应用场景）
  如上面的测试结果，当超出流量限制或服务降级规则后，我们的**服务请求会被降级为blockHandler方法**，这就是服务降级。
  再来举一个应用场景更形象的理解*服务降级*这四个字：
  电商系统在查询商品是有许多排序规则的，我们一般请求都会请求到带有排序规则的商品列表，但是如果某一时刻流量猛增，该接口响应时间过慢，就可以进行服务降级，调用降级接口，返回不排序的商品列表。
  
  **服务降级可以是连续的，即降级一次慢，那就再降。
  一般在微服务系统中，各个微服务之间互相调用，有时候可能网络故障不通，这时候就可以通过Sentinel+Feign来实现较好的熔断机制，这部分我们下次再聊。**
  
##  五、demo地址
  上述代码我已上传至github/gitee，对[从零开始SpringCloud Alibaba电商系统系列]感兴趣的朋友可以点个star。
  [https://github.com/flyChineseBoy/lel-mall/tree/master/mall04](https://github.com/flyChineseBoy/lel-mall/tree/master/mall04)
	
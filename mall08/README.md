   Befor All：按照本来计划，这一次应该继续spring security，来做SSO的部分，但是想到我们之前都是url访问各个接口实在过于不便且ugly，故本次我们集成swagger、swagger-bootstrap-ui做一个**界面好看点儿的api接口文档及接口测试工具**。


@[TOC]
## 零、系列
  欢迎来嫖**从零开始SpringCloud Alibaba电商系列**:
1. [从零开始SpringCloud Alibaba电商系统(一)——Alibaba与Nacos服务注册与发现](https://blog.csdn.net/qq_35946969/article/details/105188015)  
2. [从零开始SpringCloud Alibaba电商系统(二)——Nacos配置中心](https://blog.csdn.net/qq_35946969/article/details/105279770)  
3. [从零开始SpringCloud Alibaba电商系统(三)——Sentinel流量防卫兵介绍、流量控制demo](https://blog.csdn.net/qq_35946969/article/details/105351082)
4. [从零开始SpringCloud Alibaba电商系统(四)——Sentinel的fallback和blockHandler](https://blog.csdn.net/qq_35946969/article/details/105375003)
5.  [从零开始SpringCloud Alibaba电商系统(五)——Feign Demo，Sentinel+Feign实现多节点间熔断/服务降级](https://blog.csdn.net/qq_35946969/article/details/105397473)
6. [从零开始SpringCloud Alibaba电商系统(六)——Sentinel规则持久化到Nacos配置中心](https://blog.csdn.net/qq_35946969/article/details/105475307)
7. [从零开始SpringCloud Alibaba电商系统(七)——Spring Security实现登录认证、权限控制](https://blog.csdn.net/qq_35946969/article/details/105605650)


## 二、Swagger是什么？
   诚如一开始所说，swagger是一个接口文档，它可以将我们的所有Controller和它们的方法列在页面上，让我们可以不必自己再手写接口文档，当然Swagger也可以让我们为Controller方法们加上描述，让方法的含义展示更清晰。
   
   swagger还可以部分代替postman，我们可以直接在swagger提供的界面上像使用postman一样对本项目的接口发送请求。

  来一个例图：
  ![在这里插入图片描述](https://img-blog.csdnimg.cn/20200420221454750.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM1OTQ2OTY5,size_16,color_FFFFFF,t_70)
  *这是swagger自带的ui界面，有点儿ugly，稍后我们在实操中换成一套较为好看的swagger-bootstrap-ui页面。*


## 三、Springboo集成Swagger
  1.  在[上次demo](https://github.com/flyChineseBoy/lel-mall/tree/master/mall07)引入maven依赖，**使用demo请配置可用的nacos和sentinel dashboard或屏蔽掉**。

```yaml
 <!--Swagger-UI API文档生产工具-->
        <dependency>
            <groupId>io.springfox</groupId>
            <artifactId>springfox-swagger2</artifactId>
            <version>2.7.0</version>
        </dependency>
        
        <!-- 一个好看的swagger-ui界面-->
        <dependency>
            <groupId>com.github.xiaoymin</groupId>
            <artifactId>swagger-bootstrap-ui</artifactId>
            <version>1.8.1</version>
        </dependency>
```

  2. 使用spring的Java Config配置Swagger相关配置，主要是两方面：Swagger基本配置、页面样式配置。

```java
package com.lele.mall.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Swagger2Config implements WebMvcConfigurer {
    @Bean
    public Docket createRestApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.lele.mall.controller")) // 为controller生成文档
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("电商系统文档")
                .version("1.0")
                .build();
    }

    // 页面样式替换为swagger-bootstrap-ui
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}
```

  3. Controller都增加@Api注解并增加tags属性用于描述。
	
```java
@Api(tags = "订单服务")
@RequestMapping("/order")
@RestController
public class OrderController {

```

  4. 为/test，/order/apply等接口方法增加ApiOperation并增加描述，如：

```java
     @ApiOperation("通过并执行订单")
    @PreAuthorize("hasRole('ROLE_ADMIN')") // super是我们在MyUserDetailsService中赋予admin用户的。
    @GetMapping("apply")
    public String applyOrder(){
        return productService.reduceInventory();
    }
```
  5.  启动项目，访问 [http://localhost:8081/doc.html](http://localhost:8081/doc.html)。
首页，是不是比默认的样式好看多了！
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200420225751518.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM1OTQ2OTY5,size_16,color_FFFFFF,t_70)
细节：
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200420230040724.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM1OTQ2OTY5,size_16,color_FFFFFF,t_70)
  6. 测试`接口调试`功能，随便打开一个接口，访问。
  ![在这里插入图片描述](https://img-blog.csdnimg.cn/20200420232557134.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM1OTQ2OTY5,size_16,color_FFFFFF,t_70)
  **成功得到响应，比postman丝滑多了。**

## 四、Swagger常用注解
- @Api()用于类；
表示标识这个类是swagger的资源。
	属性：tags，表示说明。
- @ApiOperation()用于方法；
表示一个接口方法。
	属性：value用于方法描述，notes用于提示内容。
	
- @ApiParam()用于方法，参数，字段说明；
对参数的添加元数据。
属性：name–参数名,value–参数说明,required–是否必填。

- @ApiModel()用于类
 表示对类进行说明，用于参数用实体类接收。
 
- @ApiModelProperty()用于方法，字段
对实体类中字段的描述。
value–字段说明
name–重写属性名字
dataType–重写属性类型
required–是否必填
example–举例说明
hidden–隐藏



## 五、demo地址
[https://github.com/flyChineseBoy/lel-mall/tree/master/mall08](https://github.com/flyChineseBoy/lel-mall/tree/master/mall07)

下一节，我们继续来看Spring Security分布式场景下的应用。

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
8. [从零开始SpringCloud Alibaba电商系统(八)——用一个好看的Swagger接口文档](https://blog.csdn.net/qq_35946969/article/details/105645950)
9. [从零开始SpringCloud Alibaba电商系统(九)——基于Spring Security OAuth2实现SSO-认证服务器（非JWT）](https://blog.csdn.net/qq_35946969/article/details/105695243)

## 一、 概述
 	 前文我们考虑了OAuth2的鉴权方式，但是作为B2C电商系统来说，它的功能有些超出需求范围。
   **OAuth2的目的是认证中心要不要给服务提供者授权。**

  	 而我们当前权限设计所需要的是：
  **能让多子系统共享用户状态，拥有相同的鉴权模式。**

基于以上考虑，本文将尝试采用redis做session保存用户信息，在各个子服务系统复用相同的认证鉴权，并依然借用spring security的权限控制功能，*基本的系统角色有如下三种：*
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200501111024343.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM1OTQ2OTY5,size_16,color_FFFFFF,t_70)
基本登录流程如下：
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200501111818132.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM1OTQ2OTY5,size_16,color_FFFFFF,t_70)
当然，我们还需要考虑角色对资源的权限，通过spring security来实现接口的访问限制，通过用户-角色-资源等库表来实现资源粒度，表结构如下：
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200501170314790.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM1OTQ2OTY5,size_16,color_FFFFFF,t_70)
**基本了解了概念，接下来我们就从spring security配置redis session开始，实现一个可用的权限认证功能。**

## 二、如何整合Redis做Session
 	有一些方式可以选择：
 1. spring security原本是使用concurrentHashMap来做session的，我们只需要将session的相关实现类替换成redis为数据存储结构  。SessionRegistryImpl是我们需要覆盖重写的类，然后再spring security配置时将我们的实现类注册进去。
 2. 直接使用Spring-Session-Redis，**乐乐十分推荐第二种方式，毕竟都是自己做的框架，用起来就是两个字——丝滑。**

## 三、准备环境
  Redis Server   一台。
  本地代码  一堆：乐乐这里用的是父子项目。
     
     mall是最外层父模块。
     common为所有子系统的依赖模块，认证逻辑会写在这里，这个模块不会作为微服务中的一个来单独部署，下面只是为了方便演示。
     user为一个子系统模块，依赖common。
     暂时就用一个common，一个user来演示两个客户端。
     
  
## 四、实现流程
 数据库表放在了flywaydb文件中，会随项目启动自行创建到数据库中。其他相关的配置文件不再赘述，有需要的同学可以直接去底部拿demo。
  ### common模块
   主要的认证逻辑集中在common模块，然后其他子系统都会引用common的代码。

   1.  使用上述表结构，实现我们的自定义UserDetails，这里的mapper由逆向工程实现。
    **具体的sql和代码放在最下方demo中。**
   2. 为父子模块配置依赖，如果在自己项目中，已经有security的话可以只引入spring-session-redis。

```yaml
 <dependency>
            <groupId>org.springframework.session</groupId>
            <artifactId>spring-session-data-redis</artifactId>
  </dependency>
```

   3.  配置文件，application.properties的内容主要是redis相关（在spring security配置健全的前提下），**其中redis.xxx**的配置为笔者自定义。

```yaml
redis.host=localhost
redis.port=6379
session.redis.timeout=60000
spring.session.store-type=redis
server.servlet.session.timeout=120
spring.session.redis.flush-mode=on-save
spring.session.redis.namespace=spring:session
```

   4. 配置Redis的相关Bean，需要一个Java客户端，这里使用Lettuce。
**这里使用的redis.XXX就是前一步配置文件中配置的**。
```java
@Configuration
public class RedisConfig  {

    @Value("${redis.host}")
    private String redisServer;
    @Value("${redis.port}")
    private Integer redisPort;
    @Value("${redis.password}")
    private String redisPassword;

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(redisServer, redisPort);
        redisStandaloneConfiguration.setPassword(redisPassword);

        return new LettuceConnectionFactory(redisStandaloneConfiguration);
    }
}

```

   5. 使用RBAC数据库结构实现UserDetails。
		笔者这里使用了easycode+mybatisplus逆向生成了这些表的代码，不想折腾的同学可以直达底部。
		唯一个人写的就是MPermissionDao.selectPermissionByUserId方法，用来查询该用户所拥有的所有资源权限。
		![在这里插入图片描述](https://img-blog.csdnimg.cn/20200505001727410.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM1OTQ2OTY5,size_16,color_FFFFFF,t_70)

   6.  这里只是在user表插入了一条用户名密码都为root的数据，用来进行redis session的流程，**当然密码需要加密，笔者在单元测试方法写了一些插入数据时加密密码的测试方法，可以参考**。
   ![在这里插入图片描述](https://img-blog.csdnimg.cn/20200505001833380.png)

```java
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserMapperTest {

    @Autowired
    private MUserDao mUserDao;
	// Bean来自WebSecurityConfiguration
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Test
    public void testInsert(){
        MUser user = MUser.builder().username("root").password(bCryptPasswordEncoder.encode("root")).build();
        mUserDao.insert(user);
    }
}
```

**到此为止，common模块已经可以测试使用了，单模块测试，我们跑一下。**
7. 启动common的application，root/root登录。
	![在这里插入图片描述](https://img-blog.csdnimg.cn/20200505002224357.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM1OTQ2OTY5,size_16,color_FFFFFF,t_70)
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200505002339582.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM1OTQ2OTY5,size_16,color_FFFFFF,t_70)

查看我们的redis库是否有了session数据，成功！内容我们就不分析了，名字叙述的很清晰。

![在这里插入图片描述](https://img-blog.csdnimg.cn/2020050500240390.png)

### User模块

1. 新建user子模块之前，需要对common模块做一个小小的改动，那就是增加spring.factories，否则启动user模块时，common模块的配置类是不会被加载的。
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200505002635803.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM1OTQ2OTY5,size_16,color_FFFFFF,t_70)

2. 新建user模块，直接新建一个springboot项目即可，将parent改为mall。然后依赖只需要添加common，这个字数比较少直接贴上来。

```yaml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>mall</artifactId>
        <groupId>org.lele</groupId>
        <version>0.1</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>
    <packaging>jar</packaging>
    <artifactId>user</artifactId>

    <dependencies>
    <dependency>
        <groupId>org.lele</groupId>
        <artifactId>common</artifactId>
        <version>0.1</version>
    </dependency>
</dependencies>
</project>
```
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200505003003354.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM1OTQ2OTY5,size_16,color_FFFFFF,t_70)

3. 按照common的配置，配置一份user的，在本节demo中，需要修改的有服务名、服务端口、flywaydb的名称（不用也可以不改）。
*不想使用nacos配置中心的同学可以直接看下面贴的这一坨*
```yaml
spring.application.name=user
server.port=8083
spring.cloud.nacos.discovery.server-addr=localhost:8848
management.endpoints.web.exposure.include=*
#spring.cloud.sentinel.enabled=false
#spring.cloud.sentinel.transport.port=8121
#spring.cloud.sentinel.transport.dashboard=localhost:8080
spring.datasource.type=com.alibaba.druid.pool.DruidDataSource
spring.datasource.url= jdbc:mysql://localhost:3306/mall_user?useSSL=false&useUnicode=true&characterEncoding=UTF-8
spring.datasource.driver-class-name= com.mysql.jdbc.Driver
spring.datasource.username= root
spring.datasource.password= root
spring.datasource.druid.initial-size= 5
spring.datasource.druid.max-active=5
spring.datasource.druid.max-wait=10000
# flyway enable
spring.flyway.enabled=true
spring.flyway.baseline-on-migrate=true
spring.flyway.locations=classpath:db/migration
spring.flyway.table=common_version
spring.flyway.baseline-version=0
spring.flyway.encoding=UTF-8
spring.flyway.validate-on-migrate=false
spring.flyway.placeholder-prefix=##(
spring.flyway.placeholder-suffix=)
#mybatis-plus
mybatis-plus.mapper-locations= classpath:/mapper/**.xml
#redis
redis.host=localhost
redis.port=6379
redis.password=redisPassword
session.redis.timeout=60000
spring.session.store-type=redis
server.servlet.session.timeout=120
spring.session.redis.flush-mode=on-save
spring.session.redis.namespace=spring:session
```

4. user模块配置完毕，启动application。
  访问 localhost:8083
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200505003317931.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM1OTQ2OTY5,size_16,color_FFFFFF,t_70)
可以不用登陆直接访问到user模块的服务，这是因为之前common也就是8082服务已经将seesion注册到redis中了。*当然，换一个（或重启）浏览器肯定还是要重新登录的。*


## 五、demo
 至此，spring security配置完毕，权限系统也可以直接使用了，数据可以自己添加，最好是配合一个后端管理系统来管理资源权限，当然这个乐乐（笔者）之后也会慢慢做出来的。
[https://github.com/flyChineseBoy/lel-mall/tree/master/mall10](https://github.com/flyChineseBoy/lel-mall/tree/master/mall10)
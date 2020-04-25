
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


## 一、概念
#### 基于Cookie和Session的会话机制
  介绍OAuth2之前，让我们先来回顾一下早期的登录认证流程。
  远古时期，互联网上还只是一个共享的地方，没有`谁不能看什么`这样的概念；随着时代发展，互联网引用丰富，基于商业和隐私，人们对登录认证有了需求，但是大家都知道HTTP本身是无状态的协议，是没办法让HTTP来识别用户身份的。
  
  于是在上古时期，HTTP引入了**Authorization请求头**，用户可以将自己的用户名密码放入到这个请求头中，服务端收到一条请求时先解析Authorization请求头，确认用户身份。
  
  中古时期，人们觉得HTTP请求头在自己内网用用还行，放在互联网上太危险了，毕竟用户名密码就在HTTP的请求头里面放着，随便谁拦截了都能看的清清楚楚，都能copy你的信息冒充你来请求资源。
  
  于是大家都开始自己搞认证，而*自己搞*最常用的方式便是**基于Form表单的自定义认证方式**，毕竟Form表单中的数据属于`自定义请求数据`，而Aithorization属于HTTP的标准`请求头`。Form表单认证，基本上就等同于**基于Cookie和Session的会话机制**。
    
   典型的会话机制如下图所示，服务器端维护一个会session，客户端持有这个session的id，这样只要你登录一次，服务器端就知道你是谁了。大致流程是这样的：
  ![在这里插入图片描述](https://img-blog.csdnimg.cn/20200422230940536.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM1OTQ2OTY5,size_16,color_FFFFFF,t_70)
越过千年的羁绊，再看现在，由于当下分布式、微服务的常规化，大部分企业都已经不再是单体应用服务器，那么Session的使用便成为一个问题。

SSO就是解决这一类问题的方案的统称，俗称**单点登录**，即对于用户来说，登录系统就是登录了，我不需要在乎你里面有几个子系统，内部复杂度自己搞定。

#### JWT
众多解决方案之中，有一个亮眼的存在——**JWT（JSON Web Token）**。

  JWT不是基于Session工作的，而是将**用户信息使用一种加密方式全都存放在cookies**中，这样的方式显然能处理分布式系统的登录问题，因为只要持有JWT令牌，你愿意访问哪个子系统就访问哪个子系统，你愿意何时访问就何时访问。
  
  但是问题在于这个*随意*，令牌就像央行发出去的钞票，太难控制了，服务器系统不知道是谁拿着令牌，务器端不进行保存/管理，即jwt令牌一旦发放，其销毁、续期、登出等功能将很难控制。

话无绝对，人总是聪明的，解决方法总是有的，有兴趣的朋友可以多了解一下jwt，现在它还是很流行的：[https://jwt.io/introduction/](https://jwt.io/introduction/)。



#### OAuth2
我们今天的主角是OAuth2：OAuth2是一种**可用于**实现SSO单点登录的一种协议。
>OAuth 2.0关注客户端开发者的简易性。要么通过组织在资源拥有者和HTTP服务商之间的被批准的交互动作代表用户，要么允许第三方应用代表用户获得访问的权限。同时为Web应用，桌面应用和手机，和起居室设备提供专门的认证流程。             ————— 百度百科

> OAuth2协议的官方文档：[https://tools.ietf.org/html/rfc6749](https://tools.ietf.org/html/rfc6749)

综(说)上（句）所（人）述（话），OAuth2(`授权码模式，我们只关注这个`)的核心就是：服务器就负责提供服务，用户端就负责消费服务，认证和授权的事情都交给认证服务器，资源服务器就只提供资源，简要思想见下图。

  这个OAuth2怎么好用了呢？让我们来用Session作详细描述（首先让我们承认直接使用Session比使用JWT这样方法出去的令牌好简单方便，对用户更友好）。

  1. 用户需要访问工单系统，需要登录，于是发送用户密码到工单系统。
  2. 工单系统不直接鉴权，而是将用户请求、自己的回调地址都转发给认证中心。
  3. 认证中心知道用户要登录，于是给用户一个登录页面，让用户输用户密码。
  4. 认证中心拿到用户密码进行鉴权，认证通过，ok，我认证中心建立一个`全局Session`，代表这个用户在我这个系统已经登陆了，然后，**由于此时认证中心还持有工单系统的回调地址**，所有可以直接发送给工单系统一个`授权码`。
  5.  工单系统拿到`授权码`，再次通过`授权码`向工单系统请求`授权令牌`。
  6. 认证中心校验`授权码`，ok没问题，给你工单系统一个令牌。
  7. 工单系统拿到令牌，就有权限访问`资源服务器`上的相关资源了。同时工单系统与用户建立了`局部Session`，两者可以顺畅交互。
  
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200423222307748.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM1OTQ2OTY5,size_16,color_FFFFFF,t_70)
	大家会发现，感觉好像和JWT这种直接将令牌发给用户的方式有些类似，确实类似，类似在**令牌由认证中心授权给工单系统（俗称client系统），而非直接给用户。** client系统拥有资源访问权限，且与用户建立`局部Session`后，就可以实现一个**用户端的无Token访问**，即用户不需要再持有Token令牌了，对于我用户来说，还和单机时代一样，持有个`局部Session id`就行。

   工单系统解决了认证问题，那运维系统呢？按理来说对于我用户来说已经登录了，点开运维系统应该不用登陆了，这才符合SSO单点登录原则嘛！

   对于新的要访问的子系统，OAuth2是这样做的：
   
  8.  用户对运维系统发起请求。
  9. 运维系统同样将请求、自身回调地址转发给认证中心。
  10. 认证中心发现该用户的`全局Session`存在，不再鉴权，直接给运维系统一个`授权码`。
  11. 运维系统拿到`授权码`，向认证中心请求`授权令牌`。
  12. 认证中心校验`授权码`，校验没问题，返回`授权令牌`。
  13. 运维系统拿到`授权令牌`，可以访问资源服务器了，同时与用户建立`局部Session`。
 
   细心的同学可能发现了，运维系统与工单系统不同的地方仅仅在于，运维系统的鉴权直接由认证中心完成，没有再让用户来一次*登录操作*。除此之外，其他操作都一模一样。




## 二、OAuth2认证服务器搭建
 1. 首先来看一下我们的目录结构，本次不再与之前系列代码的基础上做文章，重新建了一个maven父子项目，目前主要两个子项目：**认证中心，公共服务（本次示例中可以当做一个子系统来理解）**。
	
		同时对alibaba、nacos、sentinel、feign、oauth2、flywaydb、mybatis-plus等pom进行一次集合。
		Nacos远端的配置文件内容copy在了resource文件夹下，一见便知，可直接更改地址使用。
		内心OS：依赖地狱真的可怕……
	 ![在这里插入图片描述](https://img-blog.csdnimg.cn/20200425180818823.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM1OTQ2OTY5,size_16,color_FFFFFF,t_70)

  2. 其次，我们采用认证数据持久化的方式搭建认证服务器。
乐乐在[authorization模块]()中加入mysql驱动和flywaydb(mysql版本管理工具)，有需要的同学可以直接下载本节源码，配置好mysql连接，启动项目即可得到一个完整的数据库环境。
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200425181151459.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM1OTQ2OTY5,size_16,color_FFFFFF,t_70)

    3.  库表说明。
  目前我们主要需要关注oauth_client_details表，这是配置`子系统信息`的地方，如client_id-`子系统识别id`，client_secret-`相当于子系统请求认证中心所需要的密码`，authorized_grant_types-`授权类型，我们只关注授权码模式`，web_server_redirect_uri-`子系统回调地址`。
  完整详细的认证端库表说明：[http://www.andaily.com/spring-oauth-server/db_table_description.html](http://www.andaily.com/spring-oauth-server/db_table_description.html)
  
    4. 子系统信息填入。
      我们将common作为一个client，填入oauth_client_details，之后我们就可以在common子系统中通过这个client_id进行用户信息请求了，**下面我们先只是演示认证服务器如何使用，下节在介绍认证服务器和资源服务器的配合。**
      ![在这里插入图片描述](https://img-blog.csdnimg.cn/20200425233123446.png)

    5. 认证服务器配置，主要是为了让认证服务器如何知道来访的客户端是谁。
```java
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

    // 使用druid连接池。
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DruidDataSource dataSourceConfig()  {
        DruidDataSource ds = new DruidDataSource();
        return ds;
    }

    @Bean
    public TokenStore tokenStore() {
        // 保存令牌
        return new JdbcTokenStore(dataSourceConfig());
    }

    @Bean
    public ClientDetailsService jdbcClientDetails() {
        // 获取client子系统数据
        return new JdbcClientDetailsService(dataSourceConfig());
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        // 设置令牌
        endpoints.tokenStore(tokenStore());
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(jdbcClientDetails());
    }
}
```
 6.  配置可认证的用户，本次先配置一个内存中的用户。
 

```java
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        // 设置默认的加密方式
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.inMemoryAuthentication()
                .withUser("admin").password(passwordEncoder().encode("123456")).roles("USER");

    }
}
```

 7. 启动项目。模拟common子系统发起认证请求：[http://localhost:8081/oauth/authorize?client_id=common&response_type=code](http://localhost:8081/oauth/authorize?client_id=common&response_type=code)
 
   登录之后会看到：
 ![在这里插入图片描述](https://img-blog.csdnimg.cn/20200425233939513.png)

 8.  这是在问用户，是否同意认证中心将数据访问授权给common子系统。
 选择Approve，认证，可以看到页面自动跳转了一个连接，就是我们在oauth_client_details里填的web_server_redirect_uri，同时在地址后面附加了一个code，这个code就是之前讲的授权码。
 即，**common客户端获取到这个code，再去请求一下token令牌，就拥有权限访问认证中心那边的资源了，然后与用户建立局部会话。**
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200425234417946.png)


## 三、Demo
[https://github.com/flyChineseBoy/lel-mall/tree/master/mall09](https://github.com/flyChineseBoy/lel-mall/tree/master/mall09)

 本节内容主要讲解了如何使用spring security搭建一个OAuth2.0认证中心，下一次我们将同步完善用户与权限，以及客户端与认证服务器交互的功能。

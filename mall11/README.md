# 从零开始SpringCloud Alibaba电商系统(十一)——spring security完善之动态url控制
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
10. [从零开始SpringCloud Alibaba电商系统(十)——基于Redis Session的认证鉴权](https://blog.csdn.net/qq_35946969/article/details/105875278)

## 一、动态url控制
  前几章，我们完善了权限系统的SSO登录认证以及redis共享session获取，但是对于一个权限要求灵活的后台管理系统来说还不够。
  
  回想一下，我们之前是怎么控制一个接口的权限的：

1. 在controller接口的脑袋上定义@PreAuthorize注解并声明访问规则。

```java
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("apply02")
    public String applyOrder02(){
        return "权限专用测试";
    }
```

2.  在spring security的配置类--configure(http)方法中配置url以及访问规则，http配置url访问规则的方式很灵活，下面只是举个例子。

```java
			http .authorizeRequests()
                    .antMatchers("/test/*","/order/*")
                    .hasAnyRole("admin")
```

可以发现一个问题，url的权限控制要么一个个硬编码在接口脑袋上，要么一个个硬编码在配置类，有没有什么方法可以让我们使用数据库来存储这些权限规则并动态的应用上呢？

	这就是我们今天第一个要解决的问题，实际上网上有很多相关资料，但是基本上都是一样的，都是通过增加
	一个spring security的过滤链中增加一个Filter，然后自定义一个权限决定器AccessDecisionManager和一个
	权限数据加载器FilterInvocationSecurityMetadataSource，我觉得这种方式太过麻烦，于是自己想了一种
	方法来更简单灵活的配置我们的权限系统。

  AccessDecisionManager核心的方法是decide，它传入用户信息auth和当前访问的request进行一些`决策`来决定当前用户是否可以访问当前资源。它一般的策略是通过权限加载器获得一些数据，然后根据这些数据进行一票通过、一票否决、多数通过的策略等，最后的结果是返回`是否有权访问`。*有兴趣的同学可以goole/百度一下具体的使用和原理。*

   **而我直接将decide的方法换了一个定义：你已经给了我用户的权限信息和当前访问的url，我完全可以直接在这里根据用户的权限（在之前设计中用户的权限集合存放的就是资源的url）和url做一个比对，存在即可通过。**
   **这种方法可以极大的减少代码量，只需要重写一个AccessDecisionManager，然后将其通过http配置一下就可以了。**
	

## 二、具体实现
  1. 在[上文]((https://blog.csdn.net/qq_35946969/article/details/105875278))的基础上，增加一个实现了AccessDecisionManager接口的MallAccessDecisionManager类，按照上述逻辑，实现类如下：
 **这里加了一个ant规则匹配url，灵活一些。**
```java
@Component
public class MallAccessDecisionManager implements AccessDecisionManager {

    /**
     *  若用户的权限中包含当前路径所需权限，则可以通过，否则认证异常
     * @param authentication 用户认证信息，包含用户所拥有的权限
     * @param object
     * @param configAttributes 访问当前路径所需要的权限
     * @throws AccessDeniedException
     * @throws InsufficientAuthenticationException
     */
    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
        if(configAttributes==null){
            return;
        }
        String requestUrl = ((FilterInvocation)object).getRequestUrl() ;
        AntPathMatcher antPathMatcher = new AntPathMatcher();

        for (GrantedAuthority authority : authentication.getAuthorities()) {
            // 字符串等于匹配 TODO  动态url
            if( authority.getAuthority().equals( requestUrl )) { return; }
            // ant匹配
            if( antPathMatcher.match(authority.getAuthority(),requestUrl) ) {return;}
            // 对已登录用户放开属于当前服务器静态资源，其实只有swagger资源
            boolean hasPower = authentication.isAuthenticated() && !authority.getAuthority().equals("ROLE_ANONYMOUS")
                    && (antPathMatcher.match("/webjars/**",requestUrl) ||
                    (antPathMatcher.match("/swagger-resources",requestUrl))
                    || (antPathMatcher.match("/v2/api-docs",requestUrl))
                    || (antPathMatcher.match("/doc.html",requestUrl))
            );
            if( hasPower  ) {
                return;
            }
        }
        throw new AccessDeniedException("抱歉，您没有访问权限");
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }
}
```

  2.  然后在WebSecurityConfiguration配置AccessDecisionManager的Bean并将Bean配置到http中。
  

```java
  @Bean
    public AccessDecisionManager accessDecisionManager(){
        return new MallAccessDecisionManager();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .anyRequest().authenticated()
                    .accessDecisionManager(accessDecisionManager())
                .and()
                .formLogin()
                    .defaultSuccessUrl("/doc.html")
                .and() ;
    }
```

就这么简单，完事。

3. 插入测试数据，在数据库权限表添加记录并关联到角色，角色关联到用户。
![4.](https://img-blog.csdnimg.cn/20200509234015914.png) 
ps：这里只贴了一个权限表，其他表数据很简单不再贴出来，表结构在[前文](https://blog.csdn.net/qq_35946969/article/details/105875278)有说明。

4. 打开 [http://localhost:8082/login](http://localhost:8082/login)，登录。
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200509234307329.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM1OTQ2OTY5,size_16,color_FFFFFF,t_70)
访问接口，可以发现只有无参访问GET接口可以成功，配置成功。
剩下的数据就靠大家自由发挥了。

## 三、demo地址
完整代码：
[https://github.com/flyChineseBoy/lel-mall/tree/master/mall10](https://github.com/flyChineseBoy/lel-mall/tree/master/mall10)
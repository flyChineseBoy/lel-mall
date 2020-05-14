
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
11. [从零开始SpringCloud Alibaba电商系统(十一)——spring security完善之动态url控制](https://blog.csdn.net/qq_35946969/article/details/106028631)
## 一、需求简述
  日志在任何一个系统中都是必不可少的，用户访问日志/操作日志无论在前台还是后台管理都很重要，比如前台的**用户点击行为作为特征去做推荐系统**，后台管理的**找凶手**。
  一般来说，用户访问日志可以在网关（nginx或其他）这一层就可以记录下，但是如果想更多的记录一些业务相关的，还是需要放到我们的逻辑层来。
  今天我们就将这些**Controller的访问日志借用AOP统一记录起来**。

## 二、Spring AOP
 AOP相信大家多多少少都了解，俗称面向切面编程的化身、OOP的好伴侣、动态代理的践行者、Spring的大裤衩子等等。 但是这个东西究竟有多少用用过，这又是一个神奇的话题。

闲话少说，Spring AOP是对AOP概念的一个实现，并非一模一样的实现，所以我们今天只关注Spring AOP怎么实现业务。

1. @Aspect
	aspectj组织提供的注解，用于标识这是一个切面。Spring中将其实现为一个`切面类`，在这个类里面可以去定义PointCut、Advice等。
2. @PointCut
	切点的规则，即哪些方法是我们要切入的。
	PointCut原意是可以切入方法、字段等任何`东西`的前后，但是在Spring中，它只能切入一个方法的执行前、后。
	我们在这里，也只需要它来切入Controller所有的方法。
3. Advice、@After、@Before
	通知是对PointCut的具体处理逻辑，即我们用PointCut规定了一些`要被拦截处理的方法`，Advice是对他具体的处理逻辑。
	@Befor、@After、@Around等就是Advice，分别代表在PointCut前处理的逻辑、在PointCut之后处理的逻辑、前后都处理的逻辑。
4. JointPoint
  JointPoint为不以注解的方式出现，而是在Advice中作为参数，即当前被Advice拦截处理的是哪一个具体的方法。


## 三、实现
1. 在[上一章节demo](https://blog.csdn.net/qq_35946969/article/details/106028631)(或一个完善的springboot/cloud项目)的基础上，增加一个aspect包，增加一个SystemLogAspect类。
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200514232059545.png)
这个类需要加@Aspect注解。

2. 声明一个pointcut。
	我在这里直接拦截了所有的controller下面的方法，将它们作为拦截点。
	**@pointCut的拦截规则建议直接到spring官网去查：**  
	[https://docs.spring.io/spring/docs/5.3.0-SNAPSHOT/spring-framework-reference/core.html#spring-core](https://docs.spring.io/spring/docs/5.3.0-SNAPSHOT/spring-framework-reference/core.html#spring-core)
	
```java
	@Pointcut("within(org.lele.*.controller..*)")
    private void logPointCut(){}
```
3. 根据pointcut定义Advice，我这里分别定义了@Befer、@AfterReturn(带返回值的after)、@AfterThrowing(抛异常会调用到的after)。
	**注意，jointPoint在这里就是这些Advice的参数，它实质上代表的就是被访问的那个方法。**
```java
    @Before("logPointCut()")
    public void before(JoinPoint jp) {
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        UserDTO userDTO = sessionUtils.getCurrentUser();

        System.out.println( (jp.getTarget().getClass().getName() + "." + jp.getSignature().getName() + "()——请求信息：") );
        System.out.println("请求地址:" + request.getRemoteAddr());
        System.out.println("请求人:" + userDTO.toString() );
        System.out.println("请求方法:" + (jp.getTarget().getClass().getName() + "." + jp.getSignature().getName() + "()"));
        System.out.println("请求参数:" + (jp.getArgs().toString()));
    }

    @AfterReturning(value = "logPointCut()",returning = "result")
    public void afterReturning(JoinPoint jp,Object result) {
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        UserDTO userDTO = sessionUtils.getCurrentUser();

        System.out.println( (jp.getTarget().getClass().getName() + "." + jp.getSignature().getName() + "()——返回结果：") );
        System.out.println("请求地址:" + request.getRemoteAddr());
        System.out.println("请求人:" + userDTO.toString() );
        System.out.println("返回数据:" + JSONObject.toJSONString(result) );
    }

    @AfterThrowing(value = "logPointCut()",throwing = "e")
    public void afterThrowing(JoinPoint jp,Throwable e) {
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        UserDTO userDTO = sessionUtils.getCurrentUser();

        System.out.println( (jp.getTarget().getClass().getName() + "." + jp.getSignature().getName() + "()——异常结果：") );
        System.out.println("请求地址:" + request.getRemoteAddr());
        System.out.println("请求人:" + userDTO.toString() );
        System.out.println("异常信息:" + e.getMessage() );
    }
```
*sessionUtils是通过spring security容器获取当前session的工具类，需要的话可以到下面demo中拿。*

4. 启动项目，随意访问controller下面的一个方法，可以看到控制台打印日志。
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200514233328832.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM1OTQ2OTY5,size_16,color_FFFFFF,t_70)


## 四、demo地址
这一次并没有将日志保存起来，是因为笔者想要将这些信息写入到es中，同时其他日志也写入es中，配合kibana，做一个方便的可视化，这个下期在写。
完整代码：
[https://github.com/flyChineseBoy/lel-mall/tree/master/mall12](https://github.com/flyChineseBoy/lel-mall/tree/master/mall12)
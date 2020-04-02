
## 一、Nacos配置中心
上文我们介绍了Nacos作为注册中心的简单实用：[从零搭建SpringCloud Alibaba电商系统(一)——Alibaba与Nacos服务注册与发现](https://blog.csdn.net/qq_35946969/article/details/105188015)，本文我们来聊一聊Nacos的另外一个功能：分布式系统的配置中心。  
  
   什么是配置中心？分布式/微服务的架构，每个业务模块必然是多节点多主机的，那么他们之间配置文件的统一管理就显得十分有必要。配置中心即这样一个多节点系统的唯一配置中心，大家需要的配置文件都存放在我这里。 
  
  *并且每个人还可以存放多个版本的配置文件，按需获取，比如A节点可以有开发环境的配置文件、UAT环境配置文件、生产环境配置文件等多个版本。*
    
## 二、项目中配置
1. 在业务项目中（或我们之前demo mall项目中 ）增加pom.xml文件的依赖。

```yaml
<!-- 使用Nacos Config -->
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
</dependency>
```

2. 删除原有 application.yml或application.properties。
	application.properties的配置我们将挪动到配置中心，稍后配置。
3. 新建并配置bootstrap.properties文件。
  bootstrap.properties文件会先于application.properties。在这里配置的是获取nacos配置的相关信息。
  
```bash
# nacos地址
spring.cloud.nacos.config.server-addr=127.0.0.1:8848
# 配置中心对应的配置文件所属的组，稍后在Nacos中创建文件时对应。
spring.cloud.nacos.config.group=mall:order 
spring.application.name=order 
# 配置中心存放的对应文件命名规则为： spring.application.name-spring.profiles.active
spring.profiles.active=dev 
# spring.profiles.active=prod
```


4. 在Nacos控制台配置对应的配置文件（Nacos也支持用代码推送配置文件到配置中心，方便运维）。
	![在这里插入图片描述](https://img-blog.csdnimg.cn/20200403001548143.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM1OTQ2OTY5,size_16,color_FFFFFF,t_70)
*注意，项目中配置的name/active/id需要和这里的dataid、group对应起来。 *
5.  运行项目，可以看到项目按照到配置中心配置的8081端口运行。并且在注册中心可以看到该项目节点。
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200403001719211.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM1OTQ2OTY5,size_16,color_FFFFFF,t_70)
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200403001918354.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM1OTQ2OTY5,size_16,color_FFFFFF,t_70)
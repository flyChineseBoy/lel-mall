
### SpringCloud Alibaba
	什么是SpringCloud Alibaba？
	  简言之，SpringCloud Alibaba是对SpringCloud全生态的一个封装简化，其中还引入了一些阿里自研或其他的开源组件。
	  Alibaba让我们更好的使用电商。
### Nacos是什么
Nacos官网原文：

> Nacos 致力于帮助您发现、配置和管理微服务。Nacos 提供了一组简单易用的特性集，帮助您快速实现动态服务发现、服务配置、服务元数据及流量管理。

	用过springCloud的朋友知道，Nacos对标Erueka。
	没用过的朋友可以这样理解，Nacos是一个路由表，我们的各个微服务Web之间相互寻找需要通过这个路由表。
	比如业务A有两个节点A1和A2，业务B有个节点B1想要调用A的某个接口。我们不需要在业务B中写一堆A的url来调用api，只要告诉Nacos我们要找业务A，它就
	会告诉我们业务A有哪些节点。
	
### 如何使用Nacos服务端
	nacos服务端支持很多种部署方式，详情见：https://nacos.io/zh-cn/docs/quick-start.html
	笔者比较喜欢docker-compose，使用docker部署步骤如下：
	1. 拉取nacos 源码。
		git clone https://github.com/nacos-group/nacos-docker.git
		cd nacos-docker
	2. nacos-docker自带的examples/standalone-mysql-5.7.yaml文件默认会启动一个自带的mysql并连接。
	    如果要使用自己的mysql，我们就需要修改yaml文件及一个mysql配置文件，并且自己将nacos所需的数据库导入到我们使用的mysql中（这一点实在是太不友好了,希望未来会得到改进）。
	    这里不想再开启一个mysql容器的同学可以和笔者一样，选择derby方式启动（需要先安装docker及docker-compose）。
	    docker-compose -f example/standalone-derby.yaml up -d 
	3. 页面访问 -->  http://ip:8848/nacos/
	
 ![在这里插入图片描述](https://img-blog.csdnimg.cn/20200329234025727.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM1OTQ2OTY5,size_16,color_FFFFFF,t_70)
### 项目端集成Nacos-服务提供方
	1. nacos-provider
	pom.xml文件如下：
	

```yaml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.1.0.RELEASE</version>
    </parent>
    <groupId>com.lele</groupId>
    <artifactId>mall</artifactId>
    <version>1.0-SNAPSHOT</version>
    <name>mall</name>
    <description>Demo project for Spring Boot</description>

    <properties>
        <java.version>1.8</java.version>
    </properties>

    <!-- spring cloud alibaba，必须有，版本自选 -->
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>com.alibaba.cloud</groupId>
                <artifactId>spring-cloud-alibaba-dependencies</artifactId>
                <version>2.1.0.RELEASE</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>



    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        <!-- alibaba 必须有，版本自选-->
        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
        </dependency>

        <!-- junit -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
            <exclusions>
                <exclusion>
                    <groupId>org.junit.vintage</groupId>
                    <artifactId>junit-vintage-engine</artifactId>
                </exclusion>
            </exclusions>
        </dependency>



    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

</project>
```
	2. application.properties文件如下：
	

```yaml
server.port=8081
spring.application.name=order
spring.cloud.nacos.discovery.server-addr=之前的nacos服务端ip:8848
management.endpoints.web.exposure.include=*
```
	3. application如下：
	

```java
@SpringBootApplication
@EnableDiscoveryClient //必须有， 
public class MallApplication {

    public static void main(String[] args) {
        SpringApplication.run(MallApplication.class, args);
    }

}

```
	4、正常启动后，我们会看到服务列表多出了我们的application。
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200330223409334.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM1OTQ2OTY5,size_16,color_FFFFFF,t_70)
### 项目端继承Nacos-消费者方
	在上面我们启用了order模块，order模块提供了一些对外的api接口供其他服务调用，我们可以创建一个user项目来调用这个api（通过Nacos来调用）。
	1. 首先，我们现在order模块补上一个服务方法。
```java
@RestController
    public class Hello {
        @GetMapping(value = "/hello/{string}")
        public String hello(@PathVariable String string) {
            return "Hello! 我是Order，感谢你(" + string+")来调用我的hello方法";
        }
    }
```
	2. 然后，新建一个product，pom与application.properties与上面一样不再赘述，这个项目我们给起名user。
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200330225114132.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM1OTQ2OTY5,size_16,color_FFFFFF,t_70)

	3. 最后，我们在product中加入访问order模块API接口的方法。
	
```java
@SpringBootApplication
@EnableDiscoveryClient
public class ProductApplication {

    @LoadBalanced
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public static void main(String[] args) {

        SpringApplication.run(ProductApplication.class, args);
    }

    @RestController
    public class NacosController{

        @Autowired
        private LoadBalancerClient loadBalancerClient;
        @Autowired
        private RestTemplate restTemplate;

        @Value("${spring.application.name}")
        private String appName;

        @GetMapping("/hi")
        public String hi(){
            ServiceInstance serviceInstance = loadBalancerClient.choose("order"); // 负载均衡获取我们实际要访问的项目地址
            String path = String.format("http://%s:%s/hello/%s",serviceInstance.getServiceId(),serviceInstance.getPort(),appName);
            System.out.println("request path:" +path);
            return restTemplate.getForObject(path,String.class);
        }
    }
}
```
	4. 访问product的接口，可以看到成功返回了orderAPI的信息,服务路由成功。
   http://localhost:8082/hi
   ![在这里插入图片描述](https://img-blog.csdnimg.cn/20200330233024176.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM1OTQ2OTY5,size_16,color_FFFFFF,t_70)

 
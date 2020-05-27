

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
12. [从零开始SpringCloud Alibaba电商系统(十二)——spring aop记录用户操作日志](https://blog.csdn.net/qq_35946969/article/details/106130581)
13. [从零开始SpringCloud Alibaba电商系统(十三)——ElasticSearch介绍、logback写入ES](https://blog.csdn.net/qq_35946969/article/details/106161952)
## 一、需求简述
  [前文](https://blog.csdn.net/qq_35946969/article/details/106161952)我们介绍了如何使用logback+ElasticSearch+kibana实现日志收集，今天我们继续在业务和技术的角度深入商品模块和ElasticSearch。

#### (一) 商品模块
   电商系统离不开商品，而我们这里的商品也就是大家常说的`spu`的概念，是商家可以上下架、用户可以搜索并且点开查看详情的那个东西。
   1. 功能接口。
		 - [ ] 管理员（商家）上传商品。
		 - [ ] 管理员（商家）上/下架商品。
		 - [ ] 管理员（商家）编辑商品。
		 - [ ] 管理员（商家）删除商品。
		 - [ ] 用户 关键字搜索/条件排序 商品。
		 - [ ] 用户查看商品分类。
		 - [ ] 用户查看商品。
       *暂时不包含购物车、订单相关功能。*
       **上述功能，有一点非常重要，那就是用户查看/搜索商品的体验感，在一定量级的情况下，如何做到丰富的搜索并且近实时的速度是我们的重点。**
   2. 表结构。  
    由于我们是 `厂家直销类电商`，所以商品分类划分很粗糙，主要维护商品的信息，商品的规格及价格信息（即`sku`），具体表结构及关系如下图（结构sql在demo的flywaydb配置中）：

 ![在这里插入图片描述](https://img-blog.csdnimg.cn/2020052615095620.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM1OTQ2OTY5,size_16,color_FFFFFF,t_70)

#### (二) 结合ElasticSearch
ElasticSearch可以很好的帮助我们实现复杂的且近实时的搜索，这点在前文已经提到了。那么ElasticSearch究竟是如何使用或者说搜索的呢？和我们在mysql中直接 where filed={value}是一样的吗？
**ElasticSearch的搜索大致分为两种：**  

1.   传统匹配(filtered)。
	这是我个人的分法，觉得比较好理解。所谓的传统匹配就是类似sql中where的查询过滤，可以进行`精准等值查询`、`模糊匹配/通配符查询`,`范围查询`,`null值查询`等等。这就是单纯的查询、过滤，可以多层叠加条件，但原理都是一样的。
	这种传统的匹配/排序方式可以用来做商品的分类搜索、各种排序的结果展示，**es的索引查询更快，为什么倒排索引就更快详见第二部分：倒排索引。**
	
2.  相似度匹配。
  相似度匹配是ES的一大亮点，当我们将一条`文档`存入es的时候，es大概会做下面这些事情。  
  tf-idf简述可见[文本类型处理-词袋法、TF-IDF理解](https://blog.csdn.net/qq_35946969/article/details/84562104)
	
	  	a. es会对这条`文档`数据中为`text`类型的字段进行分词。
	  	b. 分词后的数据会计算tf-idf值并保存起来。
	  	c. 当我们要查询的时候，我们的查询条件会被计算一个tf-idf值，然后用这个值来找最近似的数据，并给这些数据都打分。
	  	当然es必然还有一些优化，这里的介绍只是为了理解es能做什么。
 
 我们会发现，对于text类型的字段，我们可以搜索它的**近语义**数据，这个能力十分贴近商品的`文本框搜索`功能。
 
## 二、倒排索引
  说倒排索引之前，我们先回顾一下mysql innodb的索引结构。
#### mysql索引
  mysql索引基于B+树，简单来说，是一个多叉树，且只有叶子节点存储数据，中间节点只存储子节点指针。
  这种多叉索引树的构建相对直接扫描原数数据来说可以提升一定的搜索速度，同时也兼顾了写入的速度，多叉树的数据写入可以直接通过节点裂变来实现。
  **而es没有写入的顾虑，它就是冲着搜索来的。**
 ### es索引
   所以es很直接，我需要A字段的索引，那就把A字段所有数据拿出来，和`文档`指针一起保存成一个字典，我要通过A字段查询直接在这个字典里面找到对应的值，然后就拿到了`文档`指针，有了指针，就能直接拿到`文档`整体数据。
   这就是es的搜索方式，这种方式显然不太适合频繁的修改，尤其是在索引多的情况下，修改或新增一条数据可能意味着对n个文件进行调整。
   **倒排，理解了吗？**
   所谓的倒排，就是又原本的 id->其他字段 的映射方式变成了 其他字段->(id)数据 。这就是倒排。

## 三、代码实现
本次demo实现使用了基于common的product项目，不再需要配置es依赖等东西，不明白怎么配置spring-data-elasticsearch的同学可见[前文](https://blog.csdn.net/qq_35946969/article/details/106161952)。
*本次主要实现创建商品、查询商品这两个功能*
1. 生成表，相关实体类、dao、service等，可见最后demo。
2. 创建商品的逻辑不再赘述，主要点在于商品数据入数据库之后，还要入一次es。
   *笔者认为这里用消息队列来解耦这两次写库操作比较好一些，以后聊到MQ再说。*
   
		按照需求，商家在创建商品时，需要填写商品信息以及多个商品的规格信息。
		下面贴一段该接口的swagger json测试数据，可做了解。
```java
{
    "description": "i222222phone xr描述，这是一个抗揍的手机",
    "keywords": "222222iphone xr,氪金系列，霸气侧漏，漏了",
    "name": "2222iphone xr",
    "picUrls": "",
    "price": 5000.0,
    "productClassId": 1265305040261787650,
    "productClassName": "手机",
    "specs": [
        {
            "price": 51000.0,
            "productSpecs": "{\"颜色\":\"红色\"}",
            "stock": 12
        },
        {
            
            "price": 52000.0,
            "productSpecs": "{\"颜色\":\"黑色\"}",
            "stock": 12
        },
        {
            
            "price": 53000.0,
            "productSpecs": "{\"颜色\":\"深蓝色\"}",
            "stock": 15
        }
    ]
}
```
大致逻辑如下，代码实现全部放在demo。
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200527230926390.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM1OTQ2OTY5,size_16,color_FFFFFF,t_70)
   	新增数据后可以直接查看es数据。
   	这里没什么难点，但是不熟悉es或nosql的同学可能对于es的搜索api、新增api不熟悉，更不熟悉spring-ElasticSearch的调用方法,这个还是多看官方API并调试回来的快。
   	[https://www.elastic.co/guide/en/elasticsearch/reference/current/full-text-queries.html](https://www.elastic.co/guide/en/elasticsearch/reference/current/full-text-queries.html)![在这里插入图片描述](https://img-blog.csdnimg.cn/2020052723103280.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM1OTQ2OTY5,size_16,color_FFFFFF,t_70)
3.商品搜索。
 商品搜索主要涉及四个点：按照名称/分类（精确搜索）、搜索框搜索（语义匹配搜索，这里使用keywords关键字代表商户填写的关键字描述）、分页、排序。
*这里只使用DSL（结构体查询），简易查询功能太弱。*
- **精确搜索** 
  es的精确搜索语法如下：
```powershell
精确搜索name值为'iphone xr'的数据，name.keyword的keyword是name字段的第二类型，有疑问可见前文。
{
  "query": {
  	"bool":{
  		"must" :[{"term":{"name.keyword":"iphone xr"} }  ]
  	}
  }
}
```
在 spring-data-elasticsearch中可以使用NativeSearchQueryBuilder+QueryBuilders来实现上述语法：

```java
NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
boolQueryBuilder = boolQueryBuilder.must(QueryBuilders.termQuery("name.keyword", request.getName()));}
queryBuilder.withQuery( boolQueryBuilder )
// 使用es repository执行query
Object o = productRepository.search( queryBuilder.build());
```

- **语义匹配**

```powershell
查询keywords字段值语义可匹配'氪金'，且name字段为'iphone xr'的数据。
{
  "query": {
  	"bool":{
  		"must" :[{"match": { "keywords":"氪金 " } },{"term":{"name.keyword":"iphone xr"} }  ]
  	}
  }
}
```

```java
// 这里就贴一行，可以配合上一个例子来完整使用
boolQueryBuilder = boolQueryBuilder.must(QueryBuilders.matchQuery("keywords", request.getKeywords()));}
```

- 排序

```powershell
在上述查询基础上，增加排序：使用name字段排序
{
  "query": {
  	"bool":{
  		"must" :[{"match": { "keywords":"氪金 " } },{"term":{"name.keyword":"iphone xr"} }  ]
  	}
  },
  "sort":"name.keyword"
}
```

```java
queryBuilder.withSort( SortBuilders.fieldSort("name.keyword").order(SortOrder.ASC) );}
```

- 分页

```java
从下标为0的数据开始，一共要十条
{
  "query": {
  	"bool":{
  		"must" :[{"match": { "keywords":"氪金 " } },{"term":{"name.keyword":"iphone xr"} }  ]
  	}
  },
  "sort":"name.keyword",
  "size": 10,
  "from": 0
}
```
在 spring-data-elasticsearch中需要在repository.search时增加分页参数：

```java
Object o = productRepository.search( queryBuilder.build().getQuery(),PageRequest.of(0,10));
```
*有同学可能会发现这里search方法第一个参数变成了queryBuilder.build().getQuery()，这样获取到的是NativeSearchQueryBuilder的接口类QueryBuilder，效果是一样的。*

**上面是一些操作也是项目demo中的实际逻辑，截个图可以清楚的看明白，有兴趣的同学获取下方demo。**
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200528001329793.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM1OTQ2OTY5,size_16,color_FFFFFF,t_70)
![在这里插入图片描述](https://img-blog.csdnimg.cn/2020052800071784.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM1OTQ2OTY5,size_16,color_FFFFFF,t_70)
![在这里插入图片描述](https://img-blog.csdnimg.cn/2020052800073418.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM1OTQ2OTY5,size_16,color_FFFFFF,t_70)
## 七、demo地址
本节内容实操性强，建议多操两遍api和代码。
本demo结构包含authorization认证中心，需要先启动认证中心才能正常登陆到Product模块。
user数据可在user模块的flyway中去找。
[https://github.com/flyChineseBoy/lel-mall/tree/master/mall14](https://github.com/flyChineseBoy/lel-mall/tree/master/mall14)
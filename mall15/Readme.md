# 从零开始SpringCloud Alibaba电商系统(十五)——互斥锁的概念、分布式锁的实现
<hr>

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
14. [从零开始SpringCloud Alibaba电商系统(十四)——简单商品模块需求、使用ElasticSearch构建商品搜索](https://blog.csdn.net/qq_35946969/article/details/106341790)
## 一、互斥锁
 锁的范围很大，真要有人说给你讲讲锁，怕不是耍流氓就是准备三天三夜……

 笔者这里抛开其他的概念，从基本的`互斥锁`开始，慢慢深入扩展，什么是`互斥锁`？一句话的定义可能不好描述，但是要实现一个互斥锁需要满足以下条件？
 >不准永远耽搁一个要求进入临界区域的线程，造成死锁或是饥饿发生 。
若没有任何线程处于临界区域时，任何要求进入临界区域的线程必须立刻得到允许。
不能对线程的相对速度与处理器的数目做任何假设。
线程只能在临界区域内停留一有限的时间。
任何时间只允许一个线程在临界区域运行。
在临界区域停止运行的线程，不准影响其他线程运行。 ——wiki百科

**再来回味一下`互斥锁`，我们可能会明白，锁是为了让A线程能够独自运行一个`程序`一段时间的做法，在线程A独占的这个时间里，其他线程想要运行这个`程序`都得等着。**

那么我们可能就会有一个初步的想法，想要实现一个互斥锁，可以使用一个所有线程都可以看到的变量i=0，当有线程先将该变量置为1时，其他线程判断到该变量为1就会等待（进入临界区或不管循环判断变量是否变回了0），流程如下（*不靠谱的时序*）：
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200605145547789.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM1OTQ2OTY5,size_16,color_FFFFFF,t_70)
**重试加锁的方式被称为自旋重试机制，一般是乐观锁的实现方式，悲观锁也就是按照上述严格要求的实现，这里应该线程B进行wait，直到线程A解锁之后，共享变量i被notify，线程B才会继续执行`上锁的程序`。**
****
## 二、Java中的锁
### synchronized
 synchronized是最好的例子，我们来分析一下它是怎么做到上述`互斥锁`的作用的。

 我们知道  synchronized 有三种加锁的方式：普通方法加锁、 静态方法加锁、同步方法块加锁。
 无论是哪一种方式，实质上都是对实例对象/Class对象加锁，与我们上述`共享变量`的方式无甚差别。

 举个例子，同步方法块中对对象a加锁：synchorized(a){   doSomthing()  }。
 1. 当线程A要执行doSomthing()  ，需要先进入synchorized，此时会检查a对象是否是上锁状态，如果没有，好的，我上。
 2. 此时线程B也想执行doSomething()，检查a对象是已经被人占了，好，那我blocking，堵在门口等线程A出来。(**在jdk1.6之后，synchorized关键字被优化，当线程B发现a对象被占用时不会直接blocking，而是先采用CAS+自旋重试的操作，与我们之前的`共享变量`方式类似。而当自旋重试一段时间还等不到锁的时候，才会升级为重量锁，进入blocking状态。**)

### ReentrantLock
Retreenlock是JUC中最常用的互斥锁，它的实现原理与synchroized有异曲同工之妙。
ReentrantLock核心也是通过一个`共享变量`实现的，`共享变量`=0代表没人持有锁。

举个ReentrantLock非公平锁使用的例子（*非公平锁指的是新来的线程可能插队获取锁，公平锁则是所有线程按照先后顺序获取锁*）：
```java
	  ReentrantLock reentrantLock = new ReentrantLock(false); //tru为公平锁
     reentrantLock.lock();
     doSomthing();
     reentrantLock.unlock();
```
1. 当线程A要执行doSomthing()方法，会进入reentrantLock.lock，通过CAS尝试将`共享变量`置为1，尝试成功，加锁成功，执行doSomthing()。
2. 此时线程B也想执行doSomthing(),通过CAS尝试将`共享变量`置为1，失败，于是`AQS队列`登场，线程B入队列、同时线程B调用unsafe.park被阻塞。
  	
  		AQS是reentrantLock内部维护的一个队列，用于存储所有试图获取该锁但失败的线程。
  		我们知道队列是FIFO(先进先出)的，所以这个队列保证了公平性，相对应的，synchronized就不是公平锁。
3. 当线程A执行结束，unlock，AQS队列第一个元素出队，也就是线程B，对B进行unsafe.unpark，于是线程B就可以快了的doSomthing()；
 

   **synchronized与ReentrantLock作为互斥锁，在非公平锁的情况下，用法也会有差异。因为syncronized实际上是通过标识一个对象来区分是否加锁，而ReentrantLock是标识了一个变量来标识标识，这是很重要的一个差别。**

<hr>
  
### 三、分布式锁
我们已经实现了互斥锁，并且简单观察了一些实现的案例，接下来让我们来思考如何实现分布式锁，这是相当有必要的，一个分布式系统中，只存在于单个jvm中的锁显然没办法满足多节点公用一个锁的需求。

#### 1. 超时问题
但是分布式锁相对于jvm层面的锁来说，我们需要注意更多的问题。在jvm层面上我们对加锁的状态只需要考虑**成功、失败**这两种状态。

但是在分布式系统层面上，我们需要另一个节点`锁服务器`，`客户端`想要获得锁需要网络访问`锁服务器`。如此一来，由于网络的不确定性，我们不得不引入第三种状态——**超时**。
于是引出下面的问题：

	锁的持有者的unlock指令在网络传输中丢失了怎么办？或者因为网络问题，过了几分钟甚至几个小时才到达怎么办？
	总不能让其他节点一直等着吧。
   
   对于这样`占着茅坑不拉屎`的持锁者，我们需要制裁，于是出现了一下两种**解决方案**：
   1. 锁设置时效性，一定时间就失效，其他人就可以获得锁了。
   2. 规定锁的持有规则，只有`客户端`和`锁服务器` 保存连接才算持有锁，一旦网络波动、连接断开，那么锁就自动消除。
 
   第一种方式是redis、mysql为主的**数据云端持久化类型**的实现方案；第二种则是以zookeeper为主的**基于心跳机制**的实现方案。

#### 2.锁过期了，任务没结束
上面我们解决了*持有锁时间过长*的问题，但是在上面的第一种解决方案中，却会引出另外的问题：

	我张三加锁后，任务还没执行完，锁就过期了。李四拿到了锁，这下操作不就并发冲突了吗？

**解决方案：**	
	对锁进行续期。`客户端`持有锁之后，定期向`锁服务器`发起请求，表示自己还没用完，比如redis的一个java客户端(redission)就是采用这种方式实现锁。*实际上，zookeeper本质上也是这种不断续约的方式来维护服务端和客户端连接的。*

#### 3. 李四解了张三的锁
   除此之外，分布式锁还需要考虑一些基本问题，比如`节点B解了节点A的锁`，其根源还是受到了网络的影响，比如：
    
	张三的unlock指令半天没到`锁服务器`，我李四拿到了锁，结果张三的unlock指令来了，把我的锁解了，你想干啥？
  
   锁被别人解了，这在jvm锁中是不存在的问题，因为jvm中不存在**数据传输不可靠**的问题。
   **解决方案**：`锁服务器`记录请求人的id，比如线程id。zookeeper的一种java客户端(curator)就是采用这种方式来区分加锁人。
   

#### 4. 加锁解锁的原子性问题
  加锁解锁的原子性问题并非分布式锁独有，比如ReentrantLock，它是java代码级别实现的锁，那么在代码级别它就一定要保证自己的加锁、解锁操作是没有并发冲突的。
  同学们追一下源码就能知道，ReentrantLock的加锁解锁操作都是通过unsafe.compareAndSwapInt()来实现的对`共享变量`的更新操作，也就是我们常说的`CAS`。
  `CAS`是计算机层面的原子操作，不存在并发问题，它的语义是**将变量i从值A修改为值B，若原值不为A则修改失败**。通过CAS，就可以控制`共享变量`的值，进而保证`锁的原子性`。

**分布式锁如何保证原子性？**
  1. zookeeper：zk的操作很简单，它能够保证同一时刻只有一个请求能成功注册同一key；同时解锁操作只需要我们客户端断开与zk服务端的请求，zk就会自动消除锁。
  2. redis：redis则需要稍稍花一些心思。加锁不需要操心，setnx+expire的操作目前已经被redis官方做出了参数形式，可以保证原子性。解锁则需要实现一个简单的lua脚本（redis的多条命令可以通过写在一个lua脚本，来实现原子性）。


  <hr>
  

## 四、代码实现
  这里分别用ReentrantLock、Redission、Curator来演示三种锁的简单用法。
  案例简述： 共享变量i，是个线程同时对其进行-1操作。现在我们要求上锁来保证他不超卖。
  以下截图是笔者反复测试了好久才出现了一个超卖的例子，截图留念，两个资源，三个线程成功获取到。 ![在这里插入图片描述](https://img-blog.csdnimg.cn/20200606000752746.png)
 ### 1. ReentrantLock

```java
/**
 * com.mall.zk.demo.juc
 *
 * @author: lele
 * @date: 2020-06-05
 */
public class ReentrantRockUtil {
    private final static ReentrantLock reentrantLock;
    static {
        reentrantLock = new ReentrantLock();
    }

    public static ReentrantLock getLock(){
        return reentrantLock;
    }

    // 共享变量
    public static int i=2;
    public static void main(String[] args) throws Exception{
        final CountDownLatch cdl = new CountDownLatch(1);

        for (int i = 0; i < 50; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        cdl.await();
                        getLock().lock();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    if( ReentrantRockUtil.i>0 ){
                        int res = ReentrantRockUtil.i-1;
                        ReentrantRockUtil.i = res;
                        System.out.println(ReentrantRockUtil.i);
                    }
                    try {
                        getLock().unlock();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        cdl.countDown();
    }
}
```


### 2. Curator实现Lock（Zookeeper）

```java
public class CuratorLockUtil {

    public final static String lockRoot="/lock";
    private static CuratorFramework client;
    public static InterProcessMutex lock;

    static{
        client = CuratorFrameworkFactory.builder()
                .connectString("zookeeper ip:2181")
                .retryPolicy( new ExponentialBackoffRetry(1000,3))
                .build();
        client.start();
    }

    /**
     * 注意这里使用多例，单例没有意义，无法模拟多节点
     * @return
     */
    public static InterProcessMutex getLock(){
        // 这里为了测试自定义了LockInternalsDriver，这个参数不传也可，一般够用
        lock=new InterProcessMutex(client, lockRoot);
        return  lock;
    }

    /**
     * 非必须
     * @return
     */
    public static String getLockId(){
        return Thread.currentThread().getName();
    }

    // 共享变量
    public static int i=10;
    public static void main(String[] args) throws Exception{
        final CountDownLatch cdl = new CountDownLatch(1);
        // cdl.await(); // 等待
        // cdl.countDown(); // 代表当前线程已结束，可以放开

        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    InterProcessMutex lock = getLock();
                    try{
                        cdl.await();
                        lock.acquire();
                        //getLock().acquire();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    int res = CuratorLockUtil.i-1;
                    CuratorLockUtil.i = res;
                    System.out.println(CuratorLockUtil.i);
                    try {
                        lock.release();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        cdl.countDown();

    }
}
```
### 3. Redission实现lock（redis）

```java
public class RedissonLockUtil {

    private final static RedissonClient redissonClient;
    private final static String lockName="redisLock";
    static {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://reids ip:6379")
                .setPassword("redis密码");
        redissonClient = Redisson.create(config);
    }

    public static RLock getLock( String name ){
        return redissonClient.getLock(name);
    }
    public static RLock getLock( ){
        return getLock(lockName);
    }
    // 共享变量
    public static int i=10;
    public static void main(String[] args) throws Exception{
        final CountDownLatch cdl = new CountDownLatch(1);

        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    RLock lock = getLock();
                    try{
                        cdl.await();
                        lock.lock(15,TimeUnit.SECONDS);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    if( RedissonLockUtil.i>0 ){
                        int res = RedissonLockUtil.i-1;
                        RedissonLockUtil.i = res;
                        System.out.println(RedissonLockUtil.i);
                    }
                    try {
                        lock.unlock();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        cdl.countDown();
    }
}
```

  <hr>
  
## 五、demo地址
[https://github.com/flyChineseBoy/lel-mall/tree/master/mall15](https://github.com/flyChineseBoy/lel-mall/tree/master/mall15)
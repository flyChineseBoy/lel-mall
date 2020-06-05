package com.mall.zk.demo.redis.redission;

import com.mall.zk.demo.zookeeper.CuratorLockUtil;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * com.mall.zk.demo.redis.redission
 *
 * @author: lele
 * @date: 2020-06-05
 */
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

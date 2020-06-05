package com.mall.zk.demo.juc;

import com.mall.zk.demo.zookeeper.CuratorLockUtil;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

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

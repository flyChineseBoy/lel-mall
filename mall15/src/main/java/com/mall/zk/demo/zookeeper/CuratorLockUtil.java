package com.mall.zk.demo.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import java.util.concurrent.CountDownLatch;

/**
 * com.mall.zk.demo.zookeeper
 *
 * @author: lele
 * @date: 2020-06-03
 */
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

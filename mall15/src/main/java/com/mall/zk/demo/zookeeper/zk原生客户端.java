package com.mall.zk.demo.zookeeper;

import org.apache.zookeeper.*;

import java.util.concurrent.CountDownLatch;

/**
 * com.mall.zk.demo.zookeeper
 *
 * @author: lele
 * @date: 2020-06-03
 */
public class zk原生客户端 implements Watcher{
    public static CountDownLatch cdl = new CountDownLatch(1);
    
    public static void main(String[] args) throws Exception{
        ZooKeeper zooKeeper = new ZooKeeper("你的zkip:2181", 5000, new zk原生客户端());
        //zooKeeper.create("/",)
        cdl.await();
        System.out.println("成功连接");

        // 创建节点
        // 异步
        zooKeeper.create("/lock", "自定义数据".getBytes() ,ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT
        ,new MyClassCallback(),"context");
        //System.out.println( createNode );

        Thread.sleep(10000);
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        System.out.println(" 接收到zk事件 " + watchedEvent);
        if(Event.KeeperState.SyncConnected == watchedEvent.getState()){
            // 连接上了的话就打枪
            cdl.countDown();
        }
    }


}
class MyClassCallback implements  AsyncCallback.StringCallback{
    @Override
    public void processResult(int rc, String path, Object ctx, String name) {
        System.out.println( rc+","+path+","+ctx+","+name );
    }
}
package zookeeper.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by liequ on 18/2/9.
 */
//使用curator来创建一个ZooKeeper客户端
public class Curator_Sample {
    //使用Curator创建节点
    static String path = "/zk-book/c1";
    static CuratorFramework client = CuratorFrameworkFactory.builder()
            .connectString("192.168.33.20:2181")
            .sessionTimeoutMs(5000)
            .retryPolicy(new ExponentialBackoffRetry(1000, 3))
            .build();
    static CountDownLatch countDownLatch = new CountDownLatch(1);
    static ExecutorService executorService = Executors.newFixedThreadPool(2);
    public static void main(String[] args) throws Exception{
       /* RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client =
                CuratorFrameworkFactory.newClient("192.168.33.20:2181",
                        5000,
                        3000,
                        retryPolicy);
        client.start();
        Thread.sleep(Integer.MAX_VALUE);*/



        client.start();
        client.create()
                .creatingParentsIfNeeded()
                .withMode(CreateMode.EPHEMERAL)
                .forPath(path, "init".getBytes());

        // create path asynchronized
        client.create()
                .creatingParentsIfNeeded()
                .withMode(CreateMode.PERSISTENT)
                .inBackground(new BackgroundCallback() {
                    @Override
                    public void processResult(CuratorFramework curatorFramework, CuratorEvent curatorEvent) throws Exception {
                        System.out.println("event[code:" + curatorEvent.getResultCode() + "type:"+ curatorEvent.getType() + "]");
                        System.out.println("Thread.currentThread.name:" + Thread.currentThread().getName());
                        countDownLatch.countDown();
                    }
                }, executorService)
                .forPath(path + "/ccc", "test".getBytes());
        countDownLatch.await();
        executorService.shutdown();


        Stat stat = new Stat();

        //get data
        byte[] data = client.getData()
                            .storingStatIn(stat)
                            .forPath(path);

        if (data != null && 0 < data.length) {
            System.out.println(new String(data));
        }


        //update
        client.setData()
                .withVersion(stat.getVersion())
                .forPath(path, "update".getBytes());

        data = client.getData()
                .storingStatIn(stat)
                .forPath(path);

        if (data != null && 0 < data.length) {
            System.out.println(new String(data));
        }

        //delete

        client.getData()
                .storingStatIn(stat)
                .forPath(path);
        client.delete()
//                .guaranteed()
                .deletingChildrenIfNeeded()
                .withVersion(stat.getVersion())
                .forPath(path);





    }
}

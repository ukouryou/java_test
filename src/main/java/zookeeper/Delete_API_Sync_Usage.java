package zookeeper;

import org.apache.zookeeper.*;

import java.util.concurrent.CountDownLatch;

/**
 * Created by liequ on 18/2/8.
 */
// ZooKeeper API 删除节点，使用同步(sync)接口。
public class Delete_API_Sync_Usage implements Watcher {

    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
    private static ZooKeeper zk;

    public static void main(String[] args) throws Exception {

        String path = "/andy-zk";
        zk = new ZooKeeper("192.168.33.20:2181",
                5000, //
                new Delete_API_Sync_Usage());
        connectedSemaphore.await();

//        zk.create( path, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL );
        zk.delete( path, 0 );

        Thread.sleep( Integer.MAX_VALUE );
    }
    @Override
    public void process(WatchedEvent event) {
        if (Event.KeeperState.SyncConnected == event.getState()) {
            if (Watcher.Event.EventType.None == event.getType() && null == event.getPath()) {
                connectedSemaphore.countDown();
            }
        }
    }
}
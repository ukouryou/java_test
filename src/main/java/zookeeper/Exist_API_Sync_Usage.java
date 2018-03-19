package zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.CountDownLatch;

/**
 * Created by liequ on 18/2/8.
 */
// ZooKeeper API 删除节点，使用同步(sync)接口。
public class Exist_API_Sync_Usage implements Watcher {

    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
    private static ZooKeeper zk;
    public static void main(String[] args) throws Exception {

        String path = "/zk-book1";
        zk = new ZooKeeper("192.168.33.20:2181",
                5000, //
                new Exist_API_Sync_Usage());
        connectedSemaphore.await();

        zk.create( path, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT );

        Stat stat = zk.exists( path, true );

        System.out.println(stat);

        stat = zk.exists( path + "aaa", false );

        System.out.println(stat);



        zk.setData( path, "123".getBytes(), -1 );

        zk.create( path+"/c1", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT );

        zk.delete( path+"/c1", -1 );

        zk.delete( path, -1 );

        Thread.sleep( Integer.MAX_VALUE );
    }

    @Override
    public void process(WatchedEvent event) {
        try {
            if (Event.KeeperState.SyncConnected == event.getState()) {
                if (Event.EventType.None == event.getType() && null == event.getPath()) {
                    connectedSemaphore.countDown();
                } else if (Event.EventType.NodeCreated == event.getType()) {
                    System.out.println("Node(" + event.getPath() + ")Created");
                    zk.exists( event.getPath(), true );
                } else if (Event.EventType.NodeDeleted == event.getType()) {
                    System.out.println("Node(" + event.getPath() + ")Deleted");
                    zk.exists( event.getPath(), true );
                } else if (Event.EventType.NodeDataChanged == event.getType()) {
                    System.out.println("Node(" + event.getPath() + ")DataChanged");
                    zk.exists( event.getPath(), true );
                }
            }
        } catch (Exception e) {}
    }
}

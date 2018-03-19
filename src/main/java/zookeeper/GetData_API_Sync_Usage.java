package zookeeper;

/**
 * Created by liequ on 18/2/8.
 */

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import java.util.concurrent.CountDownLatch;

// ZooKeeper API 获取节点数据内容，使用同步(sync)接口。
public class GetData_API_Sync_Usage implements Watcher {

    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
    private static ZooKeeper zk = null;
    private static Stat stat = new Stat();

    public static void main(String[] args) throws Exception {

        String path = "/zk-book2";
        zk = new ZooKeeper("192.168.33.20:2181",
                5000, //
                new GetData_API_Sync_Usage());
        connectedSemaphore.await();
        zk.create( path, "123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL );

        System.out.println(new String(zk.getData( path, true, stat )));
        System.out.println(stat.getCzxid()+","+stat.getMzxid()+","+stat.getVersion());

        zk.setData( path, "123".getBytes(), -1 );
        zk.setData( path, "456".getBytes(), -1 );

        Thread.sleep( Integer.MAX_VALUE );
    }
    public void process(WatchedEvent event) {
        if (Event.KeeperState.SyncConnected == event.getState()) {
            if (Event.EventType.None == event.getType() && null == event.getPath()) {
                connectedSemaphore.countDown();
            } else if (event.getType() == Event.EventType.NodeDataChanged) {
                try {
                    System.out.println(new String(zk.getData( event.getPath(), true, stat )));
                    System.out.println(stat.getCzxid()+","+
                            stat.getMzxid()+","+
                            stat.getVersion());
                } catch (Exception e) {}
            }
        }
    }
}
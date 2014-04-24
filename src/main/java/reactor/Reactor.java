/**
 *
 */
package reactor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Apr 11, 2014
 * @author andy
 */
public class Reactor implements Runnable {

    final Selector selector;
    final ServerSocketChannel serverSocketChannel;
    final boolean isWithThreadPool;

    Reactor(int port, boolean isWithThreadPool) throws IOException {

        this.isWithThreadPool = isWithThreadPool;
        selector = Selector.open();
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(port));
        serverSocketChannel.configureBlocking(false);
        SelectionKey selectionKey0 = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        selectionKey0.attach(new Acceptor());
    }


    public void run() {
        System.out.println("Server listening to port: " + serverSocketChannel.socket().getLocalPort());
        try {
//            while (!Thread.interrupted()) {
            Set<SelectionKey> readyKeys;
            Iterator<SelectionKey> iterator;
            SelectionKey key;
            while(true) {
                int ready = selector.select();
                System.out.println("selector.select() return "+ ready);
                if (ready == 0) {
                    continue;
                }
/*                readyKeys = selector.selectedKeys();
                System.out.println("Selected channels: " + readyKeys.size());

                for (SelectionKey key : readyKeys) {
                    readyKeys.remove(key);
                    if (key.isValid()) {
                      // Take action...
                        dispatch(key);
                    }

                  }*/

                iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                  key = iterator.next();
                  iterator.remove();
                  dispatch(key);
                }
//                System.out.println("selectedkeys: " +it.hasNext());
               /* while (it.hasNext()) {
//                    dispatch((SelectionKey) (it.next()));
                    System.out.println("hit");
                    SelectionKey key = (SelectionKey) it.next();
                    dispatch(key);
                    it.remove();
                }*/
//                selected.clear();
//                Thread.sleep(100);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void dispatch(SelectionKey k) {
        k.readyOps();
        Runnable r = (Runnable) (k.attachment());
        //System.out.println(r.toString());

        if (k.isAcceptable()) {
            ServerSocketChannel serverSocketChannel = (ServerSocketChannel) k.channel();
            try {
                SocketChannel socketChannel = serverSocketChannel.accept();
                if (null != socketChannel) {
                    System.out.println("create new handler!");
                    new Handler(selector, socketChannel);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            if (r != null) {
                if (k.isValid()) {
                    System.out.println("valid");
                }
                System.out.println("new thread for handler ");
                System.out.println(k.readyOps());
//            r.run();
                new Thread(r).start();
            }
        }
//                new Thread(r).start();
    }

    class Acceptor implements Runnable {
        public void run() {
            try {
                SocketChannel socketChannel = serverSocketChannel.accept();
                if (socketChannel != null) {
                    if (isWithThreadPool)
                        new HandlerWithThreadPool(selector, socketChannel);
                    else
                        new Handler(selector, socketChannel);
                }
                System.out.println("Connection Accepted by Reactor");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}

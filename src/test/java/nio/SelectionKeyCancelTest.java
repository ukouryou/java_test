/**
 *
 */
package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;


import org.junit.Test;

/**
 * Apr 25, 2014
 * @author andy
 */
public class SelectionKeyCancelTest {

    @Test
    public void testCancel() throws IOException{
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        Selector selector = Selector.open();
        serverSocketChannel.bind(new InetSocketAddress(9900));
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        int ready;
        Iterator<SelectionKey> readyIterator;
        SelectionKey key;
        while(true) {
            System.out.println("register channel:" + selector.keys().size());
            ready = selector.select();
            System.out.println("ready:" + ready);
            System.out.println("selectedKeys: " + selector.selectedKeys().size());
            if (0 == ready) {
                continue;
            }
            readyIterator = selector.selectedKeys().iterator();
            while (readyIterator.hasNext()) {
                key = readyIterator.next();
                readyIterator.remove();
                dispathch(key);
            }
        }
    }

    private void dispathch(SelectionKey selectionKey) throws IOException{
        if (!selectionKey.isValid()) {
            return;
        }
        if (selectionKey.isAcceptable()) {
            ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
            SocketChannel socketChannel = serverSocketChannel.accept();
            socketChannel.configureBlocking(false);
            socketChannel.register(selectionKey.selector(), SelectionKey.OP_READ);
        } else if (selectionKey.isReadable()) {
            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            socketChannel.read(byteBuffer);
            selectionKey.cancel();
            System.out.println("cancel read");
        } else if (selectionKey.isWritable()) {
            SelectableChannel socketChannel = selectionKey.channel();
        }

    }

}

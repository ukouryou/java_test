/**
 *
 */
package reactor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * Apr 11, 2014
 * @author andy
 */
public class Handler implements Runnable {

    final SocketChannel socketChannel;
    final SelectionKey selectionKey;
    ByteBuffer input = ByteBuffer.allocate(1024);
    static final int READING = 0, SENDING = 1;
    int state = READING;
    String clientName = "";

    Handler(Selector selector, SocketChannel c) throws IOException {
        socketChannel = c;
        c.configureBlocking(false);
        selectionKey = socketChannel.register(selector, 0);
        selectionKey.attach(this);
        selectionKey.interestOps(SelectionKey.OP_READ);
        selector.wakeup();
    }


    public void run() {
        try {
            if (state == READING) {
                System.out.println("state reading");
                System.out.println("run() selectionKey.interestOps(): "+ selectionKey.interestOps());
                read();
            } else if (state == SENDING) {
                System.out.println("state writing");
                System.out.println("run().write selectionKey.interestOps(): "+ selectionKey.interestOps());
                send();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    void read() throws IOException {
        //System.out.println("read() selectionKey.interestOps(): "+ selectionKey.interestOps());
        int readCount = socketChannel.read(input);
        System.out.println("reading input");
        System.out.println("reading count" + readCount);
        if (readCount > 0) {
            readProcess(readCount);
        }
        state = SENDING;
        // Interested in writing
        selectionKey.interestOps(SelectionKey.OP_WRITE);
        //System.out.println("read()2 ..");
        //System.out.println("read() selectionKey.interestOps(): "+ selectionKey.interestOps());
    }

    /**
     * Processing of the read message. This only prints the message to stdOut.
     *
     * @param readCount
     */
    synchronized void readProcess(int readCount) {
        StringBuilder sb = new StringBuilder();
        input.flip();
        byte[] subStringBytes = new byte[readCount];
        byte[] array = input.array();
        System.arraycopy(array, 0, subStringBytes, 0, readCount);
        // Assuming ASCII (bad assumption but simplifies the example)
        sb.append(new String(subStringBytes));
        input.clear();
        clientName = sb.toString().trim();
    }

    void send() throws IOException {
        System.out.println("Saying hello to " + clientName);
        ByteBuffer output = ByteBuffer.wrap(("Hello " + clientName + "\n").getBytes());
        socketChannel.write(output);
        selectionKey.interestOps(SelectionKey.OP_READ);
        state = READING;
        System.out.println("send() selectionKey.interestOps(): "+ selectionKey.interestOps());
    }
}
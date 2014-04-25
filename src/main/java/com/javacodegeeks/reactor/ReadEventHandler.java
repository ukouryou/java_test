/**
 *
 */
package com.javacodegeeks.reactor;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * Apr 11, 2014
 * @author andy
 */
public class ReadEventHandler implements EventHandler{

     private Selector demultiplexer;
      private ByteBuffer inputBuffer = ByteBuffer.allocate(2048);

      public ReadEventHandler(Selector demultiplexer) {
        this.demultiplexer = demultiplexer;
      }

      @Override
      public void handleEvent(SelectionKey handle) throws Exception {
//          ByteBuffer inputBuffer = ByteBuffer.allocate(2048);
        SocketChannel socketChannel =
         (SocketChannel) handle.channel();

        System.out.println("socketchannel : " + socketChannel.toString());

        int readCount = socketChannel.read(inputBuffer); // Read data from client
        System.out.println("readCount:" + readCount);
        if (readCount > 0) {
            inputBuffer.flip();
            // Rewind the buffer to start reading from the beginning

            System.out.println("inputBuffer.limit"+ inputBuffer.limit());
            byte[] buffer = new byte[inputBuffer.limit()];
            inputBuffer.get(buffer);
            inputBuffer.clear();
            String writerText = new String(buffer).trim();
            //inputBuffer.wrap(writerText.getBytes());
            inputBuffer.position(0);
            inputBuffer.limit(writerText.length()+1);
            System.out.println("limit " + inputBuffer.limit());
            System.out.println("position " + inputBuffer.position());
            System.out.println("Received message from client : " +
                    new String(buffer).trim());
//            inputBuffer.flip();
            // Rewind the buffer to start reading from the beginning
            // Register the interest for writable readiness event for
            // this channel in order to echo back the message

            /* socketChannel.register(
                  demultiplexer, SelectionKey.OP_WRITE, inputBuffer);*/
            handle.interestOps(SelectionKey.OP_WRITE);
           /* byte[] byteArray = new byte[inputBuffer.limit()];
            System.arraycopy(inputBuffer.array(), 0, byteArray, 0, inputBuffer.limit());*/
//            handle.attach(byteArray);
            handle.attach(inputBuffer);
            inputBuffer.clear();
        }
      }
}

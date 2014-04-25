/**
 *
 */
package com.javacodegeeks.reactor;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * Apr 11, 2014
 * @author andy
 */
public class WriteEventHandler implements EventHandler {

      @Override
      public void handleEvent(SelectionKey handle) throws Exception {
        SocketChannel socketChannel =
          (SocketChannel) handle.channel();
        ByteBuffer inputBuffer = (ByteBuffer) handle.attachment();
        /*byte [] bytesArray = (byte[]) handle.attachment();
        ByteBuffer inputBuffer = ByteBuffer.wrap(bytesArray);
        System.out.println("array length " + bytesArray.length);
        System.out.println(bytesArray[bytesArray.length -1]);
        System.out.println("prepare to write " + new String(bytesArray).trim());*/
        System.out.println(" position:" + inputBuffer.position());
        System.out.println(" limit:" + inputBuffer.limit());
        socketChannel.write(inputBuffer);
        System.out.println(" position:" + inputBuffer.position());
        System.out.println(" limit:" + inputBuffer.limit());
//        inputBuffer.clear();
//        socketChannel.register(handle.selector(), SelectionKey.OP_READ);
        handle.interestOps(SelectionKey.OP_READ);
//        socketChannel.close(); // Close connection
      }

    }

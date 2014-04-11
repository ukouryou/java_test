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
        socketChannel.write(inputBuffer);
        socketChannel.close(); // Close connection
      }

    }

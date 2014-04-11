/**
 *
 */
package com.javacodegeeks.reactor;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * Apr 11, 2014
 * @author andy
 */
public class AcceptEventHandler implements EventHandler{

    private Selector demultiplexer;
      public AcceptEventHandler(Selector demultiplexer) {
        this.demultiplexer = demultiplexer;
      }

      @Override
      public void handleEvent(SelectionKey handle) throws Exception {
        ServerSocketChannel serverSocketChannel =
         (ServerSocketChannel) handle.channel();
        SocketChannel socketChannel = serverSocketChannel.accept();
        if (socketChannel != null) {
          socketChannel.configureBlocking(false);
          socketChannel.register(
            demultiplexer, SelectionKey.OP_READ);
        }
      }
}

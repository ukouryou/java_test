/**
 *
 */
package com.javacodegeeks.reactor;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;

/**
 * Apr 11, 2014
 * @author andy
 */
public class ReactorInitiator {

    private static final int NIO_SERVER_PORT = 9993;

      public void initiateReactiveServer(int port) throws Exception {

        ServerSocketChannel server = ServerSocketChannel.open();
        server.socket().bind(new InetSocketAddress(port));
        server.configureBlocking(false);

        Dispatcher dispatcher = new Dispatcher();
        dispatcher.registerChannel(SelectionKey.OP_ACCEPT, server);

        dispatcher.registerEventHandler(
          SelectionKey.OP_ACCEPT, new AcceptEventHandler(
          dispatcher.getDemultiplexer()));

        dispatcher.registerEventHandler(
          SelectionKey.OP_READ, new ReadEventHandler(
          dispatcher.getDemultiplexer()));

        dispatcher.registerEventHandler(
        SelectionKey.OP_WRITE, new WriteEventHandler());

        dispatcher.run(); // Run the dispatcher loop

     }

      public static void main(String[] args) throws Exception {
        System.out.println("Starting NIO server at port : " +   NIO_SERVER_PORT);
        new ReactorInitiator().
          initiateReactiveServer(NIO_SERVER_PORT);
      }

}

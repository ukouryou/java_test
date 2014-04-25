/**
 *
 */
package com.javacodegeeks.reactor;

import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Apr 11, 2014
 * @author andy
 */
public class Dispatcher {
    private Map<Integer, EventHandler> registeredHandlers =
            new ConcurrentHashMap<Integer, EventHandler>();
          private Selector demultiplexer;

          public Dispatcher() throws Exception {
            demultiplexer = Selector.open();
          }

          public Selector getDemultiplexer() {
            return demultiplexer;
          }

          public void registerEventHandler(
            int eventType, EventHandler eventHandler) {
            registeredHandlers.put(eventType, eventHandler);
          }

          // Used to register ServerSocketChannel with the
          // selector to accept incoming client connections
          public void registerChannel(
            int eventType, SelectableChannel channel) throws Exception {
            channel.register(demultiplexer, eventType);
          }

          public void run() {
            try {
                int ready;
              while (true) { // Loop indefinitely
                ready = demultiplexer.select();
                System.out.println("start round");
                System.out.println("ready : " + ready);
                Set<SelectionKey> readyHandles =
                  demultiplexer.selectedKeys();
                Iterator<SelectionKey> handleIterator =
                  readyHandles.iterator();

                System.out.println("readyHandles.size():" + readyHandles.size());
                while (handleIterator.hasNext()) {
                  SelectionKey handle = handleIterator.next();

                  handleIterator.remove();
                  System.out.println("interestOPs:" +handle.interestOps());
                  System.out.println("valid:" + handle.isValid());
                  if (handle.isAcceptable()) {
                    EventHandler handler =
                      registeredHandlers.get(SelectionKey.OP_ACCEPT);
                      handler.handleEvent(handle);
                      System.out.println("handle select!");

                   // Note : Here we don't remove this handle from
                   // selector since we want to keep listening to
                   // new client connections
                  }

                  if (handle.isReadable()) {
                    EventHandler handler =
                      registeredHandlers.get(SelectionKey.OP_READ);
                    handler.handleEvent(handle);
                    System.out.println("handle read!");
//                    handleIterator.remove();
                  }

                  if (handle.isWritable()) {
                    EventHandler handler =
                      registeredHandlers.get(SelectionKey.OP_WRITE);
                    handler.handleEvent(handle);
//                    handleIterator.remove();
                  }
                }
//                readyHandles.clear();
                System.out.println("end round");
              }
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
}

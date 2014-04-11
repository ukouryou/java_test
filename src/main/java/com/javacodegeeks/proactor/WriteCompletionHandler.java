/**
 *
 */
package com.javacodegeeks.proactor;

import java.io.IOException;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * Apr 11, 2014
 * @author andy
 */
public class WriteCompletionHandler implements
CompletionHandler<Integer, SessionState> {

private AsynchronousSocketChannel socketChannel;

public WriteCompletionHandler(
  AsynchronousSocketChannel socketChannel) {
  this.socketChannel = socketChannel;
}

@Override
public void completed(
  Integer bytesWritten, SessionState attachment) {
  try {
    socketChannel.close();
  } catch (IOException e) {
    e.printStackTrace();
  }
}

@Override
public void failed(Throwable exc, SessionState attachment) {
 // Handle write failure.....
}

}
/**
 *
 */
package com.javacodegeeks.reactor;

import java.nio.channels.SelectionKey;

/**
 * Apr 11, 2014
 * @author andy
 */
public interface EventHandler {
     public void handleEvent(SelectionKey handle) throws Exception;
}

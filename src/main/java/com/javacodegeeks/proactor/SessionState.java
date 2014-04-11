/**
 *
 */
package com.javacodegeeks.proactor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Apr 11, 2014
 * @author andy
 */
public class SessionState {

    private Map<String, String> sessionProps =
            new ConcurrentHashMap<String, String>();

           public String getProperty(String key) {
             return sessionProps.get(key);
           }

           public void setProperty(String key, String value) {
             sessionProps.put(key, value);
           }
}

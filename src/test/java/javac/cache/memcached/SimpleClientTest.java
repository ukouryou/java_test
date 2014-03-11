/**
 *
 */
package javac.cache.memcached;

import javac.cache.memcached.SimpleClient;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Feb 14, 2014
 * @author andy
 */
public class SimpleClientTest {

    private static  SimpleClient simpleClient;
    @BeforeClass
    public static void beforeClass() {
        simpleClient = new SimpleClient();
    }



    /**
     * Test method for {@link java.cache.memcached.SimpleClient#getNetwork(java.lang.Integer)}.
     */
    @Test
    public void testGetNetwork() {
        simpleClient.getNetwork(31477123);

    }

}

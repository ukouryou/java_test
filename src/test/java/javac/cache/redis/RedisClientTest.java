/**
 *
 */
package javac.cache.redis;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Set;

import javac.cache.redis.RedisClient.SerializeUtil;

import org.junit.Test;

/**
 * Feb 14, 2014
 * @author andy
 */
public class RedisClientTest {

    /**
     * Test method for {@link javac.cache.redis.RedisClient#getNetwork(java.lang.Integer)}.
     */
    @Test
    public void testGetNetwork() {
        RedisClient client = new RedisClient();
        client.getNetwork(9041480);
    }

    @Test
    public void testSerializable() {
        /*RedisClient client = new RedisClient();
        Set<Integer> set = client.getFriendsAtDegree(31477123, 3);
        System.out.println("set size " + set.size());
        String objectValue = String.valueOf(SerializeUtil.serialize(set));
        Set<Integer> hashedSet = (Set<Integer>) SerializeUtil.unserialize(objectValue.getBytes());
        System.out.println("unserialize size" + hashedSet.size());*/

        ArrayList<Integer> list = new ArrayList<Integer>();
        list.add(133);
        list.add(155);
        list.add(166);
        /*ArrayList<Integer> serializedList = (ArrayList<Integer>) SerializeUtil.unserialize(SerializeUtil.serialize(list));

        if (null != serializedList) {
            System.out.println(serializedList.size());
            for (Integer integer : serializedList) {
                System.out.println("id:" + integer);
            }
*/
        RedisClient client = new RedisClient();
        client.getList(list);

        client.getString(list);

    }

}

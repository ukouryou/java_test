/**
 *
 */
package javac.cache.redis;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import redis.clients.jedis.Jedis;

import com.tianji.network4.helper.ByteHelper;

/**
 * Feb 14, 2014
 *
 * @author andy
 */
public class RedisClient {
    private static Jedis jedis = new Jedis("127.0.0.1", 6379);
    private RestTemplate restTemplate = new RestTemplate();

    public void getNetwork(Integer userid) {

        Set<Integer> friendsSet = null;
        long start = System.currentTimeMillis();
        byte[] cachedBytes = jedis.get(userid.toString().getBytes());
        if (null != cachedBytes) {
            System.out.println("find user from cache cost " + (System.currentTimeMillis() - start));
        } else {
            System.out.println("did not get the user from cache");
            long getNetworkStart = System.currentTimeMillis();
            //friendsSet =  getFriendsAtDegree(userid, 3);
            Set<Integer> set1 = ByteHelper.toIntegerSet(getFriendsAtDegree(userid,1));
            System.out.println(set1.size());
            for (Integer integer : set1) {
                System.out.println(integer);
            }
            jedis.set(userid.toString().getBytes(), getFriendsAtDegree(userid,3));
            System.out.println("get network cost:" + (System.currentTimeMillis() - getNetworkStart));
        }
        long end = System.currentTimeMillis();
        cachedBytes =  jedis.get(userid.toString().getBytes());
        friendsSet = ByteHelper.toIntegerSet(cachedBytes);
        if (CollectionUtils.isNotEmpty(friendsSet)) {
            System.out.println(friendsSet.size());
            for (Integer integer : friendsSet) {
                System.out.println(integer);
            }
        } else {
            System.out.println("empty size");
        }
        if (CollectionUtils.isNotEmpty(friendsSet)) {
            System.out.println("find user from cache cost " + (System.currentTimeMillis() - end));
        } else {
            System.out.println("did not get the user from cache");
        }
    }

    public void getList(List<Integer> list) {
        byte[] bytes = SerializeUtil.serialize(list);
        jedis.set("31477123", new String(bytes));
        String cachedBytes = jedis.get("31477123");
        ArrayList<Integer> serializedList = (ArrayList<Integer>) SerializeUtil.unserialize(cachedBytes.getBytes());

        if (null != serializedList) {
            System.out.println(serializedList.size());
            for (Integer integer : serializedList) {
                System.out.println("id:" + integer);
            }
        }
    }

    public void getString(List<Integer> list) {
        byte[] bytes = SerializeUtil.serialize(list);
        jedis.set("31477123", new String("12345"));
        String cachedBytes = jedis.get("31477123");
        System.out.println(cachedBytes);
    }

    public byte[] getFriendsAtDegree(Integer userId, int distance) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept-Encoding", "bin");

        String url = String.format("%s%s/friends/@at/%s/bytearray",
                "http://10.40.3.72:8000/netrest/person/", userId, distance);

        try {
            ResponseEntity<byte[]> first = restTemplate.exchange(url,
                    HttpMethod.GET, new HttpEntity<byte[]>(headers),
                    byte[].class);
            byte[] bytesFirst = first.getBody();
            if (null != bytesFirst && bytesFirst.length > 0) {
                //return ByteHelper.toIntegerSet(bytesFirst);
                return bytesFirst;
            }
        } catch (Exception e) {
            System.out.println("Retrieve friends of user(" + userId
                    + ") at degree(" + distance + ") exception");
        }

        return new byte[1];
    }

    public static class SerializeUtil {
        public static byte[] serialize(Object object) {
            ObjectOutputStream oos = null;
            ByteArrayOutputStream baos = null;
            try {
                // 序列化
                baos = new ByteArrayOutputStream();
                oos = new ObjectOutputStream(baos);
                oos.writeObject(object);
                byte[] bytes = baos.toByteArray();
                return bytes;
            } catch (Exception e) {

            }
            return null;
        }

        public static Object unserialize(byte[] bytes) {
            ByteArrayInputStream bais = null;
            try {
                // 反序列化
                bais = new ByteArrayInputStream(bytes);
                ObjectInputStream ois = new ObjectInputStream(bais);
                return ois.readObject();
            } catch (Exception e) {

            }
            return null;
        }
    }



}

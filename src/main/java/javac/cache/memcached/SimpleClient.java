/**
 *
 */
package javac.cache.memcached;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Set;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.BinaryConnectionFactory;
import net.spy.memcached.MemcachedClient;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * Feb 14, 2014
 * @author andy
 */
public class SimpleClient {

    private MemcachedClient client;
    private RestTemplate restTemplate = new RestTemplate();

    public MemcachedClient getClientUsingSocket() throws IOException {
        MemcachedClient client = new MemcachedClient(new InetSocketAddress("127.0.0.1", 11211));
        return client;
    }

    public MemcachedClient getClientUsingBinaryConnection() throws IOException {
        MemcachedClient client = new MemcachedClient(new BinaryConnectionFactory(),AddrUtil.getAddresses("127.0.0.1:11211"));
        return client;
    }

    public void getNetwork(Integer userid) {
        MemcachedClient client = null;
        try {
            client = getClientUsingSocket();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Set<Integer> friendsSet = null;
        long start = System.currentTimeMillis();
        friendsSet = (Set<Integer>) client.get(userid.toString());
        if (CollectionUtils.isNotEmpty(friendsSet)) {
            System.out.println("find user from cache cost " + (System.currentTimeMillis() - start));
        } else {
            System.out.println("did not get the user from cache");
            long getNetworkStart = System.currentTimeMillis();
            client.set(userid.toString(), 60*2, getFriendsAtDegree(userid, 3));
            System.out.println("get network cost:" + (System.currentTimeMillis() - getNetworkStart));
        }
        long end = System.currentTimeMillis();
        friendsSet = (Set<Integer>) client.get(userid.toString());
        if (CollectionUtils.isNotEmpty(friendsSet)) {
            System.out.println("find user from cache cost " + (System.currentTimeMillis() - end));
        } else {
            System.out.println("did not get the user from cache");
        }
    }

    private Set<Integer> getFriendsAtDegree(Integer userId, int distance) {
       /* HttpHeaders headers = new HttpHeaders();
        headers.add("Accept-Encoding", "bin");

        String url = String.format("%s%s/friends/@at/%s/bytearray",
                "http://10.40.3.72:8000/netrest/person/", userId, distance);

        try {
            ResponseEntity<byte[]> first = restTemplate.exchange(url,
                    HttpMethod.GET, new HttpEntity<byte[]>(headers),
                    byte[].class);
            byte[] bytesFirst = first.getBody();
            if (null != bytesFirst && bytesFirst.length > 0) {
                return ByteHelper.toIntegerSet(bytesFirst);
            }
        } catch (Exception e) {
            System.out.println("Retrieve friends of user(" + userId
                    + ") at degree(" + distance + ") exception");
        }*/
        return new HashSet<Integer>();
    }


}

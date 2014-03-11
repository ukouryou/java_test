/**
 *
 */
package javac.cache.ehcache;

import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.tianji.network4.helper.ByteHelper;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;


/**
 * Feb 17, 2014
 * @author andy
 */
public class EHCacheClient {

    private CacheManager cacheManager = CacheManager.create();
    private RestTemplate restTemplate = new RestTemplate();

    public void testCache(){
        Cache cache = new Cache("test", 10000, false, false, 10000, 20000);
        cacheManager.addCache(cache);
        byte[] bytes = getFriendsAtDegree(31477123, 2);
        Set<Integer> friendsSet = ByteHelper.toIntegerSet(bytes);
        cache.put(new Element("31477123",friendsSet));
        long start = System.currentTimeMillis();
        Element element = cache.get("31477123");
        friendsSet = (Set<Integer>) element.getObjectValue();
        if (CollectionUtils.isNotEmpty(friendsSet)) {
            for (Integer integer : friendsSet) {
                System.out.println(integer);
            }
            System.out.println(friendsSet.size());
            System.out.println("find user from cache cost1 " + (System.currentTimeMillis() - start));
        } else {
            System.out.println("empty size");
        }

         start = System.currentTimeMillis();
        element = cache.get("31477123");
        Set<Integer> friendsSet2 = (Set<Integer>) element.getObjectValue();
        if (CollectionUtils.isNotEmpty(friendsSet2)) {
            System.out.println(friendsSet2.size());
            System.out.println("find user from cache cost2 " + (System.currentTimeMillis() - start));
        } else {
            System.out.println("empty size");
        }

        long start3 = System.currentTimeMillis();
        element = cache.get("31477123");
        Set<Integer> friendsSet3 = (Set<Integer>) element.getObjectValue();
        if (CollectionUtils.isNotEmpty(friendsSet3)) {
            System.out.println(friendsSet3.size());
            System.out.println("find user from cache cost3 " + (System.currentTimeMillis() - start3));
        } else {
            System.out.println("empty size");
        }

    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        EHCacheClient client = new EHCacheClient();
        client.testCache();
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

}

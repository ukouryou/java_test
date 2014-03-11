/**
 *
 */
package search.test.es;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Feb 17, 2014
 * @author andy
 */
public class TestClient {

    Client client = new TransportClient().addTransportAddress((new InetSocketTransportAddress("localhost", 9300)));


    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @Test
    public void test() throws ElasticsearchException, IOException {


        IndexResponse response = client.prepareIndex("twitter", "tweet", "1")
                .setSource(jsonBuilder()
                            .startObject()
                                .field("user", "kimchy")
                                .field("postDate", new Date())
                                .field("message", "trying out Elastic Search")
                            .endObject()
                          )
                .execute()
                .actionGet();

        SearchResponse getResponse = client.prepareSearch("twitter")
                                            .setTypes("tweet")
                                            .execute()
                                            .actionGet();
        SearchHits hits = getResponse.getHits();


        Iterator<SearchHit> iterator = hits.iterator();

        while (iterator.hasNext()) {
            SearchHit searchHit = (SearchHit) iterator.next();
            System.out.println(searchHit.getId());
            System.out.println(searchHit.getType());
        }


    }
    @Test
    public void testFloat() {


    }


}

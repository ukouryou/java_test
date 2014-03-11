/**
 *
 */
package search.test;

import org.apache.blur.lucene.search.SlowQuery;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.junit.Test;

/**
 * @author andy
 *
 */
public class BlurTest {

    @Test
    public void testQuery() {
        Query query= new TermQuery(new Term("sss","sss"));
        SlowQuery slowQuery = new SlowQuery(query);

    }

}

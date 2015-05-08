/**
 *
 */
package basic;

import java.util.HashSet;
import java.util.Set;

import org.elasticsearch.common.base.Joiner;
import org.junit.Test;

/**
 * Jul 3, 2014
 * @author andy
 */
public class TempTest {

    @Test
    public void testInterger() {
        Float floato = new Float(3.0000);
        System.out.println(floato.toString());
        Float f = Float.NaN;
        System.out.println(f == f);
    }

    @Test
    public void testJOiner() {
        Joiner joiner = Joiner.on(" OR ").skipNulls();
        Set<String> companiesSet = new HashSet<>();
        companiesSet.add("");
        companiesSet.add("ba");
        StringBuilder companyBuilder = new StringBuilder();
        String companies = companyBuilder.append("(")
                                         .append(joiner.join(companiesSet))
                                         .append(")").toString();
        System.out.println(companies);
    }
}

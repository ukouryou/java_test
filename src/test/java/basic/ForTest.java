/**
 *
 */
package basic;

import org.junit.Test;

/**
 * @author andy
 *
 */
public class ForTest {
    @Test
    public void tstFor() {
        for (int i = 0;
                i < 2;
                i++) {
            System.out.println(i);
        }
        System.out.println("here");
    }

    @Test
    public void testStringBuilderf() {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("title:engineer  city:(北京5");
        if (0 < stringBuilder.length()){
            stringBuilder.insert(1, "(");
            stringBuilder.insert(stringBuilder.length(), ")");
        }
        System.out.println(stringBuilder.toString());
    }


}

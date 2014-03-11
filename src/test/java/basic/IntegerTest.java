/**
 *
 */
package basic;

import org.junit.Test;

/**
 * @author andy
 *
 */
public class IntegerTest {

    @Test
    public void testIntegerEqual() {

        System.out.println((Integer)500==((Integer)(500+0)).intValue());
        System.out.println((Integer)500==(Integer)(500+0));

        Integer x = 127;
        Integer y = 127;
        System.out.println(x == y + 0);
        System.out.println(x == y);


        int i = 2;
        int j = (i=3) * i;
        System.out.println(j);
    }

}

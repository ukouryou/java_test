/**
 *
 */
package basic;

import java.math.BigInteger;

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

    @Test
    public void testIntValue() {
        System.out.println(-3 >>>7>>>7>>>7>>>7);
        System.out.println(Integer.toBinaryString(-3));
        int i = 256;
        int j = i & ~0x7F;
        String number = "0x80";
        //BigInteger bigInteger = new BigInteger(number,16);
//        System.out.println(bigInteger.toString(2));
        System.out.println(j);
        System.out.println((i & 0x80));
        System.out.println(0x7F);
        System.out.println(~0x7F);
        System.out.println(0x80);
        writeVInt(-3);

    }

    @Test
    public void testVint() {
        writeVInt(128);
    }


    public final void writeVInt(int i) {
             while ((i & ~0x7F) != 0) {
               System.out.println(((byte)((i & 0x7F) | 0x80)));
               System.out.println("while");
               i >>>= 7;
             }
             System.out.println(((byte)i));
           }

}

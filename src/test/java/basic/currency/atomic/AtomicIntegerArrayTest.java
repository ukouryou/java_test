/**
 *
 */
package basic.currency.atomic;

import static org.junit.Assert.*;

import java.util.concurrent.atomic.AtomicIntegerArray;

import org.junit.Test;

/**
 * Jan 28, 2014
 * @author andy
 */
public class AtomicIntegerArrayTest {

    @Test
    public void testAll() {
        AtomicIntegerArray aia = new AtomicIntegerArray(10);
        aia.set(2, 5);
        assertEquals(aia.get(2),5);
        System.out.println(((long) 5 << 2) + 16);
        System.out.println(2<<3);
        System.out.println(3<<2);
    }
}

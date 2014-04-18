/**
 *
 */
package nio;

import static org.junit.Assert.*;

import java.nio.ByteBuffer;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Apr 18, 2014
 * @author andy
 */
public class ByteBufferTest {

    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @Test
    public void testSlice() {
        ByteBuffer byteBuffer = ByteBuffer.wrap("hello".getBytes());
        assertEquals(0, byteBuffer.position());
        assertEquals(5, byteBuffer.limit());
        System.out.println((char)byteBuffer.get());
        assertEquals(1, byteBuffer.position());
        ByteBuffer sliceBuffer = byteBuffer.slice();
        assertEquals(4,sliceBuffer.capacity());
        sliceBuffer.put(0, (byte)'a');
        assertEquals((byte)'a',byteBuffer.get(1));
        System.out.println((char)byteBuffer.get(1));
    }

}

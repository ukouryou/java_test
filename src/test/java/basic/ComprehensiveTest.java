/**
 *
 */
package basic;

import java.text.ParseException;
import java.util.Vector;

import org.junit.Test;

/**
 * @author andy
 *
 */
public class ComprehensiveTest {

    private boolean hasAborted = true;
    @Test
    public void testDateFormat() throws ParseException {
        Vector<? extends Throwable> y = new Vector<Throwable>();
        System.out.println(y.getClass());
    }

    interface Event{

    }

    static interface Handler{

    }

    class EventC{

    }

    static class HandlerC{

    }

}

/**
 *
 */
package basic;

import org.junit.Test;


/**
 * @author andy
 *
 */
public class Grammer {

    @Test
    public void testLiterals() {

        int a = 123_456;

        char c = 'A';
        System.out.println(a);

        String s = "  @@ ";
        System.out.println("[" + s.trim() + "]");

    }

    @Test
    public void testInterface() {
//        Event event = new Event() {
//        };
//        Handler handler = new Handler() {
//        };
////        EventC eventC = new EventC();
//        HandlerC handlerC = new HandlerC();
    }

}

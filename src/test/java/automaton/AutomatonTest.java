/**
 *
 */
package automaton;

import static org.junit.Assert.*;

import org.junit.Test;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.RegExp;

/**
 * Jun 17, 2014
 * @author andy
 */
public class AutomatonTest {

    @Test
    public void test() {
        RegExp regExp = new RegExp("a*b|b*a");
        Automaton automaton = regExp.toAutomaton();
        System.out.println(automaton.toString());

        String s = "ab";
        System.out.println("------------------");
        System.out.println("match " + automaton.run(s));
    }

    @Test
    public void testIntersection() {
    }

}

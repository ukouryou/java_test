/**
 *
 */
package basic.currency.atomic;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/**
 * Jan 29, 2014
 *
 * @author andy
 */
public class AtomicReferenceFieldUpdaterTest {

    class DemoClass {
        volatile int age = 22;
    }

    void doit(int newValue) {
        DemoClass demoClass = new DemoClass();
        AtomicReferenceFieldUpdater arfu = AtomicReferenceFieldUpdater
                .newUpdater(DemoClass.class, int.class, "age");
        arfu.set(demoClass, newValue);
        System.out.println(demoClass.age);
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        AtomicReferenceFieldUpdaterTest atomicReferenceFieldUpdaterTest = new AtomicReferenceFieldUpdaterTest();
        atomicReferenceFieldUpdaterTest.doit(32);
    }

}

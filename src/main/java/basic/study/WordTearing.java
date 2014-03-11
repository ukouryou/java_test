/**
 *
 */
package basic.study;

import sun.misc.Unsafe;

/**
 * Jan 28, 2014
 *
 * @author andy
 */
public class WordTearing extends Thread {
    private static final Unsafe unsafe = Unsafe.getUnsafe();
    static final int LENGTH = 8;
    static final int ITERS = 1000000;
    static byte[] counts = new byte[LENGTH];
    static Thread[] threads = new Thread[LENGTH];
    final int id;

    WordTearing(int i) {
        id = i;
    }

    public void run() {
        /*byte v = 0;
        unsafe.compareAndSwapInt(this, valueOffset, expect, update);
        for (int i = 0; i < ITERS; i++) {
            byte v2 = counts[id];
            if (v != v2) {
                System.err.println("Word-Tearing found: " + "counts[" + id
                        + "] = " + v2 + ", should be " + v);
                return;
            }
            v++;
            counts[id] = v;
        }*/
    }

    public static void main(String[] args) {
        for (int i = 0; i < LENGTH; ++i)
            (threads[i] = new WordTearing(i)).start();
    }
}
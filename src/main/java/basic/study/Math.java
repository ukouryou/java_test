/**
 *
 */
package basic.study;


/**
 * Jul 9, 2014
 * @author andy
 */
public class Math {

    public static int log(int base, int N) {
        if (base < 2 || N <= 1) {
            return -1;
        }
        int result = 0;
        for (int i = N; i > 1; i = i/base) {
            result++;
        }

        return result;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        System.out.println(java.lang.Math.log10(101));
        System.out.println(Math.log(10, 101));

    }

}

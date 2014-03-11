/**
 *
 */
package basic;

/**
 * Feb 28, 2014
 * @author andy
 */
public class TryCatchTest {

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        TryCatchTest test = new TryCatchTest();
System.out.println(test.getValue());

    }



    public int getValue() throws Exception {
        try {
            System.out.println("1");
            if (1 ==1) {
                throw new Exception();
            }
            System.out.println("4");
            return 1;
        } catch (Exception e) {
            System.out.println("2");
            //return -1;
            throw e;
        } finally {
            System.out.println("3");
        }
        //return 0;
    }

}

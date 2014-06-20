package basic.study;


public class FinallyTest {

    public int inc() {
        int x ;
        try {
            x = 1;
            return x;
        } catch (Exception e) {
            x = 2;
            return x;
        } finally {
            x = 3;
            System.out.println("x is "+ x);
        }
    }

    public int test2(int x) {
        try {
            if (x == 1) {
                try {
                    if (x == 1) {
                        System.out.println("internal x ==1 ");
                        return 3;
                    }
                } finally {
                    System.out.println("internal finally");
                }
            }
            return 2;
        } finally {
            System.out.println("outer finally");
        }
    }

    public static void main(String[] args) {


        FinallyTest test = new FinallyTest();
        System.out.println("case1:"+test.inc());
        System.out.println(test.test2(1));
    }

}

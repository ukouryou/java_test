/**
 *
 */
package modifiers;

/**
 * Mar 25, 2014
 * @author andy
 */
class Test{
    private static int i=0;
    public int getNext(){
       return i++;
    }
}
public class TestB{
    public static void main(String[] args){
        Test test=new Test();
        System.out.println(test.getNext());
        Test testObject=new Test();
        System.out.println(testObject.getNext());
        System.out.println(test.getNext());
    }
}

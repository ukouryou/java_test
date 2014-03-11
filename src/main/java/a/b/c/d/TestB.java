/**
 *
 */
package a.b.c.d;

import a.b.c.Test;

/**
 * Feb 25, 2014
 * @author andy
 */
public class TestB{
    private String name;
    private String desc;
   /* public TestB(){
       Test test=new Test();
       name=test.name;
       desc=test.desc;
    }*/

    public static void main(String[] args){
        Test test=new Test();
        System.out.println(test.getNext());
        Test testObject=new Test();
        System.out.println(testObject.getNext());
        System.out.println(test.getNext());
    }
}

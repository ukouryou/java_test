package string;

public class Difference {

    private String s = "a";
    private String s2 = new String("a");

    public static void main(String[] args) {
        String a=new String("a");
        String b="b";
        String c=a+b;
        StringBuffer strBuf=new StringBuffer();
        strBuf.append("a");
        strBuf.append("b");
        System.out.println(c.intern().equals(strBuf.toString().intern()));
        System.out.println(c == strBuf.toString());
        System.out.println(c == strBuf.toString());
        System.out.println(c.intern() == strBuf.toString().intern());
        String d=strBuf.toString();
        StringBuilder strBuilder=new StringBuilder();
        strBuilder.append("a");
        strBuilder.append("b");
        String e=strBuilder.toString();
        System.out.println(c.intern() == e.intern());
    }

}

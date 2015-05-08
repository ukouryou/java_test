package java8.basic.threading;

public class SecuredClass {
    private static final String securedField = new String("SecretData");
    public static final int intVal = new Integer(1);
    public static String getSecretData() { return securedField; }
}
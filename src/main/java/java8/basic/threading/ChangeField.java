package java8.basic.threading;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class ChangeField {
    static void setFinalStatic(Field field, Object newValue) throws Exception
    {
        field.setAccessible(true);

        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~ Modifier.FINAL);

        field.set(null, newValue);
        System.out.println("show new value");
    }

    public static void main(String args[]) throws Exception
    {
        System.out.println("Before = " + SecuredClass.getSecretData());

        Field stringField = SecuredClass.class.getDeclaredField("securedField");
//        Field stringField = SecuredClass.class.getField("securedField");
        stringField.setAccessible(true);
        setFinalStatic(stringField, "Screwed Data!");
        
        
        Field intField = SecuredClass.class.getField("intVal");
        intField.setAccessible(true);
        setFinalStatic(intField,2);
        

        System.out.println("After = " + SecuredClass.getSecretData());
        System.out.println(SecuredClass.intVal);
    }
}

package jvm.classloading;

public class SubClass extends SuperClass{

	static {
		System.out.println("Subclass init!");
	}
}

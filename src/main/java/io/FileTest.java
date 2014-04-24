package io;

import java.io.File;

public class FileTest {

	public static void main(String[] args) {

		File file = new File("/home/andy/Downloads");
//		File file = new File(".");
		for (String string : file.list()) {
			System.out.println(string);
		}
		
		System.out.println("~~~~~~~~~``");
		
		for (File file1 : file.listFiles()) {
			System.out.println(file1.toString());
			
		}
		
	}

}

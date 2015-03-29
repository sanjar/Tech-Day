package com.sanjar.gcm;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Test {
public static void main(String[] args) {
	File file = new File("test.txt");
	PrintWriter printWriter=null;
	try {
		System.out.println(file.createNewFile());
		 printWriter= new PrintWriter(new FileWriter(file,true));
		printWriter.append("hello");;
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}finally{
		printWriter.close();
	}
}
}

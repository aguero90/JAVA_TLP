package lezione11;

import java.io.FileNotFoundException;
import java.io.IOException;

public class WithFinally2 {

	public static void f() throws IOException {
	  try {
		  throw new IOException();
	  } catch (FileNotFoundException e) {
		// TODO: handle exception
	  } finally {
		  System.out.println("finally in f()");
	  }
		
	} 
	
	public static void g() {
		try {
			f();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Catch in g()");
		}
	}
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		g();
	}

}

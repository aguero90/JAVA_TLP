package lezione13;

import java.io.*;

public class IOStreamDemo {
 
  // Throw exceptions to console:
  public static void main(String[] args) throws IOException {
    // 1. Reading input by lines:
    BufferedReader in = new BufferedReader(
      new FileReader("src/lezione13/IOStreamDemo.java"));
    String s, s2 = new String();
    while((s = in.readLine())!= null)
      s2 += s + "\n";
    in.close();
 
    // 1b. Reading standard input:
    BufferedReader stdin = new BufferedReader(
      new InputStreamReader(System.in));
    System.out.print("Enter a line:");
    System.out.println(stdin.readLine());
 

    // 2. Input from memory
    System.out.println("Esecuzione 2");
    StringReader in2 = new StringReader(s2);
    int c;
    while((c = in2.read()) != -1)
      System.out.print((char)c);
 

    // 3. Formatted memory input
    System.out.println("Esecuzione 3");
    try {
      DataInputStream in3 = new DataInputStream(
        new ByteArrayInputStream(s2.getBytes()));
      while(true)
        System.out.print((char)in3.readByte());
    } catch(EOFException e) {
      System.err.println("End of stream");
    }

    // 4. File output
    System.out.println("Esecuzione 4");
    try {
      BufferedReader in4 = new BufferedReader(
        new StringReader(s2));
      PrintWriter out1 = new PrintWriter(
      	new BufferedWriter(new FileWriter("src/lezione13/IODemo.out")));
     // BufferedWriter out1 = new BufferedWriter(new FileWriter("src/lezione13/IODemo.out"));
    		      
      int lineCount = 1;
      while((s = in4.readLine()) != null )
    	  out1.println(lineCount++ + ": " + s);
    	  out1.write(lineCount++ + ": " + s);
      out1.close();
    } catch(EOFException e) {
      System.err.println("End of stream");
    }
     
     
    
    // 5. Storing & recovering data
    System.out.println("Esecuzione 5");
    try {
      DataOutputStream out2 = new DataOutputStream(
        new BufferedOutputStream(
          new FileOutputStream("src/lezione13/Data.txt")));
      out2.writeDouble(3.14159);
      out2.writeUTF("That was pi");
      out2.writeDouble(1.41413);
      out2.writeUTF("Square root of 2");
      out2.close();
      DataInputStream in5 = new DataInputStream(
        new BufferedInputStream(
          new FileInputStream("src/lezione13/Data.txt")));
    
     // System.out.println(in5.readUTF());
      // Must use DataInputStream for data:
      System.out.println(in5.readDouble());
      // Only readUTF() will recover the
      // Java-UTF String properly:
      System.out.println(in5.readUTF());
      // Read the following double and String:
      System.out.println(in5.readDouble());
      System.out.println(in5.readUTF());
    } catch(EOFException e) {
      throw new RuntimeException(e);
    }

    // 6. Reading/writing random access files
    System.out.println("Esecuzione 6");
    RandomAccessFile rf = new RandomAccessFile("src/lezione13/rtest.dat", "rw");
    for(int i = 0; i < 10; i++)
      rf.writeDouble(i*1.414);
    rf.close();
    rf = new RandomAccessFile("src/lezione13/rtest.dat", "rw");
    rf.seek(5*8);
    rf.writeDouble(47.0001);
    rf.close();
    rf = new RandomAccessFile("src/lezione13/rtest.dat", "r");
    for(int i = 0; i < 10; i++)
      System.out.println("Value " + i + ": " +
        rf.readDouble());
    rf.close();   
    
  }
} 
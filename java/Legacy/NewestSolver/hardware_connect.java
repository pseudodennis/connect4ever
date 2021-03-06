//package Legacy.hardware_connect;
import com.fazecast.jSerialComm.SerialPort;

import java.io.PrintWriter;
import java.util.Scanner;


/**
 * @author nospa
 *
 */
public class hardware_connect {
   static SerialPort portSel;
   static PrintWriter output;
	
	/**
	 * 
	 */
   static Scanner inputNum ;
   public static boolean Connect() //connects to and selects the serial port
   {
      Scanner keyboard = new Scanner(System.in);
      boolean isConnected = false;
      String comInput;
      boolean errorExit = false;
      do {	
         System.out.println("Please select a communication port.");
    	 SerialPort[] portNames = SerialPort.getCommPorts();
         for(int i = 0; i < portNames.length; i++)
            System.out.println(portNames[i].getSystemPortName());
      
      
      	// attempt to connect to the serial port
         comInput = keyboard.nextLine();
         portSel = SerialPort.getCommPort(comInput);
         portSel.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 0, 0);
         if(portSel.openPort()) {
            isConnected = true;
            inputNum = new Scanner(portSel.getInputStream());
         	// create a new thread for sending data to the arduino
            Thread thread = 
               new Thread(){
                  @Override public void run() {
                  // wait after connecting, so the bootloader can finish
                     try {Thread.sleep(100); } 
                     catch(Exception e) {}
                  
                  
                  }
               };
            thread.start();
            output = new PrintWriter(portSel.getOutputStream());
         }
         
         else {
         // disconnect from the serial port
            portSel.closePort();
            isConnected = false;
         }
         if(isConnected == false && !comInput.equals("exit"))
         {
            System.out.println("Error not connected\n\rtry again\n\r");
         }
         else if  (comInput.equals("exit"))
         {
            System.out.println("exiting");
            errorExit = true;
         }
         else
         {
            System.out.println("connected");
         }
      }while(isConnected == false && !comInput.equals("exit"));
      return (errorExit);
   }

   public static void starter (boolean humanStart)
   {
      int numStart = 33;
   	
      if (humanStart == true)
      {
         numStart = 77;
      }
   	
      output.println();
      output.flush();
      try {Thread.sleep(100); } 
      catch(Exception e) {}
      
      try        
      {
         Thread.sleep(5678);
      } 
      catch(InterruptedException ex) 
      {
         Thread.currentThread().interrupt();
      }
      
      
      output.print(numStart);
      output.flush();
      try {Thread.sleep(100); } 
      catch(Exception e) {}
   	
      System.out.println(numStart);
   	
   }
	
   public static void sendNum (int num) // send number data to the serial port
   {
   	// enter an infinite loop that sends text to the arduino
   	
      output.print(num);
      output.flush();
      try {Thread.sleep(100); } 
      catch(Exception e) {}
   	
   }
	
   public static int getNum () // get number data from the serial port
   {
      int num = 0;
      do {
         num = 0;
         try{num = Integer.parseInt(inputNum.nextLine());}
         catch(Exception e){}
      
      }while(num == 0);
      return (num);
   }
	
   public static void DisConnect() // disconnect from the serial port
   {
      output.print(50);
      output.flush();
      try {Thread.sleep(100); } 
      catch(Exception e) {}
      
      int exitNum = 0;
      do{
      try {Thread.sleep(1); } 
      catch(Exception e) {}
   
      try{exitNum = Integer.parseInt(inputNum.nextLine());}
      catch(Exception e){}
      
      
      }while(exitNum != 99);
      System.out.println("arduino stopped");   
      portSel.closePort();
      System.out.println("disconnected");
   }
}

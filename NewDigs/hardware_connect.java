//package hardware_connect;
import com.fazecast.jSerialComm.SerialPort; //imports classes for serial port connection

import java.io.PrintWriter; // imports classes for java to print out to serial terminal
import java.util.Scanner;


public class hardware_connect {
   static SerialPort portSel; 
   static PrintWriter output;
	
	
   static Scanner inputNum ;
   public static boolean Connect() //connects to and selects the serial port
   {
      Scanner keyboard = new Scanner(System.in);
      boolean isConnected = false;   //boolean used to tell if java is connect to a serial port
      String comInput;              //used for port selection
      boolean errorExit = false;   //boolean used to exit loop and class if there a connection error
      do {	
         System.out.println("Please select a communication port.");
    	 SerialPort[] portNames = SerialPort.getCommPorts();
         for(int i = 0; i < portNames.length; i++)               //prints out list of ports for user to select
            System.out.println(portNames[i].getSystemPortName());
      
      
      	// attempt to connect to the serial port based on user input
         comInput = keyboard.nextLine();
         portSel = SerialPort.getCommPort(comInput);
         portSel.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 0, 0);
         if(portSel.openPort()) {   //if selected port is open, it will try to connect to that port 
            isConnected = true;    //set to true to exit error checking loop
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
         //if port is not open, it will disconnect any attenpt to connect from that serial port
            portSel.closePort();  
            isConnected = false; //set to false to keep in error checking loop
         }
         if(isConnected == false && !comInput.equals("exit")) // message for when it's not connect and user didn't enter "exit"
         {
            System.out.println("Error not connected\n\rtry again\n\r");
         }
         else if  (comInput.equals("exit"))   // message for when user enters "exit"
         {
            System.out.println("exiting");
            errorExit = true;              // set to true to exit error checking loop
         }
         else // message for when it connects to user selected port
         {
            System.out.println("connected");
         }
      }while(isConnected == false && !comInput.equals("exit")); // exits loop if connected to user selected port or if uesr enters exit
      return (errorExit); // returns if there was an error, if theres an error in connecting, the class that called it can use the return value to stop and pervent other errors higher up
   }

   public static void starter (boolean humanStart) // used to tell the arduino if a human or computer is going first
   {
      int numStart = 33; // 33 tells the arduino that the computer make the first move
   	
      if (humanStart == true) 
      {
         numStart = 77;       // if human goes first, the number is changed to 77 to tell the arduino that a human is going first
      }
   	
      // clears any exart data from the termanal and seral port
      // mainly used to clear prevous data from last run 
      output.println();
      output.flush();
      try {Thread.sleep(100); } 
      catch(Exception e) {}
      
      // makes the program pause for about 5 second, so the arduino can finnish setting up for user input and output through the serial port   
      try        
      {
         Thread.sleep(5678);
      } 
      catch(InterruptedException ex) 
      {
         Thread.currentThread().interrupt();
      }
      
      
      output.print(numStart); // sends the 33 or 77 to arduino  
      output.flush();
      try {Thread.sleep(100); } 
      catch(Exception e) {}
   	
      System.out.println(numStart); //prints out the 33 or 77
   	
   }
	
   public static void sendNum (int num) // sends number data to the arduino
   {
   	
      output.print(num); // sends number to seral port
      output.flush();
      try {Thread.sleep(100); } 
      catch(Exception e) {}
   	
   }
	
   public static int getNum () // get number data from the serial port
   {
      int num = 0;
      do {            // arduino will constantly send 0s until it is told to send something else
                     // the loop allows the code reject all the 0's and wait for actual data from the arduino 
         num = 0;
         try{num = Integer.parseInt(inputNum.nextLine());} //gets number from the seral port
         catch(Exception e){}
      
      }while(num == 0);
      return (num); //returns non 0 number from the arduino
   }
	
   public static void DisConnect() // tells the arduino to stop running the code and shutdown and then disconnects from the serial port 
   {
      output.print(50); // sends a 50 to the arduino, this tell the arduino to stop running the code and shutdow
      output.flush();
      try {Thread.sleep(100); } 
      catch(Exception e) {}
      
      int exitNum = 0;
      do{                    // enters a loop that waits for the arduino to send a 99 so java know that the arduino got the 50 and has started to shutdown
      try {Thread.sleep(1); } 
      catch(Exception e) {}
   
      try{exitNum = Integer.parseInt(inputNum.nextLine());} //gets a number from the seral port, hopely its a 99
      catch(Exception e){}
      
      
      }while(exitNum != 99);
      System.out.println("arduino stopped");   
      portSel.closePort();                     //disconnect from the serial port and closes the connection
      System.out.println("disconnected");
   }
}

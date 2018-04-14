// package hardware_connect;

import java.util.Scanner;

public class tester {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner keyboard = new Scanner(System.in);
		int num;
		char again;
		boolean error;
		error = hardware_connect.Connect();
		if(error == false)
		{
		boolean done = false;
		do {
		System.out.println();
      System.out.print("input number: ");
		num = keyboard.nextInt();
      System.out.println();
		hardware_connect.sendNum(num);
		System.out.print("output number: ");
		System.out.println(hardware_connect.getNum());
		System.out.println();
		System.out.println("enter more numbers");
		again = keyboard.next().charAt(0);
		if (again == 'n')
		{
			done = true; 
		}
		}while(done == false);
		}
		hardware_connect.DisConnect();
	}

}

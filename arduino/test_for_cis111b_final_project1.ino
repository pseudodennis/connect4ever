#include <Servo.h>
int locSWstate;
int tickCount;
Servo coinMove;

void setup()
{
  pinMode(2, INPUT);
  pinMode(3, INPUT);
  pinMode(4, INPUT);
  pinMode(5, INPUT);
  pinMode(8, INPUT);
  pinMode(9, INPUT);
  pinMode(10, INPUT);
  
  pinMode(7, OUTPUT);
  pinMode(6, OUTPUT);
  pinMode(12, OUTPUT);
  
  coinMove.attach(13);
}

void loop()
{
   
    locSWstate = 2;
  	//switchCheck();
  	beepCount(locSWstate);
  	//switchCheck();
  	beepCount(locSWstate);
  	move();
  	
}
  
   static int switchCheck()
   {
     bool exitLoop = false; 
     
     int pinNum;
     int count;
     do{
       pinNum = 2;
       count = 1;
       do{
     	
    	if (digitalRead(pinNum) == HIGH)
     	{
       		locSWstate = count;
       		exitLoop = true;
     	}  
     	count++;
     	pinNum++;
     	}while(exitLoop == false && pinNum < 6); 
       
       pinNum = 8;
       while(exitLoop == false && pinNum < 11)
       {
     
    	
    	if (digitalRead(pinNum) == HIGH)
     	{
       		locSWstate = count;
       		exitLoop = true;
     	}  
     	count++;
     	pinNum++;
       }
     } while(exitLoop == false);
     return (locSWstate);
   }

	static void beepCount(int count)
    {
      for(int i = 0; i < count; i++)
      {
        indaOUTPUTon();
        delay(100);
        indaOUTPUToff();
        delay(100);
      }
    }
	static void indaOUTPUTon()
    {
      digitalWrite(6, HIGH);
      digitalWrite(12, HIGH);
    }
	
	static void indaOUTPUToff()
    {
      digitalWrite(6, LOW);
      digitalWrite(12, LOW);
    }
	
	static void dropCoin()
    {
      if(coinMove.read() != 0)
      {
       	coinMove.write(0); 
      }
      delay(1000);
      coinMove.write(180);
      delay(1000);
      coinMove.write(0);
      delay(1000);
    }
	static void move()
    {
      	digitalWrite(7, HIGH);
  		tickCount = 0;
  		while (digitalRead(11) == LOW && tickCount != locSWstate)
    	{
  			while (digitalRead(11) == LOW)
    		{
          		delay(1);
        	}
     	 	tickCount++;
    	}
  		dropCoin();
    }

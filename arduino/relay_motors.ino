/*
  Blink

  Turns an LED on for one second, then off for one second, repeatedly.

  Most Arduinos have an on-board LED you can control. On the UNO, MEGA and ZERO
  it is attached to digital pin 13, on MKR1000 on pin 6. LED_BUILTIN is set to
  the correct LED pin independent of which board is used.
  If you want to know what pin the on-board LED is connected to on your Arduino
  model, check the Technical Specs of your board at:
  https://www.arduino.cc/en/Main/Products

  modified 8 May 2014
  by Scott Fitzgerald
  modified 2 Sep 2016
  by Arturo Guadalupi
  modified 8 Sep 2016
  by Colby Newman

  This example code is in the public domain.

  http://www.arduino.cc/en/Tutorial/Blink
*/

// the setup function runs once when you press reset or power the board
bool on = false;
bool sw1 = false;
bool sw2 = false;

void setup() {
  // initialize digital pin LED_BUILTIN as an output.
  pinMode(9, OUTPUT);
  pinMode(11, OUTPUT);
  digitalWrite(9, HIGH);
  digitalWrite(11, HIGH);
  pinMode(3, INPUT);
  pinMode(4, INPUT);
  Serial.begin(9600);
  Serial.setTimeout(50);
  
}

// the loop function runs over and over again forever
void loop() {
  //sw1 = digitalRead(3);
 // sw2 = digitalRead(4);
  if( digitalRead(3) == HIGH || digitalRead(4) == HIGH)
  {
    on = !on;
   /* if(on == false)
    {
      on = true;
    }
    else //if (on == true)
    {
      on = false;
    } */
  }
  Serial.println(on);
  while(on == true && digitalRead(3) == LOW && digitalRead(4) == LOW)
  {
  digitalWrite(11, LOW);
  digitalWrite(9, HIGH);   // turn the LED on (HIGH is the voltage level)
  delay(150);                       // wait for a second
  digitalWrite(9, LOW);    // turn the LED off by making the voltage LOW
  delay(153);
  }
    
      digitalWrite(9, HIGH);
      digitalWrite(11, HIGH);
    
  }

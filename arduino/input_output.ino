int starter = 0;
int numOut = 0;
int numIn;
void setup() {
  // put your setup code here, to run once:
  pinMode(LED_BUILTIN, OUTPUT);
  pinMode(7, OUTPUT);
  Serial.begin(9600);
  Serial.setTimeout(50);
  LED();
  digitalWrite(LED_BUILTIN, HIGH);
  do
  {
    starter = Serial.parseInt();
  }while (starter != 33 && starter != 77);
    if(starter == 33)
  {
       Serial.println("computer");
  }
  else if (starter == 77)
  {
     Serial.println("human");
  }
  Serial.println();
}

void loop() {
  // put your main code here, to run repeatedly:

  if (starter == 77) //human starts
  {
      
    numIn = 0;
    in();
    numOut = 0;
    out();
  }
  else // computer starts
  {
    numOut = 0;
    out();
    numIn = 0;
    in();
  }
 
}
void out() 
{
   do{
  numOut = Serial.parseInt();
  for(int i = 1; i <= numOut; i++)
  {
  if (numOut == 50)
  {
    Exit50();
  }
  else
  {
  digitalWrite(7, HIGH);
  delay(500);
  digitalWrite(7, LOW);
  delay(500);
  }
  }
  }while(numOut == 0);
 }
void in()
{
    
  do{
   if(digitalRead(3) == HIGH && digitalRead(4) == LOW)
  {
    numIn = 1;
  }
  else if(digitalRead(4) == HIGH && digitalRead(3) == LOW)
  {
    numIn = 2;
  }
  else if(digitalRead(4) == HIGH && digitalRead(3) == HIGH)
  {
    numIn = 3;
  }
  else if(digitalRead(5) == HIGH)
  {
    numIn = 4;
  }
  else if(digitalRead(6) == HIGH)
  {
    numIn = 5;
  }
  else if(digitalRead(8) == HIGH)
  {
    numIn = 6;
  }
 /* else if (digitalRead(12) == HIGH)
  {
    numIn = 7;
  } */
  else
  {
    numIn = 0;
  }
  Serial.println(numIn);
  delay(1);
  }while(numIn == 0);
}

void LED()
{
  digitalWrite(LED_BUILTIN, HIGH);   // turn the LED on (HIGH is the voltage level)
  delay(500);                       // wait for a second
  digitalWrite(LED_BUILTIN, LOW);    // turn the LED off by making the voltage LOW
  delay(500);  // wait for a second
  digitalWrite(LED_BUILTIN, HIGH);
  delay(500);
  digitalWrite(LED_BUILTIN, LOW);
  delay(500);
  digitalWrite(LED_BUILTIN, HIGH);
  delay(500);
  digitalWrite(LED_BUILTIN, LOW);
  delay(500);
}

void Exit50()
{
  Serial.println("exiting");
    Serial.println("99");
    delay(22);
    LED();
    exit(0);
}



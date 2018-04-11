void setup() {
  // put your setup code here, to run once:
  pinMode(7, OUTPUT);
  Serial.begin(9600);
  Serial.setTimeout(50);
}

void loop() {
  // put your main code here, to run repeatedly:
  int numOut = 0;
  int numIn;
  do{
  numOut = Serial.parseInt();
  for(int i = 1; i <= numOut; i++)
  {
  digitalWrite(7, HIGH);
  delay(500);
  digitalWrite(7, LOW);
  delay(500);
  }
  }while(numOut == 0);
  numOut = 0;
  numIn = 0;
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
  else
  {
    numIn = 0;
  }
  Serial.println(numIn);
  delay(1);
  }while(numIn == 0);
  //Serial.println();
}

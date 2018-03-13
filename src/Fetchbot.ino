  //Pin positions for sensor 4
  const int trigPin4 = 9;
  const int echoPin4 = 8;
    //Pin positions for sensor 3
  const int trigPin3 = 7;
  const int echoPin3 = 6;
    //Pin positions for sensor 2
  const int trigPin2 = 5;
  const int echoPin2 = 4;
    //Pin positions for sensor 1
  const int trigPin1 = 3;
  const int echoPin1 = 2;

  float duration1, distance1;
  float duration2, distance2;
  float duration3, distance3;
  float duration4, distance4;
  
  
void setup() 
{
  pinMode(trigPin4, OUTPUT);
  pinMode(echoPin4, INPUT);
  
  pinMode(trigPin3, OUTPUT);
  pinMode(echoPin3, INPUT);
  
  pinMode(trigPin2, OUTPUT);
  pinMode(echoPin2, INPUT);
  
  pinMode(trigPin1, OUTPUT);
  pinMode(echoPin1, INPUT);
  
  Serial.begin(9600);
}

void loop()
{

    //for sensor number 1!!
  digitalWrite(trigPin1, LOW);
  delayMicroseconds(2);
  digitalWrite(trigPin1, HIGH);
  delayMicroseconds(10);
  digitalWrite(trigPin1, LOW);

  duration1 = pulseIn(echoPin1, HIGH);
  distance1 = (duration1*.0343)/2;

    //for sensor number 2!!
  digitalWrite(trigPin2, LOW);
  delayMicroseconds(2);
  digitalWrite(trigPin2, HIGH);
  delayMicroseconds(10);
  digitalWrite(trigPin2, LOW);

  duration2 = pulseIn(echoPin2, HIGH);
  distance2 = (duration2*.0343)/2;

    //for sensor number 3!!
  digitalWrite(trigPin3, LOW);
  delayMicroseconds(2);
  digitalWrite(trigPin3, HIGH);
  delayMicroseconds(10);
  digitalWrite(trigPin3, LOW);

  duration3 = pulseIn(echoPin3, HIGH);
  distance3 = (duration3*.0343)/2;

    //for sensor number 4!!
  digitalWrite(trigPin4, LOW);
  delayMicroseconds(2);
  digitalWrite(trigPin4, HIGH);
  delayMicroseconds(10);
  digitalWrite(trigPin4, LOW);

  duration4 = pulseIn(echoPin4, HIGH);
  distance4 = (duration4*.0343)/2;

  
  Serial.print("Distance1: ");
  Serial.print(distance1);
  Serial.print(" Distance2: ");
  Serial.print(distance2);
  Serial.print(" Distance3: ");
  Serial.print(distance3);
  Serial.print(" Distance4: ");
  Serial.print(distance4);
  Serial.println(" ");
  delay(1000);
}



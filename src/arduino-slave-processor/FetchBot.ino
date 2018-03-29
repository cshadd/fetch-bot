#include <ArduinoJson.h>

// Echo Pin Constants
const int echoPin1 = 2; // Sensor 1
const int echoPin2 = 4; // Sensor 2
const int echoPin3 = 6; // Sensor 3

// Trigger Pin Constants
const int trigPin1 = 3; // Sensor 1
const int trigPin2 = 5; // Sensor 2
const int trigPin3 = 7; // Sensor 3

// Data
float distance1;
float distance2;
float distance3;
float duration1;
float duration2;
float duration3;
int move;

void setup() {
    pinMode(trigPin1, OUTPUT);
    pinMode(echoPin1, INPUT);
    pinMode(trigPin2, OUTPUT);
    pinMode(echoPin2, INPUT);
    pinMode(trigPin3, OUTPUT);
    pinMode(echoPin3, INPUT);
    distance1 = 0;
    distance2 = 0;
    distance3 = 0;
    duration1 = 0;
    duration2 = 0;
    duration3 = 0;
    move = 0;
    Serial.begin(9600);
}

void toArduino() {
    if (Serial.available() > 0) {
        move = Serial.read();
    }
    else {
        move = 0;
    }
}

void toRobot() {
    const size_t bufferSize = JSON_OBJECT_SIZE(4);
    DynamicJsonBuffer jsonBuffer(bufferSize);

    JsonObject& root = jsonBuffer.createObject();
    root["sensor-front"] = distance1;
    root["sensor-left"] = distance2;
    root["sensor-right"] = distance3;

    root.printTo(Serial);
}

void loop() {
    // For Sensor 1
    digitalWrite(trigPin1, LOW);
    delayMicroseconds(2);
    digitalWrite(trigPin1, HIGH);
    delayMicroseconds(10);
    digitalWrite(trigPin1, LOW);

    duration1 = pulseIn(echoPin1, HIGH);
    distance1 = (duration1*0.0343)/2;

    // For Sensor 2
    digitalWrite(trigPin2, LOW);
    delayMicroseconds(2);
    digitalWrite(trigPin2, HIGH);
    delayMicroseconds(10);
    digitalWrite(trigPin2, LOW);

    duration2 = pulseIn(echoPin2, HIGH);
    distance2 = (duration2*0.0343)/2;

    // For Sensor 3
    digitalWrite(trigPin3, LOW);
    delayMicroseconds(2);
    digitalWrite(trigPin3, HIGH);
    delayMicroseconds(10);
    digitalWrite(trigPin3, LOW);

    duration3 = pulseIn(echoPin3, HIGH);
    distance3 = (duration3*0.0343)/2;

    toRobot();
    toArduino();
    // Movement here...
    move = 0;
    delay(1000);
}

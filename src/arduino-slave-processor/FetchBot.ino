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
String move;

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
    move = "Stop";
    Serial.begin(9600);
}

void loop() {
    if (Serial.available() > 0) {
        move = Serial.readString();
        // Movement here...
        move = "Stop";

        // For Sensor 1
        digitalWrite(trigPin1, LOW);
        digitalWrite(trigPin2, LOW);
        digitalWrite(trigPin3, LOW);
        delay(10);
        digitalWrite(trigPin1, HIGH);
        digitalWrite(trigPin2, HIGH);
        digitalWrite(trigPin3, HIGH);
        delay(10);
        digitalWrite(trigPin1, LOW);
        digitalWrite(trigPin2, LOW);
        digitalWrite(trigPin3, LOW);

        duration1 = pulseIn(echoPin1, HIGH);
        duration2 = pulseIn(echoPin2, HIGH);
        duration3 = pulseIn(echoPin3, HIGH);

        distance1 = 1;// (duration1*0.0343)/2;
        distance2 = 2;// (duration2*0.0343)/2;
        distance3 = 3;// (duration3*0.0343)/2;

        const size_t bufferSize = JSON_OBJECT_SIZE(4);
        DynamicJsonBuffer jsonBuffer(bufferSize);

        JsonObject& root = jsonBuffer.createObject();
        root["f"] = distance1;
        root["l"] = distance2;
        root["r"] = distance3;

        root.printTo(Serial);
    }
}

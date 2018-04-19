#include <ArduinoJson.h>
#include <Stepper.h>

// Define Constants
#define STEPPER_STEPS 200
#define ULTRASONIC_SENSOR_ECHO_PIN 6
#define ULTRASONIC_SENSOR_TRIGGER_PIN 7

// Constants
const size_t COMMUNICATION_JSON_BUFFER_SIZE = JSON_OBJECT_SIZE(3);

// Instances
DynamicJsonBuffer communicationJsonBuffer(COMMUNICATION_JSON_BUFFER_SIZE);
JsonObject& communicationJsonReturn = communicationJsonBuffer.createObject();
Stepper stepperL(STEPPER_STEPS, 8, 9, 10, 11);
Stepper stepperR(STEPPER_STEPS, 2, 3, 4, 5);

// Data
float ultrasonicSensorDistance;
float ultrasonicSensorDuration;
String communication;

void setup() {
    pinMode(ULTRASONIC_SENSOR_TRIGGER_PIN, OUTPUT);
    pinMode(ULTRASONIC_SENSOR_ECHO_PIN, INPUT);
    ultrasonicSensorDistance = 0;
    ultrasonicSensorDuration = 0;
    communication = "Stop";
    stepperL.setSpeed(30);
    stepperR.setSpeed(30);
    Serial.begin(9600);
}

void moveStepperMotors(int l, int r) {
    for (int i = 0; i < STEPPER_STEPS; i++) {
      stepperL.step(l);
      stepperR.step(r);
    }
}

void loop() {
    // Serial Pull
    communication = Serial.readString();
    communicationJsonReturn["a"] = communication;

    // Movement
    if (communication.equals("Forward")) {
        moveStepperMotors(-1, 1);
        // moveStepperMotors(-1, 1);
        // moveStepperMotors(-1, 1);
    }
    /*else if (communication.equals("Back")) {
        moveStepperMotors(1, -1);
        // moveStepperMotors(1, -1);
        // moveStepperMotors(1, -1);
    }*/
    else if (communication.equals("Right")) {
        moveStepperMotors(-1, -1);
        // moveStepperMotors(-1, -1);
        // moveStepperMotors(-1, -1);
    }
    else if (communication.equals("Left")) {
        moveStepperMotors(1, 1);
        // moveStepperMotors(1, 1);
        // moveStepperMotors(1, 1);
    }
    else {
        moveStepperMotors(0, 0);
    }
    communication = "Stop";

    // Sensors
    digitalWrite(ULTRASONIC_SENSOR_TRIGGER_PIN, LOW);
    delay(2);
    digitalWrite(ULTRASONIC_SENSOR_TRIGGER_PIN, HIGH);
    delay(10);
    digitalWrite(ULTRASONIC_SENSOR_TRIGGER_PIN, LOW);
    ultrasonicSensorDuration = pulseIn(ULTRASONIC_SENSOR_ECHO_PIN, HIGH);
    ultrasonicSensorDistance = (ultrasonicSensorDuration*0.0343)/2;

    // Serial Push
    communicationJsonReturn["s"] = ultrasonicSensorDistance;
    communicationJsonReturn.printTo(Serial);
    Serial.flush();
}

#include <ArduinoJson.h>
#include <Stepper.h>

// Define Constants
#define MOTORL_AIN1_PIN 9 // Change if needed
#define MOTORL_AIN2_PIN 8 // Change if needed
#define MOTORL_BIN1_PIN 10 // Change if needed
#define MOTORL_BIN2_PIN 11 // Change if needed
#define MOTORR_AIN1_PIN 3 // Change if needed
#define MOTORR_AIN2_PIN 2 // Change if needed
#define MOTORR_BIN1_PIN 4 // Change if needed
#define MOTORR_BIN2_PIN 5 // Change if needed
#define STEPPER_SPEED 10
#define STEPPER_STEPS 200
#define ULTRASONIC_SENSOR_ECHO_PIN 6 // Change if needed
#define ULTRASONIC_SENSOR_TRIGGER_PIN 7 // Change if needed

// Constants
const size_t COMMUNICATION_JSON_BUFFER_SIZE = JSON_OBJECT_SIZE(3);

// Instances
DynamicJsonBuffer communicationJsonBuffer(COMMUNICATION_JSON_BUFFER_SIZE);
JsonObject& communicationJsonReturn = communicationJsonBuffer.createObject();
Stepper stepperL(STEPPER_STEPS, MOTORL_AIN2_PIN, MOTORL_AIN1_PIN, MOTORL_BIN1_PIN, MOTORL_BIN2_PIN);
Stepper stepperR(STEPPER_STEPS, MOTORR_AIN2_PIN, MOTORR_AIN1_PIN, MOTORR_BIN1_PIN, MOTORR_BIN2_PIN);

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
    stepperL.setSpeed(STEPPER_SPEED);
    stepperR.setSpeed(STEPPER_SPEED);
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
    if (communication.equals("DLeft")) {
        moveStepperMotors(1, 1);
        moveStepperMotors(1, 1);
    }
    else if (communication.equals("DRight")) {
        moveStepperMotors(-1, -1);
        moveStepperMotors(-1, -1);
    }
    else if (communication.equals("Forward")) {
        moveStepperMotors(-1, 1);
    }
    else if (communication.equals("Left")) {
        moveStepperMotors(1, 1);
    }
    else if (communication.equals("Right")) {
        moveStepperMotors(-1, -1);
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

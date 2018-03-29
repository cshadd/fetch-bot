// Echo Pin Constants
const int echoPin1 = 2; // Sensor 1
const int echoPin2 = 4; // Sensor 2
const int echoPin3 = 6; // Sensor 3

// Serial Constants
const String id = "MSG";
const String prefix = "OS";

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
float prevDistance1;
float prevDistance2;
float prevDistance3;

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
    prevDistance1 = 0;
    prevDistance2 = 0;
    prevDistance3 = 0;
    Serial.begin(9600);
}

int checksum(String s) {
    int cs = 0;
    int len = s.length() + 1;
    char ca[len];
    s.toCharArray(ca, len);
    for (int i=0; i<len; i++) {
        cs ^= ca[i];
    }
    return cs;
}

String generateNMEAString(String payload, String prefix, String id) {
    String nmea = "";
    if (prefix.length() != 2) {
        return nmea;
    }
    if (id.length() != 3) {
        return nmea;
    }
    nmea = prefix + id + "," + payload;
    int cs = checksum(nmea);
    String cks = String(cs, HEX);
    cks.toUpperCase();
    if (cks.length() < 2) {
        cks = "0" + cks;
    }
    nmea += ("*" + cks);
    return "$" + nmea;
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

    if (distance1 != prevDistance1 && distance2 != prevDistance2 && distance3 != prevDistance3) {
        string payload = "Distances: " + distance1 + "; " + distance2 + "; " + distance3;
        String nmea = generateNMEAString(payload, prefix, id);
        Serial.println(nmea);
    }

    prevDistance1 = distance1;
    prevDistance2 = distance2;
    prevDistance3 = distance3;

    delay(500);
}
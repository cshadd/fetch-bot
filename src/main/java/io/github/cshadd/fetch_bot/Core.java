package io.github.cshadd.fetch_bot;
import com.pi4j.util.Console;
import io.github.cshadd.fetch_bot.util.ArduinoCommunication;
import io.github.cshadd.fetch_bot.util.InterfaceCommunication;
import io.github.cshadd.fetch_bot.util.Logger;
import io.github.cshadd.fetch_bot.util.VersionCheck;

// Main
public class Core
implements FetchBot {
    // Private Constant Instance/Property Fields
    private static final int COLLISION_DISTANCE = 15;

    // Private Static Instance/Property Fields
    private static ArduinoCommunication arduinoComm;
    private static InterfaceCommunication interfaceComm;
    private static Movement movement;
    private static Logger log;
    private static Sensors sensors;

    // Private Static Methods
    private static void delayThread(long millis) {
        try {
            Thread.sleep(millis);
        }
        catch (InterruptedException e) {
            log.warn(e, "Thread was interrupted.");
        }
        catch (Exception e) {
            log.error(e, "There was an unknown issue!");
        }
    }

    // Entry Point
    public static void main(String[] args) {

    /*
mkdir -p build-uno
/usr/share/arduino/hardware/tools/avr/bin/avr-g++ -x c++ -include Arduino.h -MMD -c -mmcu=atmega328p -DF_CPU=16000000L -DARDUINO=105  -D__PROG_TYPES_COMPAT__ -I/usr/share/arduino/hardware/arduino//cores/arduino -I/usr/share/arduino/hardware/arduino//variants/standard    -Wall -ffunction-sections -fdata-sections -Os -fpermissive -fno-exceptions -std=gnu++11 -fno-threadsafe-statics -flto FetchBot.ino -o build-uno/FetchBot.ino.o
FetchBot.ino: In function ‘void loop()’:
FetchBot.ino:107:42: error: invalid operands of types ‘const char [12]’ and ‘float’ to binary ‘operator+’
         String payload = "Distances: " + distance1 + "; " + distance2 + "; " + distance3;
*/

        final Console console = new Console();
        console.title("--- Fetch Bot ---", "https://cshadd.github.io/fetch-bot/");

        String currentMode = "Idle";
        int currentSensorFront = 0;
        int currentSensorLeft = 0;
        int currentSensorRight = 0;
        String version = "v0.0.0";

        arduinoComm = new ArduinoCommunication();
        interfaceComm = new InterfaceCommunication();
        movement = new Movement();
        log = Logger.getInstance(interfaceComm);
        sensors = new Sensors();

        interfaceComm.reset();
        log.clear();
        if (args.length >= 1) {
            version = args[0];
        }
        log.info("Fetch Bot " + version + " starting!");
        VersionCheck.checkVersionMatch(version);
        interfaceComm.pushInterface();
        interfaceComm.pushRobot();

        // https://github.com/OlivierLD/raspberry-pi4j-samples/blob/master/Arduino.RaspberryPI/src/arduino/raspberrypi/SerialReader.java

        while (true) {
            interfaceComm.pullInterface();
            interfaceComm.pullRobot();

            if (!interfaceComm.getRobotValue("mode").equals(currentMode)) {
                currentMode = interfaceComm.getRobotValue("mode");
                log.info("Interface - [mode: " + currentMode + "] command received.");
            }
            interfaceComm.setInterfaceValue("mode", currentMode);

            try {
                currentSensorFront = sensors.getDistance(Sensors.DIRECTION.FRONT);
                currentSensorLeft = sensors.getDistance(Sensors.DIRECTION.LEFT);
                currentSensorRight = sensors.getDistance(Sensors.DIRECTION.RIGHT);
            }
            catch (SensorsException e) {
                Logger.error(e, "There was an issue with Sensors!");
            }
            catch (Exception e) {
                Logger.error(e, "There was an unknown issue!");
            }
            finally {
                interfaceComm.setInterfaceValue("sensor-front", "" + currentSensorFront);
                interfaceComm.setInterfaceValue("sensor-left", "" + currentSensorLeft);
                interfaceComm.setInterfaceValue("sensor-right", "" + currentSensorRight);
            }

            if (currentMode.equals("Auto")) {
                interfaceComm.setInterfaceValue("emotion", "Neutral");
            }
            else if (currentMode.equals("Idle")) {
                interfaceComm.setInterfaceValue("emotion", "Idle");
            }
            else if (currentMode.equals("Off")) {
                break;
            }
            else if (currentMode.equals("Manual")) {
                interfaceComm.setInterfaceValue("emotion", "Happy");
            }
            else {
                log.warn("Interface - [mode: " + currentMode + "] is invalid, setting to [mode: Idle].");
                interfaceComm.setRobotValue("mode", "Idle");
            }

            delayThread(500);
            interfaceComm.pushInterface();
            // interfaceComm.pushRobot();
        }

        log.info("Fetch Bot terminating! Log file: ./FetchBot.log.");
        interfaceComm.clear();
        interfaceComm.pushInterface();
        interfaceComm.pushRobot();
        console.promptForExit();
    }
}

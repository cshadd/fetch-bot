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
    // private static final int COLLISION_DISTANCE = 15;

    // Private Static Instance/Property Fields
    private static ArduinoCommunication arduinoComm;
    private static InterfaceCommunication interfaceComm;
    private static Logger log;

    // Public Static Methods
    public static void delayThread(long millis) {
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
        // Start console
        final Console console = new Console();
        console.title("--- Fetch Bot ---", "https://cshadd.github.io/fetch-bot/");

        // Assign first vars
        String currentMode = "Idle";
        String currentMove = "Stop";
        float currentSensorFront = -1;
        float currentSensorLeft = -1;
        float currentSensorRight = -1;
        String version = "v0.0.0";
        if (args.length >= 1) {
            version = args[0];
        }

        // Initiate interface communications
        interfaceComm = new InterfaceCommunication();

        // Initiate logging
        log = Logger.getInstance(interfaceComm);
        log.clear();

        // Show user that we started
        log.info("Fetch Bot " + version + " preparing!");

        // Version check
        VersionCheck.checkVersionMatch(version);

        // Initiate arduino communications
        arduinoComm = new ArduinoCommunication();

        // Reset communications
        interfaceComm.reset();
        interfaceComm.pushInterface();
        interfaceComm.pushRobot();
        arduinoComm.reset();
        arduinoComm.pushArduino();

        log.info("Fetch Bot started!");

        while (true) {
            // Pull data from communications
            interfaceComm.pullInterface();
            interfaceComm.pullRobot();
            arduinoComm.pullRobot();
            
            delayThread(1000);

            if (interfaceComm != null) {
                if (arduinoComm != null){
                    // Sensors
                    final float frontSensor = arduinoComm.getRobotValue("f");
                    if (frontSensor != -1) {
                        if (frontSensor != currentSensorFront) {
                            currentSensorFront = frontSensor;
                            log.info("Arduino - [f: " + currentSensorFront + "] received.");
                            interfaceComm.setInterfaceValue("sensor-front", "" + currentSensorFront);
                        }
                    }
                    else {
                        log.warn("Communication failure to Arduino.");
                    }

                    // Mode
                    final String mode = interfaceComm.getRobotValue("mode");
                    if (mode != null) {
                        if (!mode.equals(currentMode)) {
                            currentMode = mode;
                            log.info("Interface - [mode: " + currentMode + "] command received.");
                        }
                        interfaceComm.setInterfaceValue("mode", currentMode);

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
                            interfaceComm.setInterfaceValue("emotion", "Neutral");
                            // Movement (to be revised)
                            final String move = interfaceComm.getRobotValue("move");
                            if (!move.equals(currentMove)) {
                                currentMove = move;
                                log.info("Interface - [move: " + currentMove + "] command received.");

                                arduinoComm.setArduinoValue(currentMove);
                                arduinoComm.pushArduino();

                                if (!move.equals("Stop")) {
                                    interfaceComm.setInterfaceValue("emotion", "Happy");

                                    delayThread(1000);

                                    // Calculations

                                    arduinoComm.setArduinoValue("Stop");
                                    arduinoComm.pushArduino();
                                    interfaceComm.setRobotValue("move", "Stop");
                                    interfaceComm.pushRobot();
                                    currentMove = "Stop";
                                }
                            }
                        }
                        else {
                            log.warn("[mode: " + currentMode + "] is invalid, setting to [mode: Idle].");
                            interfaceComm.setRobotValue("mode", "Idle");
                        }
                    }
                    else {
                        log.warn("Communication failure to interface.");
                    }
                }
                else {
                    log.warn("Communication failure to Arduino.");
                }

                // Push data to communications
                interfaceComm.pushInterface();
            }
            else {
                log.warn("Communication failure to interface.");
            }
            delayThread(1000);
        }

        // Termination
        log.info("Fetch Bot terminating! Log file: ./FetchBot.log.");
        arduinoComm.setArduinoValue("Stop");
        arduinoComm.pushArduino();
        delayThread(1000);
        arduinoComm.clear();
        interfaceComm.clear();
        interfaceComm.pushInterface();
        interfaceComm.pushRobot();
        console.promptForExit();
    }
}

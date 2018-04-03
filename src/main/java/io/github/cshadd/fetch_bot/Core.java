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
    private static Movement movement;
    private static Logger log;

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
        final Console console = new Console();
        console.title("--- Fetch Bot ---", "https://cshadd.github.io/fetch-bot/");

        String currentMode = "Idle";
        String currentMove = "Stop";
        int currentSensorFront = 0;
        int currentSensorLeft = 0;
        int currentSensorRight = 0;
        String version = "v0.0.0";

        interfaceComm = new InterfaceCommunication();
        log = Logger.getInstance(interfaceComm);
        arduinoComm = new ArduinoCommunication();
        movement = new Movement();

        interfaceComm.reset();
        arduinoComm.reset();
        log.clear();
        if (args.length >= 1) {
            version = args[0];
            log.info("Fetch Bot " + version + " starting!");
        }
        VersionCheck.checkVersionMatch(version);
        arduinoComm.pushRobot();
        interfaceComm.pushInterface();
        interfaceComm.pushRobot();

        while (true) {
            interfaceComm.pullInterface();
            interfaceComm.pullRobot();
            arduinoComm.pullArduino();

            if (interfaceComm != null) {
                if (arduinoComm != null){
                    if (arduinoComm.getRobotValue("f") != -1) {
                        if (arduinoComm.getRobotValue("f") != currentSensorFront) {
                            try {
                                currentSensorFront = arduinoComm.getRobotValue("f");
                            }
                            catch (NumberFormatException e) {
                                log.error(e, "There was an issue with formatting a number!");
                            }
                            catch (Exception e) {
                                log.error(e, "There was an unknown issue!");
                            }
                            log.info("Arduino - [f: " + currentSensorFront + "] received.");
                        }
                        interfaceComm.setInterfaceValue("f", "" + currentSensorFront);
                    }
                    else {
                        log.warn("Communication failure to Arduino.");
                    }

                    if (interfaceComm.getRobotValue("mode") != null) {
                        if (!interfaceComm.getRobotValue("mode").equals(currentMode)) {
                            currentMode = interfaceComm.getRobotValue("mode");
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
                            if (!interfaceComm.getRobotValue("move").equals(currentMove)) {
                                currentMove = interfaceComm.getRobotValue("move");
                                if (!currentMove.equals("Stop")) {
                                    log.info("Interface - [move: " + currentMove + "] command received.");
                                    interfaceComm.setInterfaceValue("emotion", "Happy");
                                    interfaceComm.pushInterface();
                                    // Call Movement Class?

                                    arduinoComm.setArduinoValue(0);
                                    arduinoComm.pushRobot();

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
                interfaceComm.pushInterface();
            }
            else {
                log.warn("Communication failure to interface.");
            }
            delayThread(1000);
        }

        log.info("Fetch Bot terminating! Log file: ./FetchBot.log.");
        arduinoComm.clear();
        interfaceComm.clear();
        interfaceComm.pushInterface();
        interfaceComm.pushRobot();
        console.promptForExit();
    }
}

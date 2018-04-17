package io.github.cshadd.fetch_bot;
import io.github.cshadd.fetch_bot.io.ArduinoCommunication;
import io.github.cshadd.fetch_bot.io.CommunicationException;
import io.github.cshadd.fetch_bot.io.InterfaceCommunication;
import io.github.cshadd.fetch_bot.util.Logger;
import io.github.cshadd.fetch_bot.util.VersionCheck;
import io.github.cshadd.fetch_bot.util.VersionCheckException;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

// Main
public class Core
implements FetchBot {
    // Private Constant Instance/Property Fields
    // private static final int COLLISION_DISTANCE = 15;

    // Private Static Instance/Property Fields
    private static ArduinoCommunication arduinoComm;
    private static InterfaceCommunication interfaceComm;

    // Public Static Methods
    public static void delayThread(long millis) {
        try {
            Thread.sleep(millis);
        }
        catch (InterruptedException e) {
            Logger.warn(e, "Thread was interrupted.");
        }
        catch (Exception e) {
            Logger.error(e, "There was an unknown issue!");
        }
        finally { }
    }
    // Entry Point
    public static void main(String[] args) {
        System.loadLibrary(org.opencv.core.Core.NATIVE_LIBRARY_NAME);
        /*Mat mat = Mat.eye(3, 3, CvType.CV_8UC1);
        System.out.println("OPENCV TEST: mat = " + mat.dump());*/

        // Assign first variables
        String currentMode = "Idle";
        String currentMove = "Stop";
        String currentVersion = "v0.0.0";
        float currentUltrasonicSensor = -1;
        String version = "v0.0.0";

        if (args.length >= 1) {
            version = args[0];
        }

        // Initiate interface communications
        interfaceComm = new InterfaceCommunication();

        // Initiate logging
        Logger.setInterfaceCommunications(interfaceComm);
        Logger.clear();

        // Show user that we started
        Logger.info("Fetch Bot " + version + " preparing!");

        // Version check
        try {
            currentVersion = VersionCheck.getCurrentVersion();
        }
        catch (VersionCheckException e) {
            Logger.error(e, "VersionCheck encountered an error.");
        }
        catch (Exception e) {
            Logger.fatalError(e, "VersionCheck encountered a fatal error.");
        }
        finally { }

        if (!version.equals(currentVersion)) {
            Logger.warn("VersionCheck - [Version] mismatch (this: " + version + "; current: " + currentVersion + "), this version might be outdated!");
        }

        // Initiate Arduino communications
        arduinoComm = new ArduinoCommunication();

        // Reset communications
        try {
            interfaceComm.reset();
            interfaceComm.pushInterface();
            interfaceComm.pushRobot();
            arduinoComm.reset();
            Logger.info("ArduinoCommunication - Opened serial on " + ArduinoCommunication.SERIAL_PORT + ".");
            arduinoComm.pushArduino();
        }
        catch (CommunicationException e) {
            Logger.error(e, "Communication encountered an error.");
        }
        catch (Exception e) {
            Logger.fatalError(e, "Communication encountered a fatal error.");
        }
        finally { }

        Logger.info("Fetch Bot started!");

        while (true) {
            try {
                // Pull data from communications
                interfaceComm.pullInterface();
                interfaceComm.pullRobot();
                arduinoComm.pullRobot();

                // Sensors
                final float ultrasonicSensor = arduinoComm.getRobotValue("s");
                if (currentUltrasonicSensor != -1) {
                    if (currentUltrasonicSensor != ultrasonicSensor) {
                        currentUltrasonicSensor = ultrasonicSensor;
                        if (currentUltrasonicSensor <= 15) { // To move later...
                            interfaceComm.setInterfaceValue("emotion", "Angry");
                        }
                        Logger.info("Arduino - [s: " + currentUltrasonicSensor + "] received.");
                        interfaceComm.setInterfaceValue("ultrasonic", "" + currentUltrasonicSensor);
                    }
                }

                // Mode
                final String mode = interfaceComm.getRobotValue("mode");
                if (mode != null) {
                    if (!mode.equals(currentMode)) {
                        currentMode = mode;
                        Logger.info("Interface - [mode: " + currentMode + "] command received.");
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
                            Logger.info("Interface - [move: " + currentMove + "] command received.");

                            arduinoComm.setArduinoValue(currentMove);
                            arduinoComm.pushArduino();

                            if (!move.equals("Stop")) {
                                currentMove = "Stop";
                                interfaceComm.setInterfaceValue("emotion", "Happy");
                                interfaceComm.setRobotValue("move", "Stop");
                                interfaceComm.pushRobot();
                            }
                        }
                    }
                    else {
                        Logger.warn("[mode: " + currentMode + "] is invalid, setting to [mode: Idle].");
                        interfaceComm.setRobotValue("mode", "Idle");
                    }
                }

                // Push data to communications
                interfaceComm.pushInterface();
            }
            catch (CommunicationException e) {
                Logger.error(e, "Communication encountered an error.");
            }
            catch (Exception e) {
                Logger.fatalError(e, "Communication encountered a fatal error.");
            }
            finally { }

            // Delay for safety
            delayThread(1000);
        }

        // Termination
        Logger.info("Fetch Bot terminating! Log file: " + Logger.LOG_PATH);
        try {
            arduinoComm.setArduinoValue("Stop");
            arduinoComm.pushArduino();
            arduinoComm.clear();
            interfaceComm.clear();
            interfaceComm.pushInterface();
            interfaceComm.pushRobot();
        }
        catch (CommunicationException e) {
            Logger.error(e, "Communication encountered an error.");
        }
        catch (Exception e) {
            Logger.fatalError(e, "Communication encountered a fatal error.");
        }
        finally { }
        Logger.close();
    }
}

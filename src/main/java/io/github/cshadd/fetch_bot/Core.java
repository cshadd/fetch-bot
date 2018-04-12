package io.github.cshadd.fetch_bot;
import io.github.cshadd.fetch_bot.io.ArduinoCommunication;
import io.github.cshadd.fetch_bot.io.CommunicationException;
import io.github.cshadd.fetch_bot.io.InterfaceCommunication;
import io.github.cshadd.fetch_bot.util.Logger;
import io.github.cshadd.fetch_bot.util.VersionCheck;
import io.github.cshadd.fetch_bot.util.VersionCheckException;
import nu.pattern.OpenCV; // Testing...
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
        OpenCV.loadShared();
        Mat mat = Mat.eye(3, 3, CvType.CV_8UC1);
        System.out.println("OPENCV TEST: mat = " + mat.dump());

        // Assign first variables
        String currentMode = "Idle";
        String currentMove = "Stop";
        String currentVersion = "v0.0.0";
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
                final float frontSensor = arduinoComm.getRobotValue("f");
                final float leftSensor = arduinoComm.getRobotValue("l");
                final float rightSensor = arduinoComm.getRobotValue("r");
                if (frontSensor != -1) {
                    if (frontSensor != currentSensorFront) {
                        currentSensorFront = frontSensor;
                        Logger.info("Arduino - [f: " + currentSensorFront + "] received.");
                        interfaceComm.setInterfaceValue("sensor-front", "" + currentSensorFront);
                    }
                }
                if (leftSensor != -1) {
                    if (leftSensor != currentSensorLeft) {
                        currentSensorLeft = leftSensor;
                        Logger.info("Arduino - [l: " + currentSensorLeft + "] received.");
                        interfaceComm.setInterfaceValue("sensor-left", "" + currentSensorLeft);
                    }
                }
                if (rightSensor != -1) {
                    if (rightSensor != currentSensorRight) {
                        currentSensorRight = rightSensor;
                        Logger.info("Arduino - [r: " + currentSensorRight + "] received.");
                        interfaceComm.setInterfaceValue("sensor-right", "" + currentSensorRight);
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
                                interfaceComm.setInterfaceValue("emotion", "Happy");
                                interfaceComm.setRobotValue("move", "Stop");
                                interfaceComm.pushRobot();
                                currentMove = "Stop";
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

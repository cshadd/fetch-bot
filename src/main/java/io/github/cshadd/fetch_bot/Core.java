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
    // Private Static Instance/Property Fields
    private static ArduinoCommunication arduinoComm;
    private static InterfaceCommunication interfaceComm;

    // Public Static Methods
    static {
        System.loadLibrary(org.opencv.core.Core.NATIVE_LIBRARY_NAME);
    }
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
        Mat mat = Mat.eye(3, 3, CvType.CV_8UC1);
        System.out.println("OPENCV TEST: mat = " + mat.dump());

        // Assign first variables
        String currentMode = "Idle";
        String currentMove = "Stop";
        String currentVersion = "v0.0.0";
        int currentUltrasonicSensor = -1;
        String programMode = "Normal";
        String programVersion = "v0.0.0";
        
        if (args.length == 1) {
            programVersion = args[0];
        }        
        if (args.length == 2) {
            programMode = args[1];
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
                final int ultrasonicSensor = Integer.parseInt(arduinoComm.getRobotValue("s"));
                if (ultrasonicSensor != -1) {
                    if (ultrasonicSensor != currentUltrasonicSensor) {
                        currentUltrasonicSensor = ultrasonicSensor;
                        Logger.info("Arduino - [s: " + currentUltrasonicSensor + "] received.");
                        if (currentUltrasonicSensor <= 15) { // To move later...
                            interfaceComm.setInterfaceValue("emotion", "Angry");
                        }
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
                    webInterfaceComm.setSourceValue("mode", currentMode);
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
                        final String move = webInterfaceComm.getRobotValue("move");
                        if (currentUltrasonicSensor <= 15) {
                            webInterfaceComm.setSourceValue("emotion", "Angry");
                        }
                        else {
                            if (!move.equals(currentMove)) {
                                currentMove = move;
                                Logger.info("WebInterface - [move: " + currentMove + "] command received.");
                                arduinoComm.setSourceValue("a", currentMove);
                                arduinoComm.pushSource();
                                webInterfaceComm.setSourceValue("emotion", "Happy");
                                webInterfaceComm.setRobotValue("move", "Stop");
                            }
                        }
                    }
                    else {
                        Logger.warn("[mode: " + currentMode + "] is invalid, setting to [mode: Idle].");
                        webInterfaceComm.setRobotValue("mode", "Idle");
                    }
                }
                
                // Send data to Web Interface
                webInterfaceComm.pushRobot();
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
            arduinoComm.setArduinoValue("a", "Stop");
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

package io.github.cshadd.fetch_bot;
import io.github.cshadd.fetch_bot.io.ArduinoCommunication;
import io.github.cshadd.fetch_bot.io.ArduinoCommunicationImpl;
import io.github.cshadd.fetch_bot.io.CommunicationException;
import io.github.cshadd.fetch_bot.io.WebInterfaceCommunication;
import io.github.cshadd.fetch_bot.io.WebInterfaceCommunicationImpl;
import io.github.cshadd.fetch_bot.util.Logger;
import io.github.cshadd.fetch_bot.util.VersionCheck;
import io.github.cshadd.fetch_bot.util.VersionCheckException;

// Main
public class Core
implements FetchBot {
    // Public Constant Instance/Property Fields
    public static final String VERSION = "v1.0.0-alpha";

    // Private Static Instance/Property Fields
    private static ArduinoCommunication arduinoComm;
    private static WebInterfaceCommunication webInterfaceComm;

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
        // Assign first variables
        String currentMode = "Idle";
        String currentMove = "Stop";
        String currentTrackClass = "None";
        int currentUltrasonicSensor = -1;
        String currentVersion = "v0.0.0";
        String programMode = "Normal";

        if (args.length > 0) {
            programMode = args[0];
        }

        // Initiate interface communications
        webInterfaceComm = new WebInterfaceCommunicationImpl();

        // Initiate logging
        Logger.setWebInterfaceCommunications(webInterfaceComm);
        if (programMode.equals("debug")) {
            Logger.setToDebugMode();
        }
        Logger.clear();

        // Show user that we started
        Logger.info("Fetch Bot " + VERSION + " preparing!");

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

        if (!VERSION.equals(currentVersion)) {
            Logger.warn("VersionCheck - [Version] mismatch (this: " + VERSION + "; current: " + currentVersion + "), this version might be outdated!");
        }

        // Initiate Arduino communications
        arduinoComm = new ArduinoCommunicationImpl();

        // Reset communications
        try {
            webInterfaceComm.reset();
            webInterfaceComm.pushSource();
            webInterfaceComm.pushRobot();
            arduinoComm.reset();
            Logger.info("ArduinoCommunication - Opened serial on " + ArduinoCommunicationImpl.SERIAL_PORT + ".");
            arduinoComm.pushSource();
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
                webInterfaceComm.pullSource();
                webInterfaceComm.pullRobot();
                arduinoComm.pullRobot();

                // Tracking Class
                final String trackClass = webInterfaceComm.getRobotValue("trackclass");
                if (trackClass != null) {
                    if (trackClass != currentTrackClass) {
                        currentTrackClass = trackClass;
                        Logger.debug("WebInterface - [trackclass: " + currentTrackClass + "] received.");
                        webInterfaceComm.setSourceValue("trackclass", "" + currentTrackClass);
                    }
                }

                // Sensors
                final int ultrasonicSensor = (int)Float.parseFloat(arduinoComm.getRobotValue("s"));
                if (ultrasonicSensor != -1) {
                    if (ultrasonicSensor != currentUltrasonicSensor) {
                        currentUltrasonicSensor = ultrasonicSensor;
                        Logger.debug("Arduino - [s: " + currentUltrasonicSensor + "] received.");
                        webInterfaceComm.setSourceValue("ultrasonic", "" + currentUltrasonicSensor);
                    }
                }

                // Mode
                final String mode = webInterfaceComm.getRobotValue("mode");
                if (mode != null) {
                    if (!mode.equals(currentMode)) {
                        currentMode = mode;
                        Logger.info("WebInterface - [mode: " + currentMode + "] command received.");
                    }
                    webInterfaceComm.setSourceValue("mode", currentMode);
                    if (currentMode.equals("Auto")) {
                        webInterfaceComm.setSourceValue("emotion", "Neutral");
                    }
                    else if (currentMode.equals("Idle")) {
                        webInterfaceComm.setRobotValue("trackclass", "None");
                        webInterfaceComm.pushRobot();
                        webInterfaceComm.setSourceValue("emotion", "Idle");
                    }
                    else if (currentMode.equals("Off")) {
                        break;
                    }
                    else if (currentMode.equals("Manual")) {
                        webInterfaceComm.setRobotValue("trackclass", "None");
                        webInterfaceComm.pushRobot();
                        final String move = webInterfaceComm.getRobotValue("move");
                        if (currentUltrasonicSensor <= 15) {
                            webInterfaceComm.setSourceValue("emotion", "Angry");
                            if (!move.equals(currentMove)) {
                                currentMove = move;
                                if (move.equals("Forward")) {
                                    Logger.warn("Arduino - Sensor is blocked, refusing to move.");
                                }
                                else if (!move.equals("Stop")) {
                                    Logger.debug("WebInterface - [move: " + currentMove + "] command received.");
                                    arduinoComm.setSourceValue("a", currentMove);
                                    arduinoComm.pushSource();
                                    Logger.debug("WebInterface - [move: Stop] command received.");
                                }
                                webInterfaceComm.setRobotValue("move", "Stop");
                                webInterfaceComm.pushRobot();
                            }
                        }
                        else {
                        	webInterfaceComm.setSourceValue("emotion", "Happy");
                            if (!move.equals(currentMove)) {
                                currentMove = move;
                                Logger.debug("WebInterface - [move: " + currentMove + "] command received.");
                                arduinoComm.setSourceValue("a", currentMove);
                                arduinoComm.pushSource();
                                webInterfaceComm.setRobotValue("move", "Stop");
                                webInterfaceComm.pushRobot();
                            }
                        }
                    }
                    else {
                        Logger.warn("WebInterface - [mode: " + currentMode + "] is invalid, setting to [mode: Idle].");
                        webInterfaceComm.setRobotValue("mode", "Idle");
                        webInterfaceComm.pushRobot();
                    }
                }
                webInterfaceComm.pushSource();
            }
            catch (CommunicationException e) {
                Logger.error(e, "Communication encountered an error.");
            }
            catch (Exception e) {
                Logger.fatalError(e, "Communication encountered a fatal error.");
            }
            finally {
                // Delay for safety
                delayThread(1000);
            }
        }

        // Termination
        Logger.info("Fetch Bot terminating! Log file: " + Logger.LOG_PATH);
        try {
            arduinoComm.setSourceValue("a", "Stop");
            arduinoComm.pushSource();
            arduinoComm.clear();
            webInterfaceComm.clear();
            webInterfaceComm.pushSource();
            webInterfaceComm.pushRobot();
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

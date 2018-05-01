package io.github.cshadd.fetch_bot;
import io.github.cshadd.fetch_bot.controllers.AIController;
import io.github.cshadd.fetch_bot.controllers.AIControllerImpl;
import io.github.cshadd.fetch_bot.controllers.PathfindController;
import io.github.cshadd.fetch_bot.controllers.PathfindControllerImpl;
import io.github.cshadd.fetch_bot.io.ArduinoCommunication;
import io.github.cshadd.fetch_bot.io.ArduinoCommunicationImpl;
import io.github.cshadd.fetch_bot.io.CommunicationException;
import io.github.cshadd.fetch_bot.io.WebInterfaceCommunication;
import io.github.cshadd.fetch_bot.io.WebInterfaceCommunicationImpl;
import io.github.cshadd.fetch_bot.util.Logger;
import io.github.cshadd.fetch_bot.util.VersionCheck;
import io.github.cshadd.fetch_bot.util.VersionCheckException;

// Main
@Component("Core")
public class Core
implements FetchBot {
    // Public Constant Instance/Property Fields
    public static final String VERSION = "v1.0.0-alpha";

    // Private Static Instance/Property Fields
    private static AIController ai;
    private static ArduinoCommunication arduinoComm;
    private static PathfindController pathfind;
    private static WebInterfaceCommunication webInterfaceComm;

    // Private Static Methods
    private static void delayThread(long millis) {
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
    private static void run() {
        String currentMode = "Idle";
        String currentMove = "Stop";
        String currentTrackClass = "None";
        int currentUltrasonicSensor = -1;
        while (true) {
            try {
                // Pull data from communications
                webInterfaceComm.pullSource();
                webInterfaceComm.pullRobot();
                arduinoComm.pullRobot();

                // Sensors
                final int ultrasonicSensor = (int)Float.parseFloat(arduinoComm.getRobotValue("s"));
                if (ultrasonicSensor != -1) {
                    if (ultrasonicSensor != currentUltrasonicSensor) {
                        currentUltrasonicSensor = ultrasonicSensor;
                        Logger.debug("Arduino - [s: " + currentUltrasonicSensor + "] received.");
                        webInterfaceComm.setSourceValue("ultrasonic", "" + currentUltrasonicSensor);
                    }
                }
                
                // Tracking Class
                final String trackClass = webInterfaceComm.getRobotValue("trackclass");
                if (trackClass != null) {
                    if (trackClass != currentTrackClass) {
                        currentTrackClass = trackClass;
                        Logger.debug("WebInterface - [trackclass: " + currentTrackClass + "] received.");
                        webInterfaceComm.setSourceValue("trackclass", "" + currentTrackClass);
                    }
                }

                // Mode
                final String mode = webInterfaceComm.getRobotValue("mode");
                if (mode != null) {
                    if (!mode.equals(currentMode)) {
                        currentMode = mode;
                        Logger.debug("WebInterface - [mode: " + currentMode + "] command received.");
                        webInterfaceComm.setSourceValue("mode", currentMode);
                    }
                    
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
                        
                        // Manual move
                        final String move = webInterfaceComm.getRobotValue("move");
                        if (move != null) {
                            if (!move.equals(currentMove)) {
                                currentMove = move;
                                Logger.debug("WebInterface - [move: " + currentMove + "] command received.");

                                if (currentUltrasonicSensor <= 15) {
                                    Logger.warn("Arduino - Safety cut due to imminent collision.");
                                    webInterfaceComm.setSourceValue("emotion", "Sad");
                                    
                                    if (!move.equals("Stop")) {
                                        webInterfaceComm.setRobotValue("move", "Stop");
                                        arduinoComm.setSourceValue("a", "Stop");
                                        arduinoComm.pushSource();
                                    }
                                    webInterfaceComm.pushRobot();
                                }
                                else if (currentUltrasonicSensor <= 30) {
                                    Logger.warn("Arduino - Imminent collision.");
                                    webInterfaceComm.setSourceValue("emotion", "Angry");
                                    
                                    if (!move.equals("Stop")) {
                                        webInterfaceComm.setRobotValue("move", "Stop");
                                        if (move.equals("Forward")) {
                                            arduinoComm.setSourceValue("a", "Stop");
                                        }
                                        else {
                                            arduinoComm.setSourceValue("a", currentMove);
                                        }
                                        arduinoComm.pushSource();
                                    }
                                    webInterfaceComm.pushRobot();
                                }
                                else {
                                    webInterfaceComm.setSourceValue("emotion", "Happy");
                                    
                                    webInterfaceComm.setRobotValue("move", "Stop");
                                    arduinoComm.setSourceValue("a", currentMove);
                                    arduinoComm.pushSource();
                                    webInterfaceComm.pushRobot();                                    
                                }
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
    }
    private static void setup(String[] args) {
        String runAs = "Normal";
        if (args.length > 0) {
            runAs = args[0];
        }
        
        // Initiate communications
        webInterfaceComm = new WebInterfaceCommunicationImpl();
        arduinoComm = new ArduinoCommunicationImpl();
        Logger.setWebInterfaceCommunications(webInterfaceComm);
        Logger.clear();
        if (runAs.equals("debug")) {
            Logger.setToDebugMode();
        }

        // Reset communications
        try {
            webInterfaceComm.reset();
            webInterfaceComm.pushSource();
            webInterfaceComm.pushRobot();
            arduinoComm.reset();
            arduinoComm.pushSource();
        }
        catch (CommunicationException e) {
            Logger.error(e, "Communication encountered an error.");
        }
        catch (Exception e) {
            Logger.fatalError(e, "Communication encountered a fatal error.");
        }
        finally { }

        // Startup logging
        try {
            Logger.info("Fetch Bot " + VERSION + " started in " + runAs + " mode!");
            Logger.info("ArduinoCommunication - Opened serial on " + ArduinoCommunicationImpl.SERIAL_PORT + ".");
            webInterfaceComm.pushSource();
        }
        catch (CommunicationException e) {
            Logger.error(e, "Communication encountered an error.");
        }
        catch (Exception e) {
            Logger.fatalError(e, "Communication encountered a fatal error.");
        }
        finally { }
        
        // Initiate controllers
        ai = new AIControllerImpl();
        pathfind = new PathfindControllerImpl();
    }
    private static void terminate() {
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
    private static void versionCheck() {
        String currentVersion = "v0.0.0";
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
    }

    // Public Static Methods
    // Entry Point
    public static void main(String[] args) {
        // Setup
        setup(args);

        // Version check
        versionCheck();

        // Run
        run();

        // Termination
        terminate();
    }
}

package io.github.cshadd.fetch_bot;
import io.github.cshadd.fetch_bot.controllers.OpenCVController;
import io.github.cshadd.fetch_bot.controllers.OpenCVControllerImpl;
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
    private static ArduinoCommunication arduinoComm;
    private static OpenCVController openCVControl;
    private static PathfindController pathfindControl;
    private static WebInterfaceCommunication webInterfaceComm;

    // Public Constructors
    public Core() { }
    
    // Private Static Methods
    private static void run() {
        String currentMode = "Idle";
        String currentMove = "Stop";
        String currentTrackClass = "None";
        boolean tracked = false;
        int currentUltrasonicSensor = -1;
        while (true) {
            try {
                // Pull data from communications
                webInterfaceComm.pullSource();
                webInterfaceComm.pullRobot();
                arduinoComm.pullRobot();

                // Sensors
                final float rawUltrasonicSensor = arduinoComm.getRobotFloatValue("s");
                final int ultrasonicSensor = (int)rawUltrasonicSensor;
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
                        tracked = false;
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
                        if (currentTrackClass != null) {
                            if (!currentTrackClass.equals("None")) {
                                if (currentUltrasonicSensor <= 30) {
                                    Logger.warn("Arduino - Safety cut due to imminent collision.");
                                    pathfindControl.blockNext();
                                }
                                else {
                                    pathfindControl.unblockNext();
                                }
                                
                                if (pathfindControl.isNextTracked()) {
                                    // TODO: PUT THAT IT WAS FOUND!
                                    Logger.debug("Found you!");
                                    webInterfaceComm.setSourceValue("emotion", "Happy");
                                    webInterfaceComm.setRobotValue("trackclass", "None");
                                    arduinoComm.setSourceValue("a", "Stop");
                                    currentTrackClass = "None";
                                    tracked = true;
                                    pathfindControl.reset();
                                }
                                else if (!pathfindControl.isAnyAvailable()) {
                                    Logger.debug("I am stuck!");
                                    webInterfaceComm.setSourceValue("emotion", "Sad");
                                    webInterfaceComm.setRobotValue("trackclass", "None");
                                    arduinoComm.setSourceValue("a", "Stop");
                                    currentTrackClass = "None";
                                    tracked = true;
                                    pathfindControl.reset();
                                }
                                else {
                                    Logger.debug("Hmm... curious.");
                                    if (pathfindControl.isNextBlocked()) {
                                        Logger.debug("Get out of my way!");
                                        webInterfaceComm.setSourceValue("emotion", "Angry");
                                        if ((int)(Math.random()*2) == 0) {
                                            Logger.debug("Turning left!");
                                            pathfindControl.rotateLeft();
                                            arduinoComm.setSourceValue("a", "Left");
                                        }
                                        else {
                                            Logger.debug("Turning right!");
                                            pathfindControl.rotateRight();
                                            arduinoComm.setSourceValue("a", "Right");
                                        }
                                    }
                                    else if (pathfindControl.isNextVisited()) {
                                        Logger.debug("Wait I was just here.");
                                        webInterfaceComm.setSourceValue("emotion", "Sad");
                                        if ((int)(Math.random()*2) == 0) {
                                            Logger.debug("Turning left!");
                                            pathfindControl.rotateLeft();
                                            arduinoComm.setSourceValue("a", "Left");
                                        }
                                        else {
                                            Logger.debug("Turning right!");
                                            pathfindControl.rotateRight();
                                            arduinoComm.setSourceValue("a", "Right");
                                        }
                                    }
                                    else {
                                        Logger.debug("Nothing yet.");
                                        webInterfaceComm.setSourceValue("emotion", "Neutral");
                                        arduinoComm.setSourceValue("a", "Forward");
                                        pathfindControl.goNext();
                                    }
                                }
                            }
                            else {
                                if (!tracked) {
                                    webInterfaceComm.setSourceValue("emotion", "Neutral");
                                }
                                arduinoComm.setSourceValue("a", "Stop");
                            }
                        }
                        else {
                            webInterfaceComm.setSourceValue("emotion", "Neutral");
                            arduinoComm.setSourceValue("a", "Stop");
                        }
                    }
                    else if (currentMode.equals("Idle")) {
                        webInterfaceComm.setRobotValue("trackclass", "None");

                        webInterfaceComm.setSourceValue("emotion", "Idle");
                    }
                    else if (currentMode.equals("Off")) {
                        break;
                    }
                    else if (currentMode.equals("Manual")) {
                        webInterfaceComm.setRobotValue("trackclass", "None");
                        
                        // Manual move
                        final String move = webInterfaceComm.getRobotValue("move");
                        if (move != null) {
                            if (!move.equals(currentMove)) {
                                currentMove = move;
                                Logger.debug("WebInterface - [move: " + currentMove + "] command received.");

                                if (currentUltrasonicSensor <= 30) {
                                    if (currentUltrasonicSensor <= 15) {
                                        webInterfaceComm.setSourceValue("emotion", "Sad");
                                    }
                                    else {
                                        webInterfaceComm.setSourceValue("emotion", "Angry");   
                                    }
                                    webInterfaceComm.setRobotValue("move", "Stop");
                                    if (!move.equals("Stop")) {
                                        if (move.equals("Forward")) {
                                            Logger.warn("Arduino - Safety cut due to imminent collision.");
                                            arduinoComm.setSourceValue("a", "Stop");
                                        }
                                        else {
                                            arduinoComm.setSourceValue("a", currentMove);
                                        }
                                    }
                                }
                                else {
                                    webInterfaceComm.setSourceValue("emotion", "Happy");
                                    webInterfaceComm.setRobotValue("move", "Stop");
                                    arduinoComm.setSourceValue("a", currentMove);
                                }
                            }
                        }
                    }
                    else {
                        Logger.warn("WebInterface - [mode: " + currentMode + "] is invalid, setting to [mode: Idle].");
                        webInterfaceComm.setRobotValue("mode", "Idle");
                    }
                }
                webInterfaceComm.pushSource();
                webInterfaceComm.pushRobot();
                arduinoComm.pushSource();
            }
            catch (CommunicationException e) {
                Logger.error(e, "There was an issue with Communication!");
            }
            catch (Exception e) {
                Logger.error(e, "There was an unknown issue!");
            }
            finally {
                // Delay for safety
                delayThread(1000);
            }
        }
    }
    private static void setup(String[] args) {
        String profile = "Normal";
        if (args.length > 0) {
            profile = args[0];
            if (profile.equals("Normal")) { }
            else if (profile.equals("Debug")) { }
            else {
                profile = "Normal";
            }
        }
        
        // Initiate communications
        arduinoComm = new ArduinoCommunicationImpl();
        webInterfaceComm = new WebInterfaceCommunicationImpl();
        Logger.setWebInterfaceCommunications(webInterfaceComm);
        Logger.clear();
        if (profile.equals("Debug")) {
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
        Logger.info("Fetch Bot " + VERSION + " started as profile " + profile + "!");
        Logger.info("ArduinoCommunication - Opened serial on " + ArduinoCommunicationImpl.SERIAL_PORT + ".");        
        try {
            webInterfaceComm.pushSource();
        }
        catch (CommunicationException e) {
            Logger.error(e, "There was an issue with Communication!");
        }
        catch (Exception e) {
            Logger.error(e, "There was an unknown issue!");
        }
        finally { }
        
        // Initiate controllers
        openCVControl = new OpenCVControllerImpl();
        pathfindControl = new PathfindControllerImpl();
        pathfindControl.reset();
        openCVControl.startCamera();
        
        // Version check
        String currentVersion = "v0.0.0";
        try {
            currentVersion = VersionCheck.getCurrentVersion();
        }
        catch (VersionCheckException e) {
            Logger.error(e, "There was an issue with VersionCheck!");
        }
        catch (Exception e) {
            Logger.error(e, "There was an unknown issue!");
        }
        finally { }

        if (!VERSION.equals(currentVersion)) {
            Logger.warn("VersionCheck - [Version] mismatch (this: " + VERSION + "; current: " + currentVersion + "), this version might be outdated!");
        }
    }
    private static void terminate() {
        Logger.info("Fetch Bot terminating! Log file: " + Logger.LOG_PATH);
        try {
            openCVControl.stopCamera();
            arduinoComm.setSourceValue("a", "Stop");
            arduinoComm.pushSource();
            arduinoComm.clear();
            webInterfaceComm.clear();
            webInterfaceComm.pushSource();
            webInterfaceComm.pushRobot();
        }
        catch (CommunicationException e) {
            Logger.error(e, "There was an issue with Communication!");
        }
        catch (Exception e) {
            Logger.error(e, "There was an unknown issue!");
        }
        finally {
            Logger.close();
        }
    }

    // Public Static Final Methods
    public static final void delayThread(long millis) {
        try {
            Thread.sleep(millis);
        }
        catch (InterruptedException e) {
            Logger.warn(e, "Thread was interrupted!");
        }
        catch (Exception e) {
            Logger.error(e, "There was an unknown issue!");
        }
        finally { }
    }

    // Public Static Methods
    // Entry Point
    public static void main(String[] args) {
        // Setup
        setup(args);

        // Run
        run();

        // Termination
        terminate();
    }
}

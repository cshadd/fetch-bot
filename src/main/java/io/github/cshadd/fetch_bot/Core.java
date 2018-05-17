/*
 * MIT License
 * 
 * Copyright (c) 2018 Christian Shadd
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 * 
 * https://cshadd.github.io/fetch-bot/
 */
package io.github.cshadd.fetch_bot;

import io.github.cshadd.fetch_bot.controllers.ControllerException;
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

/**
 * The Class Core. Core is Fetch Bot's main application.
 * 
 * @author Christian Shadd
 * @author Maria Verna Aquino
 * @author Thanh Vu
 * @author Joseph Damian
 * @author Giovanni Orozco
 * @since 1.0.0
 */
@Component("Core")
public class Core implements FetchBot {
    // Private Constant Instance/Property Fields
    
    /**
     * The Constant SENSOR_LIMIT.
     */
    private static final int SENSOR_LIMIT = 30;
    
    /**
     * The Constant VERSION.
     */
    private static final String VERSION = "v1.0.0";
    
    // Private Static Instance/Property Fields
    
    /**
     * The Arduino Communications.
     */
    private static ArduinoCommunication arduinoComm;
    
    /**
     * The Open CV Controller.
     */
    private static OpenCVController openCVControl;
    
    /**
     * The Pathfind Controller.
     */
    private static PathfindController pathfindControl;
    
    /**
     * The Web Interface Communications.
     */
    private static WebInterfaceCommunication webInterfaceComm;
    
    // Public Constructors
    
    /**
     * Instantiates a new Core.
     */
    public Core() {
    }
    
    // Private Static Methods
    
    /**
     * Runs the main application actions.
     */
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
                final float rawUltrasonicSensor = arduinoComm
                                .getRobotFloatValue("s");
                final int ultrasonicSensor = (int) rawUltrasonicSensor;
                if (ultrasonicSensor != -1) {
                    if (ultrasonicSensor != currentUltrasonicSensor) {
                        currentUltrasonicSensor = ultrasonicSensor;
                        Logger.debug("Arduino - [s: " + currentUltrasonicSensor
                                        + "] received.");
                        webInterfaceComm.setSourceValue("ultrasonic", ""
                                        + currentUltrasonicSensor);
                    }
                }
                
                // Tracking Class
                final String trackClass = webInterfaceComm.getRobotValue(
                                "trackclass");
                if (trackClass != null) {
                    if (!trackClass.equals(currentTrackClass)) {
                        currentTrackClass = trackClass;
                        Logger.debug("WebInterface - [trackclass: "
                                        + currentTrackClass + "] received.");
                        openCVControl.assignTrackClass(currentTrackClass);
                        webInterfaceComm.setSourceValue("trackclass", ""
                                        + currentTrackClass);
                        Logger.info("Core - Please wait.");
                        pathfindControl.reset();
                        Logger.info("Core - Ready.");
                        tracked = false;
                    }
                }
                
                // Mode
                final String mode = webInterfaceComm.getRobotValue("mode");
                if (mode != null) {
                    if (!mode.equals(currentMode)) {
                        currentMode = mode;
                        Logger.debug("WebInterface - [mode: " + currentMode
                                        + "] received.");
                        webInterfaceComm.setSourceValue("mode", currentMode);
                        arduinoComm.setSourceValue("a", "Stop");
                    }
                    
                    if (currentMode.equals("Auto")) {
                        if (currentTrackClass != null) {
                            if (!currentTrackClass.equals("None")) {
                                if (!tracked) {
                                    if (currentUltrasonicSensor <= SENSOR_LIMIT) {
                                        Logger.warn("Arduino - Safety cut due to imminent collision.");
                                        pathfindControl.blockNext();
                                    } else {
                                        pathfindControl.unblockNext();
                                    }
                                    Logger.debug("PathfindController - ["
                                                    + pathfindControl + "].");
                                    
                                    if (openCVControl.isTrackClassFound()) {
                                        Logger.info("OpenCVController - Target found!");
                                        webInterfaceComm.setSourceValue(
                                                        "emotion", "Happy");
                                        arduinoComm.setSourceValue("a", "Stop");
                                        tracked = true;
                                    } else {
                                        Logger.info("OpenCVController - Target not found!");
                                        boolean backBlocked = false;
                                        boolean backVisited = false;
                                        boolean frontBlocked = false;
                                        boolean frontVisited = false;
                                        boolean leftBlocked = false;
                                        boolean leftVisited = false;
                                        boolean rightBlocked = false;
                                        boolean rightVisited = false;
                                        
                                        frontBlocked = pathfindControl
                                                        .isNextBlocked();
                                        frontVisited = pathfindControl
                                                        .isNextVisited();
                                        pathfindControl.rotateRight();
                                        rightBlocked = pathfindControl
                                                        .isNextBlocked();
                                        rightVisited = pathfindControl
                                                        .isNextVisited();
                                        pathfindControl.rotateRight();
                                        backBlocked = pathfindControl
                                                        .isNextBlocked();
                                        backVisited = pathfindControl
                                                        .isNextVisited();
                                        pathfindControl.rotateRight();
                                        leftBlocked = pathfindControl
                                                        .isNextBlocked();
                                        leftVisited = pathfindControl
                                                        .isNextVisited();
                                        pathfindControl.rotateRight();
                                        
                                        Logger.debug("PathfindController - [Back Blocked: "
                                                        + backBlocked
                                                        + "; Back Visited: "
                                                        + backVisited + "].");
                                        Logger.debug("PathfindController - [Front Blocked: "
                                                        + frontBlocked
                                                        + "; Front Visited: "
                                                        + frontVisited + "].");
                                        Logger.debug("PathfindController - [Left Blocked: "
                                                        + leftBlocked
                                                        + "; Left Visited: "
                                                        + leftVisited + "].");
                                        Logger.debug("PathfindController - [Right Blocked: "
                                                        + rightBlocked
                                                        + "; Right Visited: "
                                                        + rightVisited + "].");
                                        
                                        if (frontBlocked) { // B
                                            pathfindControl.rotateRight();
                                            if (rightBlocked) { // B-B
                                                pathfindControl.rotateRight();
                                                if (backBlocked) { // B-B-B
                                                    pathfindControl.rotateRight();
                                                    if (leftBlocked) { // B-B-B-B
                                                        pathfindControl.rotateRight();
                                                        Logger.debug("PathfindController - I can't seem to find a way out.");
                                                        webInterfaceComm.setSourceValue(
                                                                        "emotion",
                                                                        "Sad");
                                                        arduinoComm.setSourceValue(
                                                                        "a",
                                                                        "Stop");
                                                        tracked = true;
                                                    } else if (leftVisited) { // B-B-B-V
                                                        Logger.debug("PathfindController - Get out of my way!");
                                                        Logger.debug("PathfindController - Wait I visited that area before.");
                                                        Logger.debug("PathfindController - Left!");
                                                        webInterfaceComm.setSourceValue(
                                                                        "emotion",
                                                                        "Sad");
                                                        arduinoComm.setSourceValue(
                                                                        "a",
                                                                        "Left");
                                                    } else { // B-B-B-O
                                                        Logger.debug("PathfindController - Get out of my way!");
                                                        Logger.debug("PathfindController - Left!");
                                                        webInterfaceComm.setSourceValue(
                                                                        "emotion",
                                                                        "Angry");
                                                        arduinoComm.setSourceValue(
                                                                        "a",
                                                                        "Left");
                                                    }
                                                } else if (backVisited) { // B-B-V
                                                    pathfindControl.rotateRight();
                                                    if (leftBlocked) { // B-B-V-B
                                                        pathfindControl.rotateLeft();
                                                        Logger.debug("PathfindController - Get out of my way!");
                                                        Logger.debug("PathfindController - Back!");
                                                        webInterfaceComm.setSourceValue(
                                                                        "emotion",
                                                                        "Angry");
                                                        arduinoComm.setSourceValue(
                                                                        "a",
                                                                        "DRight");
                                                    } else if (leftVisited) { // B-B-V-V
                                                        pathfindControl.rotateLeft();
                                                        Logger.debug("PathfindController - Get out of my way!");
                                                        Logger.debug("PathfindController - Wait I visited that area before.");
                                                        Logger.debug("PathfindController - Back!");
                                                        webInterfaceComm.setSourceValue(
                                                                        "emotion",
                                                                        "Sad");
                                                        arduinoComm.setSourceValue(
                                                                        "a",
                                                                        "DLeft");
                                                    } else { // B-B-V-O
                                                        Logger.debug("PathfindController - Get out of my way!");
                                                        Logger.debug("PathfindController - Left!");
                                                        webInterfaceComm.setSourceValue(
                                                                        "emotion",
                                                                        "Angry");
                                                        arduinoComm.setSourceValue(
                                                                        "a",
                                                                        "Left");
                                                    }
                                                } else { // B-B-O
                                                    Logger.debug("PathfindController - Get out of my way!");
                                                    Logger.debug("PathfindController - Back!");
                                                    webInterfaceComm.setSourceValue(
                                                                    "emotion",
                                                                    "Angry");
                                                    arduinoComm.setSourceValue(
                                                                    "a",
                                                                    "DLeft");
                                                }
                                            } else if (rightVisited) { // B-V
                                                pathfindControl.rotateRight();
                                                if (backBlocked) { // B-V-B
                                                    pathfindControl.rotateRight();
                                                    if (leftBlocked) { // B-V-B-B
                                                        pathfindControl.rotateLeft();
                                                        pathfindControl.rotateLeft();
                                                        Logger.debug("PathfindController - Get out of my way!");
                                                        Logger.debug("PathfindController - Wait I visited that area before.");
                                                        Logger.debug("PathfindController - Right!");
                                                        webInterfaceComm.setSourceValue(
                                                                        "emotion",
                                                                        "Sad");
                                                        arduinoComm.setSourceValue(
                                                                        "a",
                                                                        "Right");
                                                    } else if (leftVisited) { // B-V-B-V
                                                        pathfindControl.rotateLeft();
                                                        pathfindControl.rotateLeft();
                                                        Logger.debug("PathfindController - Get out of my way!");
                                                        Logger.debug("PathfindController - Wait I visited that area before.");
                                                        Logger.debug("PathfindController - Right!");
                                                        webInterfaceComm.setSourceValue(
                                                                        "emotion",
                                                                        "Sad");
                                                        arduinoComm.setSourceValue(
                                                                        "a",
                                                                        "Right");
                                                    } else { // B-V-B-O
                                                        Logger.debug("PathfindController - Get out of my way!");
                                                        Logger.debug("PathfindController - Left!");
                                                        webInterfaceComm.setSourceValue(
                                                                        "emotion",
                                                                        "Angry");
                                                        arduinoComm.setSourceValue(
                                                                        "a",
                                                                        "Left");
                                                    }
                                                } else if (backVisited) { // B-V-V
                                                    pathfindControl.rotateRight();
                                                    if (leftBlocked) { // B-V-V-B
                                                        pathfindControl.rotateLeft();
                                                        Logger.debug("PathfindController - Get out of my way!");
                                                        Logger.debug("PathfindController - Wait I visited that area before.");
                                                        Logger.debug("PathfindController - Back!");
                                                        webInterfaceComm.setSourceValue(
                                                                        "emotion",
                                                                        "Sad");
                                                        arduinoComm.setSourceValue(
                                                                        "a",
                                                                        "DRight");
                                                    } else if (leftVisited) { // B-V-V-V
                                                        pathfindControl.rotateLeft();
                                                        pathfindControl.rotateLeft();
                                                        Logger.debug("PathfindController - Get out of my way!");
                                                        Logger.debug("PathfindController - Wait I visited that area before.");
                                                        Logger.debug("PathfindController - Right!");
                                                        webInterfaceComm.setSourceValue(
                                                                        "emotion",
                                                                        "Sad");
                                                        arduinoComm.setSourceValue(
                                                                        "a",
                                                                        "Right");
                                                    } else { // B-V-V-O
                                                        Logger.debug("PathfindController - Get out of my way!");
                                                        Logger.debug("PathfindController - Left!");
                                                        webInterfaceComm.setSourceValue(
                                                                        "emotion",
                                                                        "Angry");
                                                        arduinoComm.setSourceValue(
                                                                        "a",
                                                                        "Left");
                                                    }
                                                } else { // B-V-O
                                                    Logger.debug("PathfindController - Get out of my way!");
                                                    Logger.debug("PathfindController - Back!");
                                                    webInterfaceComm.setSourceValue(
                                                                    "emotion",
                                                                    "Angry");
                                                    arduinoComm.setSourceValue(
                                                                    "a",
                                                                    "DLeft");
                                                }
                                            } else { // B-O
                                                Logger.debug("PathfindController - Get out of my way!");
                                                Logger.debug("PathfindController - Right!");
                                                webInterfaceComm.setSourceValue(
                                                                "emotion",
                                                                "Angry");
                                                arduinoComm.setSourceValue("a",
                                                                "Right");
                                            }
                                        } else if (frontVisited) { // V
                                            pathfindControl.rotateRight();
                                            if (rightBlocked) { // V-B
                                                pathfindControl.rotateRight();
                                                if (backBlocked) { // V-B-B
                                                    pathfindControl.rotateRight();
                                                    if (leftBlocked) { // V-B-B-B
                                                        pathfindControl.rotateRight();
                                                        Logger.debug("PathfindController - Wait I visited that area before.");
                                                        Logger.debug("PathfindController - Forward!");
                                                        webInterfaceComm.setSourceValue(
                                                                        "emotion",
                                                                        "Sad");
                                                        pathfindControl.goNext();
                                                        arduinoComm.setSourceValue(
                                                                        "a",
                                                                        "Forward");
                                                    } else if (leftVisited) { // V-B-B-V
                                                        pathfindControl.rotateRight();
                                                        Logger.debug("PathfindController - Wait I visited that area before.");
                                                        Logger.debug("PathfindController - Forward!");
                                                        webInterfaceComm.setSourceValue(
                                                                        "emotion",
                                                                        "Sad");
                                                        pathfindControl.goNext();
                                                        arduinoComm.setSourceValue(
                                                                        "a",
                                                                        "Forward");
                                                    } else { // V-B-B-O
                                                        Logger.info("Nothing yet.");
                                                        Logger.debug("PathfindController - Left!");
                                                        webInterfaceComm.setSourceValue(
                                                                        "emotion",
                                                                        "Neutral");
                                                        arduinoComm.setSourceValue(
                                                                        "a",
                                                                        "Left");
                                                    }
                                                } else if (backVisited) { // V-B-V
                                                    pathfindControl.rotateRight();
                                                    if (leftBlocked) { // V-B-V-B
                                                        pathfindControl.rotateRight();
                                                        Logger.debug("PathfindController - Wait I visited that area before.");
                                                        Logger.debug("PathfindController - Forward!");
                                                        webInterfaceComm.setSourceValue(
                                                                        "emotion",
                                                                        "Sad");
                                                        pathfindControl.goNext();
                                                        arduinoComm.setSourceValue(
                                                                        "a",
                                                                        "Forward");
                                                    } else if (leftVisited) { // V-B-V-V
                                                        pathfindControl.rotateRight();
                                                        Logger.debug("PathfindController - Wait I visited that area before.");
                                                        Logger.debug("PathfindController - Forward!");
                                                        webInterfaceComm.setSourceValue(
                                                                        "emotion",
                                                                        "Sad");
                                                        pathfindControl.goNext();
                                                        arduinoComm.setSourceValue(
                                                                        "a",
                                                                        "Forward");
                                                    } else { // V-B-V-O
                                                        Logger.info("Nothing yet.");
                                                        Logger.debug("PathfindController - Left!");
                                                        webInterfaceComm.setSourceValue(
                                                                        "emotion",
                                                                        "Angry");
                                                        arduinoComm.setSourceValue(
                                                                        "a",
                                                                        "Left");
                                                    }
                                                } else { // V-B-O
                                                    Logger.info("Nothing yet.");
                                                    Logger.debug("PathfindController - Back!");
                                                    webInterfaceComm.setSourceValue(
                                                                    "emotion",
                                                                    "Angry");
                                                    arduinoComm.setSourceValue(
                                                                    "a",
                                                                    "DLeft");
                                                }
                                            } else if (rightVisited) { // V-V
                                                pathfindControl.rotateRight();
                                                if (backBlocked) { // V-V-B
                                                    pathfindControl.rotateRight();
                                                    if (leftBlocked) { // V-V-B-B
                                                        pathfindControl.rotateRight();
                                                        Logger.debug("PathfindController - Wait I visited that area before.");
                                                        Logger.debug("PathfindController - Forward!");
                                                        webInterfaceComm.setSourceValue(
                                                                        "emotion",
                                                                        "Sad");
                                                        pathfindControl.goNext();
                                                        arduinoComm.setSourceValue(
                                                                        "a",
                                                                        "Forward");
                                                    } else if (leftVisited) { // V-V-B-V
                                                        pathfindControl.rotateRight();
                                                        Logger.debug("PathfindController - Wait I visited that area before.");
                                                        Logger.debug("PathfindController - Forward!");
                                                        webInterfaceComm.setSourceValue(
                                                                        "emotion",
                                                                        "Sad");
                                                        pathfindControl.goNext();
                                                        arduinoComm.setSourceValue(
                                                                        "a",
                                                                        "Forward");
                                                    } else { // V-V-B-O
                                                        Logger.info("Nothing yet.");
                                                        Logger.debug("PathfindController - Left!");
                                                        webInterfaceComm.setSourceValue(
                                                                        "emotion",
                                                                        "Neutral");
                                                        arduinoComm.setSourceValue(
                                                                        "a",
                                                                        "Left");
                                                    }
                                                } else if (backVisited) { // V-V-V
                                                    pathfindControl.rotateRight();
                                                    if (leftBlocked) { // V-V-V-B
                                                        pathfindControl.rotateRight();
                                                        Logger.debug("PathfindController - Wait I visited that area before.");
                                                        Logger.debug("PathfindController - Forward!");
                                                        webInterfaceComm.setSourceValue(
                                                                        "emotion",
                                                                        "Sad");
                                                        pathfindControl.goNext();
                                                        arduinoComm.setSourceValue(
                                                                        "a",
                                                                        "Forward");
                                                    } else if (leftVisited) { // V-V-V-V
                                                        pathfindControl.rotateRight();
                                                        Logger.debug("PathfindController - Wait I visited that area before.");
                                                        Logger.debug("PathfindController - Forward!");
                                                        webInterfaceComm.setSourceValue(
                                                                        "emotion",
                                                                        "Sad");
                                                        pathfindControl.goNext();
                                                        arduinoComm.setSourceValue(
                                                                        "a",
                                                                        "Forward");
                                                    } else { // V-V-V-O
                                                        Logger.info("Nothing yet.");
                                                        Logger.debug("PathfindController - Left!");
                                                        webInterfaceComm.setSourceValue(
                                                                        "emotion",
                                                                        "Neutral");
                                                        arduinoComm.setSourceValue(
                                                                        "a",
                                                                        "Left");
                                                    }
                                                } else { // V-V-O
                                                    Logger.info("Nothing yet.");
                                                    Logger.debug("PathfindController - Back!");
                                                    webInterfaceComm.setSourceValue(
                                                                    "emotion",
                                                                    "Neutral");
                                                    arduinoComm.setSourceValue(
                                                                    "a",
                                                                    "DLeft");
                                                }
                                            } else { // V-O
                                                Logger.info("Nothing yet.");
                                                Logger.debug("PathfindController - Right!");
                                                webInterfaceComm.setSourceValue(
                                                                "emotion",
                                                                "Neutral");
                                                arduinoComm.setSourceValue("a",
                                                                "Right");
                                            }
                                        } else { // O
                                            Logger.info("Nothing yet.");
                                            Logger.debug("PathfindController - Forward!");
                                            webInterfaceComm.setSourceValue(
                                                            "emotion",
                                                            "Neutral");
                                            pathfindControl.goNext();
                                            arduinoComm.setSourceValue("a",
                                                            "Forward");
                                        }
                                    }
                                } else {
                                    arduinoComm.setSourceValue("a", "Stop");
                                }
                            } else {
                                webInterfaceComm.setSourceValue("emotion",
                                                "Neutral");
                                arduinoComm.setSourceValue("a", "Stop");
                            }
                        } else {
                            Logger.warn("WebInterface - [trackclass: "
                                            + currentTrackClass
                                            + "] is invalid, setting to [trackclass: None].");
                            webInterfaceComm.setRobotValue("trackclass",
                                            "None");
                        }
                    } else if (currentMode.equals("Idle")) {
                        webInterfaceComm.setRobotValue("trackclass", "None");
                    } else if (currentMode.equals("Off")) {
                        break;
                    } else if (currentMode.equals("Manual")) {
                        webInterfaceComm.setRobotValue("trackclass", "None");
                        webInterfaceComm.setSourceValue("emotion", "Neutral");
                        
                        // Manual move
                        final String move = webInterfaceComm.getRobotValue(
                                        "move");
                        if (move != null) {
                            if (!move.equals(currentMove)) {
                                currentMove = move;
                                Logger.debug("WebInterface - [move: "
                                                + currentMove + "] received.");
                                
                                if (currentUltrasonicSensor <= SENSOR_LIMIT) {
                                    if (currentUltrasonicSensor <= SENSOR_LIMIT
                                                    / 2) {
                                        webInterfaceComm.setSourceValue(
                                                        "emotion", "Sad");
                                    } else {
                                        webInterfaceComm.setSourceValue(
                                                        "emotion", "Angry");
                                    }
                                    webInterfaceComm.setRobotValue("move",
                                                    "Stop");
                                    if (!move.equals("Stop")) {
                                        if (move.equals("Forward")) {
                                            Logger.warn("Arduino - Safety cut due to imminent collision.");
                                            arduinoComm.setSourceValue("a",
                                                            "Stop");
                                        } else {
                                            arduinoComm.setSourceValue("a",
                                                            currentMove);
                                        }
                                    }
                                } else {
                                    webInterfaceComm.setSourceValue("emotion",
                                                    "Happy");
                                    webInterfaceComm.setRobotValue("move",
                                                    "Stop");
                                    arduinoComm.setSourceValue("a",
                                                    currentMove);
                                }
                            }
                        } else {
                            Logger.warn("WebInterface - [move: " + currentMove
                                            + "] is invalid, setting to [move: Stop].");
                            webInterfaceComm.setRobotValue("move", "Stop");
                        }
                    } else {
                        Logger.warn("WebInterface - [mode: " + currentMode
                                        + "] is invalid, setting to [mode: Idle].");
                        webInterfaceComm.setRobotValue("mode", "Idle");
                    }
                } else {
                    Logger.warn("WebInterface - [mode: " + currentMode
                                    + "] is invalid, setting to [mode: Idle].");
                    webInterfaceComm.setRobotValue("mode", "Idle");
                }
                webInterfaceComm.pushSource();
                webInterfaceComm.pushRobot();
                arduinoComm.pushSource();
            } catch (CommunicationException e) {
                Logger.error(e, "There was an issue with Communication!");
            } catch (Exception e) {
                Logger.error(e, "There was an unknown issue!");
            } finally {
                // Delay for safety
                delayThread(1000);
            }
        }
    }
    
    /**
     * Sets the application up.
     *
     * @param args
     *            the arguments
     * @throws UnsatisfiedLinkError
     *             if a native library failed to load or if the system
     *             architecture is not valid for Fetch Bot.
     */
    private static void setup(String[] args) {
        String profile = "Normal";
        if (args.length > 0) {
            profile = args[0];
            if (profile.equals("Normal")) {
                /* */ } else if (profile.equals("Debug")) {
                /* */ } else {
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
        } catch (CommunicationException e) {
            Logger.error(e, "There was an issue with Communication!");
        } catch (Exception e) {
            Logger.error(e, "There was an unknown issue!");
        } finally {
            /* */ }
            
        // Startup logging
        Logger.info("Core - Fetch Bot " + VERSION + " started as profile "
                        + profile + "!");
        try {
            webInterfaceComm.pushSource();
        } catch (CommunicationException e) {
            Logger.error(e, "There was an issue with Communication!");
        } catch (Exception e) {
            Logger.error(e, "There was an unknown issue!");
        } finally {
            /* */ }
            
        // Initiate controllers
        try {
            openCVControl = new OpenCVControllerImpl();
        } catch (ControllerException e) {
            Logger.error(e, "There was an issue with Controller!");
        } catch (Exception e) {
            Logger.error(e, "There was an unknown issue!");
        } finally {
            /* */ }
        pathfindControl = new PathfindControllerImpl();
        openCVControl.startCamera();
        
        // Version check
        boolean versionOk = false;
        try {
            versionOk = VersionCheck.verify(VERSION);
            if (!versionOk) {
                Logger.warn("VersionCheck - Version mismatch [this: " + VERSION
                                + "; current: " + VersionCheck
                                                .getCurrentVersion()
                                + "], this version might be outdated!");
            }
        } catch (VersionCheckException e) {
            Logger.error(e, "There was an issue with VersionCheck!");
        } catch (Exception e) {
            Logger.error(e, "There was an unknown issue!");
        } finally {
            /* */ }
    }
    
    /**
     * Terminates the application resources.
     */
    private static void terminate() {
        Logger.info("Core - Fetch Bot terminating! Log file: "
                        + Logger.LOG_FILE);
        try {
            openCVControl.stopCamera();
            openCVControl.close();
            arduinoComm.setSourceValue("a", "Stop");
            arduinoComm.pushSource();
            arduinoComm.clear();
            webInterfaceComm.clear();
            webInterfaceComm.pushSource();
            webInterfaceComm.pushRobot();
        } catch (CommunicationException e) {
            Logger.error(e, "There was an issue with Communication!");
        } catch (Exception e) {
            Logger.error(e, "There was an unknown issue!");
        } finally {
            Logger.close();
        }
    }
    
    // Public Static Final Methods
    
    /**
     * Delays a thread.
     *
     * @param millis
     *            the milliseconds
     */
    public static final void delayThread(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Logger.warn(e, "Thread was interrupted!");
        } catch (Exception e) {
            Logger.error(e, "There was an unknown issue!");
        } finally {
            /* */ }
    }
    
    // Public Static Methods
    // Entry Point
    
    /**
     * The main method.
     *
     * @param args
     *            the arguments
     * @throws UnsatisfiedLinkError
     *             if a native library failed to load or if the system
     *             architecture is not valid for Fetch Bot.
     */
    public static void main(String[] args) {
        // Setup
        setup(args);
        
        // Run
        run();
        
        // Termination
        terminate();
    }
}

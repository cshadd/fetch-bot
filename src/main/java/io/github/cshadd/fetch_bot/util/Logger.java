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
package io.github.cshadd.fetch_bot.util;

import com.pi4j.util.Console;
import io.github.cshadd.fetch_bot.Component;
import io.github.cshadd.fetch_bot.FetchBot;
import io.github.cshadd.fetch_bot.io.WebInterfaceCommunication;
import java.io.File;
import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.SimpleFormatter;
import org.apache.commons.io.FileUtils;

// Main

/**
 * The Class Logger. A Logger handles all log actions for Fetch Bot. It makes
 * the Java utility logger more robust.
 * 
 * @author Christian Shadd
 * @author Maria Verna Aquino
 * @author Thanh Vu
 * @author Joseph Damian
 * @author Giovanni Orozco
 * @since 1.0.0
 */
@Component("Util")
public class Logger implements FetchBot {
    // Private Constant Instance/Property Fields
    
    /**
     * The Constant CONSOLE.
     */
    private static final Console CONSOLE = new Console();
    
    /**
     * The Constant TAG.
     */
    private static final String TAG = "[FETCH BOT]";
    
    // Public Constant Instance/Property Fields
    
    /**
     * The Constant LOG_FILE.
     */
    public static final String LOG_FILE = "FetchBot.log";
    
    // Private Static Instance/Property Fields
    
    /**
     * The debug flag.
     */
    private static boolean debug;
    
    /**
     * The Java utility logger.
     */
    private static java.util.logging.Logger javaLogger;
    
    /**
     * The Java logger handler.
     */
    private static FileHandler javaLoggerHandler;
    
    /**
     * The Java logger handler formatter.
     */
    private static SimpleFormatter javaLoggerHandlerFormatter;
    
    /**
     * The Web Interface Communications.
     */
    private static WebInterfaceCommunication webInterfaceComm;
    
    // Private Constructors
    
    /**
     * Instantiates a new Logger.
     */
    private Logger() {
    }
    
    // Private Static Methods
    
    /**
     * Reads from the logs.
     *
     * @return the logs
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    private static String read() throws IOException {
        File input = new File(LOG_FILE);
        String returnData = FileUtils.readFileToString(input, "UTF-8");
        return returnData;
    }
    
    /**
     * Write to the logs.
     *
     * @param level
     *            the level
     * @param msg
     *            the message
     * @param error
     *            the error
     */
    private static void write(Level level, String msg, Throwable error) {
        if (error != null) {
            javaLogger.log(level, msg, error);
        } else {
            javaLogger.log(level, msg);
        }
    }
    
    /**
     * Write the log to the Web Interface.
     *
     * @param level
     *            the level
     * @param msg
     *            the message
     */
    private static void writeInterface(Level level, String msg) {
        writeInterface(level, msg, null);
    }
    
    /**
     * Write the log to the Web Interface.
     *
     * @param level
     *            the level
     * @param msg
     *            the message
     * @param error
     *            the error
     */
    private static void writeInterface(Level level, String msg,
                    Throwable error) {
        write(level, msg, error);
        try {
            if (webInterfaceComm != null) {
                webInterfaceComm.setSourceValue("verbose", read());
            }
        } catch (IOException e) {
            fatalError(e, "Could not read " + LOG_FILE + ".");
        } catch (Exception e) {
            fatalError(e, "Unknown issue.");
        } finally {
            /* */ }
    }
    
    // Public Static Methods
    static {
        javaLogger = java.util.logging.Logger.getLogger(
                        java.util.logging.Logger.GLOBAL_LOGGER_NAME);
        javaLogger.setLevel(Level.ALL);
        javaLoggerHandlerFormatter = new SimpleFormatter();
        final java.util.logging.Logger javaRootLogger = java.util.logging.Logger
                        .getLogger("");
        final Handler[] handlers = javaRootLogger.getHandlers();
        if (handlers[0] instanceof ConsoleHandler) {
            javaRootLogger.removeHandler(handlers[0]);
        }
    }
    
    /**
     * Clears the logger.
     */
    public static void clear() {
        CONSOLE.title("--- Fetch Bot ---",
                        "https://cshadd.github.io/fetch-bot/");
        File input = null;
        try {
            input = new File(LOG_FILE);
            FileUtils.deleteQuietly(input);
            javaLoggerHandler = new FileHandler(LOG_FILE);
            javaLoggerHandler.setFormatter(javaLoggerHandlerFormatter);
            javaLogger.addHandler(javaLoggerHandler);
        } catch (Exception e) {
            fatalError(e, "Unknown issue.");
        } finally {
            /* */ }
    }
    
    /**
     * Closes the logger.
     */
    public static void close() {
        javaLogger.removeHandler(javaLoggerHandler);
    }

    /**
     * Closes the logger with a prompt.
     */
    public static void closePrompt() {
        close();
        CONSOLE.promptForExit();
    }
    
    
    /**
     * Log a debug message.
     *
     * @param msg
     *            the message
     */
    public static void debug(String msg) {
        if (debug) {
            final String newMsg = TAG + " [DEBUG] " + msg;
            CONSOLE.println(newMsg);
            writeInterface(Level.INFO, newMsg);
        }
    }
    
    /**
     * Log an error message.
     *
     * @param e
     *            the error
     * @param msg
     *            the message
     */
    public static void error(Throwable e, String msg) {
        final String newMsg = TAG + " [ERROR (SAFELY CAUGHT)] " + msg;
        CONSOLE.println("\u001B[31m" + newMsg
                        + "\nPlease report this issue to the developers!\u001B[0m");
        writeInterface(Level.SEVERE, newMsg, e);
    }
    
    /**
     * Log a fatal error message.
     *
     * @param e
     *            the error
     * @param msg
     *            the message
     */
    public static void fatalError(Throwable e, String msg) {
        final String newMsg = TAG + " [FATAL ERROR (SAFELY CAUGHT)] " + msg;
        CONSOLE.println("\u001B[31m" + newMsg
                        + "\nPlease report this issue to the developers!\u001B[0m");
        write(Level.SEVERE, newMsg, e);
    }
    
    /**
     * Log an info message.
     *
     * @param msg
     *            the message
     */
    public static void info(String msg) {
        final String newMsg = TAG + " [INFO] " + msg;
        CONSOLE.println(newMsg);
        writeInterface(Level.INFO, newMsg);
    }
    
    /**
     * Sets the Web Interface Communications.
     *
     * @param comm
     *            the new web interface communications
     */
    public static void setWebInterfaceCommunications(
                    WebInterfaceCommunication comm) {
        webInterfaceComm = comm;
    }
    
    /**
     * Sets the Logger to debug mode.
     */
    public static void setToDebugMode() {
        debug = true;
    }
    
    /**
     * Log a warn message.
     *
     * @param msg
     *            the message
     */
    public static void warn(String msg) {
        warn(null, msg);
    }
    
    /**
     * Log a warn message.
     *
     * @param e
     *            the error
     * @param msg
     *            the message
     */
    public static void warn(Throwable e, String msg) {
        final String newMsg = TAG + " [WARN] " + msg;
        CONSOLE.println("\u001B[33m" + newMsg + "\u001B[0m");
        writeInterface(Level.WARNING, newMsg, e);
    }
}
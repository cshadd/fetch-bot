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
@Component("Util")
public class Logger
implements FetchBot {
    // Private Constant Instance/Property Fields
    private static final String TAG = "[FETCH BOT]";
    
    // Public Constant Instance/Property Fields
    public static final String LOG_PATH = "FetchBot.log";
    
    // Private Instance/Property Fields
    private static final Console console;
    private static boolean debug;
    private static java.util.logging.Logger javaLogger;
    private static FileHandler javaLoggerHandler;
    private static SimpleFormatter javaLoggerHandlerFormatter;
    private static WebInterfaceCommunication webInterfaceComm;

    // Private Constructors
    private Logger() { }

    // Private Static Methods
    private static String read()
    throws IOException {
        File input = new File(LOG_PATH);
        String returnData = FileUtils.readFileToString(input, "UTF-8");
        return returnData;
    }
    private static void write(Level level, String msg, boolean append, Throwable error) {
        if (error != null) {
            javaLogger.log(level, msg, error);
        }
        else {
            javaLogger.log(level, msg);
        }
    }
    private static void writeInterface(Level level, String msg, boolean append) {
        writeInterface(level, msg, append, null);
    }
    private static void writeInterface(Level level, String msg, boolean append, Throwable error) {
        write(level, msg, append, error);
        try {
            if (webInterfaceComm != null) {
                webInterfaceComm.setSourceValue("verbose", read());
            }
        }
        catch (Exception e) {
            fatalError(e, "There was an unknown issue!");
        }
        finally { }
    }

    // Public Static Methods
    static {
        console = new Console();
        javaLogger = java.util.logging.Logger.getLogger(java.util.logging.Logger.GLOBAL_LOGGER_NAME);
        javaLogger.setLevel(Level.ALL);
        javaLoggerHandlerFormatter = new SimpleFormatter();
        final java.util.logging.Logger javaRootLogger = java.util.logging.Logger.getLogger("");
        final Handler[] handlers = javaRootLogger.getHandlers();
        if (handlers[0] instanceof ConsoleHandler) {
            javaRootLogger.removeHandler(handlers[0]);
        }
    }
    public static void clear() {
        console.title("--- Fetch Bot ---", "https://cshadd.github.io/fetch-bot/");
        File input = null;
        try {
            input = new File(LOG_PATH);
            FileUtils.deleteQuietly(input);
            javaLoggerHandler = new FileHandler(LOG_PATH);
            javaLoggerHandler.setFormatter(javaLoggerHandlerFormatter);
            javaLogger.addHandler(javaLoggerHandler);
        }
        catch (Exception e) {
            fatalError(e, "There was an unknown issue!");
        }
        finally { }
    }
    public static void close() {
        javaLogger.removeHandler(javaLoggerHandler);
        console.promptForExit();
    }
    public static void debug(String msg) {
        debug(msg, true);
    }
    public static void debug(String msg, boolean append) {
        if (debug) {
            msg = TAG + " [DEBUG] " + msg;
            console.println(msg);
            writeInterface(Level.INFO, msg, append);
        }
    }
    public static void error(Throwable e, String msg) {
        error(e, msg, true);
    }
    public static void error(Throwable e, String msg, boolean append) {
        msg = TAG + " [ERROR (SAFELY CAUGHT)] " + msg;
        console.println("\u001B[31m" + msg + "\nPlease report this issue to the developers!\u001B[0m");
        writeInterface(Level.SEVERE, msg, append, e);
    }
    public static void fatalError(Throwable e, String msg) {
        fatalError(e, msg, true);
    }
    public static void fatalError(Throwable e, String msg, boolean append) {
        msg = TAG + " [FATAL ERROR (SAFELY CAUGHT)] " + msg;
        console.println("\u001B[31m" + msg + "\nPlease report this issue to the developers!\u001B[0m");
        write(Level.SEVERE, msg, append, e);
    }
    public static void info(String msg) {
        info(msg, true);
    }
    public static void info(String msg, boolean append) {
        msg = TAG + " [INFO] " + msg;
        console.println(msg);
        writeInterface(Level.INFO, msg, append);
    }
    public static void setWebInterfaceCommunications(WebInterfaceCommunication comm) {
        webInterfaceComm = comm;
    }
    public static void setToDebugMode() {
        debug = true;
    }
    public static void warn(String msg) {
        warn(null, msg);
    }
    public static void warn(Throwable e, String msg) {
        warn(e, msg, true);
    }
    public static void warn(Throwable e, String msg, boolean append) {
        msg = TAG + " [WARN] " + msg;
        console.println("\u001B[33m" + msg + "\u001B[0m");
        writeInterface(Level.WARNING, msg, append, e);
    }
}
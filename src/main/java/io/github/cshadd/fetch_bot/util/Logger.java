package io.github.cshadd.fetch_bot.util;
import io.github.cshadd.fetch_bot.FetchBot;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import org.apache.commons.io.FileUtils;

// Main
public final class Logger
implements FetchBot {
    // Private Constant Instance/Property Fields
    private static final String LOG_PATH = "FetchBot.log";
    private static final String TAG = "[FETCH BOT]";

    // Private Static Instance/Property Fields
    private static Logger instance;

    // Private Instance/Property Fields
    private static InterfaceCommunication interfaceComm;

    // Private Constructors
    private Logger() {
        this(null);
    }
    private Logger(InterfaceCommunication interfaceComm) {
        this.interfaceComm = interfaceComm;
    }

    // Private Static Final Methods
    private static final void write(String msg, boolean append) {
        File input = null;
        SimpleDateFormat localTime = null;
        SimpleDateFormat time = null;
        try {
            time = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
            localTime = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
            time.setTimeZone(TimeZone.getTimeZone("GMT"));
            msg = localTime.parse(time.format(new Date())) + " " + msg + "\n";
            input = new File(LOG_PATH);
            FileUtils.writeStringToFile(input, msg, "UTF-8", append);
        }
        catch (Exception e) {
            fatalError(e, "There was an unknown issue!");
        }
        finally { }
    }
    private static final void writeInterface(String msg, boolean append) {
        write(msg, append);
        try {
            if (interfaceComm != null) {
                interfaceComm.setInterfaceValue("verbose", read());
            }
        }
        catch (Exception e) {
            fatalError(e, "There was an unknown issue!");
        }
        finally { }
    }

    // Public Static Final Methods
    public static final synchronized Logger getInstance() {
        return getInstance(null);
    }
    public static final synchronized Logger getInstance(InterfaceCommunication interfaceComm) {
        if (instance != null) {
            return instance;
        }
        return new Logger(interfaceComm);
    }
    public static final void clear() {
        File input = null;
        try {
            input = new File(LOG_PATH);
            FileUtils.deleteQuietly(input);
        }
        catch (Exception e) {
            fatalError(e, "There was an unknown issue!");
        }
        finally { }
    }
    public static final void error(String msg) {
        error(msg, true);
    }
    public static final void error(Throwable e, String msg) {
        final StringWriter errors = new StringWriter();
        e.printStackTrace(new PrintWriter(errors));
        error(errors + "\n" + msg);
    }
    public static final void error(String msg, boolean append) {
        msg = TAG + " [ERROR (SAFELY CAUGHT)] " + msg;
        System.err.println(msg + "\nPlease report this issue to the developers!");
        writeInterface(msg, append);
    }
    public static final void fatalError(String msg) {
        fatalError(msg, true);
    }
    public static final void fatalError(Throwable e, String msg) {
        final StringWriter errors = new StringWriter();
        e.printStackTrace(new PrintWriter(errors));
        fatalError(errors + "\n" + msg);
    }
    public static final void fatalError(String msg, boolean append) {
        msg = TAG + " [FATAL ERROR (SAFELY CAUGHT)] " + msg;
        System.err.println(msg + "\nPlease report this issue to the developers!");
        write(msg, append);
    }
    public static final void info(String msg) {
        info(msg, true);
    }
    public static final void info(String msg, boolean append) {
        msg = TAG + " [INFO] " + msg;
        System.out.println(msg);
        writeInterface(msg, append);
    }
    public static final String read()
    throws IOException {
        File input = new File(LOG_PATH);
        String returnData = FileUtils.readFileToString(input, "UTF-8");
        return returnData;
    }
    public static final void warn(String msg) {
        warn(msg, true);
    }
    public static final void warn(Throwable e, String msg) {
        final StringWriter errors = new StringWriter();
        e.printStackTrace(new PrintWriter(errors));
        warn(errors + "\n" + msg);
    }
    public static final void warn(String msg, boolean append) {
        msg = TAG + " [WARN] " + msg;
        System.out.println(msg);
        writeInterface(msg, append);
    }
}

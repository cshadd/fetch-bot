package io.github.cshadd.fetch_bot.util;
import io.github.cshadd.fetch_bot.FetchBot;
import java.io.File;
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

    // Private Instance/Property Fields
    private static Communication comm = new Communication();

    // Private Constructors
    private Logger() { }

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
            comm.writeToInterfaceSafeLog("verbose", read());
        }
        catch (Exception e) {
            fatalError(e + "\nThere was an unknown issue!");
        }
        finally { }
    }

    // Public Static Final Methods
    public static final void clear() {
        File input = null;
        try {
            input = new File(LOG_PATH);
            FileUtils.deleteQuietly(new File(LOG_PATH));
        }
        catch (Exception e) {
            fatalError(e + "\nThere was an unknown fatal error!");
        }
        finally { }
    }
    public static final void error(String msg) {
        error(msg, true);
    }
    public static final void error(String msg, boolean append) {
        msg = TAG + " [ERROR (SAFELY CAUGHT)] " + msg;
        System.err.println(msg + "\nPlease report this issue to the developers! You may want to check FetchBot.log!");
        write(msg, append);
    }
    public static final void fatalError(String msg) {
        msg = TAG + " [FATAL ERROR (SAFELY CAUGHT, NO LOG)] " + msg;
        System.err.println(msg + "\nPlease report this issue to the developers!");
    }
    public static final void info(String msg) {
        info(msg, true);
    }
    public static final void info(String msg, boolean append) {
        msg = TAG + " [INFO] " + msg;
        System.out.println(msg);
        write(msg, append);
    }
    public static final String read() {
        File input = null;
        String returnData = null;
        try {
            input = new File(LOG_PATH);
            returnData = FileUtils.readFileToString(input, "UTF-8");
        }
        catch (Exception e) {
            fatalError(e + "\nThere was an unknown issue!");
        }
        finally { }
        return returnData;
    }
    public static final void warn(String msg) {
        warn(msg, true);
    }
    public static final void warn(String msg, boolean append) {
        msg = TAG + " [WARN] " + msg;
        System.out.println(msg + "\nYou may want to check FetchBot.log!");
        write(msg, append);
    }
}

package io.github.cshadd.fetch_bot;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import org.apache.commons.io.FileUtils;

// Main
public class Logger
implements FetchBot {
    // Private Constant Instance/Property Fields
    private static final String TAG = "[FETCH BOT]";

    // Public Static Final Methods
    public static final void error(String msg) {
        error(msg, true);
    }
    public static final void error(String msg, boolean append) {
        msg = TAG + " [ERROR (SAFELY CAUGHT)] " + msg;
        System.err.println(msg + "\nPlease report this issue to the developers! You may want to check FetchBot.log!");
        writeToFile(msg, append);
    }
    public static final void info(String msg) {
        info(msg, true);
    }
    public static final void info(String msg, boolean append) {
        msg = TAG + " [INFO] " + msg;
        System.out.println(msg);
        writeToFile(msg, append);
    }
    public static final void warn(String msg) {
        warn(msg, true);
    }
    public static final void warn(String msg, boolean append) {
        msg = TAG + " [WARN] " + msg;
        System.out.println(msg + "\nYou may want to check FetchBot.log!");
        writeToFile(msg, append);
    }

    // Private Static Final Methods
    private static final void writeToFile(String msg, boolean append) {
        try {
            final SimpleDateFormat time = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
            final SimpleDateFormat localTime = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
            time.setTimeZone(TimeZone.getTimeZone("GMT"));
            msg = localTime.parse(time.format(new Date())) + " " + msg + "\n";
            FileUtils.writeStringToFile(new File("FetchBot.log"), msg, "UTF-8", append);
        }
        catch (Exception e) {
            System.err.println("FetchBot internal unrecoverable error!");
            e.printStackTrace();
        }
        finally { }
    }
}

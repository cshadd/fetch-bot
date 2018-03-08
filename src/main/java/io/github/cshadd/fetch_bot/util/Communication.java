package io.github.cshadd.fetch_bot.util;
import io.github.cshadd.fetch_bot.FetchBot;
import java.io.*;
import org.apache.commons.io.*;
import org.json.*;

// Main
public final class Communication
implements FetchBot {
    // Private Constant Instance/Property Fields
    private static final String TO_INTERFACE_JSON_PATH = "/var/www/html/FetchBot/comms/toInterface.json";
    private static final String TO_ROBOT_JSON_PATH = "/var/www/html/FetchBot/comms/toRobot.json";

    // Public Constructors
    public Communication() { }

    // Private Final Methods
    private final JSONObject read(String filePath) {
        File input = null;
        JSONObject returnData = null;
        Logger.info("Communication - Reading JSON of " + filePath + ".");
        try {
            input = new File(filePath);
            returnData = new JSONObject(FileUtils.readFileToString(input, "UTF-8"));
        }
        catch (IOException e) {
            Logger.error(e + "\nThere was an issue with IO!");
        }
        catch (JSONException e) {
            Logger.error(e + "\nThere was an issue with JSON!");
        }
        catch (Exception e) {
            Logger.error(e + "\nThere was an unknown issue!");
        }
        finally { }
        return returnData;
    }
    private final JSONObject readSafeLog(String filePath) {
        File input = null;
        JSONObject returnData = null;
        try {
            input = new File(filePath);
            returnData = new JSONObject(FileUtils.readFileToString(input, "UTF-8"));
        }
        catch (Exception e) {
            Logger.fatalError(e + "\nThere was an unknown issue!");
        }
        finally { }
        return returnData;
    }
    private final void write(JSONObject json, String filePath) {
        File input = null;
        Logger.info("Communication - Writing JSON to " + filePath + ".");
        try {
            input = new File(filePath);
            FileUtils.writeStringToFile(input, "" + json, "UTF-8");
        }
        catch (IOException e) {
            Logger.error(e + "\nThere was an issue with IO!");
        }
        catch (JSONException e) {
            Logger.error(e + "\nThere was an issue with JSON!");
        }
        catch (Exception e) {
            Logger.error(e + "\nThere was an unknown issue!");
        }
        finally { }
    }
    private final void writeSafeLog(JSONObject json, String filePath) {
        File input = null;
        try {
            input = new File(filePath);
            FileUtils.writeStringToFile(input, "" + json, "UTF-8");
        }
        catch (Exception e) {
            Logger.fatalError(e + "\nThere was an unknown issue!");
        }
        finally { }
    }

    // Public Final Methods
    public final String readToInterface(String key) {
        String returnData = null;
        try {
            returnData = read(TO_INTERFACE_JSON_PATH).getString(key);
        }
        catch (JSONException e) {
            Logger.error(e + "\nThere was an issue with JSON!");
        }
        catch (Exception e) {
            Logger.error(e + "\nThere was an unknown issue!");
        }
        finally { }
        return returnData;
    }
    public final String readToInterfaceSafeLog(String key) {
        String returnData = null;
        try {
            returnData = readSafeLog(TO_INTERFACE_JSON_PATH).getString(key);
        }
        catch (Exception e) {
            Logger.fatalError(e + "\nThere was an unknown issue!");
        }
        finally { }
        return returnData;
    }
    public final String readToRobot(String key) {
        String returnData = null;
        try {
            returnData = read(TO_ROBOT_JSON_PATH).getString(key);
        }
        catch (JSONException e) {
            Logger.error(e + "\nThere was an issue with JSON!");
        }
        catch (Exception e) {
            Logger.error(e + "\nThere was an unknown issue!");
        }
        finally { }
        return returnData;
    }
    public final String readToRobotSafeLog(String key) {
        String returnData = null;
        try {
            returnData = readSafeLog(TO_ROBOT_JSON_PATH).getString(key);
        }
        catch (Exception e) {
            Logger.fatalError(e + "\nThere was an unknown issue!");
        }
        finally { }
        return returnData;
    }
    public final void resetToInterface() {
        try {
            FileUtils.writeStringToFile(new File(TO_INTERFACE_JSON_PATH), "{}", "UTF-8", true);
        }
        catch (Exception e) {
            Logger.fatalError(e + "\nThere was an unknown issue!");
        }
        finally { }
        JSONObject data = new JSONObject();
        data.put("emotion", "Boot")
                    .put("mode", "Off")
                    .put("rot", "0")
                    .put("sensorback", "0")
                    .put("sensorfront", "0")
                    .put("sensorleft", "0")
                    .put("sensorright", "0")
                    .put("verbose", "...")
                    .put("x", "0")
                    .put("xmax", "0")
                    .put("y", "0")
                    .put("ymax", "0");
        writeSafeLog(data, TO_INTERFACE_JSON_PATH);
    }
    public final void resetToRobot() {
        try {
            FileUtils.writeStringToFile(new File(TO_ROBOT_JSON_PATH), "{}", "UTF-8", true);
        }
        catch (Exception e) {
            Logger.fatalError(e + "\nThere was an unknown issue!");
        }
        finally { }
        JSONObject data = new JSONObject();
        data.put("Stop", "0");
        writeSafeLog(data, TO_ROBOT_JSON_PATH);
    }
    public final void writeToInterface(String key, String value) {
        JSONObject data = null;
        try {
            data = read(TO_INTERFACE_JSON_PATH).put(key, value);
            write(data, TO_INTERFACE_JSON_PATH);
        }
        catch (JSONException e) {
            Logger.error(e + "\nThere was an issue with JSON!");
        }
        catch (Exception e) {
            Logger.error(e + "\nThere was an unknown issue!");
        }
        finally { }
    }
    public final void writeToInterfaceSafeLog(String key, String value) {
        JSONObject data = null;
        try {
            data = readSafeLog(TO_INTERFACE_JSON_PATH).put(key, value);
            writeSafeLog(data, TO_INTERFACE_JSON_PATH);
        }
        catch (Exception e) {
            Logger.fatalError(e + "\nThere was an unknown issue!");
        }
        finally { }
    }
    public final void writeToRobot(String key, String value) {
        JSONObject data = null;
        try {
            data = read(TO_ROBOT_JSON_PATH).put(key, value);
            write(data, TO_ROBOT_JSON_PATH);
        }
        catch (JSONException e) {
            Logger.error(e + "\nThere was an issue with JSON!");
        }
        catch (Exception e) {
            Logger.error(e + "\nThere was an unknown issue!");
        }
        finally { }
    }
    public final void writeToRobotSafeLog(String key, String value) {
        JSONObject data = null;
        try {
            data = readSafeLog(TO_ROBOT_JSON_PATH).put(key, value);
            writeSafeLog(data, TO_ROBOT_JSON_PATH);
        }
        catch (Exception e) {
            Logger.fatalError(e + "\nThere was an unknown issue!");
        }
        finally { }
    }
}

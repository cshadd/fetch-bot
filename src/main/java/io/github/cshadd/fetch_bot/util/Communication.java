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
    private final JSONObject read(String filePath)
    throws IOException, JSONException {
        File input = new File(filePath);
        JSONObject returnData = new JSONObject(FileUtils.readFileToString(input, "UTF-8"));
        return returnData;
    }
    private final JSONObject readLog(String filePath)
    throws IOException, JSONException {
        Logger.info("Communication - Reading JSON of " + filePath + ".");
        return read(filePath);
    }
    private final JSONObject readSafeLog(String filePath) {
        JSONObject returnData = null;
        try {
            returnData = read(filePath);
        }
        catch (IOException e) {
            Logger.fatalError(e, "There was an issue with IO!");
        }
        catch (JSONException e) {
            Logger.fatalError(e, "There was an issue with JSON!");
        }
        catch (Exception e) {
            Logger.fatalError(e, "There was an unknown issue!");
        }
        finally { }
        return returnData;
    }
    private final void write(JSONObject json, String filePath)
    throws IOException, JSONException {
        File input = new File(filePath);
        FileUtils.writeStringToFile(input, "" + json, "UTF-8");
    }
    private final void writeLog(JSONObject json, String filePath)
    throws IOException, JSONException {
        Logger.info("Communication - Writing JSON to " + filePath + ".");
        write(json, filePath);
    }
    private final void writeSafeLog(JSONObject json, String filePath) {
        try {
            write(json, filePath);
        }
        catch (IOException e) {
            Logger.fatalError(e, "There was an issue with IO!");
        }
        catch (JSONException e) {
            Logger.fatalError(e, "There was an issue with JSON!");
        }
        catch (Exception e) {
            Logger.fatalError(e, "There was an unknown issue!");
        }
        finally { }
    }

    // Public Final Methods
    public final String readToInterface(String key) {
        String returnData = null;
        try {
            returnData = readLog(TO_INTERFACE_JSON_PATH).getString(key);
        }
        catch (IOException e) {
            Logger.error(e, "There was an issue with IO!");
        }
        catch (JSONException e) {
            Logger.error(e, "There was an issue with JSON!");
        }
        catch (Exception e) {
            Logger.error(e, "There was an unknown issue!");
        }
        finally { }
        return returnData;
    }
    public final String readToInterfaceSafeLog(String key) {
        return readSafeLog(TO_INTERFACE_JSON_PATH).getString(key);
    }
    public final String readToRobot(String key) {
        String returnData = null;
        try {
            returnData = readLog(TO_ROBOT_JSON_PATH).getString(key);
        }
        catch (IOException e) {
            Logger.error(e, "There was an issue with IO!");
        }
        catch (JSONException e) {
            Logger.error(e, "There was an issue with JSON!");
        }
        catch (Exception e) {
            Logger.error(e, "There was an unknown issue!");
        }
        finally { }
        return returnData;
    }
    public final String readToRobotSafeLog(String key) {
        return readSafeLog(TO_ROBOT_JSON_PATH).getString(key);
    }
    public final void resetToInterface() {
        JSONObject data = new JSONObject();
        writeSafeLog(data, TO_INTERFACE_JSON_PATH);
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
        JSONObject data = new JSONObject();
        writeSafeLog(data, TO_ROBOT_JSON_PATH);
        data.put("Stop", "0");
        writeSafeLog(data, TO_ROBOT_JSON_PATH);
    }
    public final void writeToInterface(String key, String value) {
        JSONObject data = null;
        try {
            data = readLog(TO_INTERFACE_JSON_PATH).put(key, value);
            writeLog(data, TO_INTERFACE_JSON_PATH);
        }
        catch (IOException e) {
            Logger.error(e, "There was an issue with IO!");
        }
        catch (JSONException e) {
            Logger.error(e, "There was an issue with JSON!");
        }
        catch (Exception e) {
            Logger.error(e, "There was an unknown issue!");
        }
        finally { }
    }
    public final void writeToInterfaceSafeLog(String key, String value) {
        JSONObject data = readSafeLog(TO_INTERFACE_JSON_PATH).put(key, value);
        writeSafeLog(data, TO_INTERFACE_JSON_PATH);
    }
    public final void writeToRobot(String key, String value) {
        JSONObject data = null;
        try {
            data = readLog(TO_ROBOT_JSON_PATH).put(key, value);
            writeLog(data, TO_ROBOT_JSON_PATH);
        }
        catch (IOException e) {
            Logger.error(e, "There was an issue with IO!");
        }
        catch (JSONException e) {
            Logger.error(e, "There was an issue with JSON!");
        }
        catch (Exception e) {
            Logger.error(e, "There was an unknown issue!");
        }
        finally { }
    }
    public final void writeToRobotSafeLog(String key, String value) {
        JSONObject data = readSafeLog(TO_ROBOT_JSON_PATH).put(key, value);
        writeSafeLog(data, TO_ROBOT_JSON_PATH);
    }
}

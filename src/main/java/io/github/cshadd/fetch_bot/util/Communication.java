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

    // Private Instance/Property Fields
    private JSONObject toInterfaceData;
    private JSONObject toRobotData;

    // Public Constructors
    public Communication() {
        toInterfaceData = new JSONObject();
        toRobotData = new JSONObject();
    }

    // Private Final Methods
    private final JSONObject read(String filePath) {
        JSONObject returnData = null;
        try {
            File input = new File(filePath);
            returnData = new JSONObject(FileUtils.readFileToString(input, "UTF-8"));
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
    private final void write(JSONObject json, String filePath) {
        try {
            File input = new File(filePath);
            FileUtils.writeStringToFile(input, "" + json, "UTF-8");
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
    public final void clear() {
        toInterfaceData = new JSONObject();
        toRobotData = new JSONObject();
    }
    public final String getInterfaceValue(String key) {
        String returnData = null;
        try {
            returnData = toInterfaceData.getString(key);
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
    public final String getRobotValue(String key) {
        String returnData = null;
        try {
            returnData = toRobotData.getString(key);
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
    public final void pullInterface() {
        toRobotData = read(TO_ROBOT_JSON_PATH);
    }
    public final void pullRobot() {
        toRobotData = read(TO_ROBOT_JSON_PATH);
    }
    public final void pushInterface() {
        write(toInterfaceData, TO_INTERFACE_JSON_PATH);
    }
    public final void pushRobot() {
        write(toRobotData, TO_ROBOT_JSON_PATH);
    }
    public final void reset() {
        clear();
        setInterfaceValue("emotion", "Boot");
        setInterfaceValue("mode", "Off");
        setInterfaceValue("rot", "0");
        setInterfaceValue("sensorback", "0");
        setInterfaceValue("sensorfront", "0");
        setInterfaceValue("sensorleft", "0");
        setInterfaceValue("sensorright", "0");
        setInterfaceValue("verbose", "...");
        setInterfaceValue("x", "0");
        setInterfaceValue("xmax", "0");
        setInterfaceValue("y", "0");
        setInterfaceValue("ymax", "0");

        setRobotValue("auto", "0");
        setRobotValue("kill", "0");
    }
    public final void setInterfaceValue(String key, String value) {
        try {
            toInterfaceData.put(key, value);
        }
        catch (JSONException e) {
            Logger.error(e, "There was an issue with JSON!");
        }
        catch (Exception e) {
            Logger.error(e, "There was an unknown issue!");
        }
        finally { }
    }
    public final void setRobotValue(String key, String value) {
        try {
            toRobotData.put(key, value);
        }
        catch (JSONException e) {
            Logger.error(e, "There was an issue with JSON!");
        }
        catch (Exception e) {
            Logger.error(e, "There was an unknown issue!");
        }
        finally { }
    }







    /*public final void clearToInterface() {
        final JSONObject data = new JSONObject();
    }
    public final void clearToRobot() {
        final JSONObject data = new JSONObject();
        writeSafeLog(data, TO_INTERFACE_JSON_PATH);
        writeSafeLog(data, TO_ROBOT_JSON_PATH);
    }
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
        final JSONObject data = new JSONObject();
        writeSafeLog(data, TO_ROBOT_JSON_PATH);
        data.put("Stop", "0");
        writeSafeLog(data, TO_ROBOT_JSON_PATH);
    }
    public final void writeToInterface(String key, String value) {
        final JSONObject data = null;
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
    }*/
}

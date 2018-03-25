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
        setInterfaceValue("sensor-front", "0");
        setInterfaceValue("sensor-left", "0");
        setInterfaceValue("sensor-right", "0");
        setInterfaceValue("verbose", "...");
        setInterfaceValue("x", "0");
        setInterfaceValue("x-max", "0");
        setInterfaceValue("y", "0");
        setInterfaceValue("y-max", "0");

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
}

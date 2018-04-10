package io.github.cshadd.fetch_bot.util;
import io.github.cshadd.fetch_bot.FetchBot;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;

// Main
public class InterfaceCommunication
implements FetchBot {
    // Private Constant Instance/Property Fields
    private static final  String TO_INTERFACE_JSON_PATH = "/var/www/html/FetchBot/comms/toInterface.json";
    private static final  String TO_ROBOT_JSON_PATH = "/var/www/html/FetchBot/comms/toRobot.json";

    // Private Instance/Property Fields
    private JSONObject toInterfaceData;
    private JSONObject toRobotData;

    // Public Constructors
    public InterfaceCommunication() {
        toInterfaceData = new JSONObject();
        toRobotData = new JSONObject();
    }

    // Private Methods
    private JSONObject read(String filePath) {
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
    private void write(JSONObject json, String filePath) {
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

    // Public Methods
    public void clear() {
        toInterfaceData = new JSONObject();
        toRobotData = new JSONObject();
    }
    public String getInterfaceValue(String key) {
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
    public String getRobotValue(String key) {
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
    public void pullInterface() {
        toInterfaceData = read(TO_INTERFACE_JSON_PATH);
    }
    public void pullRobot() {
        toRobotData = read(TO_ROBOT_JSON_PATH);
    }
    public void pushInterface() {
        write(toInterfaceData, TO_INTERFACE_JSON_PATH);
    }
    public void pushRobot() {
        write(toRobotData, TO_ROBOT_JSON_PATH);
    }
    public void reset() {
        clear();
        setInterfaceValue("emotion", "Idle");
        setInterfaceValue("mode", "Idle");
        setInterfaceValue("rot", "0");
        setInterfaceValue("sensor-front", "0");
        setInterfaceValue("sensor-left", "0");
        setInterfaceValue("sensor-right", "0");
        setInterfaceValue("verbose", "...");
        setInterfaceValue("x", "0");
        setInterfaceValue("x-max", "0");
        setInterfaceValue("y", "0");
        setInterfaceValue("y-max", "0");

        setRobotValue("mode", "Idle");
        setRobotValue("move", "Stop");
    }
    public void setInterfaceValue(String key, String value) {
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
    public void setRobotValue(String key, String value) {
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

package io.github.cshadd.fetch_bot;
import java.io.*;
import org.apache.commons.io.*;
import org.json.*;

// Main
public class Communication
implements FetchBot {
    // Private Constant Instance/Property Fields
    private static final String TO_INTERFACE_JSON_PATH = "/var/www/html/FetchBot/comms/toInterface.json";
    private static final String TO_ROBOT_JSON_PATH = "/var/www/html/FetchBot/comms/toRobot.json";

    // Private Instance/Property Fields
    private JSONObject toInterface;
    private JSONObject toRobot;

    // Public Constructors
    public Communication() {
        toInterface = null;
        toRobot = null;
    }

    // Private Methods
    /*private JSONObject read(String filePath) {
        File input = null;
        JSONObject returnData = null;
        Logger.info("Reading JSON of " + filePath + ".");
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
    }*/
    private JSONObject readSafeLog(String filePath) {
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
    /*private void write(JSONObject json, String filePath) {
        File input = null;
        Logger.info("Writing JSON to " + filePath + ".");
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
    }*/
    private void writeSafeLog(JSONObject json, String filePath) {
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

    // Public Methods
    public void clearToInterface() {
        File input = null;
        try {
            input = new File(TO_INTERFACE_JSON_PATH);
            FileUtils.deleteQuietly(new File(TO_INTERFACE_JSON_PATH));
        }
        catch (Exception e) {
            Logger.fatalError(e + "\nThere was an unknown issue!");
        }
        finally { }
    }
    public void clearToRobot() {
        File input = null;
        try {
            input = new File(TO_ROBOT_JSON_PATH);
            FileUtils.deleteQuietly(new File(TO_ROBOT_JSON_PATH));
        }
        catch (Exception e) {
            Logger.fatalError(e + "\nThere was an unknown issue!");
        }
        finally { }
    }
    /*public String readToInterface(String key) {
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
    }*/
    /*public String readToInterfaceSafeLog(String key) {
        String returnData = null;
        try {
            returnData = readSafeLog(TO_INTERFACE_JSON_PATH).getString(key);
        }
        catch (Exception e) {
            Logger.fatalError(e + "\nThere was an unknown issue!");
        }
        finally { }
        return returnData;
    }*/
    /*public String readToRobot(String key) {
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
    }*/
    /*public String readToRobotSafeLog(String key) {
        String returnData = null;
        try {
            returnData = readSafeLog(TO_ROBOT_JSON_PATH).getString(key);
        }
        catch (Exception e) {
            Logger.fatalError(e + "\nThere was an unknown issue!");
        }
        finally { }
        return returnData;
    }*/
    public void resetToInterface() {
        try {
            FileUtils.writeStringToFile(new File(TO_INTERFACE_JSON_PATH), "{}", "UTF-8", true);
        }
        catch (Exception e) {
            Logger.fatalError(e + "\nThere was an unknown issue!");
        }
        finally { }
        toInterface = new JSONObject();
        toInterface.put("emotion", "Boot")
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
        writeSafeLog(toInterface, TO_INTERFACE_JSON_PATH);
    }
    public void resetToRobot() {
        try {
            FileUtils.writeStringToFile(new File(TO_ROBOT_JSON_PATH), "{}", "UTF-8", true);
        }
        catch (Exception e) {
            Logger.fatalError(e + "\nThere was an unknown issue!");
        }
        finally { }
        toRobot = new JSONObject();
        writeSafeLog(toRobot, TO_ROBOT_JSON_PATH);
    }
    /*public void writeToInterface(String key, String value) {
        try {
            JSONObject data = readToInterface().put(key, value);
            write(data, TO_INTERFACE_JSON_PATH);
        }
        catch (JSONException e) {
            Logger.error(e + "\nThere was an issue with JSON!");
        }
        catch (Exception e) {
            Logger.error(e + "\nThere was an unknown issue!");
        }
        finally { }
    }*/
    public void writeToInterfaceSafeLog(String key, String value) {
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
    /*public void writeToRobot(String key, String value) {
        JSONObject data = null;
        try {
            data = readToRobot().put(key, value);
            write(data, TO_ROBOT_JSON_PATH);
        }
        catch (JSONException e) {
            Logger.error(e + "\nThere was an issue with JSON!");
        }
        catch (Exception e) {
            Logger.error(e + "\nThere was an unknown issue!");
        }
        finally { }
    }*/
    /*public void writeToRobotSafeLog(String key, String value) {
        JSONObject data = null;
        try {
            data = readSafeLog(TO_ROBOT_JSON_PATH).put(key, value);
            writeSafeLog(data, TO_ROBOT_JSON_PATH);
        }
        catch (Exception e) {
            Logger.fatalError(e + "\nThere was an unknown issue!");
        }
        finally { }
    }*/
}

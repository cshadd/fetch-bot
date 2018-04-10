package io.github.cshadd.fetch_bot.io;
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
    private JSONObject read(String filePath)
    throws InterfaceCommunicationException {
        JSONObject returnData = null;
        try {
            File input = new File(filePath);
            returnData = new JSONObject(FileUtils.readFileToString(input, "UTF-8"));
        }
        catch (IOException e) {
            throw new InterfaceCommunicationException("There was an issue with IO!", e);
        }
        catch (JSONException e) {
            throw new InterfaceCommunicationException("There was an issue with JSON!", e);
        }
        catch (Exception e) {
            throw new InterfaceCommunicationException("There was an unknown issue!", e);
        }
        finally { }
        return returnData;
    }
    private void write(JSONObject json, String filePath)
    throws InterfaceCommunicationException {
        try {
            File input = new File(filePath);
            FileUtils.writeStringToFile(input, "" + json, "UTF-8");
        }
        catch (IOException e) {
            throw new InterfaceCommunicationException("There was an issue with IO!", e);
        }
        catch (JSONException e) {
            throw new InterfaceCommunicationException("There was an issue with JSON!", e);
        }
        catch (Exception e) {
            throw new InterfaceCommunicationException("There was an unknown issue!", e);
        }
        finally { }
    }

    // Public Methods
    public void clear()
    throws InterfaceCommunicationException {
        toInterfaceData = new JSONObject();
        toRobotData = new JSONObject();
    }
    public String getInterfaceValue(String key)
    throws InterfaceCommunicationException {
        String returnData = null;
        try {
            returnData = toInterfaceData.getString(key);
        }
        catch (JSONException e) {
            throw new InterfaceCommunicationException("There was an issue with JSON!", e);
        }
        catch (Exception e) {
            throw new InterfaceCommunicationException("There was an unknown issue!", e);
        }
        finally { }
        return returnData;
    }
    public String getRobotValue(String key)
    throws InterfaceCommunicationException {
        String returnData = null;
        try {
            returnData = toRobotData.getString(key);
        }
        catch (JSONException e) {
            throw new InterfaceCommunicationException("There was an issue with JSON!", e);
        }
        catch (Exception e) {
            throw new InterfaceCommunicationException("There was an unknown issue!", e);
        }
        finally { }
        return returnData;
    }
    public void pullInterface()
    throws InterfaceCommunicationException {
        toInterfaceData = read(TO_INTERFACE_JSON_PATH);
    }
    public void pullRobot()
    throws InterfaceCommunicationException {
        toRobotData = read(TO_ROBOT_JSON_PATH);
    }
    public void pushInterface()
    throws InterfaceCommunicationException {
        write(toInterfaceData, TO_INTERFACE_JSON_PATH);
    }
    public void pushRobot()
    throws InterfaceCommunicationException {
        write(toRobotData, TO_ROBOT_JSON_PATH);
    }
    public void reset()
    throws InterfaceCommunicationException {
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
    public void setInterfaceValue(String key, String value)
    throws InterfaceCommunicationException {
        try {
            toInterfaceData.put(key, value);
        }
        catch (JSONException e) {
            throw new InterfaceCommunicationException("There was an issue with JSON!", e);
        }
        catch (Exception e) {
            throw new InterfaceCommunicationException("There was an unknown issue!", e);
        }
        finally { }
    }
    public void setRobotValue(String key, String value)
    throws InterfaceCommunicationException {
        try {
            toRobotData.put(key, value);
        }
        catch (JSONException e) {
            throw new InterfaceCommunicationException("There was an issue with JSON!", e);
        }
        catch (Exception e) {
            throw new InterfaceCommunicationException("There was an unknown issue!", e);
        }
        finally { }
    }
}

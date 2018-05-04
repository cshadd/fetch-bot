package io.github.cshadd.fetch_bot.io;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;

// Main
public class WebInterfaceCommunicationImpl
implements WebInterfaceCommunication {
    // Private Constant Instance/Property Fields
    private static final  String TO_ROBOT_JSON_PATH = "/var/www/html/FetchBot/comms/toRobot.json"; // Change if needed
    private static final  String TO_WEB_INTERFACE_JSON_PATH = "/var/www/html/FetchBot/comms/toInterface.json"; // Change if needed
    
    // Private Instance/Property Fields
    private JSONObject toRobotData;
    private JSONObject toWebInterfaceData;

    // Public Constructors
    public WebInterfaceCommunicationImpl() {
        toRobotData = new JSONObject();
        toWebInterfaceData = new JSONObject();
    }

    // Private Methods
    private JSONObject read(String filePath)
    throws CommunicationException {
        JSONObject returnData = null;
        try {
            File input = new File(filePath);
            returnData = new JSONObject(FileUtils.readFileToString(input, "UTF-8"));
        }
        catch (IOException e) {
            throw new CommunicationException("There was an issue with IO!", e);
        }
        catch (JSONException e) {
            throw new CommunicationException("There was an issue with JSON!", e);
        }
        catch (Exception e) {
            throw new CommunicationException("There was an unknown issue!", e);
        }
        finally { }
        return returnData;
    }
    private void write(JSONObject json, String filePath)
    throws CommunicationException {
        try {
            File input = new File(filePath);
            FileUtils.writeStringToFile(input, "" + json, "UTF-8");
        }
        catch (IOException e) {
            throw new CommunicationException("There was an issue with IO!", e);
        }
        catch (JSONException e) {
            throw new CommunicationException("There was an issue with JSON!", e);
        }
        catch (Exception e) {
            throw new CommunicationException("There was an unknown issue!", e);
        }
        finally { }
    }

    // Public Methods (Overrided)
    @Override
    public void clear()
    throws CommunicationException {
        toWebInterfaceData = new JSONObject();
        toRobotData = new JSONObject();
    }
    @Override
    public String getRobotValue(String key)
    throws CommunicationException {
        String returnData = null;
        try {
            returnData = toRobotData.getString(key);
        }
        catch (JSONException e) {
            throw new CommunicationException("There was an issue with JSON!", e);
        }
        catch (Exception e) {
            throw new CommunicationException("There was an unknown issue!", e);
        }
        finally { }
        return returnData;
    }
    @Override
    public String getSourceValue(String key)
    throws CommunicationException {
        String returnData = null;
        try {
            returnData = toWebInterfaceData.getString(key);
        }
        catch (JSONException e) {
            throw new CommunicationException("There was an issue with JSON!", e);
        }
        catch (Exception e) {
            throw new CommunicationException("There was an unknown issue!", e);
        }
        finally { }
        return returnData;
    }
    @Override
    public void pullRobot()
    throws CommunicationException {
        toRobotData = read(TO_ROBOT_JSON_PATH);
    }
    @Override
    public void pullSource()
    throws CommunicationException {
        toWebInterfaceData = read(TO_WEB_INTERFACE_JSON_PATH);
    }
    @Override
    public void pushRobot()
    throws CommunicationException {
        write(toRobotData, TO_ROBOT_JSON_PATH);
    }
    @Override
    public void pushSource()
    throws CommunicationException {
        write(toWebInterfaceData, TO_WEB_INTERFACE_JSON_PATH);
    }
    @Override
    public void reset()
    throws CommunicationException {
        clear();
        setRobotValue("mode", "Idle");
        setRobotValue("move", "Stop");
        
        setSourceValue("emotion", "Idle");
        setSourceValue("mode", "Idle");
        setSourceValue("rot", "0");
        setSourceValue("ultrasonic", "0");
        setSourceValue("verbose", "...");
    }
    @Override
    public void setRobotValue(String key, String value)
    throws CommunicationException {
        try {
            toRobotData.put(key, value);
        }
        catch (JSONException e) {
            throw new CommunicationException("There was an issue with JSON!", e);
        }
        catch (Exception e) {
            throw new CommunicationException("There was an unknown issue!", e);
        }
        finally { }
    }
    @Override
    public void setSourceValue(String key, String value)
    throws CommunicationException {
        try {
            toWebInterfaceData.put(key, value);
        }
        catch (JSONException e) {
            throw new CommunicationException("There was an issue with JSON!", e);
        }
        catch (Exception e) {
            throw new CommunicationException("There was an unknown issue!", e);
        }
        finally { }
    }
}

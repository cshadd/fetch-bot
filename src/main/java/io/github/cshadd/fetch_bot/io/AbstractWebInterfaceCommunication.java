package io.github.cshadd.fetch_bot.io;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;

// Main
public abstract class AbstractWebInterfaceCommunication
extends AbstractCommunication
implements WebInterfaceCommunication {
    // Public Constant Instance/Property Fields
    public static final String TO_WEB_COMM_PATH = "/var/www/html/FetchBot/comms/"; // Change if needed
    public static final String TO_ROBOT_JSON_PATH = TO_WEB_COMM_PATH + "toRobot.json";
    public static final String TO_WEB_INTERFACE_JSON_PATH = TO_WEB_COMM_PATH + "toInterface.json";

    // Protected Constructors
    protected AbstractWebInterfaceCommunication() {
        super();
    }

    // Protected Methods
    protected JSONObject read(String filePath)
    throws CommunicationException {
        JSONObject returnData = null;
        try {
            File input = new File(filePath);
            returnData = new JSONObject(FileUtils.readFileToString(input, "UTF-8"));
        }
        catch (IOException e) {
            throw new WebInterfaceCommunicationException("There was an issue with IO!", e);
        }
        catch (JSONException e) {
            throw new WebInterfaceCommunicationException("There was an issue with JSON!", e);
        }
        catch (Exception e) {
            throw new WebInterfaceCommunicationException("There was an unknown issue!", e);
        }
        finally { }
        return returnData;
    }
    protected void write(JSONObject json, String filePath)
    throws CommunicationException {
        try {
            File input = new File(filePath);
            FileUtils.writeStringToFile(input, "" + json, "UTF-8");
        }
        catch (IOException e) {
            throw new WebInterfaceCommunicationException("There was an issue with IO!", e);
        }
        catch (JSONException e) {
            throw new WebInterfaceCommunicationException("There was an issue with JSON!", e);
        }
        catch (Exception e) {
            throw new WebInterfaceCommunicationException("There was an unknown issue!", e);
        }
        finally { }
    }
}
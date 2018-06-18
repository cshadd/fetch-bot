package io.github.cshadd.fetch_bot.io.json;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;

// Main
public abstract class AbstractWebInterfaceCommunication
extends AbstractJSONCommunication
implements WebInterfaceCommunication {
    // Private Constant Instance/Property Fields
    private static final String DEFAULT_HOST_PATH = "/var/www/html/";
    private static final String DEFAULT_WEB_PATH = DEFAULT_HOST_PATH + "FetchBot/";
    private static final String DEFAULT_WEB_COM_PATH = DEFAULT_WEB_PATH + "comms/";
    
    // Protected Constant Instance/Property Fields
    protected static final String TOROBOT_JSON_FILE = "toRobot.json";
    protected static final String TOWEBINTERFACE_JSON_FILE = "toInterface.json";
    
    // Protected Final Instance/Property Fields
    protected final String commPath;
    
    // Protected Constructors
    protected AbstractWebInterfaceCommunication() {
        this(DEFAULT_WEB_COM_PATH);
    }
    protected AbstractWebInterfaceCommunication(String commPath) {
        super();
        this.commPath = DEFAULT_WEB_COM_PATH;
    }
    
    // Protected Methods
    protected JSONObject read(String filePath)
    throws WebInterfaceCommunicationException {
        JSONObject returnData = null;
        try {
            final File input = new File(this.commPath + filePath);
            returnData = new JSONObject(FileUtils.readFileToString(input, "UTF-8"));
        }
        catch (IOException e) {
            throw new WebInterfaceCommunicationException("Could not read " + filePath + ".", e);
        }
        catch (JSONException e) {
            throw new WebInterfaceCommunicationException("Could not parse JSON in " + filePath + ".", e);
        }
        catch (Exception e) {
            throw new WebInterfaceCommunicationException("Unknown issue.", e);
        }
        finally { /* */ }
        return returnData;
    }
    protected void write(JSONObject json, String filePath)
    throws WebInterfaceCommunicationException {
        try {
            final File input = new File(this.commPath + filePath);
            FileUtils.writeStringToFile(input, "" + json, "UTF-8");
        }
        catch (IOException e) {
            throw new WebInterfaceCommunicationException("Could not write to " + filePath + ".", e);
        }
        catch (JSONException e) {
            throw new WebInterfaceCommunicationException("Could not write JSON to " + filePath + ".", e);
        }
        catch (Exception e) {
            throw new WebInterfaceCommunicationException("Unknown issue.", e);
        }
        finally { /* */ }
    }
}
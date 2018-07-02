package io.github.cshadd.fetch_bot.io.json;

import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.IOException;

// Main

/**
 * The Class AbstractWebInterfaceCommunication. Defines what a Web Interface
 * Communication is. A Web Interface Communication is basically a helper that
 * directs data between a web interface and Fetch Bot.
 * 
 * @author Christian Shadd
 * @author Maria Verna Aquino
 * @author Thanh Vu
 * @author Joseph Damian
 * @author Giovanni Orozco
 * @since 1.0.0
 */
public abstract class AbstractWebInterfaceCommunication extends
                AbstractJSONCommunication implements WebInterfaceCommunication {
    // Protected Constant Instance/Property Fields
    
    protected static final String TOROBOT_JSON_FILE        = "toRobot.json";
    protected static final String TOWEBINTERFACE_JSON_FILE = "toInterface.json";
    
    // Protected Final Instance/Property Fields
    
    protected final String commPath;
    
    // Private Constructors
    
    private AbstractWebInterfaceCommunication() {
        this(null);
    }
    
    // Protected Constructors
    
    protected AbstractWebInterfaceCommunication(String interfacePath) {
        super();
        this.commPath = interfacePath + "/comms/";
    }
    
    // Protected Methods
    
    protected JSONObject read(String filePath)
                    throws WebInterfaceCommunicationException {
        try {
            final File input = new File(this.commPath + filePath);
            final String data = FileUtils.readFileToString(input, "UTF-8");
            if (data != null) {
                if (data.charAt(0) == '{' && !data.equals("{ }")) {
                    return new JSONObject(data);
                }
            }
        } catch (IOException e) {
            throw new WebInterfaceCommunicationException("Could not read "
                            + filePath + ".", e);
        } catch (JSONException e) {
            throw new WebInterfaceCommunicationException(
                            "Could not parse JSON in " + filePath + ".", e);
        } catch (Exception e) {
            throw new WebInterfaceCommunicationException("Unknown issue.", e);
        } finally {
            /* */ }
        return new JSONObject();
    }
    
    protected void write(JSONObject json, String filePath)
                    throws WebInterfaceCommunicationException {
        try {
            final File input = new File(this.commPath + filePath);
            FileUtils.writeStringToFile(input, "" + json, "UTF-8");
        } catch (IOException e) {
            throw new WebInterfaceCommunicationException("Could not write to "
                            + filePath + ".", e);
        } catch (JSONException e) {
            throw new WebInterfaceCommunicationException(
                            "Could not write JSON to " + filePath + ".", e);
        } catch (Exception e) {
            throw new WebInterfaceCommunicationException("Unknown issue.", e);
        } finally {
            /* */ }
    }
}
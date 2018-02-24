package io.github.cshadd.fetch_bot;

import java.io.InputStream;
import java.io.IOException;
import java.net.URL;
import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

// Main
public class Communication
implements FetchBot {
    // Public Static Final Methods
    // public static final void send() {

    // }
    public static final JSONObject recieve() {
        InputStream in;
        JSONObject returnData = null;
        Logger.info("Attempting to get recieving JSON.");
        try {
            in = new URL("http://localhost/FetchBot/comms/toRobot.json").openStream();
            returnData = new JSONObject(IOUtils.toString(in, "UTF-8"));
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
        finally {
            Logger.info("Recieving JSON: " + returnData);
        }
        return returnData;
    }
}

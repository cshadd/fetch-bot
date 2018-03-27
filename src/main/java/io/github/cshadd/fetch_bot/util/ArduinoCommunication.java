package io.github.cshadd.fetch_bot.util;
import com.pi4j.io.serial.Serial;
import com.pi4j.io.serial.SerialFactory;
import com.pi4j.io.serial.SerialPortException;
import io.github.cshadd.fetch_bot.FetchBot;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;

// Main
public final class ArduinoCommunication
implements FetchBot {
    // Private Instance/Property Fields
    private JSONObject toArduinoData;
    private Logger log;

    // Public Constructors
    public ArduinoCommunication() {
        toArduinoData = new JSONObject();
        log = Logger.getInstance();
    }

    // Private Final Methods
    private final JSONObject read() {
        JSONObject returnData = null;
        try {
            // returnData = new JSONObject(...;
        }
        /*catch (IOException e) {
            log.error(e, "There was an issue with IO!");
        }*/
        catch (JSONException e) {
            log.error(e, "There was an issue with JSON!");
        }
        catch (Exception e) {
            log.error(e, "There was an unknown issue!");
        }
        finally { }
        return returnData;
    }
    private final void write(JSONObject json) {
        try {
            // json
        }
        /*catch (IOException e) {
            log.error(e, "There was an issue with IO!");
        }*/
        catch (JSONException e) {
            log.error(e, "There was an issue with JSON!");
        }
        catch (Exception e) {
            log.error(e, "There was an unknown issue!");
        }
        finally { }
    }

    // Public Final Methods
    public final void clear() {
        toArduinoData = new JSONObject();
    }
    public final String getArduinoValue(String key) {
        String returnData = null;
        try {
            returnData = toArduinoData.getString(key);
        }
        catch (JSONException e) {
            log.error(e, "There was an issue with JSON!");
        }
        catch (Exception e) {
            log.error(e, "There was an unknown issue!");
        }
        finally { }
        return returnData;
    }
    public final void pullArduino() {
        toArduinoData = read();
    }
    public final void pushArduino() {
        write(toArduinoData);
    }
    public final void reset() {
        clear();
        // ...
    }
    public final void setArduinoValue(String key, String value) {
        try {
            toArduinoData.put(key, value);
        }
        catch (JSONException e) {
            log.error(e, "There was an issue with JSON!");
        }
        catch (Exception e) {
            log.error(e, "There was an unknown issue!");
        }
        finally { }
    }
}

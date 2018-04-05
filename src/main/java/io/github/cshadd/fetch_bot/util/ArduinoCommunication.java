package io.github.cshadd.fetch_bot.util;
import com.pi4j.io.serial.Baud;
import com.pi4j.io.serial.DataBits;
import com.pi4j.io.serial.FlowControl;
import com.pi4j.io.serial.Parity;
import com.pi4j.io.serial.Serial;
import com.pi4j.io.serial.SerialConfig;
import com.pi4j.io.serial.SerialFactory;
import com.pi4j.io.serial.SerialDataEventListener;
import com.pi4j.io.serial.SerialDataEvent;
import com.pi4j.io.serial.StopBits;
import io.github.cshadd.fetch_bot.FetchBot;
import java.io.IOException;
import org.json.JSONException;
import org.json.JSONObject;

// Main
public class ArduinoCommunication
implements FetchBot {
    // Private Constant Instance/Property Fields
    private static final String SERIAL_PORT = "/dev/ttyACM0";

    // Private Instance/Property Fields
    private String buffer;
    private Serial serial;
    private SerialConfig serialConfig;
    private SerialDataEventListener serialListener;
    private JSONObject toArduinoData;
    private JSONObject toRobotData;

    // Public Constructors
    public ArduinoCommunication() {
        buffer = "{ }";
        serial = SerialFactory.createInstance();
        serialConfig = new SerialConfig();
        serialConfig.device(SERIAL_PORT);
        serialConfig.baud(Baud._9600);
        serialConfig.dataBits(DataBits._8);
        serialConfig.parity(Parity.NONE);
        serialConfig.stopBits(StopBits._1);
        serialConfig.flowControl(FlowControl.NONE);
        toArduinoData = new JSONObject();
        toRobotData = new JSONObject();
    }

    // Private Methods
    private void close() {
        try {
            if (serial.isOpen()) {
                serial.discardInput();
                serial.removeListener(serialListener);
                serial.close();
                SerialFactory.shutdown();
            }
        }
        catch (IOException e) {
            Logger.error(e, "There was an issue with IO!");
        }
        catch (Exception e) {
            Logger.fatalError(e, "There was an unknown issue!");
        }
        finally { }
    }
    private void open() {
        try {
            if (!serial.isOpen()) {
                serial.open(serialConfig);
                Logger.info("ArduinoCommunication - Opened serial on " + SERIAL_PORT + ".");
                serialListener = new SerialDataEventListener() {
                    @Override
                    public void dataReceived(SerialDataEvent event) {
                        try {
                            buffer = event.getAsciiString();
                        }
                        catch (IOException e) {
                            Logger.error(e, "There was an issue with IO!");
                        }
                        catch (Exception e) {
                            Logger.error(e, "There was an unknown issue!");
                        }
                        finally { }
                    }
                };
                serial.addListener(serialListener);
            }
        }
        catch (IOException e) {
            Logger.error(e, "There was an issue with IO!");
        }
        catch (Exception e) {
            Logger.error(e, "There was an unknown issue!");
        }
        finally { }
    }
    private JSONObject read() {
        JSONObject returnData = new JSONObject();
        try {
            returnData.put("f", -1);
            returnData.put("l", -1);
            returnData.put("r", -1);
            if (buffer.charAt(0) == '{' && !buffer.equals("{ }")) {
                returnData = new JSONObject(buffer);
            }
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
    private void write() {
        try {
            if (serial.isOpen()) {
                serial.write(getArduinoValue("a"));
                serial.flush();
            }
        }
        catch (IOException e) {
            Logger.error(e, "There was an issue with IO!");
        }
        catch (JSONException e) {
            Logger.error(e, "There was an issue with JSON!");
        }
        catch (Exception e) {
            Logger.error(e, "There was an unknown issue!");
        }
        finally { }
    }

    // Public Methods
    public void clear() {
        close();
	buffer = "{ }";
        toArduinoData = new JSONObject();
        toRobotData = new JSONObject();
    }
    public String getArduinoValue(String key) {
        String returnData = null;
        try {
            returnData = toArduinoData.getString(key);
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
    public float getRobotValue(String key) {
        float returnData = -1;
        try {
            returnData = toRobotData.getFloat(key);
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
    public void pullRobot() {
        toRobotData = read();
    }
    public void pushArduino() {
        write();
    }
    public void reset() {
        clear();
        open();
        setArduinoValue("Stop");
        setRobotValue("f", -1);
        setRobotValue("l", -1);
        setRobotValue("r", -1);
    }
    public void setArduinoValue(String value) {
        try {
            toArduinoData.put("a", value);
        }
        catch (JSONException e) {
            Logger.error(e, "There was an issue with JSON!");
        }
        catch (Exception e) {
            Logger.error(e, "There was an unknown issue!");
        }
        finally { }
    }
    public void setRobotValue(String key, float value) {
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
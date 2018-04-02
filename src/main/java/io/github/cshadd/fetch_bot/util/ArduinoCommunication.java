package io.github.cshadd.fetch_bot.util;
import java.io.IOException;
import com.pi4j.io.serial.Baud;
import com.pi4j.io.serial.DataBits;
import com.pi4j.io.serial.FlowControl;
import com.pi4j.io.serial.Parity;
import com.pi4j.io.serial.Serial;
import com.pi4j.io.serial.SerialDataEvent;
import com.pi4j.io.serial.SerialDataEventListener;
import com.pi4j.io.serial.SerialFactory;
import com.pi4j.io.serial.StopBits;
import io.github.cshadd.fetch_bot.FetchBot;
import org.json.JSONException;
import org.json.JSONObject;

// Main
public final class ArduinoCommunication
implements FetchBot {
    // Private Constant Instance/Property Fields
    private String SERIAL_PORT = "/dev/pts2";//"/dev/ttyACM0";

    // Private Instance/Property Fields
    private String toArduinoData;
    private JSONObject toRobotData;
    private Serial serial;
    private String serialData;

    // Public Constructors
    public ArduinoCommunication() {
        toArduinoData = "0";
        toRobotData = new JSONObject();
        serialData = "";
        serial = SerialFactory.createInstance();
        openSerialPort();
    }

    // Private Final Methods
    private final boolean openSerialPort() {
        boolean returnData = false;
        if (serial != null) {
            if (serial.isOpen()) {
                returnData = true;
            }
            else {
                try {
                    serial.open(SERIAL_PORT, Baud._9600, DataBits._8, Parity.NONE, StopBits._1, FlowControl.HARDWARE);
                }
                catch (IOException e) {
                    Logger.error(e, "There was an issue with IO!");
                }
                catch (Exception e) {
                    Logger.error(e, "There was an unknown issue!");
                }
                finally { }
            }
        }
        return returnData;
    }
    private final JSONObject read() {
        JSONObject returnData = null;
        try {
            if (openSerialPort()) {
                returnData = new JSONObject(serialData);
            }
            else {
                Logger.warn("Was unable to process serial port, had to first open.");
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
    private final void write(String value) {
        try {
            if (openSerialPort()) {
                serial.write(value);
            }
            else {
                Logger.warn("Was unable to process serial port, had to first open.");
            }
        }
        catch (IOException e) {
            Logger.error(e, "There was an issue with IO!");
        }
        catch (Exception e) {
            Logger.error(e, "There was an unknown issue!");
        }
    }

    // Public Final Methods
    public final void clear() {
        toArduinoData = "0";
        toRobotData = new JSONObject();
        if (serial != null) {
            if (serial.isOpen()) {
                try {
                    serial.close();
                }
                catch (IOException e) {
                    Logger.error(e, "There was an issue with IO!");
                }
                catch (Exception e) {
                    Logger.error(e, "There was an unknown issue!");
                }
                finally { }
            }
        }
    }
    public final String getArduinoValue() {
        return toArduinoData;
    }
    public final String getRobotValue(String key) {
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
        returnData = "0";
        return returnData;
    }
    public final void pullRobot() {
        toRobotData = read();
    }
    public final void pushArduino() {
        write(toArduinoData);
    }
    public final void reset() {
        clear();
        setRobotValue("sf", "0");
        setRobotValue("sl", "0");
        setRobotValue("sr", "0");
    }
    public final void setArduinoValue(String value) {
        toArduinoData = value;
    }
    public final void setRobotValue(String key, String value) {
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

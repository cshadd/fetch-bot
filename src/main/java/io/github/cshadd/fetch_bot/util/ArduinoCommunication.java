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
public final class ArduinoCommunication
implements FetchBot {
    // Private Constant Instance/Property Fields
    private String SERIAL_PORT = "/dev/ttyACM0";

    // Private Instance/Property Fields
    private String buffer;
    private Serial serial;
    private SerialConfig serialConfig;
    private JSONObject toArduinoData;
    private JSONObject toRobotData;

    // Public Constructors
    public ArduinoCommunication() {
        buffer = "";
        serial = SerialFactory.createInstance();
        serialConfig = new SerialConfig();
        serialConfig.device(SERIAL_PORT)
            .baud(Baud._38400)
            .dataBits(DataBits._8)
            .parity(Parity.NONE)
            .stopBits(StopBits._1)
            .flowControl(FlowControl.NONE);

        serial.addListener(new SerialDataEventListener() {
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
        });
        toArduinoData = new JSONObject();
        toRobotData = new JSONObject();
    }

    // Private Final Methods
    private final void close() {
        try {
            if (serial.isOpen()) {
                serial.close();
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
    private final void open() {
        try {
            if (!serial.isOpen()) {
                serial.open(serialConfig);
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
    private final JSONObject read() {
        JSONObject returnData = null;
        try {
            if (buffer != "") {
                returnData = new JSONObject(buffer);
            }
            buffer = "";
        }
        catch (JSONException e) {
            Logger.fatalError(e, "There was an issue with JSON!");
        }
        catch (Exception e) {
            Logger.fatalError(e, "There was an unknown issue!");
        }
        finally { }
        return returnData;
    }
    private final void write() {
        try {
            if (serial.isOpen()) {
                serial.write((byte)getArduinoValue("m"));
            }
        }
        catch (IOException e) {
            Logger.error(e, "There was an issue with IO!");
        }
        catch (JSONException e) {
            Logger.fatalError(e, "There was an issue with JSON!");
        }
        catch (Exception e) {
            Logger.fatalError(e, "There was an unknown issue!");
        }
        finally { }
    }

    // Public Final Methods
    public final void clear() {
        open();
        toArduinoData = new JSONObject();
        toRobotData = new JSONObject();
    }
    public final int getArduinoValue(String key) {
        int returnData = -1;
        try {
            returnData = toArduinoData.getInt(key);
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
    public final int getRobotValue(String key) {
        int returnData = -1;
        try {
            returnData = toRobotData.getInt(key);
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
    public final void pullArduino() {
        open();
        toArduinoData = read();
    }
    public final void pushRobot() {
        write();
    }
    public final void reset() {
        clear();
        setArduinoValue("move", 0);
        setRobotValue("f", 0);
        setRobotValue("l", 0);
        setRobotValue("r", 0);
    }
    public final void setArduinoValue(String key, int value) {
        try {
            toArduinoData.put(key, value);
        }
        catch (JSONException e) {
            Logger.error(e, "There was an issue with JSON!");
        }
        catch (Exception e) {
            Logger.error(e, "There was an unknown issue!");
        }
        finally { }
    }
    public final void setRobotValue(String key, int value) {
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

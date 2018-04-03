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
        buffer = "{ }";
        serial = SerialFactory.createInstance();
        serialConfig = new SerialConfig();
        serialConfig.device(SERIAL_PORT)
            .baud(Baud._9600)
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
    private synchronized final void close() {
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
    private synchronized final void open() {
        try {
            if (!serial.isOpen()) {
                Logger.info("ArduinoCommunication - Opening serial on " + SERIAL_PORT + ".");
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
    private synchronized final JSONObject read() {
        System.out.println("TESTY");
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
            Logger.fatalError(e, "There was an issue with JSON!");
        }
        catch (Exception e) {
            Logger.fatalError(e, "There was an unknown issue!");
        }
        finally { }
        return returnData;
    }
    private synchronized final void write() {
        try {
            if (serial.isOpen()) {
                serial.write("" + getArduinoValue("m"));
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
        close();
        toArduinoData = new JSONObject();
        toRobotData = new JSONObject();
    }
    public final String getArduinoValue(String key) {
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
    public final float getRobotValue(String key) {
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
    public final void pullRobot() {
        toRobotData = read();
    }
    public final void pushArduino() {
        write();
    }
    public final void reset() {
        clear();
        setArduinoValue("Stop");
        setRobotValue("f", -1);
        setRobotValue("l", -1);
        setRobotValue("r", -1);
        open();
        while (!serial.isOpen()) { }
    }
    public final void setArduinoValue(String value) {
        try {
            toArduinoData.put("m", value);
        }
        catch (JSONException e) {
            Logger.error(e, "There was an issue with JSON!");
        }
        catch (Exception e) {
            Logger.error(e, "There was an unknown issue!");
        }
        finally { }
    }
    public final void setRobotValue(String key, float value) {
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

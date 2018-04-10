package io.github.cshadd.fetch_bot.io;
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
import io.github.cshadd.fetch_bot.util.Logger;
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
    private void close()
    throws ArduinoCommunicationException {
        try {
            if (serial.isOpen()) {
                serial.discardInput();
                serial.removeListener(serialListener);
                serial.close();
                SerialFactory.shutdown();
            }
        }
        catch (IOException e) {
            throw new ArduinoCommunicationException("There was an issue with IO!", e);
        }
        catch (Exception e) {
            throw new ArduinoCommunicationException("There was an unknown issue!", e);
        }
        finally { }
    }
    private void open()
    throws ArduinoCommunicationException {
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
                        catch (Exception e) { } // Suppressed
                        finally { }
                    }
                };
                serial.addListener(serialListener);
            }
        }
        catch (IOException e) {
            throw new ArduinoCommunicationException("There was an issue with IO!", e);
        }
        catch (Exception e) {
            throw new ArduinoCommunicationException("There was an unknown issue!", e);
        }
        finally { }
    }
    private JSONObject read()
    throws ArduinoCommunicationException {
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
            throw new ArduinoCommunicationException("There was an issue with JSON!", e);
        }
        catch (Exception e) {
            throw new ArduinoCommunicationException("There was an unknown issue!", e);
        }
        finally { }
        return returnData;
    }
    private void write()
    throws ArduinoCommunicationException {
        try {
            if (serial.isOpen()) {
                serial.write(getArduinoValue("a"));
                serial.flush();
            }
        }
        catch (IOException e) {
            throw new ArduinoCommunicationException("There was an issue with IO!", e);
        }
        catch (JSONException e) {
            throw new ArduinoCommunicationException("There was an issue with JSON!", e);
        }
        catch (Exception e) {
            throw new ArduinoCommunicationException("There was an unknown issue!", e);
        }
        finally { }
    }

    // Public Methods
    public void clear()
    throws ArduinoCommunicationException {
        close();
        buffer = "{ }";
        toArduinoData = new JSONObject();
        toRobotData = new JSONObject();
    }
    public String getArduinoValue(String key)
    throws ArduinoCommunicationException {
        String returnData = null;
        try {
            returnData = toArduinoData.getString(key);
        }
        catch (JSONException e) {
            throw new ArduinoCommunicationException("There was an issue with JSON!", e);
        }
        catch (Exception e) {
            throw new ArduinoCommunicationException("There was an unknown issue!", e);
        }
        finally { }
        return returnData;
    }
    public float getRobotValue(String key)
    throws ArduinoCommunicationException {
        float returnData = -1;
        try {
            returnData = toRobotData.getFloat(key);
        }
        catch (JSONException e) {
            throw new ArduinoCommunicationException("There was an issue with JSON!", e);
        }
        catch (Exception e) {
            throw new ArduinoCommunicationException("There was an unknown issue!", e);
        }
        finally { }
        return returnData;
    }
    public void pullRobot()
    throws ArduinoCommunicationException {
        toRobotData = read();
    }
    public void pushArduino()
    throws ArduinoCommunicationException {
        write();
    }
    public void reset()
    throws ArduinoCommunicationException {
        clear();
        open();
        setArduinoValue("Stop");
        setRobotValue("f", -1);
        setRobotValue("l", -1);
        setRobotValue("r", -1);
    }
    public void setArduinoValue(String value)
    throws ArduinoCommunicationException {
        try {
            toArduinoData.put("a", value);
        }
        catch (JSONException e) {
            throw new ArduinoCommunicationException("There was an issue with JSON!", e);
        }
        catch (Exception e) {
            throw new ArduinoCommunicationException("There was an unknown issue!", e);
        }
        finally { }
    }
    public void setRobotValue(String key, float value)
    throws ArduinoCommunicationException {
        try {
            toRobotData.put(key, value);
        }
        catch (JSONException e) {
            throw new ArduinoCommunicationException("There was an issue with JSON!", e);
        }
        catch (Exception e) {
            throw new ArduinoCommunicationException("There was an unknown issue!", e);
        }
        finally { }
    }
}
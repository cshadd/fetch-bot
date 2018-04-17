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
import java.io.IOException;
import org.json.JSONException;
import org.json.JSONObject;

// Main
public class ArduinoCommunication
implements Communication {
    // Public Constant Instance/Property Fields
    public static final String SERIAL_PORT = "/dev/ttyACM0";

    // Private Instance/Property Fields
    private String buffer;
    private boolean isSerialLocked;
    private Serial serial;
    private SerialConfig serialConfig;
    private SerialDataEventListener serialListener;
    private Object serialLock;
    private JSONObject toArduinoData;
    private JSONObject toRobotData;

    // Public Constructors
    public ArduinoCommunication() {
        buffer = "{ }";
        isSerialLocked = false;
        serial = SerialFactory.createInstance();
        serialConfig = new SerialConfig();
        serialConfig.device(SERIAL_PORT);
        serialConfig.baud(Baud._9600);
        serialConfig.dataBits(DataBits._8);
        serialConfig.parity(Parity.NONE);
        serialConfig.stopBits(StopBits._1);
        serialConfig.flowControl(FlowControl.NONE);
        serialLock = new Object();
        toArduinoData = new JSONObject();
        toRobotData = new JSONObject();
    }

    // Private Methods
    private void close()
    throws CommunicationException {
        try {
            if (serial.isOpen()) {
                serial.discardInput();
                serial.removeListener(serialListener);
                serial.close();
                SerialFactory.shutdown();
            }
            isSerialLocked = false;
        }
        catch (IOException e) {
            throw new CommunicationException("There was an issue with IO!", e);
        }
        catch (Exception e) {
            throw new CommunicationException("There was an unknown issue!", e);
        }
        finally { }
    }
    private void open()
    throws CommunicationException {
        try {
            if (!serial.isOpen()) {
                serial.open(serialConfig);
                serialListener = new SerialDataEventListener() {
                    @Override
                    public void dataReceived(SerialDataEvent event) {
                        try {
                            buffer = event.getAsciiString();
                            synchronized (serialLock) {
                            	isSerialLocked = false;
                            	serialLock.notifyAll();
                            }
                        }
                        catch (Exception e) { } // Suppressed
                        finally { }
                    }
                };
                serial.addListener(serialListener);
            }
        }
        catch (IOException e) {
            throw new CommunicationException("There was an issue with IO!", e);
        }
        catch (Exception e) {
            throw new CommunicationException("There was an unknown issue!", e);
        }
        finally { }
    }
    private JSONObject read()
    throws CommunicationException {
        System.out.println(buffer);
        JSONObject returnData = new JSONObject();
        try {
            returnData.put("s", -1);
            if (buffer.charAt(0) == '{' && !buffer.equals("{ }")) {
                returnData = new JSONObject(buffer);
            }
        }
        catch (JSONException e) {
            throw new CommunicationException("There was an issue with JSON!", e);
        }
        catch (Exception e) {
            throw new CommunicationException("There was an unknown issue!", e);
        }
        finally { }
        return returnData;
    }
    private void write()
    throws CommunicationException {
        try {
            if (serial.isOpen()) {
                serial.write(getArduinoValue("a"));
                serial.flush();
            }
        }
        catch (IOException e) {
            throw new CommunicationException("There was an issue with IO!", e);
        }
        catch (JSONException e) {
            throw new CommunicationException("There was an issue with JSON!", e);
        }
        catch (Exception e) {
            throw new CommunicationException("There was an unknown issue!", e);
        }
        finally { }
    }

    // Public Methods
    public void clear()
    throws CommunicationException {
        close();
        buffer = "{ }";
        toArduinoData = new JSONObject();
        toRobotData = new JSONObject();
    }
    public String getArduinoValue(String key)
    throws CommunicationException {
        String returnData = null;
        try {
            returnData = toArduinoData.getString(key);
        }
        catch (JSONException e) {
            throw new CommunicationException("There was an issue with JSON!", e);
        }
        catch (Exception e) {
            throw new CommunicationException("There was an unknown issue!", e);
        }
        finally { }
        return returnData;
    }
    public float getRobotValue(String key)
    throws CommunicationException {
        float returnData = -1;
        try {
            returnData = toRobotData.getFloat(key);
        }
        catch (JSONException e) {
            throw new CommunicationException("There was an issue with JSON!", e);
        }
        catch (Exception e) {
            throw new CommunicationException("There was an unknown issue!", e);
        }
        finally { }
        return returnData;
    }
    public void pullRobot()
    throws CommunicationException {
        toRobotData = read();
    }
    public void pushArduino()
    throws CommunicationException {
        write();
        isSerialLocked = true;
        synchronized (serialLock) {
            try {
            	while (isSerialLocked) {
                    serialLock.wait();
                }
            }
            catch (Exception e) { } // Suppressed
            finally { }
        }
    }
    public void reset()
    throws CommunicationException {
        clear();
        open();
        setArduinoValue("a", "Stop");
        setRobotValue("s", -1);
    }
    public void setArduinoValue(String key, String value)
    throws CommunicationException {
        try {
            toArduinoData.put(key, value);
        }
        catch (JSONException e) {
            throw new CommunicationException("There was an issue with JSON!", e);
        }
        catch (Exception e) {
            throw new CommunicationException("There was an unknown issue!", e);
        }
        finally { }
    }
    public void setRobotValue(String key, float value)
    throws CommunicationException {
        try {
            toRobotData.put(key, value);
        }
        catch (JSONException e) {
            throw new CommunicationException("There was an issue with JSON!", e);
        }
        catch (Exception e) {
            throw new CommunicationException("There was an unknown issue!", e);
        }
        finally { }
    }
}

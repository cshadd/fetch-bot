package io.github.cshadd.fetch_bot.io;
import java.io.IOException;
import com.pi4j.io.serial.Baud;
import com.pi4j.io.serial.DataBits;
import com.pi4j.io.serial.FlowControl;
import com.pi4j.io.serial.Parity;
import com.pi4j.io.serial.Serial;
import com.pi4j.io.serial.SerialConfig;
import com.pi4j.io.serial.SerialDataEvent;
import com.pi4j.io.serial.SerialDataEventListener;
import com.pi4j.io.serial.SerialFactory;
import com.pi4j.io.serial.StopBits;
import org.json.JSONException;
import org.json.JSONObject;

// Main
public abstract class AbstractArduinoCommunication
extends AbstractCommunication
implements ArduinoCommunication {
    // Public Constant Instance/Property Fields
    public static final String SERIAL_PORT = "/dev/ttyACM0"; // Change if needed

    // Private Final Instance/Property Fields
    private final Serial serial;
    private final SerialConfig serialConfig;
    
    // Protected Instance/Property Fields
    protected String buffer;
    protected boolean isSerialLocked;
    protected final Object serialLock;
    
    // Private Instance/Property Fields
    private SerialDataEventListener serialListener;
    
    // Protected Constructors
    protected AbstractArduinoCommunication() {
        super();
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
    }
    
    // Protected Methods
    protected void close()
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
            throw new ArduinoCommunicationException("There was an issue with IO!", e);
        }
        catch (Exception e) {
            throw new ArduinoCommunicationException("There was an unknown issue!", e);
        }
        finally { }
    }
    protected void open()
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
            throw new ArduinoCommunicationException("There was an issue with IO!", e);
        }
        catch (Exception e) {
            throw new ArduinoCommunicationException("There was an unknown issue!", e);
        }
        finally { }
    }
    protected JSONObject read()
    throws CommunicationException {
        JSONObject returnData = new JSONObject();
        try {
            returnData.put("s", -1);
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
    protected void write()
    throws CommunicationException {
        try {
            if (serial.isOpen()) {
                serial.write(getSourceValue("a"));
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
    
    // Public Methods (Overrided)
    @Override
    public float getRobotFloatValue(String key)
    throws CommunicationException {
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
}

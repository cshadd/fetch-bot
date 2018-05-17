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
    public static final String DEFAULT_SERIAL_PORT = "/dev/ttyACM0"; // Change if needed

    // Private Final Instance/Property Fields
    private final Serial serial;
    private final SerialConfig serialConfig;
    
    // Protected Final Instance/Property Fields
    protected final Object serialLock;
    protected final String serialPort;
    
    // Protected Instance/Property Fields
    protected String buffer;
    protected boolean isSerialLocked;
    
    // Private Instance/Property Fields
    private SerialDataEventListener serialListener;
    
    // Protected Constructors
    protected AbstractArduinoCommunication() {
        this(DEFAULT_SERIAL_PORT);
    }
    protected AbstractArduinoCommunication(String serialPort) {
        super();
        this.serialPort = serialPort;
        buffer = "{ }";
        isSerialLocked = false;
        serial = SerialFactory.createInstance();
        serialConfig = new SerialConfig();
        serialConfig.device(this.serialPort);
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
            throw new ArduinoCommunicationException("Could not close " + serialPort + ".", e);
        }
        catch (Exception e) {
            throw new ArduinoCommunicationException("Unknown issue.", e);
        }
        finally { /* */ }
    }
    protected void open()
    throws CommunicationException {
        try {
            if (!serial.isOpen()) {
                serial.open(serialConfig);
                serialListener = new SerialDataEventListener() {
                    // Public Methods (Overrided)
                    @Override
                    public void dataReceived(SerialDataEvent event) {
                        try {
                            buffer = event.getAsciiString();
                            synchronized (serialLock) {
                                isSerialLocked = false;
                                serialLock.notifyAll();
                            }
                        }
                        catch (Exception e) { /* */ } // Suppressed
                        finally { /* */ }
                    }
                };
                serial.addListener(serialListener);
            }
        }
        catch (IOException e) {
            throw new ArduinoCommunicationException("Could not open " + serialPort + ".", e);
        }
        catch (Exception e) {
            throw new ArduinoCommunicationException("Unknown issue.", e);
        }
        finally { /* */ }
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
            throw new ArduinoCommunicationException("Could not parse JSON in " + serialPort + ".", e);
        }
        catch (Exception e) {
            throw new ArduinoCommunicationException("Unknown issue.", e);
        }
        finally { /* */ }
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
            throw new ArduinoCommunicationException("Could not write to " + serialPort + ".", e);
        }
        catch (JSONException e) {
            throw new ArduinoCommunicationException("Could not write JSON to " + serialPort + ".", e);
        }
        catch (Exception e) {
            throw new ArduinoCommunicationException("Unknown issue.", e);
        }
        finally { /* */ }
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
            throw new ArduinoCommunicationException("Bad JSON value " + key + ".", e);
        }
        catch (Exception e) {
            throw new ArduinoCommunicationException("Unknown issue.", e);
        }
        finally { /* */ }
        return returnData;
    }
}

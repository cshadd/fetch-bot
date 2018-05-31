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
        this.buffer = "{ }";
        this.isSerialLocked = false;
        this.serial = SerialFactory.createInstance();
        this.serialConfig = new SerialConfig();
        this.serialConfig.device(this.serialPort);
        this.serialConfig.baud(Baud._9600);
        this.serialConfig.dataBits(DataBits._8);
        this.serialConfig.parity(Parity.NONE);
        this.serialConfig.stopBits(StopBits._1);
        this.serialConfig.flowControl(FlowControl.NONE);
        this.serialLock = new Object();
    }
    
    // Protected Methods
    protected void close()
    throws CommunicationException {
        try {
            if (this.serial.isOpen()) {
                this.serial.discardInput();
                this.serial.removeListener(this.serialListener);
                this.serial.close();
                SerialFactory.shutdown();
            }
            this.isSerialLocked = false;
        }
        catch (IOException e) {
            throw new ArduinoCommunicationException("Could not close " + this.serialPort + ".", e);
        }
        catch (Exception e) {
            throw new ArduinoCommunicationException("Unknown issue.", e);
        }
        finally { /* */ }
    }
    protected void open()
    throws CommunicationException {
        try {
            if (!this.serial.isOpen()) {
                this.serial.open(this.serialConfig);
                this.serialListener = new SerialDataEventListener() {
                    // Public Methods (Overrided)
                    @Override
                    public void dataReceived(SerialDataEvent event) {
                        try {
                            AbstractArduinoCommunication.this.buffer = event.getAsciiString();
                            synchronized (AbstractArduinoCommunication.this.serialLock) {
                                AbstractArduinoCommunication.this.isSerialLocked = false;
                                AbstractArduinoCommunication.this.serialLock.notifyAll();
                            }
                        }
                        catch (Exception e) { /* */ } // Suppressed
                        finally { /* */ }
                    }
                };
                this.serial.addListener(this.serialListener);
            }
        }
        catch (IOException e) {
            throw new ArduinoCommunicationException("Could not open " + this.serialPort + ".", e);
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
            if (this.buffer.charAt(0) == '{' && !this.buffer.equals("{ }")) {
                returnData = new JSONObject(this.buffer);
            }
        }
        catch (JSONException e) {
            throw new ArduinoCommunicationException("Could not parse JSON in " + this.serialPort + ".", e);
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
            if (this.serial.isOpen()) {
                this.serial.write(getSourceValue("a"));
                this.serial.flush();
            }
        }
        catch (IOException e) {
            throw new ArduinoCommunicationException("Could not write to " + this.serialPort + ".", e);
        }
        catch (JSONException e) {
            throw new ArduinoCommunicationException("Could not write JSON to " + this.serialPort + ".", e);
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
            returnData = this.toRobotData.getFloat(key);
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

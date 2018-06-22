package io.github.cshadd.fetch_bot.io.json;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

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
import io.github.cshadd.fetch_bot.References;
import io.github.cshadd.fetch_bot.util.Logger;

import org.json.JSONException;
import org.json.JSONObject;

// Main

/**
 * The Class AbstractArduninoCommunication. Defines what an Arduino
 * Communication is. An Arduino Communication is basically a helper that directs
 * data between an Arduino and Fetch Bot.
 * 
 * @author Christian Shadd
 * @author Maria Verna Aquino
 * @author Thanh Vu
 * @author Joseph Damian
 * @author Giovanni Orozco
 * @since 1.0.0
 */
public abstract class AbstractArduinoCommunication extends
                AbstractJSONCommunication implements ArduinoCommunication {
    // Private Final Instance/Property Fields
    
    private final Serial       serial;
    private final SerialConfig serialConfig;
    
    // Protected Final Instance/Property Fields
    
    protected final BlockingQueue<String> serialBufferSyncQueue;
    protected final String                serialPort;
    
    // Private Instance/Property Fields
    
    private SerialDataEventListener serialListener;

    // Private Constructors

    private AbstractArduinoCommunication() {
        this(null);
    }
    
    // Protected Constructors
    
    protected AbstractArduinoCommunication(String serialPort) {
        super();
        this.serialPort = serialPort;
        this.serial = SerialFactory.createInstance();
        this.serialConfig = new SerialConfig();
        this.serialConfig.device(this.serialPort);
        this.serialConfig.baud(Baud._9600);
        this.serialConfig.dataBits(DataBits._8);
        this.serialConfig.parity(Parity.NONE);
        this.serialConfig.stopBits(StopBits._1);
        this.serialConfig.flowControl(FlowControl.NONE);
        this.serialBufferSyncQueue = new SynchronousQueue<>();
    }
    
    // Protected Methods
    
    protected void close() throws ArduinoCommunicationException {
        try {
            if (this.serial.isOpen()) {
                this.serial.discardInput();
                this.serial.removeListener(this.serialListener);
                this.serial.close();
                SerialFactory.shutdown();
            }
        } catch (IOException e) {
            throw new ArduinoCommunicationException("Could not close "
                            + this.serialPort + ".", e);
        } catch (Exception e) {
            throw new ArduinoCommunicationException("Unknown issue.", e);
        } finally {
            /* */ }
    }
    
    protected void open() throws ArduinoCommunicationException {
        try {
            if (!this.serial.isOpen()) {
                this.serial.open(this.serialConfig);
                this.serialListener = new SerialDataEventListener() {
                    // Public Methods (Overrided)
                    
                    @Override
                    public void dataReceived(SerialDataEvent event) {
                        try {
                            AbstractArduinoCommunication.this.serialBufferSyncQueue
                                            .offer(event.getAsciiString());
                        } catch (Exception e) {
                            /* */ } // Suppressed
                        finally {
                            /* */ }
                    }
                };
                this.serial.addListener(this.serialListener);
            }
        } catch (IOException e) {
            throw new ArduinoCommunicationException("Could not open "
                            + this.serialPort + ".", e);
        } catch (Exception e) {
            throw new ArduinoCommunicationException("Unknown issue.", e);
        } finally {
            /* */ }
    }
    
    protected JSONObject read() throws ArduinoCommunicationException {
        JSONObject returnData = new JSONObject();
        try {
            returnData.put("s", -1);
            final String data = this.serialBufferSyncQueue.poll(5, TimeUnit.SECONDS);
            if (data != null) {
                if (data.charAt(0) == '{' && !data.equals("{ }")) {
                    returnData = new JSONObject(data);
                }
            }
        } catch (InterruptedException e) {
            /* */ } // Suppressed
        catch (JSONException e) {
            throw new ArduinoCommunicationException("Could not parse JSON in "
                            + this.serialPort + ".", e);
        } catch (Exception e) {
            throw new ArduinoCommunicationException("Unknown issue.", e);
        } finally {
            /* */ }
        return returnData;
    }
    
    protected void write() throws ArduinoCommunicationException {
        try {
            if (this.serial.isOpen()) {
                this.serial.write(getSourceValue("a"));
                this.serial.flush();
            }
        } catch (IOException e) {
            throw new ArduinoCommunicationException("Could not write to "
                            + this.serialPort + ".", e);
        } catch (JSONException e) {
            throw new ArduinoCommunicationException("Could not write JSON to "
                            + this.serialPort + ".", e);
        } catch (Exception e) {
            throw new ArduinoCommunicationException("Unknown issue.", e);
        } finally {
            /* */ }
    }
    
    // Public Methods (Overrided)
    
    @Override
    public float getRobotFloatValue(String key)
                    throws ArduinoCommunicationException {
        float returnData = -1;
        try {
            returnData = this.toRobotData.getFloat(key);
        } catch (JSONException e) {
            throw new ArduinoCommunicationException("Bad JSON value " + key
                            + ".", e);
        } catch (Exception e) {
            throw new ArduinoCommunicationException("Unknown issue.", e);
        } finally {
            /* */ }
        return returnData;
    }
}

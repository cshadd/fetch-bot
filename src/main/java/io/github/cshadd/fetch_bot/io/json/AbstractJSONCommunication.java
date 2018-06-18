package io.github.cshadd.fetch_bot.io.json;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;
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

import io.github.cshadd.fetch_bot.io.AbstractCommunication;

import org.json.JSONException;
import org.json.JSONObject;

// Main
public abstract class AbstractJSONCommunication
extends AbstractCommunication
implements JSONCommunication {
    // Protected Final Instance/Property Fields
    protected JSONObject toRobotData;
    protected JSONObject toSourceData;

    // Protected Constructors
    protected AbstractJSONCommunication() {
        this.toRobotData = new JSONObject();
        this.toSourceData = new JSONObject();
    }
    
    // Public Methods (Overrided)
    @Override
    public String getRobotValue(String key)
    throws JSONCommunicationException {
        String returnData = null;
        try {
            returnData = this.toRobotData.getString(key);
        }
        catch (JSONException e) {
            throw new JSONCommunicationException("Bad JSON value " + key + ".", e);
        }
        catch (Exception e) {
            throw new JSONCommunicationException("Unknown issue.", e);
        }
        finally { /* */ }
        return returnData;
    }
    @Override
    public String getSourceValue(String key)
    throws JSONCommunicationException {
        String returnData = null;
        try {
            returnData = this.toSourceData.getString(key);
        }
        catch (JSONException e) {
            throw new JSONCommunicationException("Bad JSON value " + key + ".", e);
        }
        catch (Exception e) {
            throw new JSONCommunicationException("Unknown issue.", e);
        }
        finally { /* */ }
        return returnData;
    }
    @Override
    public void pullRobot()
    throws JSONCommunicationException {
        throw new UnsupportedOperationException();
    }
    @Override
    public void pullSource()
    throws JSONCommunicationException {
        throw new UnsupportedOperationException();
    }
    @Override
    public void pushRobot()
    throws JSONCommunicationException {
        throw new UnsupportedOperationException();
    }
    @Override
    public void pushSource()
    throws JSONCommunicationException {
        throw new UnsupportedOperationException();
    }
    @Override
    public void setRobotValue(String key, String value)
    throws JSONCommunicationException {
        try {
            this.toRobotData.put(key, value);
        }
        catch (JSONException e) {
            throw new JSONCommunicationException("Bad JSON key-value " + key + ", " + value + ".", e);
        }
        catch (Exception e) {
            throw new JSONCommunicationException("Unknown issue.", e);
        }
        finally { /* */ }
    }
    @Override
    public void setSourceValue(String key, String value)
    throws JSONCommunicationException {
        try {
            this.toSourceData.put(key, value);
        }
        catch (JSONException e) {
            throw new JSONCommunicationException("Bad JSON key-value " + key + ", " + value + ".", e);
        }
        catch (Exception e) {
            throw new JSONCommunicationException("Unknown issue.", e);
        }
        finally { /* */ }
    }

}

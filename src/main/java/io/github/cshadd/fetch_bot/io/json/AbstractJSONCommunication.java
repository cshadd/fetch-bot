package io.github.cshadd.fetch_bot.io.json;

import io.github.cshadd.fetch_bot.io.AbstractCommunication;
import org.json.JSONException;
import org.json.JSONObject;

// Main

/**
 * The Class AbstractJSONCommunication. Defines what an JSON
 * Communication is. A JSON Communication is basically a helper that directs
 * data between JSON and Fetch Bot.
 * 
 * @author Christian Shadd
 * @author Maria Verna Aquino
 * @author Thanh Vu
 * @author Joseph Damian
 * @author Giovanni Orozco
 * @since 2.0.0-alpha
 */
public abstract class AbstractJSONCommunication extends AbstractCommunication
                implements JSONCommunication {
    // Protected Final Instance/Property Fields
    protected JSONObject toRobotData;
    protected JSONObject toSourceData;
    
    // Protected Constructors
    protected AbstractJSONCommunication() {
        super();
        this.toRobotData = new JSONObject();
        this.toSourceData = new JSONObject();
    }
    
    // Public Methods (Overrided)
    @Override
    public String getRobotValue(String key) throws JSONCommunicationException {
        String returnData = null;
        try {
            returnData = this.toRobotData.getString(key);
        } catch (JSONException e) {
            throw new JSONCommunicationException("Bad JSON value " + key + ".",
                            e);
        } catch (Exception e) {
            throw new JSONCommunicationException("Unknown issue.", e);
        } finally {
            /* */ }
        return returnData;
    }
    
    @Override
    public String getSourceValue(String key) throws JSONCommunicationException {
        String returnData = null;
        try {
            returnData = this.toSourceData.getString(key);
        } catch (JSONException e) {
            throw new JSONCommunicationException("Bad JSON value " + key + ".",
                            e);
        } catch (Exception e) {
            throw new JSONCommunicationException("Unknown issue.", e);
        } finally {
            /* */ }
        return returnData;
    }
    
    @Override
    public void pullRobot() throws JSONCommunicationException {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void pullSource() throws JSONCommunicationException {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void pushRobot() throws JSONCommunicationException {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void pushSource() throws JSONCommunicationException {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void setRobotValue(String key, String value)
                    throws JSONCommunicationException {
        try {
            this.toRobotData.put(key, value);
        } catch (JSONException e) {
            throw new JSONCommunicationException("Bad JSON key-value " + key
                            + ", " + value + ".", e);
        } catch (Exception e) {
            throw new JSONCommunicationException("Unknown issue.", e);
        } finally {
            /* */ }
    }
    
    @Override
    public void setSourceValue(String key, String value)
                    throws JSONCommunicationException {
        try {
            this.toSourceData.put(key, value);
        } catch (JSONException e) {
            throw new JSONCommunicationException("Bad JSON key-value " + key
                            + ", " + value + ".", e);
        } catch (Exception e) {
            throw new JSONCommunicationException("Unknown issue.", e);
        } finally {
            /* */ }
    }
    
}

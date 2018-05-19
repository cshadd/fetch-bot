package io.github.cshadd.fetch_bot.io;
import org.json.JSONException;
import org.json.JSONObject;

// Main
public abstract class AbstractCommunication
implements Communication {
    // Protected Final Instance/Property Fields
    protected JSONObject toRobotData;
    protected JSONObject toSourceData;

    // Protected Constructors
    protected AbstractCommunication() {
        toRobotData = new JSONObject();
        toSourceData = new JSONObject();
    }
    
    // Public Methods (Overrided)
    @Override
    public String getRobotValue(String key)
    throws CommunicationException {
        String returnData = null;
        try {
            returnData = toRobotData.getString(key);
        }
        catch (JSONException e) {
            throw new CommunicationException("Bad JSON value " + key + ".", e);
        }
        catch (Exception e) {
            throw new CommunicationException("Unknown issue.", e);
        }
        finally { /* */ }
        return returnData;
    }
    @Override
    public String getSourceValue(String key)
    throws CommunicationException {
        String returnData = null;
        try {
            returnData = toSourceData.getString(key);
        }
        catch (JSONException e) {
            throw new CommunicationException("Bad JSON value " + key + ".", e);
        }
        catch (Exception e) {
            throw new CommunicationException("Unknown issue.", e);
        }
        finally { /* */ }
        return returnData;
    }
    @Override
    public void pullRobot()
    throws CommunicationException {
        throw new UnsupportedOperationException();
    }
    @Override
    public void pullSource()
    throws CommunicationException {
        throw new UnsupportedOperationException();
    }
    @Override
    public void pushRobot()
    throws CommunicationException {
        throw new UnsupportedOperationException();
    }
    @Override
    public void pushSource()
    throws CommunicationException {
        throw new UnsupportedOperationException();
    }
    @Override
    public void setRobotValue(String key, String value)
    throws CommunicationException {
        try {
            toRobotData.put(key, value);
        }
        catch (JSONException e) {
            throw new CommunicationException("Bad JSON key-value " + key + ", " + value + ".", e);
        }
        catch (Exception e) {
            throw new CommunicationException("Unknown issue.", e);
        }
        finally { /* */ }
    }
    @Override
    public void setSourceValue(String key, String value)
    throws CommunicationException {
        try {
            toSourceData.put(key, value);
        }
        catch (JSONException e) {
            throw new CommunicationException("Bad JSON key-value " + key + ", " + value + ".", e);
        }
        catch (Exception e) {
            throw new CommunicationException("Unknown issue.", e);
        }
        finally { /* */ }
    }
}
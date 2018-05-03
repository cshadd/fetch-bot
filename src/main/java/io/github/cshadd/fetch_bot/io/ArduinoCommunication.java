package io.github.cshadd.fetch_bot.io;

// Main
public abstract interface ArduinoCommunication
extends Communication {
    // Public Abstract Methods
    public abstract float getRobotFloatValue(String key) throws CommunicationException;
    
    // Public Methods (Overrided)
    @Override
    public default String getRobotValue(String key)
    throws CommunicationException {
        return "" + getRobotFloatValue(key);
    }
}
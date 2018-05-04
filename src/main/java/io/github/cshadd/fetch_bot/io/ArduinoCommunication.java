package io.github.cshadd.fetch_bot.io;

// Main
public abstract interface ArduinoCommunication
extends Communication {
    // Public Abstract Methods
    public abstract float getRobotFloatValue(String key) throws CommunicationException;
}
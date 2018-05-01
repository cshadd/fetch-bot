package io.github.cshadd.fetch_bot.io;

import io.github.cshadd.fetch_bot.controllers.CommunicationException;

// Main
public abstract interface ArduinoCommunication
extends Communication {
    // Public Abstract Methods
    public abstract void pullRobot() throws CommunicationException;
    public abstract void pushSource() throws CommunicationException;
}

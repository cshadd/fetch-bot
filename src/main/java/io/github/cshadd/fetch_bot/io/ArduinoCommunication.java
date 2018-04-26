package io.github.cshadd.fetch_bot.io;

// Main
public abstract interface ArduinoCommunication
extends Communication {
	public abstract void pullRobot() throws CommunicationException;
	public abstract void pushSource() throws CommunicationException;
}

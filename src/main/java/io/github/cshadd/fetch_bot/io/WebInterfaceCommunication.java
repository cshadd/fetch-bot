package io.github.cshadd.fetch_bot.io;

// Main
public abstract interface WebInterfaceCommunication
extends Communication {
	// Public Abstract Methods
	public abstract void pullRobot() throws CommunicationException;
	public abstract void pullSource() throws CommunicationException;
	public abstract void pushRobot() throws CommunicationException;
	public abstract void pushSource() throws CommunicationException;
}

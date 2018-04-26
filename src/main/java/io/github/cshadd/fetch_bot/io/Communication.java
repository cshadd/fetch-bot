package io.github.cshadd.fetch_bot.io;
import io.github.cshadd.fetch_bot.FetchBot;

// Main
public abstract interface Communication
extends FetchBot {
	// Public Abstract Methods
	public abstract void clear() throws CommunicationException;
	public abstract String getRobotValue(String key) throws CommunicationException;
	public abstract String getSourceValue(String key) throws CommunicationException;
	public abstract void pullRobot() throws CommunicationException;
	public abstract void pullSource() throws CommunicationException;
	public abstract void pushRobot() throws CommunicationException;
	public abstract void pushSource() throws CommunicationException;
	public abstract void reset() throws CommunicationException;
	public abstract void setRobotValue(String key, String value) throws CommunicationException;
	public abstract void setSourceValue(String key, String value) throws CommunicationException;
}

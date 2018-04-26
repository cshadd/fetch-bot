package io.github.cshadd.fetch_bot.controllers;
import io.github.cshadd.fetch_bot.io.ArduinoCommunication;
import io.github.cshadd.fetch_bot.io.ArduinoCommunicationImpl;
import io.github.cshadd.fetch_bot.io.CommunicationException;

// Main
public class ArduinoControllerImpl
implements ArduinoController {
    // Private Instance/Property Fields
	private ArduinoCommunication arduinoComm;

	// Public Constructors
	public ArduinoControllerImpl() {
		arduinoComm = new ArduinoCommunicationImpl();
	}
	
	// Private Methods
	private void sendAction(String action)
	throws CommunicationException {
		arduinoComm.setSourceValue("a", action);
        arduinoComm.pushSource();
	}

	// Public Methods
	public void move(String direction)
	throws CommunicationException {
		sendAction(direction);
	}
	public void moveForward()
	throws CommunicationException {
		move("Forward");
	}
	public void moveLeft()
	throws CommunicationException {
		move("Left");
	}
	public void moveRight()
	throws CommunicationException {
		move("Right");
	}
	public void moveStop()
	throws CommunicationException {
		move("Stop");
	}
	public int ultrasonicSensorValue()
	throws CommunicationException {
        arduinoComm.pullRobot();
		return Integer.parseInt(arduinoComm.getRobotValue("s"));
	}
}
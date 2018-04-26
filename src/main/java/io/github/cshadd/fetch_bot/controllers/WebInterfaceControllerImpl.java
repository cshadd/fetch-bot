package io.github.cshadd.fetch_bot.controllers;
import io.github.cshadd.fetch_bot.io.CommunicationException;
import io.github.cshadd.fetch_bot.io.WebInterfaceCommunication;
import io.github.cshadd.fetch_bot.io.WebInterfaceCommunicationImpl;

// Main
public class WebInterfaceControllerImpl
implements WebInterfaceController {
    // Private Instance/Property Fields
	private WebInterfaceCommunication webInterfaceComm;

	// Public Constructors
	public WebInterfaceControllerImpl() {
		webInterfaceComm = new WebInterfaceCommunicationImpl();
	}
	
	// Private Methods

	// Public Methods
}
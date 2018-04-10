package io.github.cshadd.fetch_bot.io;

// Main
public class ArduinoCommunicationException
extends CommunicationException {
    // Private Constant Instance/Property Fields
    private static final long serialVersionUID = 1L;

    // Public Constructors
    public ArduinoCommunicationException() { }
    public ArduinoCommunicationException(String message) {
        super(message);
    }
    public ArduinoCommunicationException(String message, Throwable cause) {
        super(message, cause);
    }
    public ArduinoCommunicationException(Throwable cause) {
        super(cause);
    }
}
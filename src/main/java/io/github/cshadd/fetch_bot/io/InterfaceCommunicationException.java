package io.github.cshadd.fetch_bot.io;

// Main
public class InterfaceCommunicationException
extends CommunicationException {
    // Private Constant Instance/Property Fields
    private static final long serialVersionUID = 1L;

    // Public Constructors
    public InterfaceCommunicationException() { }
    public InterfaceCommunicationException(String message) {
        super(message);
    }
    public InterfaceCommunicationException(String message, Throwable cause) {
        super(message, cause);
    }
    public InterfaceCommunicationException(Throwable cause) {
        super(cause);
    }
}
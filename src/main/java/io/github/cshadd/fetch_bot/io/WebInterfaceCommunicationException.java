package io.github.cshadd.fetch_bot.io;

// Main
public class WebInterfaceCommunicationException
extends CommunicationException {
    // Private Constant Instance/Property Fields
    private static final long serialVersionUID = 1L;

    // Public Constructors
    public WebInterfaceCommunicationException() { }
    public WebInterfaceCommunicationException(String message) {
        super(message);
    }
    public WebInterfaceCommunicationException(String message, Throwable cause) {
        super(message, cause);
    }
    public WebInterfaceCommunicationException(Throwable cause) {
        super(cause);
    }    
}
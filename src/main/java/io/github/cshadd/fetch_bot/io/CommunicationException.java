package io.github.cshadd.fetch_bot.io;
import io.github.cshadd.fetch_bot.FetchBotException;

// Main
public class CommunicationException
extends FetchBotException {
    // Private Constant Instance/Property Fields
    private static final long serialVersionUID = 1L;

    // Public Constructors
    public CommunicationException() { }
    public CommunicationException(String message) {
        super(message);
    }
    public CommunicationException(String message, Throwable cause) {
        super(message, cause);
    }
    public CommunicationException(Throwable cause) {
        super(cause);
    }
}
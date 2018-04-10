package io.github.cshadd.fetch_bot;

// Main
public class FetchBotException
extends Exception {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Public Constructors
    public FetchBotException() { }
    public FetchBotException(String message) {
        super(message);
    }
    public FetchBotException(String message, Throwable cause) {
        super(message, cause);
    }
    public FetchBotException(Throwable cause) {
        super(cause);
    }
}
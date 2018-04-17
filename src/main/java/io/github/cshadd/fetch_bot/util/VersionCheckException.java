package io.github.cshadd.fetch_bot.util;
import io.github.cshadd.fetch_bot.FetchBotException;

// Main
public class VersionCheckException
extends FetchBotException {
    // Private Constant Instance/Property Fields
    private static final long serialVersionUID = 1L;

    // Public Constructors
    public VersionCheckException() { }
    public VersionCheckException(String message) {
        super(message);
    }
    public VersionCheckException(String message, Throwable cause) {
        super(message, cause);
    }
    public VersionCheckException(Throwable cause) {
        super(cause);
    }
}
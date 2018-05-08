package io.github.cshadd.fetch_bot.controllers;
import io.github.cshadd.fetch_bot.FetchBotException;

// Main
public class ControllerException
extends FetchBotException {
    // Private Constant Instance/Property Fields
    private static final long serialVersionUID = 1L;

    // Public Constructors
    public ControllerException() { }
    public ControllerException(String message) {
        super(message);
    }
    public ControllerException(String message, Throwable cause) {
        super(message, cause);
    }
    public ControllerException(Throwable cause) {
        super(cause);
    }
}
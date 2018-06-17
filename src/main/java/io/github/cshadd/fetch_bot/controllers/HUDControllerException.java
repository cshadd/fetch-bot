package io.github.cshadd.fetch_bot.controllers;

// Main

public class HUDControllerException extends ControllerException {
    // Private Constant Instance/Property Fields

    private static final long serialVersionUID = 1L;
    
    // Public Constructors

    public HUDControllerException() {
    }
    public HUDControllerException(String message) {
        super(message);
    }
    public HUDControllerException(String message, Throwable cause) {
        super(message, cause);
    }
    public HUDControllerException(Throwable cause) {
        super(cause);
    }
}
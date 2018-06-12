package io.github.cshadd.fetch_bot.controllers;

// Main

public class HudControllerException extends ControllerException {
    // Private Constant Instance/Property Fields

    private static final long serialVersionUID = 1L;
    
    // Public Constructors

    public HudControllerException() {
    }
    public HudControllerException(String message) {
        super(message);
    }
    public HudControllerException(String message, Throwable cause) {
        super(message, cause);
    }
    public HudControllerException(Throwable cause) {
        super(cause);
    }
}
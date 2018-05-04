package io.github.cshadd.fetch_bot.controllers;

// Main
public class PathfindControllerException
extends ControllerException {
    // Private Constant Instance/Property Fields
    private static final long serialVersionUID = 1L;

    // Public Constructors
    public PathfindControllerException() { }
    public PathfindControllerException(String message) {
        super(message);
    }
    public PathfindControllerException(String message, Throwable cause) {
        super(message, cause);
    }
    public PathfindControllerException(Throwable cause) {
        super(cause);
    }    
}
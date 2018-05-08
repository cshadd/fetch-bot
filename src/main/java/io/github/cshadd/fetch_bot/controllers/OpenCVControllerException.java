package io.github.cshadd.fetch_bot.controllers;

// Main
public class OpenCVControllerException
extends ControllerException {
    // Private Constant Instance/Property Fields
    private static final long serialVersionUID = 1L;

    // Public Constructors
    public OpenCVControllerException() { }
    public OpenCVControllerException(String message) {
        super(message);
    }
    public OpenCVControllerException(String message, Throwable cause) {
        super(message, cause);
    }
    public OpenCVControllerException(Throwable cause) {
        super(cause);
    }    
}
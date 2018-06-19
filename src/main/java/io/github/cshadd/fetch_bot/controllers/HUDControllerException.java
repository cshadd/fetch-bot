package io.github.cshadd.fetch_bot.controllers;

// Main

/**
 * The Class HUDControllerException. A Controller Exception specific to
 * HUD Controller.
 * 
 * @author Christian Shadd
 * @author Maria Verna Aquino
 * @author Thanh Vu
 * @author Joseph Damian
 * @author Giovanni Orozco
 * @since 2.0.0-alpha
 */
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
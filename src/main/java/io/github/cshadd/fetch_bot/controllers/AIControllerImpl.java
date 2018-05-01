package io.github.cshadd.fetch_bot.controllers;
import io.github.cshadd.fetch_bot.Component;
import org.opencv.core.Core;

// Main
@Component("AI")
public class AIControllerImpl
implements AIController {
    // Private Instance/Property Fields
    
    // Public Constructors
    public AIControllerImpl() { }

    // Public Static Methods
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }
}
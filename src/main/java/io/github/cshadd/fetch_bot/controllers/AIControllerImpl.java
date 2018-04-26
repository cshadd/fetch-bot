package io.github.cshadd.fetch_bot.controllers;
import org.opencv.core.Core;

// Main
public class AIControllerImpl
implements AIController {
    // Private Instance/Property Fields
    
	// Public Constructors
	public AIControllerImpl() { }
	
	// Public Static Methods
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }	
	
	// Private Methods

	// Public Methods
}
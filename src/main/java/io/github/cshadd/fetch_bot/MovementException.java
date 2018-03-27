package io.github.cshadd.fetch_bot;

// Main
public class MovementException
extends Exception
implements FetchBot {
    // Public Constructors
    public MovementException(String message) {
        super(message);
    }
}
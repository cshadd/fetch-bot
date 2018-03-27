package io.github.cshadd.fetch_bot;

// Main
public class Movement
implements FetchBot {
    // Public Constant Instance/Property Fields
    public enum COMMAND {
        FORWARD,
        LEFT,
        RIGHT,
        STOP
    }

    // Public Constructors
    public Movement() { }

    // Public Methods
    public void move(COMMAND command)
    throws MovementException {
        if (command == COMMAND.FORWARD) {

        }
        else if (command == COMMAND.LEFT) {

        }
        else if (command == COMMAND.RIGHT) {

        }
        else if (command == COMMAND.STOP) {

        }
        else {
            throw new MovementException("Invalid movement type, " + command + ".");
        }
    }
}

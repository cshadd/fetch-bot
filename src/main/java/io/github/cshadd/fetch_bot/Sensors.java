package io.github.cshadd.fetch_bot;

// Main
public class Sensors
implements FetchBot {
    // Public Constructors
    public Sensors() { }

    // Public Static Nested Classes
    public enum DIRECTION {
        FRONT,
        LEFT,
        RIGHT
    }

    // Public Methods
    public int getDistance(DIRECTION direction)
    throws SensorsException {
        if (direction == DIRECTION.FRONT) {
            return -1;
        }
        else if (direction == DIRECTION.LEFT) {
            return -1;
        }
        else if (direction == DIRECTION.RIGHT) {
            return -1;
        }
        else {
            throw new SensorsException("Invalid direction type, " + direction + ".");
        }
    }
}

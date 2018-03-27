package io.github.cshadd.fetch_bot;

// Main
public class Sensor
implements FetchBot {
    // Public Constructors
    public Sensor() { }

    // Public Static Nested Classes
    public enum DIRECTION {
        FRONT,
        LEFT,
        RIGHT
    }

    // Public Methods
    public int getDistance(DIRECTION direction)
    throws SensorException {
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
            throw new SensorException("Invalid direction type, " + direction + ".");
        }
    }
}
package io.github.cshadd.fetch_bot;

// Main
public class Sensor
implements FetchBot {
    // Public Constant Instance/Property Fields
    public enum DIRECTION {
        FRONT,
        LEFT,
        RIGHT
    }

    // Public Constructors
    public Sensor() { }

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
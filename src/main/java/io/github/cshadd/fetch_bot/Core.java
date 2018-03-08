package io.github.cshadd.fetch_bot;
import io.github.cshadd.fetch_bot.util.Logger;
import io.github.cshadd.fetch_bot.util.Communication;

// Main
public class Core
implements FetchBot {
    // Private Static Instance/Property Fields
    private static Communication comm;
    private static Movement movement;
    private static Pathfinding path;

    // Public Static Methods
    public static void delay(long length) {
        try {
            Thread.sleep(length);
        }
        catch (InterruptedException e) {
            Logger.error(e, "There was an issue with Interrupt!");
        }
        catch (Exception e) {
            Logger.error(e, "There was an unknown issue!");
        }
    }

    // Entry Point
    public static void main(String[] args) {
        comm = new Communication();
        comm.resetToRobot();
        comm.resetToInterface();
        Logger.clear();
        Logger.info("Fetch Bot starting!");

        movement = new Movement();
        // path = new Pathfinding();

        while (comm.readToRobot("Stop").equals("0")) {
            movement.move(Movement.Direction.FORWARD);
        }
        movement.move(Movement.Direction.STOP);

        // loop...
        // import toRobot.json
        // store toRobot.json vars
        // if toRobot manualDrive...
        // driveControl

        // if not toRobot manualDrive...
        // pathfind and driveControl

        // send toInterface.json
        // clear toRobot.json accessed var
        // end loop...

        Logger.info("Fetch Bot terminating!");

        comm.resetToRobot();
        comm.resetToInterface();
    }
}

package io.github.cshadd.fetch_bot;
import io.github.cshadd.fetch_bot.util.Logger;
import io.github.cshadd.fetch_bot.util.Communication;
// Main
public class Core
implements FetchBot {
    // Private Static Instance/Property Fields
    private static Communication comm;

    // Private Static Methods
    private static void delayThread(long millis) {
        try {
            Thread.sleep(millis);
        }
        catch (InterruptedException e) {
            // Thread.currentThread().interrupt();
            Logger.warn(e, "Thread was interrupted.");
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

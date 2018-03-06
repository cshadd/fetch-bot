package io.github.cshadd.fetch_bot;
// import java.io.*;
// import org.apache.commons.io.*;

// Main
public class Core
implements FetchBot {
    // Private Static Instance/Property Fields
    private static Communication comm;
    private static Pathfinding path;

    // Entry Point
    public static void main(String[] args)
    throws InterruptedException {
        comm = new Communication();

        // path = new Pathfinding();

        Logger.clear();
        comm.resetToRobot();
        comm.resetToInterface();

        Logger.info("Fetch Bot starting!");
            while (comm.readToRobot("Stop").equals("0")) {
                Thread.sleep(1000);
                Logger.info("Stepped!");
            }
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

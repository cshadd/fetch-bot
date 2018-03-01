package io.github.cshadd.fetch_bot;
// import java.io.*;
// import org.apache.commons.io.*;

// Main
public class Core
implements FetchBot {
    // Private Final Static Instance/Property Fields
    private static Communication comm;
    private static Pathfinding path;

    // Entry Point
    public static void main(String[] args) {
        comm = new Communication();
        path = new Pathfinding();

        Logger.clear();
        comm.clearToInterface();
        comm.clearToRobot();
        comm.resetToRobot();
        comm.resetToInterface();

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
            try {
                int a = 1/0;
            }
            catch (Exception e) {
                Logger.error(e + "\nwtf");
            }
        Logger.info("Fetch Bot terminating!");

        comm.clearToInterface();
        comm.clearToRobot();
    }
}

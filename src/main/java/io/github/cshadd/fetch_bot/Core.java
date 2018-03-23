package io.github.cshadd.fetch_bot;
import io.github.cshadd.fetch_bot.util.Logger;
import io.github.cshadd.fetch_bot.util.Communication;
// Main
public class Core
implements FetchBot {
    // Private Static Instance/Property Fields
    private static Communication comm;
    private static Logger log;

    // Private Static Methods
    private static void delayThread(long millis) {
        try {
            Thread.sleep(millis);
        }
        catch (InterruptedException e) {
            log.warn(e, "Thread was interrupted.");
        }
        catch (Exception e) {
            log.error(e, "There was an unknown issue!");
        }
    }

    // Entry Point
    public static void main(String[] args) {
        String auto = "0";
        String isAuto = "0";

        comm = new Communication();
        log = Logger.getInstance(comm);

        comm.reset();
        log.clear();
        log.info("Fetch Bot starting!");
        log.info("Product information at https://cshadd.github.io/fetch-bot/.");

        comm.pushInterface();
        comm.pushRobot();

        while (true) {
            comm.pullInterface();
            comm.pullRobot();

            if (comm.getRobotValue("kill").equals("1")) {
                log.info("Interface - [Kill] command received.");
                break;
            }

            auto = comm.getRobotValue("auto");

            if (auto.equals("0")) { // Manual
                if (!auto.equals(isAuto)) {
                    log.info("Interface - [Manual] mode command received.");
                }
                // driveControl
            }
            else if (auto.equals("1")) { // AI
                if (!auto.equals(isAuto)) {
                    log.info("Interface - [Auto] mode command received.");
                }
                // pathfind and driveControl
            }
            else {
                log.warn("Interface - Invalid mode command recieved.");
            }

            isAuto = auto;

            delayThread(1000);
            comm.pushInterface();
        }

        log.info("Fetch Bot terminating!");

        comm.clear();
        comm.pushInterface();
        comm.pushRobot();
    }
}

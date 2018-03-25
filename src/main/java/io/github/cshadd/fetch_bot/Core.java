package io.github.cshadd.fetch_bot;
import io.github.cshadd.fetch_bot.util.Logger;
import io.github.cshadd.fetch_bot.util.Communication;
// Main
public class Core
implements FetchBot {
    // Private Constant Instance/Property Fields
    private static final int COLLISION_DISTANCE = 15;

    // Private Static Instance/Property Fields
    private static Communication comm;
    private static Movement movement;
    private static Logger log;
    private static Sensor sensor;

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
        boolean isDirectionFrontBlocked = false;
        boolean isDirectionLeftBlocked = false;
        boolean isDirectionRightBlocked = false;
        int sensorFront = 0;
        int sensorLeft = 0;
        int sensorRight = 0;

        comm = new Communication();
        movement = new Movement();
        log = Logger.getInstance(comm);
        sensor = new Sensor();

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

            try {
                sensorFront = sensor.getDistance(sensor.DIRECTION.FRONT);
                sensorLeft = sensor.getDistance(sensor.DIRECTION.LEFT);
                sensorRight = sensor.getDistance(sensor.DIRECTION.RIGHT);
            }
            catch (SensorException e) {
                Logger.error(e, "There was an issue with Sensor!");
            }
            catch (Exception e) {
                Logger.error(e, "There was an unknown issue!");
            }
            finally {
                comm.setInterfaceValue("sensor-front", sensorFront);
                comm.setInterfaceValue("sensor-left", sensorLeft);
                comm.setInterfaceValue("sensor-right", sensorRight);

                if (sensorFront == COLLISION_DISTANCE) {
                    if (!isDirectionFrontBlocked) {
                        Logger.info("Sensor - [Front] is blocked.");
                    }
                    isDirectionFrontBlocked = true;
                }
                else if (sensorFront < COLLISION_DISTANCE) {
                    if (!isDirectionFrontBlocked) {
                        Logger.warn("Sensor - [Front] imminent collision.");
                    }
                    isDirectionFrontBlocked = true;
                }
                else {
                    if (!isDirectionFrontBlocked) {
                        Logger.info("Sensor - [Front] is clear.");
                    }
                    isDirectionFrontBlocked = false;
                }

                if (sensorLeft == COLLISION_DISTANCE) {
                    if (!isDirectionLeftBlocked) {
                        Logger.info("Sensor - [Left] is blocked.");
                    }
                    isDirectionLeftBlocked = true;
                }
                else if (sensorLeft < COLLISION_DISTANCE) {
                    if (!isDirectionLeftBlocked) {
                        Logger.warn("Sensor - [Left] imminent collision.");
                    }
                    isDirectionLeftBlocked = true;
                }
                else {
                    if (!isDirectionLeftBlocked) {
                        Logger.info("Sensor - [Left] is clear.");
                    }
                    isDirectionLeftBlocked = false;
                }

                if (sensorRight == COLLISION_DISTANCE) {
                    if (!isDirectionRightBlocked) {
                        Logger.info("Sensor - [Right] is blocked.");
                    }
                    isDirectionRightBlocked = true;
                }
                else if (sensorRight < COLLISION_DISTANCE) {
                    if (!isDirectionRightBlocked) {
                        Logger.warn("Sensor - [Right] imminent collision.");
                    }
                    isDirectionRightBlocked = true;
                }
                else {
                    if (!isDirectionRightBlocked) {
                        Logger.info("Sensor - [Right] is clear.");
                    }
                    isDirectionRightBlocked = false;
                }
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
                if (!auto.equals(isAuto)) {
                    log.warn("Interface - Invalid mode command recieved.");
                }
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

package io.github.cshadd.fetch_bot;
import io.github.cshadd.fetch_bot.util.Logger;
import io.github.cshadd.fetch_bot.util.Communication;

import com.bb8log.raspberrypi.adafruitmotorhat.AdafruitMotorHat;
import com.bb8log.raspberrypi.adafruitmotorhat.exception.MotorException;
import com.bb8log.raspberrypi.adafruitmotorhat.motor.MotorCommand;
import com.bb8log.raspberrypi.adafruitmotorhat.motor.stepper.StepperMotor;
import com.bb8log.raspberrypi.adafruitmotorhat.motor.stepper.StepStyle;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CFactory;
import com.pi4j.io.i2c.I2CFactory.UnsupportedBusNumberException;

import java.io.IOException;

// Main
public class Core
implements FetchBot {
    // Private Static Instance/Property Fields
    private static Communication comm;
    private static AdafruitMotorHat hat;
    private static I2CBus i2CBus;
    private static boolean paused;
    private static StepperMotor stepperL;
    private static StepperMotor stepperR;

    // Private Static Nested Classes
    private static class MotorTester extends Thread {
        // Private Instance/Property Fields
        StepperMotor stepper;

        // Public Constructors
        public MotorTester() {
            this(null);
        }
        public MotorTester(StepperMotor stepper) {
            this.stepper = stepper;
        }

        // Public Methods
        public void run() {
            try {
                stepper.setSpeed(60);
                stepper.doSteps(200, MotorCommand.FORWARD, StepStyle.SINGLE);
                delayThread(2000);
                stepper.setSpeed(0);
            }
            catch (InterruptedException e) {
                // Thread.currentThread().interrupt();
                Logger.warn(e, "Thread was interrupted.");
            }
            catch (Exception e) {
                Logger.error(e, "There was an unknown issue!");
            }
            finally { }
        }
    }
    private static class r extends Thread {
        // Public Constructors
        public r() { }

        // Public Methods
        public void run() {
            try {
                delayThread(5000);
                paused = true;
            }
            catch (Exception e) {
                Logger.error(e, "There was an unknown issue!");
            }
            finally { }
        }
    }

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
    public static void main(String[] args)
    throws MotorException, IOException, UnsupportedBusNumberException {
        paused = false;
        comm = new Communication();
        comm.resetToRobot();
        comm.resetToInterface();
        Logger.clear();
        Logger.info("Fetch Bot starting!");
        // (new r()).start();
        // while (!paused) {
            // delayThread(1000);
        // }
        Logger.info("Getting i2c.");
        i2CBus = I2CFactory.getInstance(1);

        Logger.info("Getting AdafruitMotorHat");
        hat = new AdafruitMotorHat(i2CBus);

        stepperL = hat.getStepper(0);
        stepperR = hat.getStepper(1);
        (new MotorTester(stepperL)).start();
        (new MotorTester(stepperR)).start();

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

        while (!paused) { }

        Logger.info("Fetch Bot terminating!");

        comm.resetToRobot();
        comm.resetToInterface();
    }
}

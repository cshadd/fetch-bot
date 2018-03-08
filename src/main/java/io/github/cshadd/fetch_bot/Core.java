package io.github.cshadd.fetch_bot;
import io.github.cshadd.fetch_bot.util.Logger;
import io.github.cshadd.fetch_bot.util.Communication;

import com.bb8log.raspberrypi.adafruitmotorhat.AdafruitMotorHat;
import com.bb8log.raspberrypi.adafruitmotorhat.exception.MotorException;
import com.bb8log.raspberrypi.adafruitmotorhat.motor.MotorCommand;
import com.bb8log.raspberrypi.adafruitmotorhat.motor.stepper.StepStyle;
import com.bb8log.raspberrypi.adafruitmotorhat.motor.stepper.StepperMotor;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CFactory;
import com.pi4j.io.i2c.I2CFactory.UnsupportedBusNumberException;
import java.io.IOException;

// Main
public class Core
implements FetchBot {
    // Private Static Instance/Property Fields
    private static Communication comm;
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
    public static void main(String[] args)
    throws IOException, MotorException, UnsupportedBusNumberException {
        comm = new Communication();
        comm.resetToRobot();
        comm.resetToInterface();
        Logger.clear();
        Logger.info("Fetch Bot starting!");

        I2CBus i2CBus = I2CFactory.getInstance(1);
        AdafruitMotorHat hat = new AdafruitMotorHat(i2CBus);
        StepperMotor dcMotor = hat.getStepper(1);
        dcMotor.setSpeed(100);

        while (comm.readToRobot("Stop").equals("0")) {
            delay(2000);
            Logger.info("Stepped");
            dcMotor.doOneStep(MotorCommand.FORWARD, StepStyle.SINGLE);
        }

        dcMotor.setSpeed(0);

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

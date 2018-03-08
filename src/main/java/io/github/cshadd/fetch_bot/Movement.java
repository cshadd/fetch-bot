package io.github.cshadd.fetch_bot;
import com.pi4j.io.i2c.I2CFactory;
import io.github.cshadd.fetch_bot.util.adafruit.MotorHAT;
import io.github.cshadd.fetch_bot.util.Logger;
import java.io.IOException;

// Adopted from: https://github.com/OlivierLD/raspberry-pi4j-samples
// Main
public class Movement
implements FetchBot {
    // Private Constant Instance/Property Fields
    private static final String DEFAULT_RPM = "30";

    // Private Static Instance/Property Fields
    private static int nbStepsPerRev = MotorHAT.AdafruitStepperMotor.DEFAULT_NB_STEPS; // 200 steps per rev

    // Private Instance/Property Fields
    private MotorHAT mh;
    private int nbSteps = 100;
    private MotorHAT.AdafruitStepperMotor stepperL;
    private MotorHAT.AdafruitStepperMotor stepperR;

    // Public Instance/Property Fields
    public enum Direction {
        BACK, FORWARD, LEFT, RIGHT, STOP
    };

    // Public Constructors
    public Movement() {
        int rpm = Integer.parseInt(System.getProperty("rpm", DEFAULT_RPM));
        Logger.info("Movement - RPM set to " + rpm + ".");
        nbSteps = Integer.parseInt(System.getProperty("steps", "100"));

        try {
            mh = new MotorHAT(nbStepsPerRev); // Default addr
            Logger.info("Movement - Accessing Motor L.");
            stepperL = mh.getStepper(MotorHAT.AdafruitStepperMotor.PORT_M1_M2);
            stepperL.setSpeed(rpm); // Default 30 RPM

            Logger.info("Movement - Accessing Motor R.");
            stepperR = mh.getStepper(MotorHAT.AdafruitStepperMotor.PORT_M3_M4);
            stepperR.setSpeed(rpm); // Default 30 RPM
        }
        catch (I2CFactory.UnsupportedBusNumberException e) {
            Logger.error(e + "\nThere was an issue with I2CFactory!");
        }
        catch (Exception e) {
            Logger.error(e + "\nThere was an unknown issue!");
        }
    }

    // Private Methods
    private void moveCommand(int nbSteps, MotorHAT.ServoCommand command) {
        try { // Release all
            Logger.info("Movement - " + command + ".");
            stepperL.step(nbSteps, command, MotorHAT.Style.SINGLE);
            stepperR.step(nbSteps, command, MotorHAT.Style.SINGLE);
        }
        catch (IOException e) {
            Logger.error(e + "\nThere was an issue with IO!");
        }
        catch (Exception e) {
            Logger.error(e + "\nThere was an unknown issue!");
        }
    }

    // Public Methods
    public void move(Direction direction) {
        if (direction == Direction.FORWARD) {
            moveCommand(nbSteps, MotorHAT.ServoCommand.FORWARD);
        }
        else if (direction == Direction.STOP) {
            if (mh != null) {
                try { // Release all
                    Logger.info("Movement - Stopping.");
                    mh.getMotor(MotorHAT.Motor.M1).run(MotorHAT.ServoCommand.RELEASE);
                    mh.getMotor(MotorHAT.Motor.M2).run(MotorHAT.ServoCommand.RELEASE);
                    mh.getMotor(MotorHAT.Motor.M3).run(MotorHAT.ServoCommand.RELEASE);
                    mh.getMotor(MotorHAT.Motor.M4).run(MotorHAT.ServoCommand.RELEASE);
                }
                catch (IOException e) {
                    Logger.error(e + "\nThere was an issue with IO!");
    			}
                catch (Exception e) {
                    Logger.error(e + "\nThere was an unknown issue!");
                }
            }
        }
    }
}

package io.github.cshadd.fetch_bot;
import static io.github.cshadd.fetch_bot.Core.delay;
import com.pi4j.io.i2c.I2CFactory;
import io.github.cshadd.fetch_bot.util.adafruit.MotorHAT;
import io.github.cshadd.fetch_bot.util.Communication;
import io.github.cshadd.fetch_bot.util.Logger;
import java.io.IOException;

// Adopted from: https://github.com/OlivierLD/raspberry-pi4j-samples
// Main
public class Movement
implements FetchBot {
    // Private Constant Instance/Property Fields
    private static final String DEFAULT_RPM = "30";

    // Private Static Instance/Property Fields
    private static Communication comm = new Communication();
    private static int nbStepsPerRev = MotorHAT.AdafruitStepperMotor.DEFAULT_NB_STEPS; // 200 steps per rev

    // Private Instance/Property Fields
    private MotorHAT mh;
    private int nbSteps = 100;
    private MotorHAT.AdafruitStepperMotor stepper;

    // Private Constructors
    private Movement() {
        this(0);
    }

    // Public Constructors
    public Movement(int motorNumber) {
        final int rpm = Integer.parseInt(System.getProperty("rpm", DEFAULT_RPM));
        Logger.info("Movement - RPM set to " + rpm + ".");
        nbSteps = Integer.parseInt(System.getProperty("steps", "100"));

        try {
            mh = new MotorHAT(nbStepsPerRev); // Default addr
            stepper = mh.getStepper(motorNumber);
            stepper.setSpeed(rpm);
        }
        catch (I2CFactory.UnsupportedBusNumberException e) {
            Logger.error(e, "There was an issue with I2CFactory!");
        }
        catch (Exception e) {
            Logger.error(e, "There was an unknown issue!");
        }
    }

    // Public Methods
    public void move() {
        final Thread moveThread = new Thread(() -> {
            while (comm.readToRobot("Stop").equals("0")) {
                try {
                    stepper.step(nbSteps, MotorHAT.ServoCommand.FORWARD, MotorHAT.Style.SINGLE);
                }
                catch (IOException e) {
                    Logger.error(e, "There was an issue with IO!");
                }
                catch (Exception e) {
                    Logger.error(e, "There was an unknown issue!");
                }
            }
        });

        moveThread.start();
    }
    public void stop() {
        if (mh != null) {
            try { // Release all
                mh.getMotor(MotorHAT.Motor.M1).run(MotorHAT.ServoCommand.RELEASE);
                mh.getMotor(MotorHAT.Motor.M2).run(MotorHAT.ServoCommand.RELEASE);
                mh.getMotor(MotorHAT.Motor.M3).run(MotorHAT.ServoCommand.RELEASE);
                mh.getMotor(MotorHAT.Motor.M4).run(MotorHAT.ServoCommand.RELEASE);
            }
            catch (IOException e) {
                Logger.error(e, "There was an issue with IO!");
            }
            catch (Exception e) {
                Logger.error(e, "There was an unknown issue!");
            }
        }
    }
}

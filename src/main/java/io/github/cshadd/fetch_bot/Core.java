package io.github.cshadd.fetch_bot;
import java.io.*;
// import org.apache.commons.io.*;

import com.pi4j.io.i2c.I2CFactory;
import io.github.cshadd.fetch_bot.utils.adafruit_stepper_motor.AdafruitMotorHAT;

// Main
public class Core
implements FetchBot {

    private AdafruitMotorHAT mh;
    	private AdafruitMotorHAT.AdafruitStepperMotor stepper;

    	private boolean keepGoing = true;
    	private final static String DEFAULT_RPM = "1000";
    	int nbSteps = 200;

    	private static int nbStepsPerRev = AdafruitMotorHAT.AdafruitStepperMotor.DEFAULT_NB_STEPS; // 200 steps per rev

    	public Core() throws I2CFactory.UnsupportedBusNumberException {

    		System.out.println("Starting Stepper Demo");
    		int rpm = Integer.parseInt(System.getProperty("rpm", DEFAULT_RPM));
    		System.out.println(String.format("RPM set to %d.", rpm));

    		nbSteps = Integer.parseInt(System.getProperty("steps", "100"));

    		this.mh = new AdafruitMotorHAT(nbStepsPerRev); // Default addr 0x60
    		this.stepper = mh.getStepper(AdafruitMotorHAT.AdafruitStepperMotor.PORT_M1_M2);
    		this.stepper.setSpeed(rpm); // Default 30 RPM
    	}

    	public void go() {
    		keepGoing = true;
    		while (keepGoing) {
    			try {
    				System.out.println(String.format(
    						"-----------------------------------------------------------------------------------\n" +
    						"Motor # %d, RPM set to %f, %d Steps per Rev, %f sec per step, %d steps per move.\n" +
    						"-----------------------------------------------------------------------------------",
    						this.stepper.getMotorNum(),
    						this.stepper.getRPM(),
    						this.stepper.getStepPerRev(),
    						this.stepper.getSecPerStep(),
    						nbSteps));
    				if (keepGoing) {
    					System.out.println("-- 1. Single coil steps --");
    					System.out.println("  Forward");
    					this.stepper.step(nbSteps, AdafruitMotorHAT.ServoCommand.FORWARD, AdafruitMotorHAT.Style.SINGLE);
    				}
    				if (keepGoing) {
    					System.out.println("  Backward");
    					this.stepper.step(nbSteps, AdafruitMotorHAT.ServoCommand.BACKWARD, AdafruitMotorHAT.Style.SINGLE);
    				}
    				if (keepGoing) {
    					System.out.println("-- 2. Double coil steps --");
    					System.out.println("  Forward");
    					this.stepper.step(nbSteps, AdafruitMotorHAT.ServoCommand.FORWARD, AdafruitMotorHAT.Style.DOUBLE);
    				}
    				if (keepGoing) {
    					System.out.println("  Backward");
    					this.stepper.step(nbSteps, AdafruitMotorHAT.ServoCommand.BACKWARD, AdafruitMotorHAT.Style.DOUBLE);
    				}
    				if (keepGoing) {
    					System.out.println("-- 3. Interleaved coil steps --");
    					System.out.println("  Forward");
    					this.stepper.step(nbSteps, AdafruitMotorHAT.ServoCommand.FORWARD, AdafruitMotorHAT.Style.INTERLEAVE);
    				}
    				if (keepGoing) {
    					System.out.println("  Backward");
    					this.stepper.step(nbSteps, AdafruitMotorHAT.ServoCommand.BACKWARD, AdafruitMotorHAT.Style.INTERLEAVE);
    				}
    				if (keepGoing) {
    					System.out.println("-- 4. Microsteps --");
    					System.out.println("  Forward");
    					this.stepper.step(nbSteps, AdafruitMotorHAT.ServoCommand.FORWARD, AdafruitMotorHAT.Style.MICROSTEP);
    				}
    				if (keepGoing) {
    					System.out.println("  Backward");
    					this.stepper.step(nbSteps, AdafruitMotorHAT.ServoCommand.BACKWARD, AdafruitMotorHAT.Style.MICROSTEP);
    				}
    			} catch (IOException ioe) {
    				ioe.printStackTrace();
    			}
    			System.out.println("==== Again! ====");
    		}
    		System.out.println("... Done with the demo ...");
    //	try { Thread.sleep(1_000); } catch (Exception ex) {} // Wait for the motors to be released.
    	}

    	public void stop() {
    		this.keepGoing = false;
    		if (mh != null) {
    			try { // Release all
    				mh.getMotor(AdafruitMotorHAT.Motor.M1).run(AdafruitMotorHAT.ServoCommand.RELEASE);
    				mh.getMotor(AdafruitMotorHAT.Motor.M2).run(AdafruitMotorHAT.ServoCommand.RELEASE);
    				mh.getMotor(AdafruitMotorHAT.Motor.M3).run(AdafruitMotorHAT.ServoCommand.RELEASE);
    				mh.getMotor(AdafruitMotorHAT.Motor.M4).run(AdafruitMotorHAT.ServoCommand.RELEASE);
    			} catch (IOException ioe) {
    				ioe.printStackTrace();
    			}
    		}
    	}

    	/**
    	 * System properties:
    	 * rpm, default 30
    	 * hat.debug, default false
    	 *
    	 * @param args Not used
    	 * @throws Exception
    	 */
    	public static void main(String... args) throws Exception {
    		Core demo = new Core();
    		System.out.println("Hit Ctrl-C to stop the demo");
    		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
    			demo.stop();
    			try { Thread.sleep(5_000); } catch (Exception absorbed) {}
    		}));

    		demo.go();

    		System.out.println("Bye.");
    }





    // Private Static Instance/Property Fields
    /*private static Communication comm;
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
    }*/
}

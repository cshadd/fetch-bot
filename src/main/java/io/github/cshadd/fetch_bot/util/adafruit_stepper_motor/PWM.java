package io.github.cshadd.fetch_bot.util.adafruit_stepper_motor;
import static io.github.cshadd.fetch_bot.util.TimeUtil.delay;
import io.github.cshadd.fetch_bot.util.Logger;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import java.io.IOException;

public class PWM {
	// Private Constant Instance/Property Fields
    private static final int ALLCALL = 0x01;
	private static final int ALL_LED_OFF_H = 0xFD;
	private static final int ALL_LED_OFF_L = 0xFC;
    private static final int ALL_LED_ON_H = 0xFB;
    private static final int ALL_LED_ON_L = 0xFA;
    private static final int INVRT   = 0x10;
	private static final int LED0_OFF_H = 0x09;
    private static final int LED0_OFF_L = 0x08;
    private static final int LED0_ON_H = 0x07;
    private static final int LED0_ON_L = 0x06;
    private static final int MODE1 = 0x00;
    private static final int MODE2 = 0x01;
    private static final int OUTDRV  = 0x04;
	private static final int PRESCALE = 0xFE;
    private static final int RESTART = 0x80;
    private static final int SERVO_ADDRESS = 0x40;
    private static final int SLEEP   = 0x10;
	private static final int SUBADR1 = 0x02;
    private static final int SUBADR2 = 0x03;
    private static final int SUBADR3 = 0x04;

	// Private Instance/Property Fields
	private int deviceAddr;
    private I2CBus bus;
    private I2CDevice servoDriver;
    private boolean verbose = "true".equals(System.getProperty("hat.debug", "false"));
// Check

    public PWM()
	throws I2CFactory.UnsupportedBusNumberException {
        this(SERVO_ADDRESS);
    }

    public PWM(int address)
	throws I2CFactory.UnsupportedBusNumberException {
        this.deviceAddr = address;
        try {
            // Get I2C bus
            bus = I2CFactory.getInstance(I2CBus.BUS_1); // Depends on the RasPI version
			Logger.info("Connected to bus ok.");
            servoDriver = bus.getDevice(address); // Get device itself
            Logger.info("Connected to device ok.");
        }
        catch (IOException e) {
            Logger.error(e + "\nThere was an issue with IO!");
        }
        catch (Exception e) {
            Logger.error(e + "\nThere was an unknown issue!");
        }

        try {
            Logger.info("PMW - Resetting Adafruit HAT MODE1 (no SLEEP) and MODE2.");
            this.setAllPWM((byte)0, (byte)0);
            Logger.info("PMW - Write 0x" + OUTDRV + " to 0x" + MODE2);
            this.servoDriver.write(MODE2, (byte)OUTDRV);

            Logger.info("PMW - Write 0x" + ALLCALL + " to 0x" + MODE1);
            this.servoDriver.write(MODE1, (byte)ALLCALL);
            delay(5); // Wait for oscillator

            int mode1 = this.servoDriver.read(MODE1);
            Logger.info("PMW - Device 0x" + deviceAddr + " returned 0x" + model + " from 0x" + MODE1);

            mode1 = mode1 & ~SLEEP; // Wake up (reset sleep)
            Logger.info("PMW - Write 0x" + mode1 + " to 0x" + MODE1);
            this.servoDriver.write(MODE1, (byte)mode1);
            delay(5); // Wait for oscillator
        }
        catch (IOException e) {
            Logger.error(e + "\nThere was an issue with IO!");
        }
        catch (Exception e) {
            Logger.error(e + "\nThere was an unknown issue!");
        }
    }

    public void setAllPWM(byte on, byte off) throws IOException {
        // Sets a all PWM channels
        if (verbose) {
            System.out.printf("05 - Writing 0x%02x to register 0x%02x\n", (on & 0xFF), ALL_LED_ON_L);
            System.out.printf("06 - Writing 0x%02x to register 0x%02x\n", (on >> 8), ALL_LED_ON_H);
            System.out.printf("07 - Writing 0x%02x to register 0x%02x\n", (off & 0xFF), ALL_LED_OFF_L);
            System.out.printf("08 - Writing 0x%02x to register 0x%02x\n", (off >> 8), ALL_LED_OFF_H);
        }
        this.servoDriver.write(ALL_LED_ON_L, (byte) (on & 0xFF));
        this.servoDriver.write(ALL_LED_ON_H, (byte) (on >> 8));
        this.servoDriver.write(ALL_LED_OFF_L, (byte) (off & 0xFF));
        this.servoDriver.write(ALL_LED_OFF_H, (byte) (off >> 8));
    }

    public void setPWM(int channel, short on, short off) throws IOException {
        // Sets a single PWM channel
        if (verbose) {
            System.out.printf("ON:0x%02x, OFF:0x%02x\n", on, off);
            System.out.printf("09 - Writing 0x%02x to register 0x%02x\n", (on & 0xFF), LED0_ON_L + 4 * channel);
            System.out.printf("10 - Writing 0x%02x to register 0x%02x\n", (on >> 8) & 0xFF, LED0_ON_H + 4 * channel);
            System.out.printf("11 - Writing 0x%02x to register 0x%02x\n", (off & 0xFF), LED0_OFF_L + 4 * channel);
            System.out.printf("12 - Writing 0x%02x to register 0x%02x\n", (off >> 8) & 0xFF, LED0_OFF_H + 4 * channel);
        }
        this.servoDriver.write(LED0_ON_L + 4 * channel, (byte) (on & 0xFF));
        this.servoDriver.write(LED0_ON_H + 4 * channel, (byte) ((on >> 8) & 0xFF));
        this.servoDriver.write(LED0_OFF_L + 4 * channel, (byte) (off & 0xFF));
        this.servoDriver.write(LED0_OFF_H + 4 * channel, (byte) ((off >> 8) & 0xFF));
    }

    public void setPWMFreq(int freq) throws IOException {
        // Sets the PWM frequency
        double preScaleVal = 25_000_000.0; // 25MHz
        preScaleVal /= 4096.0; // 12-bit
        preScaleVal /= (float) freq;
        preScaleVal -= 1.0;
        if (verbose) {
            System.out.println("Setting PWM frequency to " + freq + " Hz");
            System.out.println("Estimated pre-scale:" + preScaleVal);
        }
        double preScale = Math.floor(preScaleVal + 0.5);
        if (verbose) {
            System.out.println("Final pre-scale: " + preScale);
        }
        int oldMode = this.servoDriver.read(MODE1);
        byte newMode = (byte) ((oldMode & 0x7F) | 0x10); // sleep
        if (verbose) {
            System.out.printf("13 - Writing 0x%02x to register 0x%02x\n", newMode, MODE1);
            System.out.printf("14 - Writing 0x%02x to register 0x%02x\n", (byte) (Math.floor(preScale)), PRESCALE);
            System.out.printf("15 - Writing 0x%02x to register 0x%02x\n", oldMode, MODE1);
        }
        this.servoDriver.write(MODE1, newMode); // go to sleep
        this.servoDriver.write(PRESCALE, (byte) (Math.floor(preScale)));
        this.servoDriver.write(MODE1, (byte) oldMode);
        delay(5);
        if (verbose) {
            System.out.printf("16 - Writing 0x%02x to register 0x%02x\n", (oldMode | 0x80), MODE1);
        }
        this.servoDriver.write(MODE1, (byte) (oldMode | 0x80));
    }
}

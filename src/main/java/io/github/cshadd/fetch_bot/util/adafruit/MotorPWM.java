package io.github.cshadd.fetch_bot.util.adafruit;
import static io.github.cshadd.fetch_bot.Core.delay;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import io.github.cshadd.fetch_bot.util.Logger;
import java.io.IOException;

// Adopted from: https://github.com/OlivierLD/raspberry-pi4j-samples
// Main
public class MotorPWM
implements Adafruit {
    // Private Constant Instance/Property Fields
    private static final int ALLCALL = 0x01;
    private static final int ALL_LED_OFF_H = 0xFD;
    private static final int ALL_LED_OFF_L = 0xFC;
    private static final int ALL_LED_ON_H = 0xFB;
    private static final int ALL_LED_ON_L = 0xFA;
    private static final int INVRT = 0x10;
    private static final int LED0_OFF_H = 0x09;
    private static final int LED0_OFF_L = 0x08;
    private static final int LED0_ON_H = 0x07;
    private static final int LED0_ON_L = 0x06;
    private static final int MODE1 = 0x00;
    private static final int MODE2 = 0x01;
    private static final int OUTDRV = 0x04;
    private static final int PRESCALE = 0xFE;
    private static final int RESTART = 0x80;
    private static final int SERVO_ADDRESS = 0x40;
    private static final int SLEEP = 0x10;
    private static final int SUBADR1 = 0x02;
    private static final int SUBADR2 = 0x03;
    private static final int SUBADR3 = 0x04;

    // Private Instance/Property Fields
    private int deviceAddr;
    private I2CBus bus;
    private I2CDevice servoDriver;

    // Public Constructors
    public MotorPWM()
    throws I2CFactory.UnsupportedBusNumberException {
        this(SERVO_ADDRESS);
    }
    public MotorPWM(int address)
    throws I2CFactory.UnsupportedBusNumberException {
        deviceAddr = address;

        try {
            // Get I2C bus
            bus = I2CFactory.getInstance(I2CBus.BUS_1); // Depends on the RasPI version
            Logger.info("MotorPWM - Connected to bus ok.");
            servoDriver = bus.getDevice(address); // Get device itself
            Logger.info("MotorPWM - Connected to device ok.");
        }
        catch (IOException e) {
            Logger.error(e + "\nThere was an issue with IO!");
        }
        catch (Exception e) {
            Logger.error(e + "\nThere was an unknown issue!");
        }

        try {
            Logger.info("MotorPWM - Resetting Adafruit HAT MODE1 (no SLEEP) and MODE2.");
            setAllMotorPWM((byte)0, (byte)0);
            Logger.info("MotorPWM - Write 0x" + Integer.toHexString(OUTDRV) + " to 0x" + Integer.toHexString(MODE2) + ".");
            servoDriver.write(MODE2, (byte)OUTDRV);

            Logger.info("MotorPWM - Write 0x" + Integer.toHexString(ALLCALL) + " to 0x" + Integer.toHexString(MODE1) + ".");
            servoDriver.write(MODE1, (byte)ALLCALL);
            delay(5); // Wait for oscillator

            int mode1 = servoDriver.read(MODE1);
            Logger.info("MotorPWM - Device 0x" + Integer.toHexString(deviceAddr) + " returned 0x" + Integer.toHexString(mode1) + " from 0x" + Integer.toHexString(MODE1) + ".");

            mode1 = mode1 & ~SLEEP; // Wake up (Reset sleep)
            Logger.info("MotorPWM - Write 0x" + Integer.toHexString(mode1) + " to 0x" + Integer.toHexString(MODE1) + ".");
            servoDriver.write(MODE1, (byte)mode1);
            delay(5); // Wait for oscillator
        }
        catch (IOException e) {
            Logger.error(e + "\nThere was an issue with IO!");
        }
        catch (Exception e) {
            Logger.error(e + "\nThere was an unknown issue!");
        }
    }

    // Public Methods
    public void setAllMotorPWM(byte on, byte off)
    throws IOException {
        // Sets a all MotorPWM channels
        Logger.info("MotorPWM - Write 0x" + Integer.toHexString((on & 0xFF)) + " to 0x" + Integer.toHexString(ALL_LED_ON_L) + ".");
        servoDriver.write(ALL_LED_ON_L, (byte)(on & 0xFF));
        Logger.info("MotorPWM - Write 0x" + Integer.toHexString((on >> 8)) + " to 0x" + Integer.toHexString(ALL_LED_ON_H) + ".");
        servoDriver.write(ALL_LED_ON_H, (byte)(on >> 8));
        Logger.info("MotorPWM - Write 0x" + Integer.toHexString((off & 0xFF)) + " to 0x" + Integer.toHexString(ALL_LED_OFF_L) + ".");
        servoDriver.write(ALL_LED_OFF_L, (byte)(off & 0xFF));
        Logger.info("MotorPWM - Write 0x" + Integer.toHexString((off >> 8)) + " to 0x" + Integer.toHexString(ALL_LED_OFF_H) + ".");
        servoDriver.write(ALL_LED_OFF_H, (byte)(off >> 8));
    }
    public void setMotorPWM(int channel, short on, short off)
    throws IOException {
        // Sets a single MotorPWM channel
        Logger.info("MotorPWM - On: 0x" + on + "; Off: 0x" + off);

        Logger.info("MotorPWM - Write 0x" + Integer.toHexString((on & 0xFF)) + " to 0x" + Integer.toHexString(LED0_ON_L + 4*channel) + ".");
        servoDriver.write(LED0_ON_L + 4*channel, (byte)(on & 0xFF));
        Logger.info("MotorPWM - Write 0x" + Integer.toHexString((on >> 8) & 0xFF) + " to 0x" + Integer.toHexString(LED0_ON_H + 4*channel) + ".");
        servoDriver.write(LED0_ON_H + 4*channel, (byte)((on >> 8) & 0xFF));
        Logger.info("MotorPWM - Write 0x" + Integer.toHexString((off & 0xFF)) + " to 0x" + Integer.toHexString(LED0_OFF_L + 4*channel) + ".");
        servoDriver.write(LED0_OFF_L + 4*channel, (byte)(off & 0xFF));
        Logger.info("MotorPWM - Write 0x" + Integer.toHexString((off >> 8) & 0xFF) + " to 0x" + Integer.toHexString(LED0_OFF_H + 4*channel) + ".");
        servoDriver.write(LED0_OFF_H + 4*channel, (byte)((off >> 8) & 0xFF));
    }
    public void setMotorPWMFreq(int freq)
    throws IOException {
        // Sets the MotorPWM frequency
        double preScaleVal = 25_000_000.0; // 25MHz
        preScaleVal /= 4096.0; // 12-bit
        preScaleVal /= (float)freq;
        preScaleVal -= 1.0;
        Logger.info("MotorPWM - Setting MotorPWM frequency to " + freq + " Hz.");
        Logger.info("MotorPWM - Estimated pre-scale: " + preScaleVal + ".");
        final double preScale = Math.floor(preScaleVal + 0.5);
        Logger.info("MotorPWM - Final pre-scale: " + preScale + ".");
        int oldMode = servoDriver.read(MODE1);
        byte newMode = (byte)((oldMode & 0x7F) | 0x10); // WSleep

        Logger.info("MotorPWM - Write 0x" + Integer.toHexString(newMode) + " to 0x" + Integer.toHexString(MODE1) + ".");
        servoDriver.write(MODE1, newMode); // Go to sleep
        Logger.info("MotorPWM - Write 0x" + Integer.toHexString((byte)(Math.floor(preScale))) + " to 0x" + Integer.toHexString(PRESCALE) + ".");
        servoDriver.write(PRESCALE, (byte)(Math.floor(preScale)));
        Logger.info("MotorPWM - Write 0x" + Integer.toHexString(oldMode) + " to 0x" + Integer.toHexString(MODE1) + ".");
        servoDriver.write(MODE1, (byte)oldMode);
        delay(5);

        Logger.info("MotorPWM - Write 0x" + Integer.toHexString((oldMode | 0x80)) + " to 0x" + Integer.toHexString(MODE1) + ".");
        servoDriver.write(MODE1, (byte) (oldMode | 0x80));
    }
}

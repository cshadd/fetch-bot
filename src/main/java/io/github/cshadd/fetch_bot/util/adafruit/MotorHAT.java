package io.github.cshadd.fetch_bot.util.adafruit;
import static io.github.cshadd.fetch_bot.Core.delay;
import com.pi4j.io.i2c.I2CFactory;
import io.github.cshadd.fetch_bot.util.Logger;
import java.io.IOException;

// Adopted from: https://github.com/OlivierLD/raspberry-pi4j-samples
// Main
public class MotorHAT {
    // Private Constant Instance/Property Fields
    private static final int DEFAULT_FREQ = 1_600;
    private static final int HAT_ADDR = 0x60;

    // Public Constant Instance/Property Fields
    public static final int STEPPER_1 = 1;
    public static final int STEPPER_2 = 2;

    // Private Instance/Property Fields
    private AdafruitDCMotor motors[];
    private MotorPWM pwm;
    private AdafruitStepperMotor steppers[];

    // Public Instance/Property Fields
    public enum Motor {
        M1, M2, M3, M4
    }
    public enum ServoCommand {
        FORWARD, BACKWARD, BRAKE, RELEASE
    }
    public enum Style {
        SINGLE, DOUBLE, INTERLEAVE, MICROSTEP
    }

    // Public Constructors
    public MotorHAT()
    throws I2CFactory.UnsupportedBusNumberException {
        this(HAT_ADDR, DEFAULT_FREQ);
    }
    public MotorHAT(int nbSteps)
    throws I2CFactory.UnsupportedBusNumberException {
        this(HAT_ADDR, DEFAULT_FREQ, nbSteps);
    }
    public MotorHAT(int addr, int freq)
    throws I2CFactory.UnsupportedBusNumberException {
        this(addr, freq, AdafruitStepperMotor.DEFAULT_NB_STEPS);
    }
    public MotorHAT(int addr, int freq, int nbSteps)
    throws I2CFactory.UnsupportedBusNumberException {
        motors = new AdafruitDCMotor[4];
        int i = 0;
        for (Motor motor : Motor.values()) {
            motors[i++] = new AdafruitDCMotor(this, motor);
        }

        steppers = new AdafruitStepperMotor[2];
        steppers[0] = new AdafruitStepperMotor(this, STEPPER_1, nbSteps);
        steppers[1] = new AdafruitStepperMotor(this, STEPPER_2, nbSteps);
        pwm = new MotorPWM(addr);
        try {
            pwm.setMotorPWMFreq(freq);
        }
        catch (IOException e) {
            Logger.error(e + "\nThere was an issue with IO!");
        }
        catch (Exception e) {
            Logger.error(e + "\nThere was an unknown issue!");
        }
    }

    // Public Static Nested Classes
    public static class AdafruitDCMotor {
        // Private Instance/Property Fields
        private int in1 = 0;
        private int in1Pin = 0;
        private int in2 = 0;
        private int in2Pin = 0;
        private MotorHAT mh;
        private Motor motorNum;
        private int pwm = 0;
        private int pwmPin = 0;

        // Private Constructors
        private AdafruitDCMotor() {
            this(null, null);
        }

        // Public Constructors
        public AdafruitDCMotor(MotorHAT controller, Motor mn) {
            mh = controller;
            motorNum = mn;
            if (mn == Motor.M1) {
                pwm = 8;
                in2 = 9;
                in1 = 10;
            }
            else if (mn == Motor.M2) {
                pwm = 13;
                in2 = 12;
                in1 = 11;
            }
            else if (mn == Motor.M3) {
                pwm = 2;
                in2 = 3;
                in1 = 4;
            }
            else if (mn == Motor.M4) {
                pwm = 7;
                in2 = 6;
                in1 = 5;
            }
            else {
                throw new MotorHATExceptionHandler("Bad MotorHAT Motor # " + mn);
            }
            pwmPin = pwm;
            in1Pin = in1;
            in2Pin = in2;

            Logger.info("MotorHAT - DCMotor: " + mn +
                "; MotorPWM pin:" + pwmPin +
                "; IN1 pin:" + in1Pin +
                "; IN2 pin:" + in2Pin);
        }

        // Public Methods
        public void run(ServoCommand command)
        throws IOException {
            if (mh == null) {
                return;
            }
            if (command == ServoCommand.FORWARD) {
                mh.setPin(in1Pin, 1);
                mh.setPin(in2Pin, 0);
            } else if (command == ServoCommand.BACKWARD) {
                mh.setPin(in1Pin, 0);
                mh.setPin(in2Pin, 1);
            } else if (command == ServoCommand.RELEASE) {
                mh.setPin(in1Pin, 0);
                mh.setPin(in2Pin, 0);
            }
        }
        public void setSpeed(int speed)
        throws IOException {
            if (speed < 0) {
                speed = 0;
            }
            if (speed > 255) {
                speed = 255;
            }
            mh.pwm.setMotorPWM(pwmPin, (short)0, (short)(speed*16));
        }
    }
    public static class AdafruitStepperMotor {
        // Private Constant Instance/Property Fields
        public static final int DEFAULT_NB_STEPS = 200; // Between 35 & 200, Nb steps per revolution.
        private int[] MICROSTEP_CURVE = new int[] {
                0, 50, 98, 142, 180, 212, 236, 250, 255
        };
        /*
        private int[] MICROSTEP_CURVE = new int[] {
            0, 25, 50, 74, 98, 120, 141, 162, 180, 197, 212, 225, 236, 244, 250, 253, 255
        };
        */
        private int MICROSTEPS = 8;
        // private int MICROSTEPS = 16;

        // Public Constant Instance/Property Fields
        public static final int PORT_M1_M2 = 1; // Port #1
        public static final int PORT_M3_M4 = 2; // Port #2

        // Private Instance/Property Fields
        private int ain1 = 10;
        private int ain2 = 9;
        private int bin2 = 12;
        private int bin1 = 11;
        private int currentStep = 0;
        private MotorHAT mc;
        private int motorNum;
        private int motorPWMA = 8;
        private int motorPWMB = 13;
        private int revSteps;
        private double rpm = 30; // Default
        private double secPerStep = 0.1;
        private int steppingCounter = 0;

        // Public Constructors
        public AdafruitStepperMotor(MotorHAT controller, int num) {
            this(controller, num, DEFAULT_NB_STEPS);
        }
        public AdafruitStepperMotor(MotorHAT controller, int num, int steps) {
            mc = controller;
            if (steps < 35 || steps > 200) {
                throw new MotorHATExceptionHandler(String.format("StepsPerRevolution must be in [35-200], found %d", steps));
            }
            revSteps = steps;
            if (num != PORT_M1_M2 && num != PORT_M3_M4) {
                throw new MotorHATExceptionHandler(String.format("Motor Num can only be 1 or 2, found %d.", num));
            }
            motorNum = num;
            secPerStep = 0.1;
            steppingCounter = 0;
            currentStep = 0;

            if ((num - 1) == 0) { // num == STEPPER_1
                ain1 = 10;
                ain2 = 9;
                bin1 = 11;
                bin2 = 12;
                motorPWMA = 8;
                motorPWMB = 13;
            }
            else if ((num - 1) == 1) { // num == STEPPER_2
                ain1 = 4;
                ain2 = 3;
                bin1 = 5;
                bin2 = 6;
                motorPWMA = 2;
                motorPWMB = 7;
            }
            else {
                throw new MotorHATExceptionHandler("MotorHAT Stepper must be 1 or 2");
            }
        }

        // Public Property Accessor Methods
        public int getMotorNum() {
            return motorNum;
        }
        public double getSecPerStep() {
            return secPerStep;
        }

        // Public Methods
        public double getRPM() {
            return rpm;
        }
        public int getStepPerRev() {
            return revSteps;
        }
        public int oneStep(ServoCommand dir, Style style)
        throws IOException {
            int pwmA = 255;
            int pwmB = 255;

            // First determine what sort of stepping procedure we're up to
            if (style == Style.SINGLE) {
                if ((int)(currentStep/(int)(MICROSTEPS/2)) % 2 == 1) {
                    // We're at an odd step, weird
                    if (dir == ServoCommand.FORWARD) {
                        currentStep += ((int)(MICROSTEPS/2));
                    }
                    else {
                        currentStep -= ((int)(MICROSTEPS/2));
                    }
                }
                else {
                    // Go to next even step
                    if (dir == ServoCommand.FORWARD) {
                        currentStep += MICROSTEPS;
                    }
                    else {
                        currentStep -= MICROSTEPS;
                    }
                }
            }
            else if (style == Style.DOUBLE) {
                if ((int)(currentStep / (int)(MICROSTEPS/2)) % 2 == 0) {
                    // We're at an even step, weird
                    if (dir == ServoCommand.FORWARD) {
                        currentStep += ((int)(MICROSTEPS/2));
                    }
                    else {
                        currentStep -= ((int)(MICROSTEPS/2));
                    }
                }
                else {
                    // Go to next odd step
                    if (dir == ServoCommand.FORWARD) {
                        currentStep += MICROSTEPS;
                    }
                    else {
                        currentStep -= MICROSTEPS;
                    }
                }
            }
            else if (style == Style.INTERLEAVE) {
                if (dir == ServoCommand.FORWARD) {
                    currentStep += ((int)(MICROSTEPS/2));
                }
                else {
                    currentStep -= ((int)(MICROSTEPS/2));
                }
            }
            else if (style == Style.MICROSTEP) {
                if (dir == ServoCommand.FORWARD) {
                    currentStep += 1;
                }
                else {
                    currentStep -= 1;
                    // Go to next 'step' and wrap around
                    currentStep += MICROSTEPS*4;
                    currentStep %= MICROSTEPS*4;
                }
                pwmA = 0;
                pwmB = 0;
                if (currentStep >= 0 && currentStep < MICROSTEPS) {
                    pwmA = MICROSTEP_CURVE[MICROSTEPS - currentStep];
                    pwmB = MICROSTEP_CURVE[currentStep];
                }
                else if (currentStep >= MICROSTEPS && currentStep < MICROSTEPS*2) {
                    pwmA = MICROSTEP_CURVE[currentStep - MICROSTEPS];
                    pwmB = MICROSTEP_CURVE[MICROSTEPS*2 - currentStep];
                }
                else if (currentStep >= MICROSTEPS*2 && currentStep < MICROSTEPS*3) {
                    pwmA = MICROSTEP_CURVE[MICROSTEPS*3 - currentStep];
                    pwmB = MICROSTEP_CURVE[currentStep - MICROSTEPS*2];
                }
                else if (currentStep >= MICROSTEPS*3 && currentStep < MICROSTEPS*4) {
                    pwmA = MICROSTEP_CURVE[currentStep - MICROSTEPS*3];
                    pwmB = MICROSTEP_CURVE[MICROSTEPS*4 - currentStep];
                }
            }

            // Go to next "step" and wrap around
            currentStep += MICROSTEPS*4;
            currentStep %= MICROSTEPS*4;

            // Only really used for microstepping, otherwise always on
            mc.pwm.setMotorPWM(motorPWMA, (short)0, (short)(pwmA*16));
            mc.pwm.setMotorPWM(motorPWMB, (short)0, (short)(pwmB*16));

            // Set up coil energizing
            int coils[] = new int[]{
                0, 0, 0, 0
            };

            if (style == Style.MICROSTEP) {
                if (currentStep >= 0 && currentStep < MICROSTEPS) {
                    coils = new int[] {
                        1, 1, 0, 0
                    };
                }
                else if (currentStep >= MICROSTEPS && currentStep < MICROSTEPS*2) {
                    coils = new int[] {
                        0, 1, 1, 0
                    };
                }
                else if (currentStep >= MICROSTEPS*2 && currentStep < MICROSTEPS*3) {
                    coils = new int[] {
                        0, 0, 1, 1
                    };
                }
                else if (currentStep >= MICROSTEPS*3 && currentStep < MICROSTEPS*4) {
                    coils = new int[] {
                        1, 0, 0, 1
                    };
                }
            }
            else {
                int[][] step2coils = new int[][] {
                        {
                            1, 0, 0, 0
                        },
                        {
                            1, 1, 0, 0
                        },
                        {
                            0, 1, 0, 0
                        },
                        {
                            0, 1, 1, 0
                        },
                        {
                            0, 0, 1, 0
                        },
                        {
                            0, 0, 1, 1
                        },
                        {
                            0, 0, 0, 1
                        },
                        {
                            1, 0, 0, 1
                        }
                };
                coils = step2coils[(int)(currentStep/(int)(MICROSTEPS/2))];
            }
            // Print "coils state = " + str(coils)
            mc.setPin(ain2, coils[0]);
            mc.setPin(bin1, coils[1]);
            mc.setPin(ain1, coils[2]);
            mc.setPin(bin2, coils[3]);

            return currentStep;
        }
        public void setSpeed(double rpm) {
            rpm = rpm;
            secPerStep = 60.0 / (revSteps * rpm);
            steppingCounter = 0;
        }
        public void step(int steps, ServoCommand direction, Style stepStyle)
        throws IOException {
            double sPerS = secPerStep;
            int latestStep = 0;

            if (stepStyle == Style.INTERLEAVE) {
                sPerS = sPerS/2.0;
            } else if (stepStyle == Style.MICROSTEP) {
                sPerS /= MICROSTEPS;
                steps *= MICROSTEPS;
            }

            Logger.info("MotorHAT - " + sPerS + " sec per step.");

            for (int s = 0; s < steps; s++) {
                latestStep = oneStep(direction, stepStyle);
                delay((long)(sPerS*1_000));
            }
            if (stepStyle == Style.MICROSTEP) {
                // This is an edge case, if we are in between full steps, lets just keep so we end on a full step
                while (latestStep != 0 && latestStep != MICROSTEPS) {
                    latestStep = oneStep(direction, stepStyle);
                    delay((long)(sPerS*1_000));
                }
            }
        }
    }
    public static class MotorHATExceptionHandler
    extends RuntimeException {
        // Public Constructors
        public MotorHATExceptionHandler() {
            super();
            Logger.error("There was an issue with MotorHAT!");
        }
        public MotorHATExceptionHandler(String s) {
            super(s);
            Logger.error(s + "\nThere was an issue with MotorHAT!");
        }
        public MotorHATExceptionHandler(String s, Throwable throwable) {
            super(s, throwable);
            Logger.error(s + "\nThere was an issue with MotorHAT!");
        }
        public MotorHATExceptionHandler(Throwable throwable) {
            super(throwable);
            Logger.error("There was an issue with MotorHAT!");
        }
    }

    // Public Methods
    public AdafruitDCMotor getMotor(Motor mn) {
        AdafruitDCMotor motor = null;
        for (AdafruitDCMotor m : motors) {
            if (m.motorNum == mn) {
                motor = m;
                Logger.info("MotorHAT - getMotor (DC):" + mn);
                break;
            }
        }
        return motor;
    }
    public AdafruitStepperMotor getStepper(int num) {
        if (num != STEPPER_1 && num != STEPPER_2) {
            throw new MotorHATExceptionHandler("MotorHAT Stepper must be 1 or 2.");
        }
        return steppers[num - 1];
    }
    public void setPin(int pin, int value)
    throws IOException {
        if (pin < 0 || pin > 15) {
            throw new MotorHATExceptionHandler("PWM pin must be in [0-15], found " + pin);
        }

        if (value == 0) {
            pwm.setMotorPWM(pin, (short)0, (short)4_096);
        }
        else if (value == 1) {
            pwm.setMotorPWM(pin, (short)4_096, (short)0);
        }
        else {
            throw new MotorHATExceptionHandler("Pin value must be 0 or 1, found " + value);
        }
    }
}

package io.github.cshadd.fetch_bot.util;
import java.io.IOException;
import com.pi4j.io.serial.Serial;
import com.pi4j.io.serial.SerialDataEvent;
import com.pi4j.io.serial.SerialDataEventListener;
import com.pi4j.io.serial.SerialFactory;
import com.pi4j.io.serial.SerialPortException;
import io.github.cshadd.fetch_bot.FetchBot;
import org.json.JSONException;
import org.json.JSONObject;

// Main
public final class ArduinoCommunication
implements FetchBot {
    // Private Instance/Property Fields
    private String toArduinoData;
    private JSONObject toRobotData;
    private Serial serial;
    private int serialBr;
    private String serialData;
    private String serialPort;

    // Public Constructors
    public ArduinoCommunication() {
        toArduinoData = "";
        toRobotData = new JSONObject();
        serial = SerialFactory.createInstance();
        serialBr = 9600;
        serialData = "";
        serialPort = Serial.DEFAULT_COM_PORT;
        serial.addListener(new SerialDataEventListener() {
            @Override
            public void dataReceived(SerialDataEvent event) {
                try {
                    System.out.println("[ASCII DATA] " + event.getAsciiString());
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // Private Final Methods
    private final boolean openSerialPort() {
        boolean returnData = false;
        if (!serial.isOpen()) {
            try {
                try {
                    serial.open(serialPort, serialBr);
                    Logger.info("ArduinoCommunication - Opened serial port.");
                    returnData = true;
                }
                catch (IOException e) {
                    throw new RuntimeException(e);
                }
                catch (Exception e) {
                    throw new RuntimeException(e);
                }
                finally { }

                final Thread me = Thread.currentThread();
                synchronized (me) {
                    me.wait();
                }
            }
            catch (SerialPortException e) {
                Logger.error(e, "There was an issue with Serial!");
            }
            catch (Exception e) {
                Logger.error(e, "There was an unknown issue!");
            }
            finally { }
        }
        else {
            returnData = true;
        }
        return returnData;
    }
    private final JSONObject read() {
        JSONObject returnData = null;
        try {
            if (openSerialPort()) {
                // returnData = new JSONObject(serialData);
            }
            else {
                Logger.warn("Was unable to process serial port, had to first open.");
            }
        }
        catch (JSONException e) {
            Logger.error(e, "There was an issue with JSON!");
        }
        catch (Exception e) {
            Logger.error(e, "There was an unknown issue!");
        }
        finally { }
        return returnData;
    }
    private final void write(String value) {
        try {
            if (openSerialPort()) {
                serial.write(value);
            }
            else {
                Logger.warn("Was unable to process serial port, had to first open.");
            }
        }
        catch (IOException e) {
            Logger.error(e, "There was an issue with IO!");
        }
        catch (Exception e) {
            Logger.error(e, "There was an unknown issue!");
        }
    }

    // Public Final Methods
    public final void clear() {
        toArduinoData = "";
        toRobotData = new JSONObject();
        if (serial.isOpen()) {
            try {
                serial.close();
            }
            catch (IOException e) {
                Logger.error(e, "There was an issue with IO!");
            }
            catch (Exception e) {
                Logger.error(e, "There was an unknown issue!");
            }
            finally { }
        }
    }
    public final String getArduinoValue() {
        return toArduinoData;
    }
    public final String getRobotValue(String key) {
        String returnData = null;
        /*try {
            returnData = toRobotData.getString(key);
        }
        catch (JSONException e) {
            Logger.error(e, "There was an issue with JSON!");
        }
        catch (Exception e) {
            Logger.error(e, "There was an unknown issue!");
        }
        finally { }*/
        returnData = "0";
        return returnData;
    }
    public final void pullRobot() {
        toRobotData = read();
    }
    public final void pushArduino() {
        write(toArduinoData);
    }
    public final void reset() {
        clear();
        setRobotValue("sensor-front", "0");
        setRobotValue("sensor-left", "0");
        setRobotValue("sensor-right", "0");
    }
    public final void setArduinoValue(String value) {
        toArduinoData = value;
    }
    public final void setRobotValue(String key, String value) {
        try {
            toRobotData.put(key, value);
        }
        catch (JSONException e) {
            Logger.error(e, "There was an issue with JSON!");
        }
        catch (Exception e) {
            Logger.error(e, "There was an unknown issue!");
        }
        finally { }
    }
}

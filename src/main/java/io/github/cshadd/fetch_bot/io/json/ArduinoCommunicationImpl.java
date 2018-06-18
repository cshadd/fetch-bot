package io.github.cshadd.fetch_bot.io.json;
import io.github.cshadd.fetch_bot.Component;
import org.json.JSONObject;

// Main
@Component("Communication")
public class ArduinoCommunicationImpl
extends AbstractArduinoCommunication {
    // Public Constructors
    public ArduinoCommunicationImpl() {
        super();
    }
    public ArduinoCommunicationImpl(String serialPort) {
        super(serialPort);
    }

    // Public Methods (Overrided)
    @Override
    public void clear()
    throws JSONCommunicationException {
        close();
        // this.buffer = "{ }";
        this.toSourceData = new JSONObject();
        this.toRobotData = new JSONObject();
    }
    @Override
    public void pullRobot()
    throws JSONCommunicationException {
        this.toRobotData = read();
    }
    @Override
    public void pushSource()
    throws JSONCommunicationException {
        write();
        // this.isSerialLocked = true;
        // synchronized (this.serialLock) {
            // try {
            	// while (this.isSerialLocked) {
                    // this.serialLock.wait();
                // }
            // }
            // catch (Exception e) { /* */ } // Suppressed
            // finally { /* */ }
        // }
    }
    @Override
    public void reset()
    throws JSONCommunicationException {
        clear();
        open();
        setRobotValue("s", "-1");

        setSourceValue("a", "Stop");
    }
    @Override
    public String toString() {
        return this.serialPort;
    }
}

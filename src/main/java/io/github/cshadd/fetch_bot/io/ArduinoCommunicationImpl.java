package io.github.cshadd.fetch_bot.io;
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

    // Public Methods (Overrided)
    @Override
    public void clear()
    throws CommunicationException {
        close();
        buffer = "{ }";
        toSourceData = new JSONObject();
        toRobotData = new JSONObject();
    }
    @Override
    public void pullRobot()
    throws CommunicationException {
        toRobotData = read();
    }
    @Override
    public void pushSource()
    throws CommunicationException {
        write();
        isSerialLocked = true;
        synchronized (serialLock) {
            try {
            	while (isSerialLocked) {
                    serialLock.wait();
                }
            }
            catch (Exception e) { } // Suppressed
            finally { }
        }
    }
    @Override
    public void reset()
    throws CommunicationException {
        clear();
        open();
        setRobotValue("s", "-1");

        setSourceValue("a", "Stop");
    }
}

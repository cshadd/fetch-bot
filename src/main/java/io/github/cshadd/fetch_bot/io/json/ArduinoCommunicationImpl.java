package io.github.cshadd.fetch_bot.io.json;

import io.github.cshadd.fetch_bot.Component;
import org.json.JSONObject;

// Main

/**
 * The Class ArduinoCommunicationImpl. An Ardunio Communication with basic
 * implementation.
 * 
 * @author Christian Shadd
 * @author Maria Verna Aquino
 * @author Thanh Vu
 * @author Joseph Damian
 * @author Giovanni Orozco
 * @since 1.0.0
 */
@Component("Communication")
public class ArduinoCommunicationImpl extends AbstractArduinoCommunication {
    // Private Constructors
    
    private ArduinoCommunicationImpl() {
        this(null);
    }
    
    // Public Constructors
    
    public ArduinoCommunicationImpl(String serialPort) {
        super(serialPort);
    }
    
    // Public Methods (Overrided)
    
    @Override
    public void clear() throws JSONCommunicationException {
        close();
        this.toSourceData = new JSONObject();
        this.toRobotData = new JSONObject();
    }
    
    @Override
    public void pullRobot() throws JSONCommunicationException {
        this.toRobotData = read();
    }
    
    @Override
    public void pushSource() throws JSONCommunicationException {
        write();
    }
    
    @Override
    public void reset() throws JSONCommunicationException {
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

package io.github.cshadd.fetch_bot.io.json;

import io.github.cshadd.fetch_bot.Component;

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
    public void pullRobot() throws ArduinoCommunicationException {
        this.toRobotData = read();
    }
    
    @Override
    public void pushSource() throws ArduinoCommunicationException {
        write();
    }
    
    @Override
    public void reset() throws ArduinoCommunicationException {
        try {
            setRobotValue("s", "-1");
            
            setSourceValue("a", "Stop");
        } catch (JSONCommunicationException e) {
            throw new ArduinoCommunicationException("Could not reset Arduino JSON.", e);
        } catch (Exception e) {
            throw new ArduinoCommunicationException("Unknown issue.", e);            
        }
    }
    
    @Override
    public String toString() {
        return this.serialPort;
    }
}

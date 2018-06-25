package io.github.cshadd.fetch_bot.io.json;

import io.github.cshadd.fetch_bot.Component;
import io.github.cshadd.fetch_bot.References;

// Main

/**
 * The Class WebInterfaceCommunicationImpl. A Web Interface Communication with
 * basic implementation.
 * 
 * @author Christian Shadd
 * @author Maria Verna Aquino
 * @author Thanh Vu
 * @author Joseph Damian
 * @author Giovanni Orozco
 * @since 1.0.0
 */
@Component("Communication")
public class WebInterfaceCommunicationImpl extends
                AbstractWebInterfaceCommunication {
    // Private Constructors
    
    private WebInterfaceCommunicationImpl() {
        this(null);
    }
    
    // Public Constructors
    
    public WebInterfaceCommunicationImpl(String commPath) {
        super(commPath);
    }
    
    // Public Methods (Overrided)
    
    @Override
    public void pullRobot() throws WebInterfaceCommunicationException {
        this.toRobotData = read(TOROBOT_JSON_FILE);
    }
    
    @Override
    public void pullSource() throws WebInterfaceCommunicationException {
        this.toSourceData = read(TOWEBINTERFACE_JSON_FILE);
    }
    
    @Override
    public void pushRobot() throws WebInterfaceCommunicationException {
        write(this.toRobotData, TOROBOT_JSON_FILE);
    }
    
    @Override
    public void pushSource() throws WebInterfaceCommunicationException {
        write(this.toSourceData, TOWEBINTERFACE_JSON_FILE);
    }
    
    @Override
    public void reset() throws WebInterfaceCommunicationException {
        try {
            setRobotValue("mode", "Idle");
            setRobotValue("move", "Stop");
            setRobotValue("trackclass", "None");
            
            setSourceValue("emotion", "Idle");
            setSourceValue("hudstreamport", "" + References.HUD_STREAM_PORT);
            setSourceValue("mode", "Idle");
            setSourceValue("rawgraph", "");
            setSourceValue("rot", "0");
            setSourceValue("trackclass", "None");
            setSourceValue("ultrasonic", "0");
            setSourceValue("verbose", "");
        } catch (JSONCommunicationException e) {
            throw new WebInterfaceCommunicationException(
                            "Could not reset Web Interface JSON.", e);
        } catch (Exception e) {
            throw new WebInterfaceCommunicationException("Unknown issue.", e);
        }
    }
}

package io.github.cshadd.fetch_bot.io.json;

import io.github.cshadd.fetch_bot.Component;
import io.github.cshadd.fetch_bot.References;
import org.json.JSONObject;

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
    public void clear() throws JSONCommunicationException {
        this.toSourceData = new JSONObject();
        this.toRobotData = new JSONObject();
    }
    
    @Override
    public void pullRobot() throws JSONCommunicationException {
        this.toRobotData = read(TOROBOT_JSON_FILE);
    }
    
    @Override
    public void pullSource() throws JSONCommunicationException {
        this.toSourceData = read(TOWEBINTERFACE_JSON_FILE);
    }
    
    @Override
    public void pushRobot() throws JSONCommunicationException {
        write(this.toRobotData, TOROBOT_JSON_FILE);
    }
    
    @Override
    public void pushSource() throws JSONCommunicationException {
        write(this.toSourceData, TOWEBINTERFACE_JSON_FILE);
    }
    
    @Override
    public void reset() throws JSONCommunicationException {
        clear();
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
    }
}

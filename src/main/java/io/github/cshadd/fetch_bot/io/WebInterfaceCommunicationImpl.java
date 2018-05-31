package io.github.cshadd.fetch_bot.io;
import io.github.cshadd.fetch_bot.Component;
import org.json.JSONObject;

// Main
@Component("Communication")
public class WebInterfaceCommunicationImpl
extends AbstractWebInterfaceCommunication {
    // Public Constructors
    public WebInterfaceCommunicationImpl() {
        super();
    }
    public WebInterfaceCommunicationImpl(String commPath) {
        super(commPath);
    }

    // Public Methods (Overrided)
    @Override
    public void clear()
    throws CommunicationException {
        this.toSourceData = new JSONObject();
        this.toRobotData = new JSONObject();
    }
    @Override
    public void pullRobot()
    throws CommunicationException {
        this.toRobotData = read(TOROBOT_JSON_FILE);
    }
    @Override
    public void pullSource()
    throws CommunicationException {
        this.toSourceData = read(TOWEBINTERFACE_JSON_FILE);
    }
    @Override
    public void pushRobot()
    throws CommunicationException {
        write(this.toRobotData, TOROBOT_JSON_FILE);
    }
    @Override
    public void pushSource()
    throws CommunicationException {
        write(this.toSourceData, TOWEBINTERFACE_JSON_FILE);
    }
    @Override
    public void reset()
    throws CommunicationException {
        clear();
        setRobotValue("mode", "Idle");
        setRobotValue("move", "Stop");
        setRobotValue("trackclass", "None");
        
        setSourceValue("emotion", "Idle");
        setSourceValue("mode", "Idle");
        setSourceValue("rot", "0");
        setSourceValue("trackclass", "None");
        setSourceValue("ultrasonic", "0");
        setSourceValue("verbose", "...");
    }
}

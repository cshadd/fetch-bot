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

    // Public Methods (Overrided)
    @Override
    public void clear()
    throws CommunicationException {
        toSourceData = new JSONObject();
        toRobotData = new JSONObject();
    }
    @Override
    public void pullRobot()
    throws CommunicationException {
        toRobotData = read(TO_ROBOT_JSON_PATH);
    }
    @Override
    public void pullSource()
    throws CommunicationException {
        toSourceData = read(TO_WEB_INTERFACE_JSON_PATH);
    }
    @Override
    public void pushRobot()
    throws CommunicationException {
        write(toRobotData, TO_ROBOT_JSON_PATH);
    }
    @Override
    public void pushSource()
    throws CommunicationException {
        write(toSourceData, TO_WEB_INTERFACE_JSON_PATH);
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

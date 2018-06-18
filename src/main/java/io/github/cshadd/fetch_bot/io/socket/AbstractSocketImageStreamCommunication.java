package io.github.cshadd.fetch_bot.io.socket;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;
import com.pi4j.io.serial.Baud;
import com.pi4j.io.serial.DataBits;
import com.pi4j.io.serial.FlowControl;
import com.pi4j.io.serial.Parity;
import com.pi4j.io.serial.Serial;
import com.pi4j.io.serial.SerialConfig;
import com.pi4j.io.serial.SerialDataEvent;
import com.pi4j.io.serial.SerialDataEventListener;
import com.pi4j.io.serial.SerialFactory;
import com.pi4j.io.serial.StopBits;
import org.json.JSONException;
import org.json.JSONObject;

// Main
public abstract class AbstractSocketImageStreamCommunication
extends AbstractSocketCommunication
implements SocketCommunication {}

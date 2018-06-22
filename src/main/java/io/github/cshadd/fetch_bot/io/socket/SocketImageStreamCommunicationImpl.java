package io.github.cshadd.fetch_bot.io.socket;

import io.github.cshadd.fetch_bot.Component;

// Main

/**
 * The Class SocketImageStreamCommunicationImpl. A Socket Image Stream
 * Communication with basic implementation.
 * 
 * @author Christian Shadd
 * @author Maria Verna Aquino
 * @author Thanh Vu
 * @author Joseph Damian
 * @author Giovanni Orozco
 * @since 2.0.0-alpha
 */
@Component("Communication")
public class SocketImageStreamCommunicationImpl extends
                AbstractSocketImageStreamCommunication {
    // Private Constructors
    
    private SocketImageStreamCommunicationImpl() {
        this("", 0);
    }
    
    // Public Constructors
    
    public SocketImageStreamCommunicationImpl(String socketHost,
                    int socketPort) {
        super(socketHost, socketPort);
    }
}

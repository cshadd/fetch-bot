package io.github.cshadd.fetch_bot.io.socket;

import java.awt.image.BufferedImage;

// Main
public abstract class AbstractSocketImageStreamCommunication
extends AbstractSocketCommunication
implements SocketImageStreamCommunication {
    // Public Constant Instance/Property Fields
    public static final int DEFAULT_SOCKET_PORT = 7676; // Change if needed

    
    // Public Constructors
    
    public AbstractSocketImageStreamCommunication() throws SocketCommunicationException {
        super();
        open(DEFAULT_SOCKET_PORT);
    }
    
    // Public Methods (Overrided)
    
    @Override
    public void write(BufferedImage image) throws SocketCommunicationException {
        
    }
}

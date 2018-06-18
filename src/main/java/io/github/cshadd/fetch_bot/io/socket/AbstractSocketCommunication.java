package io.github.cshadd.fetch_bot.io.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import io.github.cshadd.fetch_bot.io.AbstractCommunication;

// Main
public abstract class AbstractSocketCommunication
extends AbstractCommunication
implements SocketCommunication {
    // Protected Instance/Property Fields
    
    protected ServerSocket serverSocket;
    protected Socket serverSocketHandler;
    
    // Protected Constructors
    
    protected AbstractSocketCommunication() {
        this.serverSocket = null;
        this.serverSocketHandler = null;
    }
    
    // Public Methods (Overrided)

    @Override
    public void close() throws SocketCommunicationException {
        try {
            if (this.serverSocketHandler != null) {
                if (this.serverSocketHandler.isBound()) {
                    this.serverSocketHandler.close();
                }
            }
            if (this.serverSocket != null) {
                if (this.serverSocket.isBound()) {
                    this.serverSocket.close();
                }
            }
        } catch (IOException e) {
            throw new SocketCommunicationException("Could not close server sockets!", e);
        } catch (Exception e) {
            throw new SocketCommunicationException("Unknown issue.", e);
        } finally {
            this.serverSocket = null;
            this.serverSocketHandler = null;
        }
    }
    @Override
    public void open(int port) throws SocketCommunicationException {
        this.close();
        try {
            this.serverSocket = new ServerSocket(port);
            this.serverSocketHandler = this.serverSocket.accept();
        } catch (IOException e) {
            throw new SocketCommunicationException("Could not close server sockets!", e);
        } catch (Exception e) {
            throw new SocketCommunicationException("Unknown issue.", e);
        } finally { }
    }
}

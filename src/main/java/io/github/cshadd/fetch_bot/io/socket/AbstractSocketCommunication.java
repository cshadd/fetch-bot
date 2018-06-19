package io.github.cshadd.fetch_bot.io.socket;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import io.github.cshadd.fetch_bot.io.AbstractCommunication;

// Main
public abstract class AbstractSocketCommunication extends AbstractCommunication
                implements SocketCommunication {
    // Public Constant Instance/Property Fields
    
    public static final String DEFAULT_SOCKET_ADDRESS = "localhost"; // Change
                                                                     // if
                                                                     // needed
    
    // Protected Constant Instance/Property Fields
    
    protected static final String CRLF = "\r\n";
    
    // Protected Instance/Property Fields
    
    protected ServerSocket serverSocket;
    protected Socket       socket;
    
    // Protected Constructors
    
    protected AbstractSocketCommunication() {
        this.serverSocket = null;
        this.socket = null;
    }
    
    // Protected Methods
    
    protected void listen() throws SocketCommunicationException {
        try {
            this.socket = this.serverSocket.accept();
        } catch (SocketException e) {
            throw new SocketCommunicationException(
                            "There was a problem with listening to the socket!",
                            e);
        } catch (Exception e) {
            throw new SocketCommunicationException("Unknown issue.", e);
        } finally {
            /* */ }
    }
    
    // Public Methods (Overrided)
    
    @Override
    public void close() throws SocketCommunicationException {
        try {
            if (this.serverSocket != null) {
                if (this.serverSocket.isBound()) {
                    this.serverSocket.close();
                }
            }
            
            if (this.socket != null) {
                if (this.socket.isBound()) {
                    this.socket.close();
                }
            }
        } catch (SocketException e) {
            throw new SocketCommunicationException("Could not close socket!",
                            e);
        } catch (Exception e) {
            throw new SocketCommunicationException("Unknown issue.", e);
        } finally {
            this.serverSocket = null;
            this.socket = null;
        }
    }
    
    @Override
    public void open(int port) throws SocketCommunicationException {
        this.close();
        try {
            this.serverSocket = new ServerSocket(port, 50, InetAddress
                            .getByName(DEFAULT_SOCKET_ADDRESS));
        } catch (SocketException e) {
            throw new SocketCommunicationException("Could not open socket!", e);
        } catch (Exception e) {
            throw new SocketCommunicationException("Unknown issue.", e);
        } finally {
        }
    }
}

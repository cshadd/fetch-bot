package io.github.cshadd.fetch_bot.io.socket;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import io.github.cshadd.fetch_bot.io.AbstractCommunication;

// Main

/**
 * The Class AbstractSocketCommunication. Defines what a Socket Communication
 * is. A Socket Communication is basically a helper that directs data between
 * sockets and Fetch Bot.
 * 
 * @author Christian Shadd
 * @author Maria Verna Aquino
 * @author Thanh Vu
 * @author Joseph Damian
 * @author Giovanni Orozco
 * @since 2.0.0-alpha
 */
public abstract class AbstractSocketCommunication extends AbstractCommunication
                implements SocketCommunication {
    // Private Constant Instance/Property Fields
    
    // Protected Constant Instance/Property Fields
    
    protected static final String CRLF = "\r\n";
    
    // Protected Instance/Property Fields
    
    protected final String socketHost;
    protected final int    socketPort;
    
    // Protected Instance/Property Fields
    
    protected ServerSocket serverSocket;
    protected Socket       socket;
    
    // Private Constructors
    
    private AbstractSocketCommunication() {
        this("", 0);
    }
    
    // Protected Constructors
    
    protected AbstractSocketCommunication(String socketHost, int socketPort) {
        super();
        this.serverSocket = null;
        this.socket = null;
        this.socketHost = socketHost;
        this.socketPort = socketPort;
    }
    
    // Public Methods (Overrided)
    
    @Override
    public void close() throws SocketCommunicationException {
        try {
            if (this.serverSocket != null) {
                this.serverSocket.close();
            }
            
            if (this.socket != null) {
                this.socket.close();
            }
        } catch (SocketException e) {
            throw new SocketCommunicationException("Could not close socket.",
                            e);
        } catch (Exception e) {
            throw new SocketCommunicationException("Unknown issue.", e);
        } finally {
            this.serverSocket = null;
            this.socket = null;
        }
    }
    
    @Override
    public void listen() throws SocketCommunicationException {
        if (this.serverSocket != null) {
            if (!this.serverSocket.isClosed()) {
                try {
                    this.socket = this.serverSocket.accept();
                } catch (IOException e) {
                    throw new SocketCommunicationException(
                                    "Could not listen to socket.", e);
                } catch (Exception e) {
                    throw new SocketCommunicationException("Unknown issue.", e);
                } finally {
                    /* */ }
            }
        }
    }
    
    @Override
    public void open() throws SocketCommunicationException {
        try {
            if (this.serverSocket == null) {
                this.serverSocket = new ServerSocket(this.socketPort, 50,
                                InetAddress.getByName(this.socketHost));
            }
        } catch (UnknownHostException e) {
            throw new SocketCommunicationException(
                            "Could not open socket at host.", e);
        } catch (SocketException e) {
            throw new SocketCommunicationException("Could not open socket.", e);
        } catch (Exception e) {
            throw new SocketCommunicationException("Unknown issue.", e);
        } finally {
        }
    }
}

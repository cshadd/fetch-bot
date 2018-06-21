package io.github.cshadd.fetch_bot.io.socket;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
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
    
    private static final String DEFAULT_SOCKET_ADDRESS = "localhost";
    
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
        try {
            if (this.serverSocket == null) {
                this.serverSocket = new ServerSocket(port, 50, InetAddress
                                .getByName(DEFAULT_SOCKET_ADDRESS));
            }
        } catch (SocketException e) {
            throw new SocketCommunicationException("Could not open socket!", e);
        } catch (Exception e) {
            throw new SocketCommunicationException("Unknown issue.", e);
        } finally {
        }
    }
}

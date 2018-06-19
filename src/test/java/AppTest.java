
/*
 * MIT License
 *
 * Copyright (c) 2018 Christian Shadd
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 * https://cshadd.github.io/fetch-bot/
 */

import org.junit.Test;

import io.github.cshadd.fetch_bot.FetchBot;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import sun.misc.BASE64Encoder;

// Main

/**
 * The Class AppTest. This is for testing.
 *
 * @author Christian Shadd
 * @author Maria Verna Aquino
 * @author Thanh Vu
 * @author Joseph Damian
 * @author Giovanni Orozco
 * @since 1.0.0
 */
public class AppTest {
    // Public Static Methods
    
    /**
     * Test.
     * 
     * @throws IOException
     */
    @Test
    @SuppressWarnings("static-method")
    public void test() throws IOException {
        System.out.println("Tested.");
        
        WebSocketServer j = new WebSocketServerImpl();
        j.connect();
        
        BufferedReader br = new BufferedReader(new InputStreamReader(
                        System.in));
        while (true) {
            System.out.println("Write something to the client!");
            j.sendMessage(br.readLine().getBytes());
        }
    }
}

abstract interface WebSocketServer extends FetchBot {
    public abstract void connect();
    
    public abstract void disconnect();
    
    public abstract void sendMessage(byte[] bytes);
}

abstract class AbstractWebSocketServer implements WebSocketServer {
    public static final int MASK_SIZE             = 4;
    public static final int SINGLE_FRAME_UNMASKED = 0x81;
    
    protected final SocketListenerThread socketListenerRunnable;
    protected final Thread               socketListenerThread;
    
    private ServerSocket serverSocket;
    private Socket       socket;
    
    private AbstractWebSocketServer() {
        this.socket = null;
        this.socketListenerRunnable = new SocketListenerThread();
        this.socketListenerThread = new Thread(this.socketListenerRunnable);
    }
    
    protected AbstractWebSocketServer(int port) {
        this();
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
        } finally {
        }
    }
    
    protected final class SocketListenerThread implements Runnable {
        // Private Instance/Property Fields
        
        /**
         * The running state.
         */
        private volatile boolean running;
        
        // Public Constructors
        
        /**
         * Instantiates a new Socket Listener Thread.
         */
        public SocketListenerThread() {
            super();
            this.running = false;
        }
        
        // Public Methods
        
        /**
         * Terminate the thread.
         */
        public void terminate() {
            this.running = false;
        }
        
        // Public Methods (Overrided)
        
        /**
         * Runs the listener.
         * 
         * @see java.lang.Runnable#run()
         */
        @Override
        public void run() {
            this.running = true;
            while (this.running) {
                System.out.println("Recieved from client: " + receiveMessage());
            }
        }
    }
    
    private void convertAndPrint(byte[] bytes) {
        final StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X ", b));
        }
        System.out.println(sb.toString());
    }
    
    private int getSizeOfPayload(byte b) {
        // Must subtract 0x80 from masked frames
        return ((b & 0xFF) - 0x80);
    }
    
    private byte[] unMask(byte[] mask, byte[] data) {
        for (int i = 0; i < data.length; i++) {
            data[i] = (byte) (data[i] ^ mask[i % mask.length]);
        }
        return data;
    }
    
    @SuppressWarnings("restriction")
    private boolean handshake() throws IOException, NoSuchAlgorithmException {
        final PrintWriter out = new PrintWriter(this.socket.getOutputStream());
        final BufferedReader in = new BufferedReader(new InputStreamReader(
                        this.socket.getInputStream()));
        final HashMap<String, String> keys = new HashMap<>();
        String str;
        // Reading client handshake
        while (!(str = in.readLine()).equals("")) {
            String[] s = str.split(": ");
            System.out.println();
            System.out.println(str);
            if (s.length == 2) {
                keys.put(s[0], s[1]);
            }
        }
        
        final String hash;
        try {
            hash = new BASE64Encoder().encode(MessageDigest.getInstance("SHA-1")
                            .digest((keys.get("Sec-WebSocket-Key")
                                            + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11")
                                                            .getBytes()));
        } catch (NoSuchAlgorithmException e) {
            // throw e;
            return false;
        } catch (Exception e) {
            throw e;
        } finally {
        }
        
        // Write handshake response
        out.write("HTTP/1.1 101 Switching Protocols\r\n"
                        + "Upgrade: websocket\r\n" + "Connection: Upgrade\r\n"
                        + "Sec-WebSocket-Accept: " + hash + "\r\n" + "\r\n");
        out.flush();
        
        return true;
    }
    
    private byte[] readBytes(int numOfBytes) throws IOException {
        final byte[] b = new byte[numOfBytes];
        this.socket.getInputStream().read(b);
        return b;
    }
    
    protected String receiveMessage() {
        String returnData = "";
        byte[] buf;
        try {
            buf = readBytes(2);
            
            System.out.println("Headers:");
            convertAndPrint(buf);
            int opcode = buf[0] & 0x0F;
            if (opcode == 8) {
                // Client want to close connection!
                System.out.println("Client closed!");
                disconnect();
            }
            final int payloadSize = getSizeOfPayload(buf[1]);
            System.out.println("Payload size: " + payloadSize);
            try {
                buf = readBytes(MASK_SIZE + payloadSize);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Payload:");
            convertAndPrint(buf);
            buf = unMask(Arrays.copyOfRange(buf, 0, 4), Arrays.copyOfRange(buf,
                            4, buf.length));
            returnData = new String(buf);
        } catch (IOException e) {
        } catch (Exception e) {
        } finally {
        }
        return returnData;
    }
    
    @Override
    public void connect() {
        System.out.println("Listening");
        try {
            this.socket = this.serverSocket.accept();
            System.out.println("Got connection");
            if (handshake()) {
                AbstractWebSocketServer.this.socketListenerThread.start();
            }
        } catch (NoSuchAlgorithmException e) {
        } catch (IOException e) {
        } catch (Exception e) {
        } finally {
        }
    }
    
    @Override
    public void disconnect() {
        System.out.println("Closing connection");
        try {
            this.socketListenerRunnable.terminate();
            this.socketListenerThread.join();
            this.socket.close();
        } catch (IOException e) {
        } catch (Exception e) {
        } finally {
        }
    }
    
    @Override
    public void sendMessage(byte[] msg) {
        System.out.println("Sending to client");
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final BufferedOutputStream os;
        try {
            os = new BufferedOutputStream(this.socket.getOutputStream());
            baos.write(SINGLE_FRAME_UNMASKED);
            baos.write(msg.length);
            baos.write(msg);
            baos.flush();
            baos.close();
            convertAndPrint(baos.toByteArray());
            os.write(baos.toByteArray(), 0, baos.size());
            os.flush();
        } catch (IOException e) {
        } catch (Exception e) {
        } finally {
        }
        
    }
}

class WebSocketServerImpl extends AbstractWebSocketServer {
    
    public WebSocketServerImpl() {
        super(2005);
    }
    
    public WebSocketServerImpl(int port) {
        super(port);
    }
    
}
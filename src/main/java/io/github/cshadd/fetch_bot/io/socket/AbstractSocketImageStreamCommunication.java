package io.github.cshadd.fetch_bot.io.socket;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.SocketException;
import javax.imageio.ImageIO;

// Main

/**
 * The Class AbstractSocketImageStreamCommunication. Defines what a Socket Image
 * Stream Communication is. A Socket Image Stream Communication is basically a
 * helper that directs data between image stream sockets and Fetch Bot.
 * 
 * @author Christian Shadd
 * @author Maria Verna Aquino
 * @author Thanh Vu
 * @author Joseph Damian
 * @author Giovanni Orozco
 * @since 2.0.0-alpha
 */
public abstract class AbstractSocketImageStreamCommunication extends
                AbstractSocketCommunication implements
                SocketImageStreamCommunication {
    // Private Constant Instance/Property Fields
    
    private static final String BOUNDARY = "mjpegframe";
    
    // Public Constructors
    
    public AbstractSocketImageStreamCommunication() {
        super();
    }
    
    // Private Methods
    
    private void listen() throws IOException {
        this.socket = this.serverSocket.accept();
    }
    
    // Public Methods (Overrided)
    
    @Override
    public synchronized void write(BufferedImage image)
                    throws SocketImageStreamCommunicationException {
        try {
            listen();
            if (this.socket != null) {
                if (!this.socket.isClosed()) {
                    
                    try (final BufferedReader br = new BufferedReader(
                                    new InputStreamReader(this.socket
                                                    .getInputStream()))) {
                        while (br.ready()) {
                            br.readLine();
                        }
                        try (final BufferedOutputStream bos = new BufferedOutputStream(
                                        this.socket.getOutputStream())) {
                            try (final ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                                final StringBuilder sb = new StringBuilder();
                                sb.append("HTTP/1.0 200 OK").append(CRLF);
                                sb.append("Connection: close").append(CRLF);
                                sb.append("Cache-Control: no-cache").append(
                                                CRLF);
                                sb.append("Cache-Control: private").append(
                                                CRLF);
                                sb.append("Pragma: no-cache").append(CRLF);
                                sb.append("Content-type: multipart/x-mixed-replace; boundary=--")
                                                .append(BOUNDARY).append(CRLF);
                                sb.append(CRLF);
                                bos.write(sb.toString().getBytes());
                                baos.reset();
                                ImageIO.write(image, "jpg", baos);
                                
                                sb.delete(0, sb.length());
                                sb.append("--").append(BOUNDARY).append(CRLF);
                                sb.append("Content-type: image/jpeg").append(
                                                CRLF);
                                sb.append("Content-Length: ").append(baos
                                                .size()).append(CRLF);
                                sb.append(CRLF);
                                
                                bos.write(sb.toString().getBytes());
                                bos.write(baos.toByteArray());
                                bos.write(CRLF.getBytes());
                                bos.flush();
                            } catch (IOException e) {
                                throw e;
                            } catch (Exception e) {
                                throw e;
                            } finally {
                                /* */ }
                        } catch (SocketException e) {
                            throw e;
                        } catch (IOException e) {
                            throw e;
                        } catch (Exception e) {
                            throw e;
                        } finally {
                            /* */ }
                    } catch (IOException e) {
                        throw e;
                    } catch (Exception e) {
                        throw e;
                    } finally {
                    }
                }
            }
        } catch (IOException e) {
            throw new SocketImageStreamCommunicationException(
                            "There was a problem with writing!", e);
        } catch (Exception e) {
            throw new SocketImageStreamCommunicationException("Unknown issue.",
                            e);
        } finally {
            /* */ }
    }
}
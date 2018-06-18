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
public abstract class AbstractSocketImageStreamCommunication extends
                AbstractSocketCommunication implements
                SocketImageStreamCommunication {
    // Public Constant Instance/Property Fields
    
    public static final int DEFAULT_SOCKET_PORT = 7676; // Change if needed
    
    // Private Constant Instance/Property Fields
    
    private static final String BOUNDARY = "mjpegframe";
    
    // Public Constructors
    
    public AbstractSocketImageStreamCommunication()
                    throws SocketCommunicationException {
        super();
        open(DEFAULT_SOCKET_PORT);
    }
    
    // Public Methods (Overrided)
    
    @Override
    public void write(BufferedImage image) throws SocketCommunicationException {
        listen();
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
                        sb.append("Cache-Control: no-cache").append(CRLF);
                        sb.append("Cache-Control: private").append(CRLF);
                        sb.append("Pragma: no-cache").append(CRLF);
                        sb.append("Content-type: multipart/x-mixed-replace; boundary=--")
                                        .append(BOUNDARY).append(CRLF);
                        sb.append(CRLF);
                        bos.write(sb.toString().getBytes());
                        baos.reset();
                        ImageIO.write(image, "jpg", baos);
                        
                        sb.delete(0, sb.length());
                        sb.append("--").append(BOUNDARY).append(CRLF);
                        sb.append("Content-type: image/jpeg").append(CRLF);
                        sb.append("Content-Length: ").append(baos.size())
                                        .append(CRLF);
                        sb.append(CRLF);
                        
                        bos.write(sb.toString().getBytes());
                        bos.write(baos.toByteArray());
                        bos.write(CRLF.getBytes());
                        bos.flush();
                    } catch (SocketException e) {
                        throw e;
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
            } catch (SocketException e) {
                throw new SocketCommunicationException(
                                "There was a problem with writing to the socket!",
                                e);
            } catch (IOException e) {
                throw new SocketCommunicationException(
                                "There was a problem with writing!", e);
            } catch (Exception e) {
                throw new SocketCommunicationException("Unknown issue.", e);
            } finally {
                /* */ }
        }
    }
}

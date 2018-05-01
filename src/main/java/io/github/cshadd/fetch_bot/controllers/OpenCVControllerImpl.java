package io.github.cshadd.fetch_bot.controllers;
// import static io.github.cshadd.fetch_bot.Core.delayThread;
import io.github.cshadd.fetch_bot.Component;
import io.github.cshadd.fetch_bot.io.WebInterfaceCommunicationImpl;
// import io.github.cshadd.fetch_bot.util.Logger;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.VideoWriter;

//Main
@Component("OpenCV")
public class OpenCVControllerImpl
implements OpenCVController {
    // Public Constant Instance/Property Fields
    public static final int CAMERA_PORT = 0; // Change if needed
    public static final String STREAM_PATH = WebInterfaceCommunicationImpl.TO_WEB_COMM_PATH + "/stream.avi";

    // Private Final Instance/Property Fields
    private final VideoCapture camera;
    private final int fourcc;
    
    // Private Instance/Property Fields
    // private BufferedImage cameraBufferImage;
    private VideoWriter cameraWriter;
    private Mat cameraFrame;
    private Thread cameraThread;
    private CameraThread cameraRunnable;
    
    // Public Constructors
    public OpenCVControllerImpl() {
        camera = new VideoCapture(CAMERA_PORT);
        cameraWriter = null;
        // cameraBufferImage = null;
        cameraFrame = new Mat();
        cameraRunnable = null;
        cameraThread = null;
        fourcc = VideoWriter.fourcc('M','J','P','G');
    }
    
    // Protected Final Nested Classes
    protected final class CameraThread
    implements Runnable {
        // Private Instance/Property Fields
        private volatile boolean running;

        // Private Constructors
        public CameraThread() {
            running = false;
        }
        
        // Public Methods
        public void terminate() {
            running = false;
            camera.release();
        }

        // Public Methods
        public BufferedImage MatToBufferedImage(Mat frame) {
            int type = 0;
            if (frame.channels() == 1) {
                type = BufferedImage.TYPE_BYTE_GRAY;
            }
            else if (frame.channels() == 3) {
                type = BufferedImage.TYPE_3BYTE_BGR;
            }
            final BufferedImage image = new BufferedImage(frame.width(), frame.height(), type);
            final WritableRaster raster = image.getRaster();
            final DataBufferByte dataBuffer = (DataBufferByte)raster.getDataBuffer();
            final byte[] data = dataBuffer.getData();
            frame.get(0, 0, data);

            return image;
        }
        public void saveImage(BufferedImage cameraBufferImage)
        throws ControllerException {
            File imageFile = null;
            try {
                imageFile = new File(WebInterfaceCommunicationImpl.TO_WEB_COMM_PATH + "/stream.png");
                ImageIO.write(cameraBufferImage, "png", imageFile);
            }
            catch (IOException e) {
                throw new ControllerException("There was an issue with IO!", e);
            }
            catch (Exception e) {
                throw new ControllerException("There was an unknown issue!", e);
            }
            finally { }
        }
        
        // Public Methods (Overrided)
        @Override
        public void run() {
            if (camera.isOpened()) {
                if (camera.read(cameraFrame)) {
                    running = true;
                    cameraWriter = new VideoWriter(STREAM_PATH, fourcc, 20, cameraFrame.size(), true);
                    while (running) {
                        if (camera.read(cameraFrame)) {
                            if (!cameraWriter.isOpened()) {
                                cameraWriter.write(cameraFrame);
                            }
                        }
                    }
                }
            }
            /*(while (running) {
                if (camera.isOpened()) {
                    if (camera.read(cameraFrame)) {
                        cameraBufferImage = MatToBufferedImage(cameraFrame);
                        try {
                            saveImage(cameraBufferImage);
                        }
                        catch (ControllerException e) {
                            Logger.warn(e, "There was an issue with Controller.");
                        }
                        catch (Exception e) {
                            Logger.warn(e, "There was an unknown issue!");                            
                        }
                        finally {
                            delayThread(500);
                        }
                    }
                }
            }*/
        }
    }
    
    // Public Static Methods
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }
    
    // Public Methods (Overrided)
    @Override
    public void startCamera() {
        cameraRunnable = new CameraThread();
        cameraThread = new Thread(cameraRunnable);
        cameraThread.start();
    }
    @Override
    public void stopCamera()
    throws InterruptedException {
        if (cameraThread != null) {
            cameraRunnable.terminate();
            cameraThread.join();
        }
    }
}
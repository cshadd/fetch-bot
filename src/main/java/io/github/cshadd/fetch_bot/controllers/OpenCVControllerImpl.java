package io.github.cshadd.fetch_bot.controllers;
import io.github.cshadd.fetch_bot.Component;
import io.github.cshadd.fetch_bot.io.WebInterfaceCommunicationImpl;
// import io.github.cshadd.fetch_bot.util.Logger;
// import java.awt.image.BufferedImage;
// import java.awt.image.DataBufferByte;
// import java.awt.image.WritableRaster;
// import java.io.File;
// import java.io.IOException;
// import javax.imageio.ImageIO;
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
    public static final String CAMERA_STREAM = WebInterfaceCommunicationImpl.TO_WEB_COMM_PATH + "/cam-stream.mp4";
    public static final int CAMERA_STREAM_FPS = 60;

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
        fourcc = VideoWriter.fourcc('X','2','6','4');
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
        }

        // Public Methods
        /*public BufferedImage MatToBufferedImage(Mat frame) {
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
                imageFile = new File(CAMERA_STREAM_PATH);
                ImageIO.write(cameraBufferImage, "jpg", imageFile);
            }
            catch (IOException e) {
                throw new ControllerException("There was an issue with IO!", e);
            }
            catch (Exception e) {
                throw new ControllerException("There was an unknown issue!", e);
            }
            finally { }
        }*/
        
        // Public Methods (Overrided)
        @Override
        public void run() {
            if (camera.isOpened()) {
                if (camera.read(cameraFrame)) {
                    running = true;
                    /*cameraBufferImage = MatToBufferedImage(cameraFrame);
                    while (running) {
                        if (camera.read(cameraFrame)) {
                            try {
                                saveImage(cameraBufferImage);
                            }
                            catch (ControllerException e) {
                                Logger.warn(e, "There was an issue with Controller.");
                            }
                            catch (Exception e) {
                                Logger.warn(e, "There was an unknown issue!");                            
                            }
                            finally { }   
                        }
                    }*/
                    cameraWriter = new VideoWriter(CAMERA_STREAM, fourcc, CAMERA_STREAM_FPS, cameraFrame.size(), true);
                    if (cameraWriter.isOpened()) {
                        while (running) {
                            if (camera.read(cameraFrame)) {
                                cameraWriter.write(cameraFrame);
                            }
                        }
                        camera.release();
                        cameraWriter.release();
                    }
                }
            }
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
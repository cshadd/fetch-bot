package io.github.cshadd.fetch_bot.controllers;
import static io.github.cshadd.fetch_bot.Core.delayThread;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

// Main
public abstract class AbstractOpenCVController
extends AbstractController
implements OpenCVController {
    // Public Constant Instance/Property Fields
    public static final int CAMERA_PORT = 0; // Change if needed

    // Protected Final Instance/Property Fields
    protected final Thread cameraThread;
    protected final CameraThread cameraRunnable;    
    
    // Private Final Instance/Property Fields
    private final VideoCapture camera;
    
    // Private Instance/Property Fields    
    private BufferedImage cameraBufferImage;
    private Mat cameraFrame;

    // Protected Constructors
    protected AbstractOpenCVController() {
        super();
        camera = new VideoCapture(CAMERA_PORT);
        cameraBufferImage = null;
        cameraFrame = new Mat();
        cameraRunnable = new CameraThread();
        cameraThread = new Thread(cameraRunnable);
    }
    
    // Protected Final Nested Classes
    protected final class CameraThread
    implements Runnable {
        // Private Instance/Property Fields
        private volatile boolean running;

        // Public Constructors
        public CameraThread() {
            running = false;
        }
        
        // Private Methods
        private BufferedImage MatToBufferedImage(Mat frame) {
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
        
        // Public Methods
        public void terminate() {
            running = false;
        }
        
        // Public Methods (Overrided)
        @Override
        public void run() {
            if (camera.isOpened()) {
                if (camera.read(cameraFrame)) {
                    running = true;
                    cameraBufferImage = MatToBufferedImage(cameraFrame);
                    while (running) {
                        if (camera.read(cameraFrame)) {
                            delayThread(1000);
                        }
                    }
                    camera.release();
                }
            }
        }
    }
    
    // Public Static Methods
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }
}



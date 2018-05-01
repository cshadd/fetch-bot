package io.github.cshadd.fetch_bot.controllers;
import static io.github.cshadd.fetch_bot.Core.delayThread;
import io.github.cshadd.fetch_bot.Component;
import java.awt.image.BufferedImage;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

//Main
@Component("OpenCV")
public class OpenCVControllerImpl
implements OpenCVController {
    // Public Constant Instance/Property Fields
    public static final int CAMERA_PORT = 0; // Change if needed

    // Private Final Instance/Property Fields
    private final VideoCapture camera;
    private final BufferedImage cameraBufferImage;    
    private final Mat cameraFrame;
    
    // Public Constructors
    public OpenCVControllerImpl() {
        camera = new VideoCapture(CAMERA_PORT);
        cameraBufferImage = null;
        cameraFrame = new Mat();
    }
    
    // Protected Static Final Nested Classes
    protected static final class CameraThread extends Thread {
        public void run() {
            while (true) {
                System.out.println("Test");
                delayThread(1000);
            }
        }
    }
    
    // Public Static Methods
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }
    
    // Public Methods
    @Override
    public void startCamera() {
        (new CameraThread()).start();
    }
}
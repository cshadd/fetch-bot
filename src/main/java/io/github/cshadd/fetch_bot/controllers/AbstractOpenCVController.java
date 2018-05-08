package io.github.cshadd.fetch_bot.controllers;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.OverlayLayout;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.dnn.Dnn;
import org.opencv.dnn.Net;
import org.opencv.videoio.VideoCapture;

// Main
public abstract class AbstractOpenCVController
extends AbstractController
implements OpenCVController {
    // Public Constant Instance/Property Fields
    public static final int CAMERA_PORT = 1; // Change if needed
    public static final int CONFIDENCE_LIMIT = 90;
    public static final String MOBILENETSSD_DEPLOY_CAFFEMODEL = "MobileNetSSD_deploy.caffemodel";
    public static final String MOBILENETSSD_DEPLOY_PROTOTXT_TXT = "MobileNetSSD_deploy.prototxt.txt";
    public static final double SCALE_FACTOR = 0.007;
    public static final int SIZE = 300;
    
    // Protected Final Instance/Property Fields
    protected final Thread cameraThread;
    protected final CameraThread cameraRunnable;    
    
    // Private Final Instance/Property Fields
    private final VideoCapture camera;
    private final JFrame terminalFrame;
    private final LayoutManager terminalOverlayLayout;
    private final JPanel terminalPanel;
    private final TrackClasses trackClassArr[];

    // Protected Instance/Property Fields
    protected String trackClass;
    protected boolean trackClassFound;
    
    // Private Instance/Property Fields
    private enum TrackClasses
    {
        BACKGROUND,
        AEROPLANE,
        BICYCLE,
        BIRD,
        BOAT,
        BOTTLE,
        BUS,
        CAR,
        CAT,
        CHAIR,
        COW,
        DININGTABLE,
        DOG,
        HORSE,
        MOTORBIKE,
        PERSON,
        POTTEDPLANT,
        SHEEP,
        SOFA,
        TRAIN,
        TVMONITOR
    }
    private Mat cameraFrame;
    private BufferedImage buffer;
    private BufferedImage redBuffer;
    private Mat det;
    private Net net;
    private JLabel terminalImageLabel;
    private JLabel terminalStatusLabel;
    
    // Protected Constructors
    protected AbstractOpenCVController() {
        super();
        buffer = null;
        redBuffer = null;
        camera = new VideoCapture(CAMERA_PORT);
        cameraFrame = new Mat();
        cameraRunnable = new CameraThread();
        cameraThread = new Thread(cameraRunnable);
        net = Dnn.readNetFromCaffe(MOBILENETSSD_DEPLOY_PROTOTXT_TXT, MOBILENETSSD_DEPLOY_CAFFEMODEL);
        
        terminalFrame = new JFrame("Fetch Bot OpenCVController Terminal");
        terminalFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        terminalFrame.setResizable(false);
        terminalFrame.setLayout(new BorderLayout());
        terminalFrame.setSize(SIZE, SIZE);
        terminalFrame.setLocationByPlatform(true);
        
        terminalPanel = new JPanel();
        terminalOverlayLayout = new OverlayLayout(terminalPanel);
        terminalPanel.setLayout(terminalOverlayLayout);
       
        terminalStatusLabel = new JLabel(">Status: Unknown");
        terminalStatusLabel.setForeground(Color.WHITE);
        terminalStatusLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        terminalPanel.add(terminalStatusLabel);
        
        terminalImageLabel = new JLabel();        
        terminalPanel.add(terminalImageLabel);
        
        terminalFrame.add(terminalPanel);
        terminalFrame.setVisible(true);
        trackClassArr = TrackClasses.values();
        trackClass = "";
        trackClassFound = false;
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
        
        // Public Methods
        public void terminate() {
            buffer.flush();
            redBuffer.flush();
            running = false;
        }
        
        // Public Methods (Overrided)
        @Override
        public void run() {
            if (camera.isOpened()) {
                if (camera.read(cameraFrame)) {
                    running = true;
                    while (running) {
                        if (camera.read(cameraFrame)) {
                            detections(cameraFrame);
                        }
                    }
                    camera.release();
                }
            }
        }
    }
    
    // Private Static Methods
    private static BufferedImage matToBufferedImage(Mat mat) {
        int type = 0;
        if (mat.channels() == 1) {
            type = BufferedImage.TYPE_BYTE_GRAY;
        }
        else if (mat.channels() == 3) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        final BufferedImage image = new BufferedImage(mat.width(), mat.height(), type);
        final WritableRaster raster = image.getRaster();
        final DataBufferByte dataBuffer = (DataBufferByte)raster.getDataBuffer();
        final byte[] data = dataBuffer.getData();
        mat.get(0, 0, data);

        return image;
    }
    private static BufferedImage redFilter(BufferedImage img) {
        for (int i = 0; i < img.getHeight(); i++) {
            for (int i2 = 0; i2 < img.getWidth(); i2++) {
                final Color c = new Color(img.getRGB(i2, i));
                final int red = (int)(c.getRed() * 0.3);
                final int green = (int)(c.getGreen() * 0.1);
                final int blue = (int)(c.getBlue() * 0.1);
                final Color newColor = new Color(red + green + blue, green, blue);
                img.setRGB(i2, i, newColor.getRGB());
            }
        }
        return img;
    }

    
    // Public Static Methods
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    // Private Methods
    private void detections(Mat mat)
    {
        buffer = matToBufferedImage(mat);
        redBuffer = redFilter(matToBufferedImage(mat));

        final ImageIcon img = new ImageIcon(redBuffer);
        terminalImageLabel.setIcon(img);
        /*
         * Converts the image matrix into a "blob," which is then fed into the Caffe neural network using net.setInput()
         *         and detections are output as a matrix in net.forward().
         */
        final Mat blob = Dnn.blobFromImage(mat, SCALE_FACTOR, new Size(SIZE, SIZE), new Scalar(SIZE, SIZE), false, false);
        net.setInput(blob);
        det = net.forward();
        /* 
         * For each detection, there are 7 numbers the matrix det outputs. The first is always 0 for some unknown reason, the second is the object it
         * detects (output as the index from the Classes enum (which is why you shouldn't change the order of the classes)), the third is the confidence
         * the network has that the object is what they predicted it was, and the fourth to seventh numbers are the starting x position, starting y
         * position, ending x position, and ending y position of the object respectively. They are represented proportionally to the image, so you must
         * multiply them by the size of the image to get the actual location of the object with respects to the image. This for loop then draws a
         * rectangle around each detection and text identifying each image.
         */
        for (int i = 0; i < det.reshape(1, 1).size().width; i += 7)
        {
            final int index = (int)det.reshape(1, 1).get(0, i + 1)[0];
            final String currentTrackClass = trackClassArr[index].toString();
            final double con = det.reshape(1, 1).get(0, i + 2)[0];
            final int fullCon = (int)(100*con);
            terminalStatusLabel.setText(">Status: Track Class= ; Current=" + currentTrackClass + " [" + fullCon + "%]");
            if (fullCon >= CONFIDENCE_LIMIT && trackClass.equals(currentTrackClass)) {
                trackClassFound = true;
            }
            else {
                trackClassFound = false;
            }
        }
    }
}
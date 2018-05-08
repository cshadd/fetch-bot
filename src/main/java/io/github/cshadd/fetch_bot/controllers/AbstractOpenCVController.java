package io.github.cshadd.fetch_bot.controllers;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import org.apache.commons.io.FileUtils;
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
    // Private Constant Instance/Property Fields
    private static final int CAMERA_PORT = 0; // Change if needed
    private static final int CONFIDENCE_LIMIT = 90;
    private static final String DEPLOY_PATH = "./libs/fetch-bot/";
    private static final String DEPLOY_CAFFEMODEL = DEPLOY_PATH + "deploy.caffemodel";
    private static final String DEPLOY_PROTOTXT_TXT = DEPLOY_PATH + "deploy.prototxt.txt";
    private static final String DEPLOY_TRACK_CLASSES = DEPLOY_PATH + "deploy.track.classes";
    private static final double SCALE_FACTOR = 0.007843;
    private static final int SCENE_W = 640;
    private static final int SCENE_H = 480;
    private static final int SIZE = 300;

    // Private Final Instance/Property Fields
    private final VideoCapture camera;
    // private final TrackClasses trackClassArr[];
    private final List<String> trackClasses;
    
    // Protected Final Instance/Property Fields
    protected final Thread cameraThread;
    protected final CameraThread cameraRunnable;    

    // Private Instance/Property Fields
    /*private enum TrackClasses
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
    }*/
    private Mat cameraFrame;
    private BufferedImage buffer;
    private Mat det;
    private Net net;
    private JComponent terminalContent;
    private JFrame terminalFrame;
    private JLabel terminalLabelCam;
    private JLabel terminalLabelCamFilter;
    private JLabel terminalLabelStatus;
    private JLabel terminalLabelTrack;
    private JLayeredPane terminalLayerPane;

    // Protected Instance/Property Fields
    protected String trackClass;
    protected boolean trackClassFound;
    
    // Protected Constructors
    protected AbstractOpenCVController()
    throws OpenCVControllerException {
        super();
        buffer = null;
        camera = new VideoCapture(CAMERA_PORT);
        cameraFrame = new Mat();
        cameraRunnable = new CameraThread();
        cameraThread = new Thread(cameraRunnable);
        det = null;
        try {
            net = Dnn.readNetFromCaffe(DEPLOY_PROTOTXT_TXT, DEPLOY_CAFFEMODEL);
            final File trackClassesInput = new File(DEPLOY_TRACK_CLASSES);
            trackClasses = FileUtils.readLines(trackClassesInput, "UTF-8");
        }
        catch (Exception e) {
            throw new OpenCVControllerException("There was an unknown issue!", e);
        }

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                terminalLabelCam = new JLabel();
                terminalLabelCam.setVerticalAlignment(JLabel.TOP);
                terminalLabelCam.setHorizontalAlignment(JLabel.LEFT);
                terminalLabelCam.setOpaque(false);
                terminalLabelCam.setBounds(0, 0, SCENE_W, SCENE_H);
                
                terminalLabelCamFilter = new JLabel();
                terminalLabelCamFilter.setVerticalAlignment(JLabel.TOP);
                terminalLabelCamFilter.setHorizontalAlignment(JLabel.LEFT);
                terminalLabelCamFilter.setOpaque(true);
                terminalLabelCamFilter.setBackground(new Color(255, 0, 0, 150));
                terminalLabelCamFilter.setBounds(0, 0, SCENE_W, SCENE_H);
                
                terminalLabelStatus = new JLabel();
                terminalLabelStatus.setVerticalAlignment(JLabel.BOTTOM);
                terminalLabelStatus.setHorizontalAlignment(JLabel.LEFT);
                terminalLabelStatus.setOpaque(false);
                terminalLabelStatus.setBounds(0, 0, SCENE_W, SCENE_H);

                terminalLabelTrack = new JLabel();
                terminalLabelTrack.setVerticalAlignment(JLabel.TOP);
                terminalLabelTrack.setHorizontalAlignment(JLabel.LEFT);
                terminalLabelTrack.setOpaque(false);
                terminalLabelTrack.setBorder(BorderFactory.createLineBorder(Color.white));
                
                terminalLayerPane = new JLayeredPane();
                terminalLayerPane.setPreferredSize(new Dimension(SCENE_W, SCENE_H));
                terminalLayerPane.add(terminalLabelStatus, 0);
                terminalLayerPane.add(terminalLabelTrack, 1);
                terminalLayerPane.add(terminalLabelCamFilter, 2);
                terminalLayerPane.add(terminalLabelCam, 3);
                
                terminalContent = new JPanel();
                terminalContent.setLayout(new BoxLayout(terminalContent, BoxLayout.PAGE_AXIS));
                terminalContent.setOpaque(true);
                terminalContent.add(terminalLayerPane);
                
                terminalFrame = new JFrame("Fetch Bot OpenCVController Terminal");
                terminalFrame.setContentPane(terminalContent);
                terminalFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                terminalFrame.setLocationByPlatform(true);
                terminalFrame.setResizable(false);

                terminalFrame.pack();
                terminalFrame.setVisible(true);
            }
        });
        // trackClassArr = TrackClasses.values();
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
    
    // Public Static Methods
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    // Private Methods
    private void detections(Mat mat)
    {
        buffer = matToBufferedImage(mat);
        final ImageIcon img = new ImageIcon(buffer);
        terminalLabelCam.setIcon(img);
        /*
         * Converts the image matrix into a "blob," which is then fed into the Caffe neural network using net.setInput()
         * and detections are output as a matrix in net.forward().
         */
        final Mat blob = Dnn.blobFromImage(mat, SCALE_FACTOR, new Size(SIZE, SIZE), new Scalar(SIZE, SIZE), false, false);
        net.setInput(blob);
        det = net.forward();
        /* 
         * For each detection, there are 7 numbers the matrix det outputs. The first is always 0 for some unknown reason, the second is the object it
         * detects (output as the index from the TrackClasses enum (which is why you shouldn't change the order of the classes)), the third is the confidence
         * the network has that the object is what they predicted it was, and the fourth to seventh numbers are the starting x position, starting y
         * position, ending x position, and ending y position of the object respectively. They are represented proportionally to the image, so you must
         * multiply them by the size of the image to get the actual location of the object with respects to the image. This for loop then draws a
         * rectangle around each detection and text identifying each image.
         */
        for (int i = 0; i < det.reshape(1, 1).size().width; i += 7)
        {
            final int index = (int)det.reshape(1, 1).get(0, i + 1)[0];
            final double con = det.reshape(1, 1).get(0, i + 2)[0];
            final int startX = (int)(det.reshape(1, 1).get(0, i + 3)[0]*SCENE_W);
            final int startY = (int)(det.reshape(1, 1).get(0, i + 4)[0]*SCENE_H);
            final int endX = (int)(det.reshape(1, 1).get(0, i + 5)[0]*SCENE_W);
            final int endY = (int)(det.reshape(1, 1).get(0, i + 6)[0]*SCENE_H);
            final String capturedTrackClass;
            final int fullCon = (int)(100*con);
            
            if (index > 0 && index < trackClasses.size()) {
                capturedTrackClass = trackClasses.get(index);
            }
            else {
                capturedTrackClass = "???"; 
            }
            
            if (fullCon >= CONFIDENCE_LIMIT && trackClass.equals(capturedTrackClass)) {
                trackClassFound = true;
                terminalLabelTrack.setText("<html><p style='color: white;'>" + capturedTrackClass
                        + " [" + fullCon + "%]<br />"
                        + "TARGET</p></html>");
            }
            else {
                trackClassFound = false;
                terminalLabelTrack.setText("<html><p style='color: white;'>" + capturedTrackClass
                        + " [" + fullCon + "%]</p></html>");
            }
            terminalLabelStatus.setText("<html><p style='color: white; font-size: 20px'>&#187; Status: Processing<br />"
                    + "Target: " + trackClass + "<br />"
                    + "Found Target: " + trackClassFound + "<br />"
                    + "Raw: " + buffer.hashCode() + "</p></html>");
            terminalLabelTrack.setBounds(startX, startY, endX - startX, endY - startY);
        }
    }
}
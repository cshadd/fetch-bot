package io.github.cshadd.fetch_bot.controllers;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

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
    private static final int CONFIDENCE_LIMIT = 90;
    private static final int DEFAULT_CAMERA_PORT = 0;
    private static final String DEPLOY_PATH = "./libs/fetch-bot/";
    private static final String DEPLOY_CAFFEMODEL_FILE = DEPLOY_PATH + "deploy.caffemodel";
    private static final String DEPLOY_PROTOTXT_TXT_FILE = DEPLOY_PATH + "deploy.prototxt.txt";
    private static final String DEPLOY_TRACK_CLASSES_FILE = DEPLOY_PATH + "deploy.track.classes";
    private static final double SCALE_FACTOR = 0.007843;
    private static final int SCENE_W = 640;
    private static final int SCENE_H = 480;
    private static final int SIZE = 300;

    // Private Final Instance/Property Fields
    private final List<String> trackClasses;
    
    // Protected Final Instance/Property Fields
    protected final VideoCapture camera;
    protected final int cameraPort;
    protected final Thread cameraThread;
    protected final CameraThread cameraRunnable;
    
    // Private Instance/Property Fields
    private String capturedLabel;
    private Mat det;
    private int endX;
    private int endY;
    private Net net;
    private int startX;
    private int startY;

    // Protected Instance/Property Fields
    protected BufferedImage buffer;
    protected Mat cameraFrame;
    protected JComponent terminalContent;
    protected JFrame terminalFrame;
    protected JLabel terminalLabelCam;
    protected JLabel terminalLabelCamFilter;
    protected JLabel terminalLabelStatus;
    protected JLabel terminalLabelTrack;
    protected JLayeredPane terminalLayerPane;
    protected String trackClass;
    protected boolean trackClassFound;
    
    // Protected Constructors
    protected AbstractOpenCVController()
    throws OpenCVControllerException {
        this(DEFAULT_CAMERA_PORT);
    }
    protected AbstractOpenCVController(int newCameraPort)
    throws OpenCVControllerException {
        super();
        this.buffer = null;
        this.cameraPort = newCameraPort;
        this.camera = new VideoCapture(this.cameraPort);
        this.cameraFrame = new Mat();
        this.cameraRunnable = new CameraThread();
        this.cameraThread = new Thread(this.cameraRunnable);
        this.capturedLabel = "";
        this.det = null;
        this.endX = 0;
        this.endY = 0;
        try {
            this.net = Dnn.readNetFromCaffe(DEPLOY_PROTOTXT_TXT_FILE, DEPLOY_CAFFEMODEL_FILE);
            final File trackClassesInput = new File(DEPLOY_TRACK_CLASSES_FILE);
            this.trackClasses = FileUtils.readLines(trackClassesInput, "UTF-8");
        }
        catch (IOException e) {
            throw new OpenCVControllerException("Could not read neural network files.", e);
        }
        catch (Exception e) {
            throw new OpenCVControllerException("Unknown issue.", e);
        }
        finally { /* */ }
        this.trackClass = "None";
        this.trackClassFound = false;
        this.startX = 0;
        this.startY = 0;

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            // Public Methods (Overrided)
            @Override
            public void run() {
                AbstractOpenCVController.this.terminalLabelCam = new JLabel();
                AbstractOpenCVController.this.terminalLabelCam.setVerticalAlignment(SwingConstants.TOP);
                AbstractOpenCVController.this.terminalLabelCam.setHorizontalAlignment(SwingConstants.LEFT);
                AbstractOpenCVController.this.terminalLabelCam.setOpaque(false);
                AbstractOpenCVController.this.terminalLabelCam.setBounds(0, 0, SCENE_W, SCENE_H);
                
                AbstractOpenCVController.this.terminalLabelCamFilter = new JLabel();
                AbstractOpenCVController.this.terminalLabelCamFilter.setVerticalAlignment(SwingConstants.TOP);
                AbstractOpenCVController.this.terminalLabelCamFilter.setHorizontalAlignment(SwingConstants.LEFT);
                AbstractOpenCVController.this.terminalLabelCamFilter.setOpaque(true);
                AbstractOpenCVController.this.terminalLabelCamFilter.setBackground(new Color(255, 0, 0, 150));
                AbstractOpenCVController.this.terminalLabelCamFilter.setBounds(0, 0, SCENE_W, SCENE_H);
                
                AbstractOpenCVController.this.terminalLabelStatus = new JLabel();
                AbstractOpenCVController.this.terminalLabelStatus.setVerticalAlignment(SwingConstants.BOTTOM);
                AbstractOpenCVController.this.terminalLabelStatus.setHorizontalAlignment(SwingConstants.LEFT);
                AbstractOpenCVController.this.terminalLabelStatus.setOpaque(false);
                AbstractOpenCVController.this.terminalLabelStatus.setBounds(0, 0, SCENE_W, SCENE_H);

                AbstractOpenCVController.this.terminalLabelTrack = new JLabel();
                AbstractOpenCVController.this.terminalLabelTrack.setVerticalAlignment(SwingConstants.TOP);
                AbstractOpenCVController.this.terminalLabelTrack.setHorizontalAlignment(SwingConstants.LEFT);
                AbstractOpenCVController.this.terminalLabelTrack.setOpaque(false);
                AbstractOpenCVController.this.terminalLabelTrack.setBorder(BorderFactory.createLineBorder(Color.white));
                
                AbstractOpenCVController.this.terminalLayerPane = new JLayeredPane();
                AbstractOpenCVController.this.terminalLayerPane.setPreferredSize(new Dimension(SCENE_W, SCENE_H));
                AbstractOpenCVController.this.terminalLayerPane.add(AbstractOpenCVController.this.terminalLabelStatus, 0);
                AbstractOpenCVController.this.terminalLayerPane.add(AbstractOpenCVController.this.terminalLabelTrack, 1);
                AbstractOpenCVController.this.terminalLayerPane.add(AbstractOpenCVController.this.terminalLabelCamFilter, 2);
                AbstractOpenCVController.this.terminalLayerPane.add(AbstractOpenCVController.this.terminalLabelCam, 3);
                
                AbstractOpenCVController.this.terminalContent = new JPanel();
                AbstractOpenCVController.this.terminalContent.setLayout(new BoxLayout(AbstractOpenCVController.this.terminalContent, BoxLayout.PAGE_AXIS));
                AbstractOpenCVController.this.terminalContent.setOpaque(true);
                AbstractOpenCVController.this.terminalContent.add(AbstractOpenCVController.this.terminalLayerPane);
                
                AbstractOpenCVController.this.terminalFrame = new JFrame("Fetch Bot OpenCVController Terminal");
                AbstractOpenCVController.this.terminalFrame.setContentPane(AbstractOpenCVController.this.terminalContent);
                AbstractOpenCVController.this.terminalFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                AbstractOpenCVController.this.terminalFrame.setLocationByPlatform(true);
                AbstractOpenCVController.this.terminalFrame.setResizable(false);

                AbstractOpenCVController.this.terminalFrame.pack();
                AbstractOpenCVController.this.terminalFrame.setVisible(true);
            }
        });
    }

    // Protected Final Nested Classes
    protected final class CameraThread
    implements Runnable {
        // Private Instance/Property Fields
        private volatile boolean running;

        // Public Constructors
        public CameraThread() {
            this.running = false;
        }
        
        // Public Methods
        public void terminate() {
            AbstractOpenCVController.this.buffer.flush();
            this.running = false;
        }
        
        // Public Methods (Overrided)
        @Override
        public void run() {
            if (AbstractOpenCVController.this.camera.isOpened()) {
                if (AbstractOpenCVController.this.camera.read(AbstractOpenCVController.this.cameraFrame)) {
                    this.running = true;
                    while (this.running) {
                        if (AbstractOpenCVController.this.camera.read(AbstractOpenCVController.this.cameraFrame)) {
                            detections(AbstractOpenCVController.this.cameraFrame);
                        }
                    }
                    AbstractOpenCVController.this.camera.release();
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

    // Protected Methods
    protected void detections(Mat mat)
    {
        this.buffer = matToBufferedImage(mat);
        final ImageIcon img = new ImageIcon(this.buffer);
        /*
         * Converts the image matrix into a "blob," which is then fed into the Caffe neural network using net.setInput()
         * and detections are output as a matrix in net.forward().
         */
        final Mat blob = Dnn.blobFromImage(mat, SCALE_FACTOR, new Size(SIZE, SIZE), new Scalar(SIZE, SIZE), false, false);
        this.net.setInput(blob);
        this.det = this.net.forward();
        /* 
         * For each detection, there are 7 numbers the matrix det outputs. The first is always 0 for some unknown reason, the second is the object it
         * detects (output as the index from the TrackClasses enum (which is why you shouldn't change the order of the classes)), the third is the confidence
         * the network has that the object is what they predicted it was, and the fourth to seventh numbers are the starting x position, starting y
         * position, ending x position, and ending y position of the object respectively. They are represented proportionally to the image, so you must
         * multiply them by the size of the image to get the actual location of the object with respects to the image. This for loop then draws a
         * rectangle around each detection and text identifying each image.
         */
        for (int i = 0; i < this.det.reshape(1, 1).size().width; i += 7)
        {
            final int index = (int)this.det.reshape(1, 1).get(0, i + 1)[0];
            final double con = this.det.reshape(1, 1).get(0, i + 2)[0];
            final String capturedTrackClass;
            final int fullCon = (int)(100*con);
            
            this.startX = (int)(this.det.reshape(1, 1).get(0, i + 3)[0]*SCENE_W);
            this.startY = (int)(this.det.reshape(1, 1).get(0, i + 4)[0]*SCENE_H);
            this.endX = (int)(this.det.reshape(1, 1).get(0, i + 5)[0]*SCENE_W);
            this.endY = (int)(this.det.reshape(1, 1).get(0, i + 6)[0]*SCENE_H);
            
            if (index > 0 && index < this.trackClasses.size()) {
                capturedTrackClass = this.trackClasses.get(index);
            }
            else {
                capturedTrackClass = "???"; 
            }
            
            if (fullCon >= CONFIDENCE_LIMIT && capturedTrackClass.equals(this.trackClass)) {
                this.trackClassFound = true;
                this.capturedLabel = "<html><p style='color: white;'>" + capturedTrackClass
                        + " [" + fullCon + "%]<br />"
                        + "TARGET</p></html>";
            }
            else {
                this.trackClassFound = false;
                this.capturedLabel = "<html><p style='color: white;'>" + capturedTrackClass
                        + " [" + fullCon + "%]</p></html>";
            }
        }
        this.terminalLabelCam.setIcon(img);
        this.terminalLabelStatus.setText("<html><p style='color: white; font-size: 20px'>&#187; Status: Processing<br />"
                + "Target: " + this.trackClass + "<br />"
                + "Found Target: " + this.trackClassFound + "<br />"
                + "Raw I/O Buffer: " + this.buffer.hashCode() + "</p></html>");
        this.terminalLabelTrack.setBounds(this.startX, this.startY, this.endX - this.startX, this.endY - this.startY);
        this.terminalLabelTrack.setText(this.capturedLabel);
    }
}
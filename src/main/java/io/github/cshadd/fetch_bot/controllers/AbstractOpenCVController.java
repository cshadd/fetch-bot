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
package io.github.cshadd.fetch_bot.controllers;

import io.github.cshadd.fetch_bot.References;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.swing.ImageIcon;
import org.apache.commons.io.FileUtils;
import org.opencv.core.Core;
import org.opencv.core.CvException;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.dnn.Dnn;
import org.opencv.dnn.Net;
import org.opencv.videoio.VideoCapture;

// Main

/**
 * The Class AbstractOpenCVController. Defines what an OpenCV Controller is. An
 * OpenCV Controller is basically a manager for OpenCV tasks in Fetch Bot.
 * 
 * @author Christian Shadd
 * @author Maria Verna Aquino
 * @author Thanh Vu
 * @author Joseph Damian
 * @author Giovanni Orozco
 * @since 1.0.0
 */
public abstract class AbstractOpenCVController extends AbstractController
                implements OpenCVController {
    // Private Constant Instance/Property Fields
    
    /**
     * The Constant CONFIDENCE_LIMIT.
     */
    private static final int CONFIDENCE_LIMIT = 90;
    
    /**
     * The Constant DEPLOY_CAFFEMODEL_FILE.
     */
    private static final String DEPLOY_CAFFEMODEL_FILE = References.PROGRAM_LIBRARY_PATH
                    + "deploy.caffemodel";
    
    /**
     * The Constant DEPLOY_PROTOTXT_TXT_FILE.
     */
    private static final String DEPLOY_PROTOTXT_TXT_FILE = References.PROGRAM_LIBRARY_PATH
                    + "deploy.prototxt.txt";
    
    /**
     * The Constant DEPLOY_TRACK_CLASSES_FILE.
     */
    private static final String DEPLOY_TRACK_CLASSES_FILE = References.PROGRAM_LIBRARY_PATH
                    + "deploy.track.classes";
    
    /**
     * The Constant SCALE_FACTOR.
     */
    private static final double SCALE_FACTOR = 0.007843;
    
    /**
     * The Constant SIZE.
     */
    private static final int SIZE = 300;
    
    // Private Final Instance/Property Fields
    
    /**
     * The track classes.
     */
    private final List<String> trackClasses;
    
    // Protected Final Instance/Property Fields
    
    /**
     * The camera.
     */
    protected final VideoCapture camera;
    
    /**
     * The camera thread.
     */
    protected final Thread cameraThread;
    
    /**
     * The camera runnable thread.
     */
    protected final CameraThread cameraRunnable;
    
    // Private Instance/Property Fields
    
    /**
     * The neural network.
     */
    private Net net;
    
    // Protected Instance/Property Fields
    
    protected ImageIcon cameraImageIconBuffer;
    protected String    cameraStatusBuffer;
    protected int[]     cameraTrackBoundBuffer;
    protected String    cameraTrackCaptureLabelBuffer;
    
    /**
     * The track class.
     */
    protected String trackClass;
    
    /**
     * The track class found state.
     */
    protected boolean trackClassFound;
    
    // Protected Constructors
    
    /**
     * Instantiates a new Abstract OpenCV Controller.
     *
     * @throws OpenCVControllerException
     *             if OpenCV could not load
     */
    protected AbstractOpenCVController() throws OpenCVControllerException {
        this(References.CAMERA_PORT);
    }
    
    protected AbstractOpenCVController(int newCameraPort)
                    throws OpenCVControllerException {
        super();
        try {
            this.camera = new VideoCapture(newCameraPort);
        } catch (CvException e) {
            throw new OpenCVControllerException("Could not open camera.", e);
        } catch (Exception e) {
            throw new OpenCVControllerException("Unknown issue.", e);
        } finally {
            /* */ }
        this.cameraImageIconBuffer = null;
        this.cameraRunnable = new CameraThread();
        this.cameraStatusBuffer = "";
        this.cameraThread = new Thread(this.cameraRunnable);
        final int[] initialTrackBounds = { 0, 0, 0, 0 };
        this.cameraTrackBoundBuffer = initialTrackBounds;
        this.cameraTrackCaptureLabelBuffer = "";
        try {
            if (!new File(DEPLOY_CAFFEMODEL_FILE).exists() || !new File(
                            DEPLOY_PROTOTXT_TXT_FILE).exists() || !new File(
                                            DEPLOY_TRACK_CLASSES_FILE)
                                                            .exists()) { throw new CvException(
                                                                            "Missing neural network files."); }
            this.net = Dnn.readNetFromCaffe(DEPLOY_PROTOTXT_TXT_FILE,
                            DEPLOY_CAFFEMODEL_FILE);
            final File trackClassesInput = new File(DEPLOY_TRACK_CLASSES_FILE);
            this.trackClasses = FileUtils.readLines(trackClassesInput, "UTF-8");
        } catch (CvException e) {
            throw new OpenCVControllerException(
                            "Could not load neural network.", e);
        } catch (IOException e) {
            throw new OpenCVControllerException(
                            "Could not load neural network.", e);
        } catch (Exception e) {
            throw new OpenCVControllerException("Unknown issue.", e);
        } finally {
            /* */ }
        this.trackClass = "None";
        this.trackClassFound = false;
    }
    
    // Protected Final Nested Classes
    
    /**
     * The Class CameraThread. A Runnable that controls the camera.
     * 
     * @author Christian Shadd
     * @author Maria Verna Aquino
     * @author Thanh Vu
     * @author Joseph Damian
     * @author Giovanni Orozco
     * @since 1.0.0
     */
    protected final class CameraThread implements Runnable {
        // Private Instance/Property Fields
        
        /**
         * The running state.
         */
        private volatile boolean running;
        
        // Public Constructors
        
        /**
         * Instantiates a new Camera Thread.
         */
        public CameraThread() {
            super();
            this.running = false;
        }
        
        // Public Methods
        
        /**
         * Terminate the thread.
         */
        public void terminate() {
            this.running = false;
            AbstractOpenCVController.this.camera.release();
        }
        
        // Public Methods (Overrided)
        
        /**
         * Runs the camera with each frame being processed.
         * 
         * @see java.lang.Runnable#run()
         */
        @Override
        public void run() {
            if (AbstractOpenCVController.this.camera.isOpened()) {
                this.running = true;
                while (this.running) {
                    final Mat frame = new Mat();
                    if (AbstractOpenCVController.this.camera.read(frame)) {
                        process(frame);
                    }
                }
            }
        }
    }
    
    // Private Static Methods
    
    static {
        /*
         * We use this to load the OpenCV library from the system. This
         * requires
         * the jar file with the Java bindings.
         */
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }
    
    /**
     * Mat to buffered image.
     *
     * @param mat
     *            the mat
     * @return the buffered image
     */
    private static BufferedImage matToBufferedImage(Mat mat) {
        int type = 0;
        if (mat.channels() == 1) {
            type = BufferedImage.TYPE_BYTE_GRAY;
        } else if (mat.channels() == 3) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        final BufferedImage image = new BufferedImage(mat.width(), mat.height(),
                        type);
        final WritableRaster raster = image.getRaster();
        final DataBufferByte dataBuffer = (DataBufferByte) raster
                        .getDataBuffer();
        final byte[] data = dataBuffer.getData();
        mat.get(0, 0, data);
        
        return image;
    }
    
    // Protected Methods
    
    /**
     * Processes the detections and data from the matrix from the mat. The mat
     * is loaded from the buffer of frames from the camera thread.
     * 
     * The process follows:<br />
     * 1. Convert the image matrix into a "blob".<br />
     * 2. Feed "blob" into Caffe neural network.<br />
     * 3. Detections are outputted as matrix.<br />
     * <table border="1">
     * <tr>
     * <th>Index</th>
     * <th>Value</th>
     * </tr>
     * <tr>
     * <td>0</td>
     * <td>Always 0 (?)</td>
     * </tr>
     * <tr>
     * <td>1</td>
     * <td>Detected object</td>
     * </tr>
     * <tr>
     * <td>2</td>
     * <td>Confidence level of detection</td>
     * </tr>
     * <tr>
     * <td>3</td>
     * <td>Starting X coordinate of detection</td>
     * </tr>
     * <tr>
     * <td>4</td>
     * <td>Starting Y coordinate of detection</td>
     * </tr>
     * <tr>
     * <td>5</td>
     * <td>Ending X coordinate of detection</td>
     * </tr>
     * <tr>
     * <td>6</td>
     * <td>Ending Y coordinate of detection</td>
     * </tr>
     * </table>
     * this.startX, this.startY, this.endX
     * - this.startX, this.endY - this.startY
     * <br />
     * 4. Draw terminals in the terminal for each detection.
     *
     *
     * @param mat
     *            the mat
     */
    protected void process(Mat mat) {
        final BufferedImage cameraBuffertrackClass = matToBufferedImage(mat);
        /*
         * Converts the image matrix into a "blob," which is then fed into
         * the Caffe neural network using net.setInput() and detections are
         * output as a matrix in net.forward().
         */
        final Mat blob = Dnn.blobFromImage(mat, SCALE_FACTOR, new Size(SIZE,
                        SIZE), new Scalar(SIZE, SIZE), false, false);
        this.net.setInput(blob);
        final Mat det = this.net.forward();
        
        int endX = 0;
        int endY = 0;
        int startX = 0;
        int startY = 0;
        String trackCaptureLabel = "";
        
        /*
         * For each detection, there are 7 numbers the matrix det outputs.
         * The first is always 0 for some unknown reason, the second is the
         * object it detects (output as the index from the TrackClasses enum
         * (which is why you shouldn't change the order of the classes)), the
         * third is the confidence the network has that the object is what they
         * predicted it was, and the fourth to seventh numbers are the starting
         * x position, starting y position, ending x position, and ending y
         * position of the object respectively. They are represented
         * proportionally to the image, so you must multiply them by the size of
         * the image to get the actual location of the object with respects to
         * the image. This for loop then draws a rectangle around each detection
         * and text identifying each image.
         */
        for (int i = 0; i < det.reshape(1, 1).size().width; i += 7) {
            final int index = (int) det.reshape(1, 1).get(0, i + 1)[0];
            final double con = det.reshape(1, 1).get(0, i + 2)[0];
            final String capturedTrackClass;
            final int fullCon = (int) (100 * con);
            
            startX = (int) (det.reshape(1, 1).get(0, i + 3)[0]
                            * AbstractHUDController.SCENE_W);
            startY = (int) (det.reshape(1, 1).get(0, i + 4)[0]
                            * AbstractHUDController.SCENE_H);
            endX = (int) (det.reshape(1, 1).get(0, i + 5)[0]
                            * AbstractHUDController.SCENE_W);
            endY = (int) (det.reshape(1, 1).get(0, i + 6)[0]
                            * AbstractHUDController.SCENE_H);
            
            if (index > 0 && index < this.trackClasses.size()) {
                capturedTrackClass = this.trackClasses.get(index);
            } else {
                capturedTrackClass = "???";
            }
            
            if (fullCon >= CONFIDENCE_LIMIT && capturedTrackClass.equals(
                            this.trackClass)) {
                this.trackClassFound = true;
                trackCaptureLabel = capturedTrackClass + " [" + fullCon
                                + "%]<br />" + "TARGET";
            } else {
                this.trackClassFound = false;
                trackCaptureLabel = capturedTrackClass + " [" + fullCon + "%]";
            }
            
        }
        final int[] trackBounds = { startX, startY, endX - startX, endY
                        - startY };
        this.cameraImageIconBuffer = new ImageIcon(cameraBuffertrackClass);
        this.cameraStatusBuffer = "Target: " + this.trackClass + "<br />"
                        + "Found Target: " + this.trackClassFound + "<br />"
                        + "Raw I/O Buffer: " + cameraBuffertrackClass
                                        .hashCode();
        this.cameraTrackBoundBuffer = trackBounds;
        this.cameraTrackCaptureLabelBuffer = trackCaptureLabel;
    }
}
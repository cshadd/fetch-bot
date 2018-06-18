package io.github.cshadd.fetch_bot.controllers;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import io.github.cshadd.fetch_bot.io.socket.SocketCommunicationException;
import io.github.cshadd.fetch_bot.io.socket.SocketImageStreamCommunication;
import io.github.cshadd.fetch_bot.io.socket.SocketImageStreamCommunicationImpl;

// Main
public abstract class AbstractHUDController extends AbstractController
                implements HUDController {
    // Public Constant Instance/Property Fields
    
    public static final int SCENE_H = 480;
    public static final int SCENE_W = 640;
    
    // Private Final Instance/Property Fields
    
    private final SocketImageStreamCommunication stream;
    
    // Protected Final Instance/Property Fields
    
    protected final HudThread      hudRunnable;
    protected final HudSetupThread hudSetupRunnable;
    protected final Thread         hudThread;
    
    // Protected Instance/Property Fields
    
    protected JComponent   hudContent;
    protected JFrame       hudFrame;
    protected JLabel       hudLabelBack;
    protected JLabel       hudLabelBackFilter;
    protected JLabel       hudLabelStatus;
    protected JLabel       hudLabelTrack;
    protected JLayeredPane hudLayerPane;
    
    // Protected Constructors
    
    protected AbstractHUDController() {
        this(null);
    }
    
    protected AbstractHUDController(SocketImageStreamCommunication newStream) {
        super();
        this.hudSetupRunnable = new HudSetupThread();
        javax.swing.SwingUtilities.invokeLater(this.hudSetupRunnable);
        
        this.hudRunnable = new HudThread();
        this.hudThread = new Thread(this.hudRunnable);
        this.stream = newStream;
    }
    
    // Protected Final Nested Classes
    
    protected final class HudThread implements Runnable {
        // Private Instance/Property Fields
        
        /**
         * The running state.
         */
        private volatile boolean running;
        
        // Public Constructors
        
        /**
         * Instantiates a new Camera Thread.
         */
        public HudThread() {
            super();
            this.running = false;
        }
        
        // Public Methods
        
        /**
         * Terminate the thread.
         */
        public void terminate() {
            this.running = false;
        }
        
        // Public Methods (Overrided)
        
        /**
         * Runs the camera with each frame being processed.
         * 
         * @see java.lang.Runnable#run()
         */
        @Override
        public void run() {
            this.running = true;
            while (this.running) {
                try {
                    writeToStream();
                } catch (Exception e) {
                    /* */ } // Suppressed
                finally {
                    /* */ }
            }
        }
    }
    
    protected final class HudSetupThread implements Runnable {
        // Public Constructors
        
        /**
         * Instantiates a new Hud Setup Thread.
         */
        public HudSetupThread() {
            super();
        }
        
        // Public Methods (Overrided)
        
        /**
         * Runs the hud setup.
         * 
         * @see java.lang.Runnable#run()
         */
        @Override
        public void run() {
            AbstractHUDController.this.hudLabelBack = new JLabel();
            AbstractHUDController.this.hudLabelBack.setVerticalAlignment(
                            SwingConstants.TOP);
            AbstractHUDController.this.hudLabelBack.setHorizontalAlignment(
                            SwingConstants.LEFT);
            AbstractHUDController.this.hudLabelBack.setOpaque(false);
            AbstractHUDController.this.hudLabelBack.setBounds(0, 0, SCENE_W,
                            SCENE_H);
            
            AbstractHUDController.this.hudLabelBackFilter = new JLabel();
            AbstractHUDController.this.hudLabelBackFilter.setVerticalAlignment(
                            SwingConstants.TOP);
            AbstractHUDController.this.hudLabelBackFilter
                            .setHorizontalAlignment(SwingConstants.LEFT);
            AbstractHUDController.this.hudLabelBackFilter.setOpaque(true);
            AbstractHUDController.this.hudLabelBackFilter.setBackground(
                            new Color(255, 0, 0, 150));
            AbstractHUDController.this.hudLabelBackFilter.setBounds(0, 0,
                            SCENE_W, SCENE_H);
            
            AbstractHUDController.this.hudLabelStatus = new JLabel();
            AbstractHUDController.this.hudLabelStatus.setVerticalAlignment(
                            SwingConstants.BOTTOM);
            AbstractHUDController.this.hudLabelStatus.setHorizontalAlignment(
                            SwingConstants.LEFT);
            AbstractHUDController.this.hudLabelStatus.setOpaque(false);
            AbstractHUDController.this.hudLabelStatus.setBounds(0, 0, SCENE_W,
                            SCENE_H);
            
            AbstractHUDController.this.hudLabelTrack = new JLabel();
            AbstractHUDController.this.hudLabelTrack.setVerticalAlignment(
                            SwingConstants.TOP);
            AbstractHUDController.this.hudLabelTrack.setHorizontalAlignment(
                            SwingConstants.LEFT);
            AbstractHUDController.this.hudLabelTrack.setOpaque(false);
            AbstractHUDController.this.hudLabelTrack.setBorder(BorderFactory
                            .createLineBorder(Color.white));
            
            AbstractHUDController.this.hudLayerPane = new JLayeredPane();
            AbstractHUDController.this.hudLayerPane.setPreferredSize(
                            new Dimension(SCENE_W, SCENE_H));
            AbstractHUDController.this.hudLayerPane.add(
                            AbstractHUDController.this.hudLabelStatus, 0);
            AbstractHUDController.this.hudLayerPane.add(
                            AbstractHUDController.this.hudLabelTrack, 1);
            AbstractHUDController.this.hudLayerPane.add(
                            AbstractHUDController.this.hudLabelBackFilter, 2);
            AbstractHUDController.this.hudLayerPane.add(
                            AbstractHUDController.this.hudLabelBack, 3);
            
            AbstractHUDController.this.hudContent = new JPanel();
            AbstractHUDController.this.hudContent.setLayout(new BoxLayout(
                            AbstractHUDController.this.hudContent,
                            BoxLayout.PAGE_AXIS));
            AbstractHUDController.this.hudContent.setOpaque(true);
            AbstractHUDController.this.hudContent.add(
                            AbstractHUDController.this.hudLayerPane);
            
            AbstractHUDController.this.hudFrame = new JFrame("Fetch Bot HUD");
            AbstractHUDController.this.hudFrame.setContentPane(
                            AbstractHUDController.this.hudContent);
            AbstractHUDController.this.hudFrame.setDefaultCloseOperation(
                            WindowConstants.DISPOSE_ON_CLOSE);
            AbstractHUDController.this.hudFrame.setLocationByPlatform(true);
            AbstractHUDController.this.hudFrame.setResizable(false);
            
            AbstractHUDController.this.hudFrame.pack();
            AbstractHUDController.this.hudFrame.setVisible(false);
        }
    }
    
    // Protected Methods
    
    protected void writeToStream() throws SocketCommunicationException {
        final BufferedImage img = new BufferedImage(SCENE_W, SCENE_H,
                        BufferedImage.TYPE_INT_RGB);
        final Graphics2D g2d = img.createGraphics();
        AbstractHUDController.this.hudContent.printAll(g2d);
        g2d.dispose();
        this.stream.write(img);
    }
}
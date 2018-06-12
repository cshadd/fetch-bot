package io.github.cshadd.fetch_bot.controllers;

import java.awt.Color;
import java.awt.Dimension;
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
import org.opencv.core.Mat;

import io.github.cshadd.fetch_bot.controllers.AbstractOpenCVController.CameraThread;

// Main

public abstract class AbstractHudController extends AbstractController
                implements HudController {
    // Public Constant Instance/Property Fields
    
    public static final int SCENE_H = 480;
    public static final int SCENE_W = 640;
    
    // Protected Final Instance/Property Fields
    
    protected final HudThread      hudRunnable;
    protected final HudSetupThread hudSetupRunnable;
    protected final Thread         hudThread;
    
    // Protected Instance/Property Fields
    
    protected BufferedImage buffer;
    protected Mat           cameraFrame;
    protected JComponent    hudContent;
    protected JFrame        hudFrame;
    protected JLabel        hudLabelBack;
    protected JLabel        hudLabelBackFilter;
    protected JLabel        hudLabelStatus;
    protected JLabel        hudLabelTrack;
    protected JLayeredPane  hudLayerPane;
    
    // Protected Constructors
    
    protected AbstractHudController() {
        super();
        this.hudSetupRunnable = new HudSetupThread();
        javax.swing.SwingUtilities.invokeLater(this.hudSetupRunnable);
        
        this.hudRunnable = new HudThread();
        this.hudThread = new Thread(this.hudRunnable);
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
            // while (this.running) {
                
            // }
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
            AbstractHudController.this.hudLabelBack = new JLabel();
            AbstractHudController.this.hudLabelBack.setVerticalAlignment(
                            SwingConstants.TOP);
            AbstractHudController.this.hudLabelBack.setHorizontalAlignment(
                            SwingConstants.LEFT);
            AbstractHudController.this.hudLabelBack.setOpaque(false);
            AbstractHudController.this.hudLabelBack.setBounds(0, 0, SCENE_W,
                            SCENE_H);
            
            AbstractHudController.this.hudLabelBackFilter = new JLabel();
            AbstractHudController.this.hudLabelBackFilter.setVerticalAlignment(
                            SwingConstants.TOP);
            AbstractHudController.this.hudLabelBackFilter
                            .setHorizontalAlignment(SwingConstants.LEFT);
            AbstractHudController.this.hudLabelBackFilter.setOpaque(true);
            AbstractHudController.this.hudLabelBackFilter.setBackground(
                            new Color(255, 0, 0, 150));
            AbstractHudController.this.hudLabelBackFilter.setBounds(0, 0,
                            SCENE_W, SCENE_H);
            
            AbstractHudController.this.hudLabelStatus = new JLabel();
            AbstractHudController.this.hudLabelStatus.setVerticalAlignment(
                            SwingConstants.BOTTOM);
            AbstractHudController.this.hudLabelStatus.setHorizontalAlignment(
                            SwingConstants.LEFT);
            AbstractHudController.this.hudLabelStatus.setOpaque(false);
            AbstractHudController.this.hudLabelStatus.setBounds(0, 0, SCENE_W,
                            SCENE_H);
            
            AbstractHudController.this.hudLabelTrack = new JLabel();
            AbstractHudController.this.hudLabelTrack.setVerticalAlignment(
                            SwingConstants.TOP);
            AbstractHudController.this.hudLabelTrack.setHorizontalAlignment(
                            SwingConstants.LEFT);
            AbstractHudController.this.hudLabelTrack.setOpaque(false);
            AbstractHudController.this.hudLabelTrack.setBorder(BorderFactory
                            .createLineBorder(Color.white));
            
            AbstractHudController.this.hudLayerPane = new JLayeredPane();
            AbstractHudController.this.hudLayerPane.setPreferredSize(
                            new Dimension(SCENE_W, SCENE_H));
            AbstractHudController.this.hudLayerPane.add(
                            AbstractHudController.this.hudLabelStatus, 0);
            AbstractHudController.this.hudLayerPane.add(
                            AbstractHudController.this.hudLabelTrack, 1);
            AbstractHudController.this.hudLayerPane.add(
                            AbstractHudController.this.hudLabelBackFilter, 2);
            AbstractHudController.this.hudLayerPane.add(
                            AbstractHudController.this.hudLabelBack, 3);
            
            AbstractHudController.this.hudContent = new JPanel();
            AbstractHudController.this.hudContent.setLayout(new BoxLayout(
                            AbstractHudController.this.hudContent,
                            BoxLayout.PAGE_AXIS));
            AbstractHudController.this.hudContent.setOpaque(true);
            AbstractHudController.this.hudContent.add(
                            AbstractHudController.this.hudLayerPane);
            
            AbstractHudController.this.hudFrame = new JFrame("Fetch Bot Hud");
            AbstractHudController.this.hudFrame.setContentPane(
                            AbstractHudController.this.hudContent);
            AbstractHudController.this.hudFrame.setDefaultCloseOperation(
                            WindowConstants.DISPOSE_ON_CLOSE);
            AbstractHudController.this.hudFrame.setLocationByPlatform(true);
            AbstractHudController.this.hudFrame.setResizable(false);
            
            AbstractHudController.this.hudFrame.pack();
            // AbstractHudController.this.hudFrame.setVisible(false);
            AbstractHudController.this.hudFrame.setVisible(true);
        }
    }
}
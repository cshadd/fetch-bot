package io.github.cshadd.fetch_bot.controllers;

import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import io.github.cshadd.fetch_bot.Component;

// Main

/**
 * The Class HUDControllerImpl. An HUD Controller with basic
 * implementation.
 * 
 * @author Christian Shadd
 * @author Maria Verna Aquino
 * @author Thanh Vu
 * @author Joseph Damian
 * @author Giovanni Orozco
 * @since 1.0.0
 */
@Component("HUD")
public class HUDControllerImpl extends AbstractHUDController {
    // Public Constructors
    
    public HUDControllerImpl() {
        super();
    }
    
    // Public Methods (Overrided)
    
    @Override
    public void closeHud() throws HUDControllerException {
        this.hudFrame.setEnabled(false);
        this.hudFrame.dispose();
        try {
            this.hudRunnable.terminate();
            this.hudThread.join();
        } catch (InterruptedException e) {
            throw new HUDControllerException("Thread was interrupted.", e);
        } catch (Exception e) {
            throw new HUDControllerException("Unknown issue.", e);
        } finally {
            /* */ }
    }
    
    @Override
    public void openHud() {
        this.hudThread.start();
    }
    
    @Override
    public BufferedImage takeHUD() {
        return hudBuffer;
    }
    
    @Override
    public void updateBack(ImageIcon img) {
        this.hudLabelBack.setIcon(img);
    }
    
    @Override
    public void updateTrackBounds(int startX, int startY, int endX, int endY) {
        this.hudLabelTrack.setBounds(startX, startY, endX, endY);
    }
    
    @Override
    public void updateTrackCaptureLabel(String label) {
        this.hudLabelTrack.setText("<html><p style='color: white;'>" + label
                        + "</p></html>");
    }
    
    @Override
    public void updateStatus(String status) {
        this.hudLabelStatus.setText(
                        "<html><p style='color: white; font-size: 10px'>"
                                        + status + "</p></html>");
    }
}
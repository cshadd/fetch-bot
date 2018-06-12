package io.github.cshadd.fetch_bot.controllers;

import javax.swing.ImageIcon;

import io.github.cshadd.fetch_bot.Component;

// Main

@Component("Hud")
public class HudControllerImpl extends AbstractHudController {
    // Public Constructors
    
    public HudControllerImpl() {
        super();
    }
    
    // Public Methods (Overrided)
    
    @Override
    public void closeHud() throws HudControllerException {
        this.hudFrame.setEnabled(false);
        this.hudFrame.dispose();
        try {
            this.hudRunnable.terminate();
            this.hudThread.join();
        } catch (InterruptedException e) {
            throw new HudControllerException("Thread was interrupted.", e);
        } catch (Exception e) {
            throw new HudControllerException("There was an unknown issue!", e);
        } finally {
            /* */ }
    }
    
    @Override
    public void openHud() {
        this.hudThread.start();
    }
    
    @Override
    public void updateBack(ImageIcon img) {
        this.hudLabelBack.setIcon(img);
    }
    
    @Override
    public void updateTrack(String label) {
        this.hudLabelTrack.setText("<html><p style='color: white;'>"
                        + label + "</p></html>");
    }
    
    @Override
    public void updateTrackBounds(int startX, int startY, int endX, int endY) {
        this.hudLabelTrack.setBounds(startX, startY, endX, endY);
    }
    
    @Override
    public void updateStatus(String status) {
        this.hudLabelStatus.setText(
                        "<html><p style='color: white; font-size: 20px'>"
                                        + status + "</p></html>");
    }
}
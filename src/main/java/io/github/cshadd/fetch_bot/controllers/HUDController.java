package io.github.cshadd.fetch_bot.controllers;

import javax.swing.ImageIcon;

// Main
//Since 2.0.0-alpha
public abstract interface HUDController extends Controller {
    // Public Abstract Methods
    
    public abstract void closeHud() throws HUDControllerException;
    public abstract void openHud();
    public abstract String pullBufferData();
    public abstract void updateBack(ImageIcon img);
    public abstract void updateTrack(String label);
    public abstract void updateTrackBounds(int startX, int startY, int endX, int endY);
    public abstract void updateStatus(String status);
}
package io.github.cshadd.fetch_bot.controllers;

import javax.swing.ImageIcon;

// Main

public abstract interface HudController extends Controller {
    // Public Abstract Methods
    
    public abstract void closeHud() throws HudControllerException;
    public abstract void openHud();
    public abstract void updateBack(ImageIcon img);
    public abstract void updateTrack(String label);
    public abstract void updateTrackBounds(int startX, int startY, int endX, int endY);
    public abstract void updateStatus(String status);
}
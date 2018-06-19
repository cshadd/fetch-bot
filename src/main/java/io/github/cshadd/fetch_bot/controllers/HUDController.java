package io.github.cshadd.fetch_bot.controllers;

import javax.swing.ImageIcon;

// Main

/**
 * The Interface HUDController. Defines what a HUD Controller does.
 * 
 * @author Christian Shadd
 * @author Maria Verna Aquino
 * @author Thanh Vu
 * @author Joseph Damian
 * @author Giovanni Orozco
 * @since 2.0.0-alpha
 */
public abstract interface HUDController extends Controller {
    // Public Abstract Methods
    
    public abstract void closeHud() throws HUDControllerException;
    
    public abstract void openHud();
    
    public abstract void updateBack(ImageIcon img);
    
    public abstract void updateTrack(String label);
    
    public abstract void updateTrackBounds(int startX, int startY, int endX,
                    int endY);
    
    public abstract void updateStatus(String status);
}
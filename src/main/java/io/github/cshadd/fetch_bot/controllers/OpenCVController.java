package io.github.cshadd.fetch_bot.controllers;

// Main
public abstract interface OpenCVController
extends Controller {
    // Public Abstract Methods
    public abstract void assignTrackClass(String trackClass);
    public abstract void close();
    public abstract boolean isTrackClassFound();
    public abstract void startCamera();
    public abstract void stopCamera() throws InterruptedException;
}
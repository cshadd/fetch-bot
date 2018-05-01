package io.github.cshadd.fetch_bot.controllers;

// Main
public abstract interface PathfindController
extends Controller {
    // Public Abstract Methods
    public abstract void block(boolean state);
    public abstract void process();
}

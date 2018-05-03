package io.github.cshadd.fetch_bot.controllers;

// Main
public abstract interface PathfindController
extends Controller {
    // Public Abstract Methods
    public abstract void blockNext();
    public abstract void goNext();
    public abstract boolean isAnyAvailable();
    public abstract boolean isNextBlocked();
    public abstract boolean isNextTracked();
    public abstract boolean isNextVisited();
    public abstract void reset();
    public abstract void rotateLeft();
    public abstract void rotateRight();
    public abstract void visitNext();
}
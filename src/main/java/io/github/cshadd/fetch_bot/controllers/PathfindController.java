package io.github.cshadd.fetch_bot.controllers;

// Main
public abstract interface PathfindController
extends Controller {
    // Public Abstract Methods
    public abstract void blockNext();
    public abstract void goNext();
    public abstract boolean isNextAvalible();
    public abstract void reset();
    public abstract void rotateLeft();
    public abstract void rotateRight();
    public abstract void visitNext();
}
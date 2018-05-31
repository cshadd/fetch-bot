/*
 * MIT License
 * 
 * Copyright (c) 2018 Christian Shadd
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 * 
 * https://cshadd.github.io/fetch-bot/
 */
package io.github.cshadd.fetch_bot.controllers;

// Main

/**
 * The Interface PathfindController. Defines what a Pathfind Controller does.
 * 
 * @author Christian Shadd
 * @author Maria Verna Aquino
 * @author Thanh Vu
 * @author Joseph Damian
 * @author Giovanni Orozco
 * @since 1.0.0
 */
public abstract interface PathfindController extends Controller {
    // Public Abstract Methods
    
    /**
     * Block next coordinate in path.
     */
    public abstract void blockNext();
    
    /**
     * Calculate pathfinding and the states for visited and blocked of the
     * back, front, left, right respectively.
     *
     * @return the array of states
     */
    public abstract boolean[] calculate();
    
    /**
     * Go to next coordinate in path.
     */
    public abstract void goNext();
    
    /**
     * Checks if next coordinate in path is blocked.
     *
     * @return true, if next coordinate in path is blocked
     */
    public abstract boolean isNextBlocked();
    
    /**
     * Checks if next coordinate in path is visited.
     *
     * @return true, if next coordinate in path is visited
     */
    public abstract boolean isNextVisited();
    
    /**
     * String representation of the raw graph.
     *
     * @return the string
     */
    public abstract String rawGraphToString();
    
    /**
     * Reset pathfinding.
     */
    public abstract void reset();
    
    /**
     * Rotate to the left coordinate in path.
     */
    public abstract void rotateLeft();
    
    /**
     * Rotate to the right coordinate in path.
     */
    public abstract void rotateRight();
    
    /**
     * Unblock next coordinate in path.
     */
    public abstract void unblockNext();
    
    /**
     * Visit current coordinate in path.
     */
    public abstract void visit();
}